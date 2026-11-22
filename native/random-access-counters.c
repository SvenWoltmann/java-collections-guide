// Hardware counters for the random reads of RandomAccessBenchmark on Apple Silicon.
//
// RandomAccessBenchmark.arrayList() reads ArrayList.get(i) at 10,000 precomputed random
// indices; the time per call rises from 1.0 to 1.5 ns between 1,000 and 1,000,000 elements,
// and arrayListNear() - the same number of indices from the first 1,000 positions only -
// stays at 1.0 ns. This program repeats that access pattern without the JVM - an array of
// 4-byte slots like the Object[] of an ArrayList with compressed oops, read at 10,000 random
// indices - and reads the core's performance counters around the loop: cycles, L1 data
// cache misses, L1 and L2 data TLB misses.
//
// The counters come from the private kperf and kperfdata frameworks, loaded with dlopen()
// (approach after ibireme's kpc_demo.c). kperf needs root:
//
//     clang -O2 -o random-access-counters native/random-access-counters.c
//     sudo ./random-access-counters
//
// The event names are those of the kpep database in /usr/share/kpep for the CPU the program
// runs on; the program prints the database name it found.

#include <dlfcn.h>
#include <pthread.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

#define KPC_MAX_COUNTERS 32

typedef uint64_t kpc_config_t;
typedef struct kpep_db kpep_db;
typedef struct kpep_config kpep_config;
typedef struct kpep_event kpep_event;

// kperf.framework
static int (*kpc_set_counting)(uint32_t classes);
static int (*kpc_set_thread_counting)(uint32_t classes);
static int (*kpc_set_config)(uint32_t classes, kpc_config_t *config);
static int (*kpc_get_thread_counters)(uint32_t tid, uint32_t buf_count, uint64_t *buf);
static int (*kpc_force_all_ctrs_set)(int val);

// kperfdata.framework
static int (*kpep_db_create)(const char *name, kpep_db **db);
static int (*kpep_db_name)(kpep_db *db, const char **name);
static int (*kpep_db_event)(kpep_db *db, const char *name, kpep_event **ev);
static int (*kpep_config_create)(kpep_db *db, kpep_config **cfg);
static int (*kpep_config_force_counters)(kpep_config *cfg);
static int (*kpep_config_add_event)(kpep_config *cfg, kpep_event **ev, uint32_t flag, uint32_t *err);
static int (*kpep_config_kpc_classes)(kpep_config *cfg, uint32_t *classes);
static int (*kpep_config_kpc_count)(kpep_config *cfg, size_t *count);
static int (*kpep_config_kpc_map)(kpep_config *cfg, size_t *buf, size_t buf_size);
static int (*kpep_config_kpc)(kpep_config *cfg, kpc_config_t *buf, size_t buf_size);

static void *symbol(void *lib, const char *name) {
  void *s = dlsym(lib, name);
  if (!s) {
    fprintf(stderr, "symbol %s not found\n", name);
    exit(1);
  }
  return s;
}

static void load_frameworks(void) {
  void *kperf = dlopen("/System/Library/PrivateFrameworks/kperf.framework/kperf", RTLD_LAZY);
  void *kperfdata =
      dlopen("/System/Library/PrivateFrameworks/kperfdata.framework/kperfdata", RTLD_LAZY);
  if (!kperf || !kperfdata) {
    fprintf(stderr, "cannot load kperf/kperfdata: %s\n", dlerror());
    exit(1);
  }
  kpc_set_counting = symbol(kperf, "kpc_set_counting");
  kpc_set_thread_counting = symbol(kperf, "kpc_set_thread_counting");
  kpc_set_config = symbol(kperf, "kpc_set_config");
  kpc_get_thread_counters = symbol(kperf, "kpc_get_thread_counters");
  kpc_force_all_ctrs_set = symbol(kperf, "kpc_force_all_ctrs_set");
  kpep_db_create = symbol(kperfdata, "kpep_db_create");
  kpep_db_name = symbol(kperfdata, "kpep_db_name");
  kpep_db_event = symbol(kperfdata, "kpep_db_event");
  kpep_config_create = symbol(kperfdata, "kpep_config_create");
  kpep_config_force_counters = symbol(kperfdata, "kpep_config_force_counters");
  kpep_config_add_event = symbol(kperfdata, "kpep_config_add_event");
  kpep_config_kpc_classes = symbol(kperfdata, "kpep_config_kpc_classes");
  kpep_config_kpc_count = symbol(kperfdata, "kpep_config_kpc_count");
  kpep_config_kpc_map = symbol(kperfdata, "kpep_config_kpc_map");
  kpep_config_kpc = symbol(kperfdata, "kpep_config_kpc");
}

static const char *EVENTS[] = {
    "FIXED_CYCLES",
    "FIXED_INSTRUCTIONS",
    "L1D_CACHE_MISS_LD_NONSPEC",
    "L1D_TLB_MISS_NONSPEC",
    "L2_TLB_MISS_DATA",
};
#define EVENT_COUNT (sizeof(EVENTS) / sizeof(EVENTS[0]))

static size_t counter_index[EVENT_COUNT];

