package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.utils.TimeSupplier;
import com.dsniatecki.carstorage.utils.Utils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.Optional;
import java.util.Set;

public record CarService(
        CarRepository carRepository,
        TimeSupplier timeSupplier
) {

    Mono<Car> get(String carId) {
        return carRepository.findById(carId);
    }

    Flux<Car> getMultiple(Set<String> carIds) {
        return carRepository.findByIds(carIds)
                .sort(Comparator.comparing(Car::createdAt));
    }

    Flux<Car> getAll() {
        return carRepository.findAll()
                .sort(Comparator.comparing(Car::createdAt));
    }

    Mono<Car> save(CarData carData) {
        return carRepository.save(createNewCar(carData));
    }

    Mono<Car> update(String carId, CarData carData) {
        return carRepository.findById(carId)
                .flatMap(car -> carRepository.save(updateCar(car, carData)));
    }


    Mono<String > delete(String carId) {
        return carRepository.findById(carId)
                .flatMap(car -> carRepository.delete(carId, timeSupplier.now())
                        .map(value -> carId)
                        .switchIfEmpty(Mono.just(carId))
                );
    }

    private Car createNewCar(CarData carData) {
        return new Car(
                Utils.generateId(),
                carData.brand(),
                carData.model(),
                carData.producedAt(),
                timeSupplier.now(),
                Optional.empty()
        );
    }

    private Car updateCar(Car car, CarData carData) {
        return new Car(
                car.id(),
                carData.brand(),
                carData.model(),
                carData.producedAt(),
                car.createdAt(),
                Optional.of(timeSupplier.now())
        );
    }
}
