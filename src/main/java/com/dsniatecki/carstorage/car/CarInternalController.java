package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.api.internal.CarsApi;
import com.dsniatecki.carstorage.model.internal.CarDataDto;
import com.dsniatecki.carstorage.model.internal.CarDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/api/internal")
class CarInternalController implements CarsApi {

    private final CarService carService;

    CarInternalController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public Mono<ResponseEntity<CarDto>> getCar(String carId, ServerWebExchange exchange) {
        return carService.get(carId)
                .map(car -> ResponseEntity.ok(InternalMapper.toCarDto(car)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")));
    }

    @Override
    public Mono<ResponseEntity<Flux<CarDto>>> getCars(Optional<Set<String>> carIds, ServerWebExchange exchange) {
        return Mono.just(carIds.map(carService::getMultiple).orElseGet(carService::getAll))
                .map(flux -> flux.map(InternalMapper::toCarDto))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CarDto>> createCar(Mono<CarDataDto> carDataDto, ServerWebExchange exchange) {
        return carDataDto
                .flatMap(Dto -> carService.save(InternalMapper.toCarData(Dto)))
                .map(car -> ResponseEntity.status(HttpStatus.CREATED).body(InternalMapper.toCarDto(car)));
    }

    @Override
    public Mono<ResponseEntity<CarDto>> updateCar(String carId, Mono<CarDataDto> carDataDto, ServerWebExchange exchange) {
        return carDataDto
                .flatMap(Dto -> carService.update(carId, InternalMapper.toCarData(Dto)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")))
                .map(car -> ResponseEntity.status(HttpStatus.CREATED).body(InternalMapper.toCarDto(car)));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCar(String carId, ServerWebExchange exchange) {
        return carService.delete(carId)
                .map(id -> ResponseEntity.noContent().<Void>build())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")));
    }
}
