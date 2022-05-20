package com.dsniatecki.carstorage.car;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertAll;

class CarDataValidatorTest {

    private static final LocalDate TEST_DATE = LocalDate.of(1998, 5, 5);

    @Test
    @DisplayName("Should pass CarData validation")
    void shouldPassCarDataValidation() {
        CarData carData = new CarData("SuperBrand", "SuperModel", TEST_DATE);
        assertThat(CarDataValidator.validate(carData)).isEqualTo(carData);
    }

    @Test
    @DisplayName("Should not pass CarData validation due to invalid car brand")
    void shouldNotPassCarDataValidationDueToInvalidCarBrand() {
        CarData carData1 = new CarData("", "SuperModel", TEST_DATE);
        CarData carData2 = new CarData("A".repeat(CarDataValidator.MAX_BRAND_LENGTH + 1), "SuperModel", TEST_DATE);
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> CarDataValidator.validate(carData1)),
                () -> assertThrows(IllegalStateException.class, () -> CarDataValidator.validate(carData2))
        );
    }

    @Test
    @DisplayName("Should not pass CarData validation due to invalid car model")
    void shouldNotPassCarDataValidationDueToInvalidCarModel() {
        CarData carData1 = new CarData("SuperBrand", "", TEST_DATE);
        CarData carData2 = new CarData("SuperBrand", "A".repeat(CarDataValidator.MAX_MODEL_LENGTH + 1), TEST_DATE);
        assertAll(
                () -> assertThrows(IllegalStateException.class, () -> CarDataValidator.validate(carData1)),
                () -> assertThrows(IllegalStateException.class, () -> CarDataValidator.validate(carData2))
        );
    }
}