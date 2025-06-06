
package com.genspark.task4;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Main {

    private final ExternalAnalyticsClient client = new ExternalAnalyticsClient();

    // BUG: synchronous blocking call
    public CompletableFuture<String> generateReport(String userId) {
        String raw = client.fetchDataBlocking(userId); // blocks 250‑300 ms
        return CompletableFuture.completedFuture("REPORT::" + raw);
    }
    
   

   static class ExternalAnalyticsClient {
        public String fetchDataBlocking(String userId) {
            try { TimeUnit.MILLISECONDS.sleep(280); } catch (InterruptedException ignored) {}
            return "{data:" + userId + "}";
        }
    }
    
    }
