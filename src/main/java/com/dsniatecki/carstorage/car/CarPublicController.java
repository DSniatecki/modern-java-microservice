package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.api.pub.CarsApi;
import com.dsniatecki.carstorage.model.pub.CarDto;
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
@RequestMapping(path = "/api/public")
class CarPublicController implements CarsApi {

    private final CarService carService;

    CarPublicController(CarService carService) {
        this.carService = carService;
    }

    @Override
    public Mono<ResponseEntity<CarDto>> getCar(String carId, ServerWebExchange exchange) {
        return carService.get(carId)
                .map(car -> ResponseEntity.ok(PublicMapper.toCarDto(car)))
                .switchIfEmpty(Mono.error(new NoSuchElementException("Car with id: '" + carId + "' does not exist.")));
    }

    @Override
    public Mono<ResponseEntity<Flux<CarDto>>> getCars(Optional<Set<String>> carIds, ServerWebExchange exchange) {
        return Mono.just(carIds.map(carService::getMultiple).orElseGet(carService::getAll))
                .map(flux -> flux.map(PublicMapper::toCarDto))
                .map(ResponseEntity::ok);
    }
}
