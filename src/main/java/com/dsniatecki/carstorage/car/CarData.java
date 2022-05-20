package com.dsniatecki.carstorage.car;

import java.time.LocalDate;

record CarData(
        String brand,
        String model,
        LocalDate producedAt
) {
}
