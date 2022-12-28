package com.dataart.secondmonth.testcontainers.commons;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@DirtiesContext
public abstract class AbstractTest {

    private static final String RAW_BASE_URL = "http://localhost:%d/api";

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:14-alpine");

    @Autowired
    protected TestRestClient testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.liquibase.contexts", () -> "dev");
    }

    protected String getBaseUrl() {
        return RAW_BASE_URL.formatted(randomServerPort);
    }

    protected String getUrlWithEndpoint(String endpoint) {
        return getBaseUrl().concat(endpoint);
    }

}
