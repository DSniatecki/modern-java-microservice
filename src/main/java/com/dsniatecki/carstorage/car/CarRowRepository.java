package com.dsniatecki.carstorage.car;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Set;

public interface CarRowRepository extends ReactiveCrudRepository<CarRow, String> {

    @Override
    @Query("SELECT * FROM car WHERE id = :id AND is_deleted = FALSE")
    Mono<CarRow> findById(String id);

    @Query("SELECT * FROM car WHERE id IN (:ids) AND is_deleted = FALSE")
    Flux<CarRow> findByIds(Set<String> ids);

    @Override
    @Query("SELECT * FROM car WHERE is_deleted = FALSE")
    Flux<CarRow> findAll();

    @Query("UPDATE car SET updated_at = :deleted_at, is_deleted = TRUE WHERE id = :id AND is_deleted = FALSE")
    Mono<Void> delete(String id, LocalDateTime deleted_at);
}
