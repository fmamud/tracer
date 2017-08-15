package com.simscale.tracer.model;

import com.simscale.tracer.cmd.Step;
import com.simscale.tracer.cmd.processing.Processable;
import com.simscale.tracer.cmd.separation.Separable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class Statistics {

    private static long TOTAL_TRACES, LOGLINE_AVG_SIZE = 85;

    private static final AtomicInteger ORPHANS = new AtomicInteger(0),
            MALFORMEDS = new AtomicInteger(0);

    private static Map<String, Long> COUNTS = new HashMap<>(2);

    public static int orphan() {
        return ORPHANS.getAndIncrement();
    }

    public static int malformed() {
        return MALFORMEDS.getAndIncrement();
    }

    public static void printStatistics() {
        System.out.printf(" Statistics:\n" +
                "   Orphans:\t\t%d\n" +
                "   Malformeds:\t\t%d\n" +
                "   Traces:\t\t%d\n" +
                "   JSON trees:\t\t%d\n", ORPHANS.get(), MALFORMEDS.get(), COUNTS.get(Separable.class.getSimpleName()), COUNTS.get(Processable.class.getSimpleName()));
    }

    public static void totalTraces(long inputSize) {
        TOTAL_TRACES = inputSize / LOGLINE_AVG_SIZE;
    }

    public static class ProgressBar {
        static {
            StopWatch.START = System.nanoTime();
        }

        private static final AtomicLong TRACE_COUNT = new AtomicLong(0);

        public static void hit(Step step) {
            System.out.printf("  Processing: %s (%d/%d)\r", step.getClass().getSimpleName(), TRACE_COUNT.getAndIncrement(), TOTAL_TRACES);
        }

        public static void reset(Step step) {
            System.out.println();
            COUNTS.put(step.getClass().getInterfaces()[0].getSimpleName(), TRACE_COUNT.get());
            TRACE_COUNT.set(0);
        }
    }

    public static class StopWatch {
        private static long START;

        public static void finish() {
            System.out.printf("Finished: %ss", TimeUnit.SECONDS.convert((System.nanoTime() - START), TimeUnit.NANOSECONDS));
        }
    }
}