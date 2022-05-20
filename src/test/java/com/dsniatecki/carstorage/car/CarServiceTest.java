package com.dsniatecki.carstorage.car;

import com.dsniatecki.carstorage.CarStorageApplication;
import com.dsniatecki.carstorage.TestUtils;
import com.dsniatecki.carstorage.utils.Utils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(classes = {CarStorageApplication.class})
@Testcontainers
class CarServiceTest {

    @Autowired
    private CarService carService;
    @Autowired
    private DatabaseClient databaseClient;

    @Container
    private static final PostgreSQLContainer<?> DB_CONTAINER = TestUtils.createDbTestContainer();

    @DynamicPropertySource
    private static void registerProperties(DynamicPropertyRegistry registry) {
        TestUtils.registerDbProperties(DB_CONTAINER, registry);
    }

    @AfterEach
    void clean() {
        TestUtils.cleanDb(databaseClient);
    }

    @Test
    @DisplayName("Should not get car when db is empty")
    void shouldNotGetCarWhenDbIsEmpty() {
        assertThat(carService.get(Utils.generateId()).block()).isNull();
    }

    @Test
    @DisplayName("Should not get car when car with given id doesn't exist")
    void shouldNotGetCarWhenCarWithGivenIdDoesntExist() {
        carService.save(createTestCarData("Brand1")).block();
        carService.save(createTestCarData("Brand2")).block();
        assertThat(carService.get(Utils.generateId()).block()).isNull();
    }

    @Test
    @DisplayName("Should get car")
    void shouldGetCar() {
        Car savedCar = carService.save(createTestCarData()).block();
        assertThat(carService.get(savedCar.id()).block()).isEqualTo(savedCar);
    }

    @Test
    @DisplayName("Should get multiple cars")
    void shouldGetMultipleCars() {
        Car savedCar1 = carService.save(createTestCarData("Brand1")).block();
        Car savedCar2 = carService.save(createTestCarData("Brand2")).block();
        assertThat(toList(carService.getMultiple(Set.of(savedCar1.id(), savedCar2.id()))))
                .isEqualTo(List.of(savedCar1, savedCar2));
    }

    @Test
    @DisplayName("Should get all cars")
    void shouldGetAllCars() {
        Car savedCar1 = carService.save(createTestCarData("Brand1")).block();
        Car savedCar2 = carService.save(createTestCarData("Brand2")).block();
        assertThat(toList(carService.getAll())).isEqualTo(List.of(savedCar1, savedCar2));
    }

    @Test
    @DisplayName("Should update car")
    void shouldUpdateCar() {
        Car savedCar = carService.save(createTestCarData()).block();
        CarData updateData = new CarData("UpdatedBrand", "UpdatedModel", savedCar.producedAt().plusYears(1));
        Car updatedCar = carService.update(savedCar.id(), updateData).block();
        assertThat(updatedCar).isEqualTo(new Car(
                savedCar.id(),
                updateData.brand(),
                updateData.model(),
                updatedCar.producedAt(),
                savedCar.createdAt(),
                updatedCar.updatedAt()
        ));
        assertThat(updatedCar.updatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should delete car")
    void shouldDeleteCar() {
        Car savedCar = carService.save(createTestCarData()).block();
        carService.delete(savedCar.id()).block();
        assertThat(carService.get(savedCar.id()).block()).isNull();
        assertThat(toList(carService.getMultiple(Set.of(savedCar.id())))).isEqualTo(List.of());
        assertThat(toList(carService.getAll())).isEqualTo(List.of());
    }

    private CarData createTestCarData() {
        return createTestCarData("SuperBrand");
    }

    private CarData createTestCarData(String brand) {
        return new CarData(brand, "SuperModel", LocalDate.of(1998, 5, 5));
    }

    private <T> List<T> toList(Flux<T> flux) {
        return flux.toStream().toList();
    }
}