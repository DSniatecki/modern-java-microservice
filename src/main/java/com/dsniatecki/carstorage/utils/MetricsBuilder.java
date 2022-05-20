package com.dsniatecki.carstorage.utils;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

public record MetricsBuilder(MeterRegistry meterRegistry) {

    private record TimeRecorderMetric(Timer timer, MeterRegistry meterRegistry) implements TimeRecorder {
        @Override
        public void record(long amount, TimeUnit timeUnit) {
            timer.record(amount, timeUnit);
        }
    }

    private record TimeRecorderWithCounterMetric(TimeRecorder timeRecorder, Counter counter) implements TimeRecorder {
        @Override
        public void record(long amount, TimeUnit timeUnit) {
            counter.increment();
            timeRecorder.record(amount, timeUnit);
        }
    }

    private record CounterMetric(LongAdder counter) implements Counter {
        private long count() {
            return counter.sum();
        }

        @Override
        public void add(int number) {
            counter.add(number);
        }
    }

    public TimeRecorder createTimeRecorder(String name, String description) {
        return new TimeRecorderMetric(
                Timer.builder(name)
                        .description(description)
                        .publishPercentileHistogram()
                        .register(meterRegistry),
                meterRegistry
        );
    }

    public Counter createCounter(String name, String description) {
        CounterMetric operationsCounter = new CounterMetric(new LongAdder());
        Gauge.builder(name, operationsCounter::count).description(description).register(meterRegistry);
        return operationsCounter;
    }

    public TimeRecorder createTimeRecorderWithCounter(TimeRecorder timeRecorder, Counter counter) {
        return new TimeRecorderWithCounterMetric(timeRecorder, counter);
    }
}
