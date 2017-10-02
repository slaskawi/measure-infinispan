package org.infinispan.test.perf;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class FillInfinispanWithStringsAndREST {

    public static final String INFINISPAN_ADDRESS = "172.17.0.2";
    public static final int NUMBER_OF_THREADS = 16;

    public static void main(String[] args) {
        AtomicLong totalSize = new AtomicLong();

        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

        IntStream.range(0, NUMBER_OF_THREADS).forEach((a) -> {
            executor.execute(() -> {
                while (true) {
                    String key = UUID.randomUUID().toString();
                    String val = UUID.randomUUID().toString();
                    totalSize.addAndGet((key.length() + val.length()) * 2);
                    try {
                        URL url = new URL(String.format("http://%s:8080/rest/default/%s", INFINISPAN_ADDRESS, key));
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setDoOutput(true);
                        conn.setRequestMethod("POST");
                        conn.setRequestProperty("Content-Type", "application/text");
                        conn.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");

                        OutputStream os = conn.getOutputStream();
                        os.write(val.getBytes());
                        os.flush();

                        if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                            throw new RuntimeException("Failed : HTTP error code : "
                                    + conn.getResponseCode());
                        }

                        conn.disconnect();
                    } catch (Exception e) {
                        System.out.println("EXCEPTION" +  e);
                    }
                    System.out.println("Loaded into ISPN: " + (totalSize.get() / 1024) + " kB / " + ((totalSize.get() / 1024)/1024) + " MB");
                }
            });
        });

        System.out.println("KONIEC");
    }

}
