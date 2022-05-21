package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.model.pub.CarDto;

class PublicMapper {

    private PublicMapper() {
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
