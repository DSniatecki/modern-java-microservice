package com.dsniatecki.carstorage;

import com.dsniatecki.carstorage.config.CarConfig;
import com.dsniatecki.carstorage.config.FlywayConfig;
import com.dsniatecki.carstorage.config.SecurityConfig;
import com.dsniatecki.carstorage.config.UtilsConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({UtilsConfig.class, SecurityConfig.class, FlywayConfig.class, CarConfig.class})
public class CarStorageApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarStorageApplication.class, args);
    }
}
