package com.dsniatecki.carstorage.car;

import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = {"/api/public"})
record CarPublicController(
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
}
