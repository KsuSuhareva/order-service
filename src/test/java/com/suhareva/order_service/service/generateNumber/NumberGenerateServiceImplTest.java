package com.suhareva.order_service.service.generateNumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhareva.order_service.dto.OrderNumber;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.service.generateNumber.impl.NumberGenerateServiceImpl;
import lombok.SneakyThrows;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static com.suhareva.order_service.testEntityFactory.TestEntityFactory.buildNumber;


public class NumberGenerateServiceImplTest {

    private static NumberGenerateServiceImpl service;
    private static ObjectMapper objectMapper;
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
        objectMapper = new ObjectMapper();
        String baseUrl = String.format("http://localhost:%s", mockWebServer.getPort());
        service = new NumberGenerateServiceImpl(WebClient.builder().baseUrl(baseUrl).build());
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    @SneakyThrows
    public void generate_shouldReturnOrderNumber() throws DaoException {
        OrderNumber number = buildNumber();
        mockWebServer.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(buildNumber()))
                .addHeader("Content-Type", "application/json"));
        OrderNumber result = service.generate();
        Assertions.assertNotNull(result);
        Assertions.assertEquals(number, result);
    }
}
