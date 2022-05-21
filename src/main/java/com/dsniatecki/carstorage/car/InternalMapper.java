package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.model.internal.CarDataDto;
import com.dsniatecki.carstorage.model.internal.CarDto;

class InternalMapper {

    private InternalMapper() {
    }

    static CarData toCarData(CarDataDto carDataDto) {
        return new CarData(carDataDto.getBrand(), carDataDto.getModel(), carDataDto.getProducedAt());
    }

    static CarDto toCarDto(Car car) {
        return new CarDto()
                .id(car.id())
                .brand(car.brand())
                .model(car.model())
                .producedAt(car.producedAt())
                .createdAt(car.createdAt())
                .updatedAt(car.updatedAt().orElse(null));
    }
}
