package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.api.internal.CarsApi;
import com.dsniatecki.carstorage.model.internal.CarDTO;
import com.dsniatecki.carstorage.model.internal.CarDataDTO;
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
    public Mono<ResponseEntity<CarDTO>> getCar(String carId, ServerWebExchange exchange) {
        return carService.get(carId)
                .map(car -> ResponseEntity.ok(InternalMapper.toCarDTO(car)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")));
    }

    @Override
    public Mono<ResponseEntity<Flux<CarDTO>>> getCars(Optional<Set<String>> carIds, ServerWebExchange exchange) {
        return Mono.just(carIds.map(carService::getMultiple).orElseGet(carService::getAll))
                .map(flux -> flux.map(InternalMapper::toCarDTO))
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<CarDTO>> createCar(Mono<CarDataDTO> carDataDTO, ServerWebExchange exchange) {
        return carDataDTO
                .flatMap(dto -> carService.save(InternalMapper.toCarData(dto)))
                .map(car -> ResponseEntity.status(HttpStatus.CREATED).body(InternalMapper.toCarDTO(car)));
    }

    @Override
    public Mono<ResponseEntity<CarDTO>> updateCar(String carId, Mono<CarDataDTO> carDataDTO, ServerWebExchange exchange) {
        return carDataDTO
                .flatMap(dto -> carService.update(carId, InternalMapper.toCarData(dto)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")))
                .map(car -> ResponseEntity.status(HttpStatus.CREATED).body(InternalMapper.toCarDTO(car)));
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteCar(String carId, ServerWebExchange exchange) {
        return carService.delete(carId)
                .map(id -> ResponseEntity.noContent().<Void>build())
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")));
    }
}
