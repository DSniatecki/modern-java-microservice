package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.model.pub.CarDTO;

class PublicMapper {

    private PublicMapper() {
    }

    static CarDTO toCarDTO(Car car) {
        return new CarDTO()
                .id(car.id())
                .brand(car.brand())
                .model(car.model())
                .producedAt(car.producedAt())
                .createdAt(car.createdAt())
                .updatedAt(car.updatedAt().orElse(null));
    }
}
