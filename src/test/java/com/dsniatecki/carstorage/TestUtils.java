package com.dsniatecki.carstorage;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class TestUtils {

    private static final String DB_CONTAINER = "postgres:14-alpine";
    private static final String CAR_TABLE = "car";

    private TestUtils() {
    }

    public static PostgreSQLContainer<?> createDbTestContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse(DB_CONTAINER).asCompatibleSubstituteFor("postgres"));
    }

    public static void registerDbProperties(PostgreSQLContainer<?> dbContainer, DynamicPropertyRegistry registry) {
        int dbPort = dbContainer.getMappedPort(PostgreSQLContainer.POSTGRESQL_PORT);
        registry.add("spring.r2dbc.url", () -> "r2dbc:postgresql://" + dbContainer.getHost() + ":" + dbPort +
                "/" + dbContainer.getDatabaseName());
        registry.add("spring.r2dbc.username", dbContainer::getUsername);
        registry.add("spring.r2dbc.password", dbContainer::getPassword);
        registry.add("spring.flyway.url", dbContainer::getJdbcUrl);
        registry.add("spring.flyway.user", dbContainer::getUsername);
        registry.add("spring.flyway.password", dbContainer::getPassword);
    }

    public static void cleanDb(DatabaseClient databaseClient) {
        databaseClient.sql("DELETE FROM " + CAR_TABLE + " WHERE true")
                .fetch()
                .one()
                .block();
    }
}


