// Hardware counters for a Java program on Apple Silicon: a small library that
// TreeLookupCountersBenchmark (package eu.happycoders.collections.hashmap.benchmark) calls
// through the Foreign Function & Memory API to read the performance counters of the benchmark
// thread around every iteration: cycles, instructions, mispredicted branches, L1 data cache
// misses.
//
// The counters come from the private kperf and kperfdata frameworks, loaded with dlopen()
// (approach after ibireme's kpc_demo.c). kperf needs root, so the benchmark has to run with
// sudo; the command is in the class comment of TreeLookupCountersBenchmark.
//
//     clang -O2 -dynamiclib -o target/libkpccounters.dylib native/kpc-counters.c
//
// The event names are those of the kpep database in /usr/share/kpep for the CPU the program
// runs on; kpc_counters_start() prints the database name it found.

#include <dlfcn.h>
#include <pthread.h>
#include <stdint.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

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

static int missing_symbols;

static void *symbol(void *lib, const char *name) {
  void *s = dlsym(lib, name);
  if (!s) {
    fprintf(stderr, "symbol %s not found\n", name);
    missing_symbols++;
  }
  return s;
}

static int load_frameworks(void) {
  void *kperf = dlopen("/System/Library/PrivateFrameworks/kperf.framework/kperf", RTLD_LAZY);
  void *kperfdata =
      dlopen("/System/Library/PrivateFrameworks/kperfdata.framework/kperfdata", RTLD_LAZY);
  if (!kperf || !kperfdata) {
    fprintf(stderr, "cannot load kperf/kperfdata: %s\n", dlerror());
    return 1;
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
  return missing_symbols;
}

static const char *EVENTS[] = {
    "FIXED_CYCLES",
    "FIXED_INSTRUCTIONS",
    "BRANCH_MISPRED_NONSPEC",
    "L1D_CACHE_MISS_LD_NONSPEC",
};
#define EVENT_COUNT (sizeof(EVENTS) / sizeof(EVENTS[0]))

static size_t counter_index[EVENT_COUNT];

// Configures and starts the counters for the calling thread and moves it to a performance core.
// Returns 0 on success; on failure it prints the reason and returns a non-zero value, and the
// caller goes on without counters.
int kpc_counters_start(void) {
  pthread_set_qos_class_self_np(QOS_CLASS_USER_INTERACTIVE, 0);
  if (load_frameworks()) {
    return 1;
  }
  if (kpc_force_all_ctrs_set(1)) {
    fprintf(stderr, "kpc_force_all_ctrs_set failed - the counters need root (sudo)\n");
    return 2;
  }
  kpep_db *db;
  if (kpep_db_create(NULL, &db)) {
    fprintf(stderr, "no kpep database for this CPU\n");
    return 3;
  }
  const char *db_name;
  kpep_db_name(db, &db_name);
  printf("kpep database: %s\n", db_name);
  fflush(stdout);

  kpep_config *cfg;
  kpep_config_create(db, &cfg);
  kpep_config_force_counters(cfg);
  for (size_t i = 0; i < EVENT_COUNT; i++) {
    kpep_event *ev;
    if (kpep_db_event(db, EVENTS[i], &ev)) {
      fprintf(stderr, "event %s not found\n", EVENTS[i]);
      return 4;
    }
    uint32_t err;
    if (kpep_config_add_event(cfg, &ev, 0, &err)) {
      fprintf(stderr, "cannot add event %s (%u)\n", EVENTS[i], err);
      return 5;
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
    fprintf(stderr, "cannot start the counters - the counters need root (sudo)\n");
    return 6;
  }
  return 0;
}

// Writes the current values of the four counters of the calling thread to out[0..3]: cycles,
// instructions, mispredicted branches, L1 data cache load misses. Returns 0 on success.
int kpc_counters_read(uint64_t *out) {
  uint64_t raw[KPC_MAX_COUNTERS];
  if (kpc_get_thread_counters(0, KPC_MAX_COUNTERS, raw)) {
    return 1;
  }
  for (size_t e = 0; e < EVENT_COUNT; e++) {
    out[e] = raw[counter_index[e]];
  }
  return 0;
}

// Hands the counters back to the system.
void kpc_counters_stop(void) {
  kpc_force_all_ctrs_set(0);
}
