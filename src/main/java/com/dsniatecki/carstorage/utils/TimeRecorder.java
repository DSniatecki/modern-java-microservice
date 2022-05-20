package com.dsniatecki.carstorage.utils;

import java.util.concurrent.TimeUnit;

@FunctionalInterface
public interface TimeRecorder {
    void record(long amount, TimeUnit timeUnit);
}