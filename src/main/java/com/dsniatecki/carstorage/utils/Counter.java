package com.dsniatecki.carstorage.utils;

@FunctionalInterface
public interface Counter {
    void add(int number);
    default void increment() {
        add(1);
    }
}