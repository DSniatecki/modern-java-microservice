package com.dsniatecki.carstorage.utils;


import java.util.UUID;

public class Utils {

    private Utils() {
    }

    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}