static void configure_counters(void) {
  if (kpc_force_all_ctrs_set(1)) {
    fprintf(stderr, "kpc_force_all_ctrs_set failed - run with sudo\n");
    exit(1);
  }
  kpep_db *db;
  if (kpep_db_create(NULL, &db)) {
    fprintf(stderr, "no kpep database for this CPU\n");
    exit(1);
  }
  const char *db_name;
  kpep_db_name(db, &db_name);
  printf("kpep database: %s\n", db_name);

  kpep_config *cfg;
  kpep_config_create(db, &cfg);
  kpep_config_force_counters(cfg);
  for (size_t i = 0; i < EVENT_COUNT; i++) {
    kpep_event *ev;
    if (kpep_db_event(db, EVENTS[i], &ev)) {
      fprintf(stderr, "event %s not found\n", EVENTS[i]);
      exit(1);
    }
    uint32_t err;
    if (kpep_config_add_event(cfg, &ev, 0, &err)) {
      fprintf(stderr, "cannot add event %s (%u)\n", EVENTS[i], err);
      exit(1);
    }
  }

  uint32_t classes;
  size_t reg_count;
  size_t map[KPC_MAX_COUNTERS] = {0};
  kpc_config_t regs[KPC_MAX_COUNTERS] = {0};
  kpep_config_kpc_classes(cfg, &classes);
  kpep_config_kpc_count(cfg, &reg_count);
  kpep_config_kpc_map(cfg, map, sizeof(map));
  kpep_config_kpc(cfg, regs, sizeof(regs));
  for (size_t i = 0; i < EVENT_COUNT; i++) {
    counter_index[i] = map[i];
  }
  if (kpc_set_config(classes, regs) || kpc_set_counting(classes) ||
      kpc_set_thread_counting(classes)) {
    fprintf(stderr, "cannot start the counters - run with sudo\n");
    exit(1);
  }
}

#define INDEX_COUNT 10000
#define NEAR_RANGE 1000
#define PASSES 2000 // 20 million reads per measurement

static uint64_t rng_state = 42;

static uint32_t next_random(uint32_t bound) {
  rng_state ^= rng_state << 13;
  rng_state ^= rng_state >> 7;
  rng_state ^= rng_state << 17;
  return (uint32_t)(rng_state % bound);
}

static double now_ns(void) {
  struct timespec ts;
  clock_gettime(CLOCK_MONOTONIC_RAW, &ts);
  return ts.tv_sec * 1e9 + ts.tv_nsec;
}

static volatile uint64_t sink;

// Reads array[indices[i]] for all indices, PASSES times. The reads are independent of each
// other, as in the benchmark, where JMH consumes each returned reference.
static void read_all(const uint32_t *array, const uint32_t *indices, uint64_t *counters,
                     double *ns) {
  uint64_t before[KPC_MAX_COUNTERS], after[KPC_MAX_COUNTERS];
  uint64_t sum = 0;
  double t0 = now_ns();
  kpc_get_thread_counters(0, KPC_MAX_COUNTERS, before);
  for (int pass = 0; pass < PASSES; pass++) {
    for (int i = 0; i < INDEX_COUNT; i++) {
      sum += array[indices[i]];
    }
  }
  kpc_get_thread_counters(0, KPC_MAX_COUNTERS, after);
  *ns = now_ns() - t0;
  sink = sum;
  for (size_t e = 0; e < EVENT_COUNT; e++) {
    counters[e] = after[counter_index[e]] - before[counter_index[e]];
  }
}

static void measure(const char *label, size_t size, const uint32_t *array, uint32_t range) {
  uint32_t *indices = malloc(INDEX_COUNT * sizeof(uint32_t));
  for (int i = 0; i < INDEX_COUNT; i++) {
    indices[i] = next_random(range);
  }
  uint64_t counters[EVENT_COUNT];
  double ns;
  read_all(array, indices, counters, &ns); // warm-up: caches and TLB in their steady state
  read_all(array, indices, counters, &ns);
  double reads = (double)PASSES * INDEX_COUNT;
  printf("%-6s %9zu  %6.3f ns  %6.2f cycles  %6.2f instr", label, size, ns / reads,
         counters[0] / reads, counters[1] / reads);
  for (size_t e = 2; e < EVENT_COUNT; e++) {
    printf("  %7.4f", counters[e] / reads);
  }
  printf("\n");
  free(indices);
}

int main(void) {
  pthread_set_qos_class_self_np(QOS_CLASS_USER_INTERACTIVE, 0); // on a performance core
  load_frameworks();
  configure_counters();

  printf("per read:                                        ");
  for (size_t e = 2; e < EVENT_COUNT; e++) {
    printf("  %s", EVENTS[e]);
  }
  printf("\n");

  size_t sizes[] = {1000, 100000, 1000000};
  for (size_t s = 0; s < sizeof(sizes) / sizeof(sizes[0]); s++) {
    size_t size = sizes[s];
    uint32_t *array = malloc(size * sizeof(uint32_t)); // 4-byte slots: compressed oops
    for (size_t i = 0; i < size; i++) {
      array[i] = (uint32_t)i;
    }
    measure("random", size, array, (uint32_t)size);
    measure("near", size, array, size < NEAR_RANGE ? (uint32_t)size : NEAR_RANGE);
    free(array);
  }
  kpc_force_all_ctrs_set(0);
  return 0;
}
