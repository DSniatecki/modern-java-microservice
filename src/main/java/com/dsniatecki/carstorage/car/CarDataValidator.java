package com.dsniatecki.carstorage.car;

class CarDataValidator {

    static final int MIN_BRAND_LENGTH = 1;
    static final int MAX_BRAND_LENGTH = 64;

    static final int MIN_MODEL_LENGTH = 1;
    static final int MAX_MODEL_LENGTH = 256;

    static CarData validate(CarData carData) {
        validateBrand(carData.brand());
        validateModel(carData.model());
        return carData;
    }

    private static void validateBrand(String name) {
        if (name.length() < MIN_BRAND_LENGTH || name.length() > MAX_BRAND_LENGTH) {
            throw new IllegalStateException("Car brand is not valid.");
        }
    }

    private static void validateModel(String name) {
        if (name.length() < MIN_MODEL_LENGTH || name.length() > MAX_MODEL_LENGTH) {
            throw new IllegalStateException("Car model is not valid.");
        }
    }
}
