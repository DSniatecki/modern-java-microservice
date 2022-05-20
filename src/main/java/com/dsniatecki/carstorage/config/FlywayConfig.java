package com.dsniatecki.carstorage.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

public class FlywayConfig {
    private final FluentConfiguration fluentConfiguration;

    FlywayConfig(
            @Value("${spring.flyway.url}") String url,
            @Value("${spring.flyway.user}") String user,
            @Value("${spring.flyway.password}") String password,
            @Value("${spring.flyway.locations}") String locations
    ) {
        this.fluentConfiguration = Flyway.configure()
                .baselineOnMigrate(true)
                .locations(locations)
                .dataSource(url, user, password);
    }

    @Bean(initMethod = "migrate")
    Flyway flyway() {
        return new Flyway(fluentConfiguration);
    }
}
