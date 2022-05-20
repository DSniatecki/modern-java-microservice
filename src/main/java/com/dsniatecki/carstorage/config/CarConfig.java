package com.dsniatecki.carstorage.config;

import com.dsniatecki.carstorage.car.CarRepository;
import com.dsniatecki.carstorage.car.CarRowRepository;
import com.dsniatecki.carstorage.car.CarService;
import com.dsniatecki.carstorage.utils.MetricsBuilder;
import com.dsniatecki.carstorage.utils.TimeSupplier;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.annotation.Bean;

public class CarConfig {

    @Bean
    CarService carService(CarRepository carRepository, TimeSupplier timeSupplier) {
        return new CarService(carRepository, timeSupplier);
    }

    @Bean
    CarRepository carRepository(CarRowRepository carRowRepository, MeterRegistry meterRegistry) {
        MetricsBuilder metricsBuilder = new MetricsBuilder(meterRegistry);
        return new CarRepository(
                carRowRepository,
                metricsBuilder.createTimeRecorderWithCounter(
                        metricsBuilder.createTimeRecorder(
                                "car_query_find_time",
                                "Time of query responsible for finding car by id"
                        ),
                        metricsBuilder.createCounter(
                                "car_query_find_count",
                                "Number of executed queries responsible for finding car by id"
                        )
                ),
                metricsBuilder.createTimeRecorderWithCounter(
                        metricsBuilder.createTimeRecorder(
                                "car_query_find_multiple_time",
                                "Time of query responsible for finding multiple cars by ids"
                        ),
                        metricsBuilder.createCounter(
                                "car_query_find_multiple_count",
                                "Number of executed queries responsible for finding all cars"
                        )
                ),
                metricsBuilder.createTimeRecorderWithCounter(
                        metricsBuilder.createTimeRecorder(
                                "car_query_find_all_time",
                                "Time of query responsible for finding all cars"
                        ),
                        metricsBuilder.createCounter(
                                "car_query_find_all_count",
                                "Number of executed queries responsible for finding all cars"
                        )
                ),
                metricsBuilder.createTimeRecorderWithCounter(
                        metricsBuilder.createTimeRecorder(
                                "car_query_save_time",
                                "Time of query responsible for saving car"
                        ),
                        metricsBuilder.createCounter(
                                "car_query_save_count",
                                "Number of executed queries responsible for saving car"
                        )
                ),
                metricsBuilder.createTimeRecorderWithCounter(
                        metricsBuilder.createTimeRecorder(
                                "car_query_delete_time",
                                "Time of query responsible for deleting car"
                        ),
                        metricsBuilder.createCounter(
                                "car_query_delete_count",
                                "Number of executed queries responsible for deleting car"
                        )
                )
        );
    }
}
