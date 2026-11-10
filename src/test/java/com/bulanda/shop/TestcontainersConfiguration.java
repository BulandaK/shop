package com.bulanda.shop;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Izolowana baza PostgreSQL dla testów. {@link ServiceConnection} sam podstawia
 * URL, użytkownika i hasło do datasource, więc testy nie korzystają z bazy z docker-compose.
 * Kontekst Springa jest cache'owany, więc kontener startuje raz na całe uruchomienie testów.
 */
@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgres() {
        return new PostgreSQLContainer("postgres:17");
    }
}
