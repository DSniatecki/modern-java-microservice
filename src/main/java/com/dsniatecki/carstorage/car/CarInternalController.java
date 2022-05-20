package com.dsniatecki.carstorage.car;

import org.reactivestreams.Publisher;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/internal"})
record CarInternalController(
        CarService carService
) {

    @GetMapping(value = {"/cars/{carId}"}, produces = {APPLICATION_JSON_VALUE})
    Publisher<Car> getCar(@PathVariable(name = "carId") String carId) {
        return carService.get(carId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")))
                .onErrorMap(NoSuchElementException.class, exc -> new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage()));
    }

    @GetMapping(value = {"/cars"}, produces = {APPLICATION_JSON_VALUE})
    Publisher<Car> getCars(@RequestParam(name = "carIds", required = false) Optional<Set<String>> carIds) {
        return carIds.map(carService::getMultiple).orElseGet(carService::getAll);
    }

    @PostMapping(value = {"/cars"}, consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    @ResponseStatus(HttpStatus.CREATED)
    Publisher<Car> createCar(@RequestBody CarData newCarData) {
        return handleErrors(Mono.just(newCarData)
                .map(CarDataValidator::validate)
                .flatMap(carService::save)
        );
    }

    @PutMapping(value = {"/cars/{carId}"}, consumes = {APPLICATION_JSON_VALUE}, produces = {APPLICATION_JSON_VALUE})
    Publisher<Car> updateCar(@PathVariable(name = "carId") String carId, @RequestBody CarData carData) {
        return handleErrors(Mono.just(carData)
                .map(CarDataValidator::validate)
                .flatMap(data -> carService.update(carId, data))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")))
        );
    }

    @DeleteMapping(value = {"/cars/{carId}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    Publisher<String> deleteCar(@PathVariable(name = "carId") String carId) {
        return handleErrors(carService.delete(carId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")))
        );
    }

    private <T> Mono<T> handleErrors(Mono<T> mono) {
        return mono.onErrorMap(NoSuchElementException.class, exc -> new ResponseStatusException(HttpStatus.NOT_FOUND, exc.getMessage()))
                .onErrorMap(IllegalStateException.class, exc -> new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage()))
                .onErrorMap(IllegalArgumentException.class, exc -> new ResponseStatusException(HttpStatus.BAD_REQUEST, exc.getMessage()))
                .onErrorMap(DataIntegrityViolationException.class, exc -> new ResponseStatusException(HttpStatus.CONFLICT, exc.getMessage()));
    }
}
