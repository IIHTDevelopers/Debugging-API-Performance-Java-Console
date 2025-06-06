package com.genspark.task4;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.genspark.task4.utils.TestUtils.businessTestFile;
import static com.genspark.task4.utils.TestUtils.currentTest;
import static com.genspark.task4.utils.TestUtils.yakshaAssert;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    @Test
    public void testGenerateReportAsyncPerformance() throws Exception {
        Main main = new Main();
        int requestCount = 500;
        List<Long> latencies = new ArrayList<>();

        ExecutorService testExecutor = Executors.newFixedThreadPool(50);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < requestCount; i++) {
            int userId = i;
            futures.add(CompletableFuture.runAsync(() -> {
                try {
                    long start = System.nanoTime();
                     
                    String result = main.generateReport("user" + userId).get(1, TimeUnit.SECONDS);
                    long durationMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                    synchronized (latencies) {
                        latencies.add(durationMs);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, testExecutor));
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        testExecutor.shutdown();

        // Sort and calculate 95th percentile
        List<Long> sorted = latencies.stream().sorted().collect(Collectors.toList());
        long percentile95 = sorted.get((int) (requestCount * 0.95) - 1);

        System.out.println("95th percentile latency: " + percentile95 + " ms");

        // Assert that 95th percentile latency is less than 300ms
        yakshaAssert(currentTest(), percentile95 < 300, businessTestFile);
        //assertTrue(percentile95 < 300, "95th percentile latency must be < 300ms");
    }
}
