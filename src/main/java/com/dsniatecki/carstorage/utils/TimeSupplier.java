package com.dsniatecki.carstorage.utils;

import java.time.LocalDateTime;

@FunctionalInterface
public interface TimeSupplier {
    LocalDateTime now();
}