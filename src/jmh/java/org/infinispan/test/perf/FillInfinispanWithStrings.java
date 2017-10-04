package org.infinispan.test.perf;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FillInfinispanWithStrings {

    public static final String INFINISPAN_ADDRESS = "172.17.0.2";
    public static final int BATCH = 100;

    public static void main(String[] args) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.addServer().host(INFINISPAN_ADDRESS);
        RemoteCacheManager cacheManager = new RemoteCacheManager(configurationBuilder.build());
        RemoteCache<Object, Object> remoteCache = cacheManager.getCache();

        long totalSize = 0;

        while(true) {
            Map<Object, Object> batch = new HashMap<>();
            for (int i = 0; i < BATCH; ++i) {
                String key = UUID.randomUUID().toString();
                String val = UUID.randomUUID().toString();
                totalSize += (key.getBytes().length + val.getBytes().length);
                batch.put(key, val);
            }
            remoteCache.putAll(batch);
            System.out.println("Loaded into ISPN: " + (totalSize / 1024) + " kB / " + ((totalSize / 1024)/1024) + " MB");
        }
    }

}
