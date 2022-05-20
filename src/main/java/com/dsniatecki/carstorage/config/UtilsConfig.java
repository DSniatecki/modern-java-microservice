package com.dsniatecki.carstorage.config;

import com.dsniatecki.carstorage.utils.TimeSupplier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class UtilsConfig {
    private final ZoneId zoneId;

    UtilsConfig(@Value("${car-storage.time-zone-id}") String timeZoneId) {
        this.zoneId = ZoneId.of(timeZoneId);
    }

    @Bean
    TimeSupplier timeSupplier() {
        return () -> LocalDateTime.now(zoneId);
    }
}
