package com.dsniatecki.carstorage.car;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

record Car(
        String id,
        String brand,
        String model,
        LocalDate producedAt,
        LocalDateTime createdAt,
        Optional<LocalDateTime> updatedAt
) {
}
