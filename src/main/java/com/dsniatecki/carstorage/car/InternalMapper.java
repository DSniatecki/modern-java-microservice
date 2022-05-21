package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.model.internal.CarDTO;
import com.dsniatecki.carstorage.model.internal.CarDataDTO;

class InternalMapper {

    private InternalMapper() {
    }

    static CarData toCarData(CarDataDTO carDataDTO) {
        return new CarData(carDataDTO.getBrand(), carDataDTO.getModel(), carDataDTO.getProducedAt());
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
