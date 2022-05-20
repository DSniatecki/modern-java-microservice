package com.dsniatecki.carstorage.car;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table("car")
record CarRow(
        @Id @Column("id") String id,
        @Column("brand") String brand,
        @Column("model") String model,
        @Column("produced_at") LocalDate producedAt,
        @Column("created_at") LocalDateTime createdAt,
        @Column("updated_at") @Nullable LocalDateTime updatedAt,
        @Column("is_deleted") boolean isDeleted
) implements Persistable<String> {
    @Override
    public boolean isNew() {
        return updatedAt == null;
    }

    @Override
    public String getId() {
        return id;
    }
}

