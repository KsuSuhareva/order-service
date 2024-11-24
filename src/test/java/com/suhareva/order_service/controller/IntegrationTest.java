package com.suhareva.order_service.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class IntegrationTest {
    public final static String SELECT_ORDER_BY_NUMBER = "SELECT ID, NUMBER, TOTAL_AMOUNT, ORDER_DATE, DELIVERY_ADDRESS," +
            " CLIENT, PAYMENT_TYPE, DELIVERY_TYPE FROM ORDERS WHERE NUMBER = ?";
}
