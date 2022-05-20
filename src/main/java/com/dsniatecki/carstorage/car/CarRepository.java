package com.dsniatecki.carstorage.car;


import com.dsniatecki.carstorage.utils.Metrics;
import com.dsniatecki.carstorage.utils.TimeRecorder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;


public record CarRepository(
        CarRowRepository carRowRepository,
        TimeRecorder findTimeRecorder,
        TimeRecorder findMultipleTimeRecorder,
        TimeRecorder findAllTimeRecorder,
        TimeRecorder saveTimeRecorder,
        TimeRecorder deleteTimeRecorder
) {

    Mono<Car> findById(String objectId) {
        return Metrics.recorded(carRowRepository.findById(objectId), findTimeRecorder)
                .map(this::mapFromRow);
    }

    Flux<Car> findByIds(Set<String> objectIds) {
        return Metrics.recorded(carRowRepository.findByIds(objectIds), findMultipleTimeRecorder)
                .map(this::mapFromRow);
    }

    Flux<Car> findAll() {
        return Metrics.recorded(carRowRepository.findAll(), findAllTimeRecorder)
                .map(this::mapFromRow);
    }

    Mono<Car> save(Car car) {
        return Metrics.recorded(carRowRepository.save(mapToRow(car)), saveTimeRecorder)
                .map(this::mapFromRow);
    }

    Mono<Void> delete(String objectId, LocalDateTime deleted_at) {
        return Metrics.recorded(carRowRepository.delete(objectId, deleted_at), deleteTimeRecorder);
    }

    private Car mapFromRow(CarRow row) {
        return new Car(
                row.id(),
                row.brand(),
                row.model(),
                row.producedAt(),
                row.createdAt(),
                Optional.ofNullable(row.updatedAt())
        );
    }

    private CarRow mapToRow(Car car) {
        return new CarRow(
                car.id(),
                car.brand(),
                car.model(),
                car.producedAt(),
                car.createdAt(),
                car.updatedAt().orElse(null),
                false
        );
    }
}
