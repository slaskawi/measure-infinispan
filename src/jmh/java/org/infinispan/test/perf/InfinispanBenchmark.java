package org.infinispan.test.perf;


import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class InfinispanBenchmark {
   public static final String INFINISPAN_ADDRESS = "172.17.0.2";

   @State(Scope.Benchmark)
   public static class SharedState {

      public RemoteCacheManager cacheManager;
      public RemoteCache remoteCache;

      @Setup
      public synchronized void setup() throws Exception {
         ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
         configurationBuilder.addServer().host(INFINISPAN_ADDRESS);
         cacheManager = new RemoteCacheManager(configurationBuilder.build());
         remoteCache = cacheManager.getCache();
         remoteCache.clear();
      }

      @TearDown
      public synchronized void tearDown() throws Exception {
         cacheManager.stop();
      }
   }

   @State(Scope.Thread)
   public static class PerThreadState {

      //confined to a thread
      private long counter = 0;

      public String getNextKey() {
         return Thread.currentThread().getName() + counter++;
      }

   }

   @Benchmark
   @Threads(1)
   public void benchamrk_1_thread(final SharedState sharedState, final PerThreadState state, final Blackhole blackhole) {
      String key = state.getNextKey();
      sharedState.remoteCache.put(key, key);
      blackhole.consume(sharedState.remoteCache.get(key));
   }

   @Benchmark
   @Threads(2)
   public void benchamrk_2_threads(final SharedState sharedState, final PerThreadState state, final Blackhole blackhole) {
      String key = state.getNextKey();
      sharedState.remoteCache.put(key, key);
      blackhole.consume(sharedState.remoteCache.get(key));
   }

   @Benchmark
   @Threads(4)
   public void benchamrk_4_threads(final SharedState sharedState, final PerThreadState state, final Blackhole blackhole) {
      String key = state.getNextKey();
      sharedState.remoteCache.put(key, key);
      blackhole.consume(sharedState.remoteCache.get(key));
   }

   @Benchmark
   @Threads(8)
   public void benchamrk_8_threads(final SharedState sharedState, final PerThreadState state, final Blackhole blackhole) {
      String key = state.getNextKey();
      sharedState.remoteCache.put(key, key);
      blackhole.consume(sharedState.remoteCache.get(key));
   }

   @Benchmark
   @Threads(16)
   public void benchamrk_16_threads(final SharedState sharedState, final PerThreadState state, final Blackhole blackhole) {
      String key = state.getNextKey();
      sharedState.remoteCache.put(key, key);
      blackhole.consume(sharedState.remoteCache.get(key));
   }
}
