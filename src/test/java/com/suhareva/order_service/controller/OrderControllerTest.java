package com.suhareva.order_service.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suhareva.order_service.dto.*;
import com.suhareva.order_service.model.entity.Order;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.io.IOException;
import java.util.List;

import static com.suhareva.order_service.testEntityFactory.TestEntityFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderControllerTest extends IntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    public static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start(9080);
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void saveOrder_returnOrderNumber() throws Exception {
        SaveOrder saveOrder = buildSaveOrder();
        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(buildNumber()))
                .addHeader("Content-Type", "application/json"));

        MvcResult mvcResult = mockMvc.perform(post("/orders/saveOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveOrder)))
                .andExpect(status().is(HttpStatus.ACCEPTED.value()))
                .andDo(print())
                .andReturn();
        OrderNumber actualNumber = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                OrderNumber.class);
        RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);
        Order order = jdbcTemplate.query(SELECT_ORDER_BY_NUMBER, rowMapper, actualNumber.getNumber()).get(0);
        assertEquals(actualNumber.getNumber(), order.getNumber());
    }

    @Test
    public void saveOrder_throwValidExceptionTotalAmount() throws Exception {
        SaveOrder saveOrder = buildSaveOrder();
        saveOrder.setTotalAmount(-1);
        mockMvc.perform(post("/orders/saveOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveOrder)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorMessage").value("totalAmount must be greater than or equal to 0"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void saveOrder_throwValidExceptionDeliveryAddress() throws Exception {
        SaveOrder saveOrder = buildSaveOrder();
        saveOrder.setDeliveryAddress(null);
        mockMvc.perform(post("/orders/saveOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saveOrder)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorMessage").value("deliveryAddress must not be null"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrderByNumber_returnOrder() throws Exception {
        OrderNumber orderNumber = new OrderNumber("1838020241120");
        MvcResult mvcResult = mockMvc.perform(post("/orders/getOrderByNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumber)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print())
                .andReturn();
        Order actualOrder = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                Order.class);
        assertEquals(actualOrder.getNumber(), orderNumber.getNumber());
    }

    @Test
    public void getOrderByNumber_throwOrderNotFound() throws Exception {
        OrderNumber orderNumber = new OrderNumber("9838020241120");
        mockMvc.perform(post("/orders/getOrderByNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumber)))
                .andExpect(status().is(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.errorName").value("OrderNotFoundException"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrderByNumber_throwValidExceptionNumber() throws Exception {
        OrderNumber orderNumber = new OrderNumber("18380202411200");
        mockMvc.perform(post("/orders/getOrderByNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumber)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorMessage").value("number size must be between 0 and 13"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrderByNumber_throwValidExceptionNumberNull() throws Exception {
        OrderNumber orderNumber = new OrderNumber();
        mockMvc.perform(post("/orders/getOrderByNumber")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderNumber)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("HttpMessageNotReadableException"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: number is marked non-null but is null"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrdersByDateAndGrTotalAmount_returnOrderList() throws Exception {
        OrdersByDateTotalAmount orderRequest = buildOrderByDateTotalAmount();
        MvcResult mvcResult = mockMvc.perform(post("/orders/getOrdersByDateAndGrTotalAmount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andDo(print())
                .andReturn();
        TypeReference<List<OrderResponse>> type = new TypeReference<>() {
        };
        List<OrderResponse> actualOrders = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                type);
        assertNotNull(actualOrders.get(0));
        assertTrue(orderRequest.getTotalAmount() < actualOrders.get(0).getTotalAmount());
    }

    @Test
    public void getOrdersByDateAndGrTotalAmount_throwValidExceptionAmount() throws Exception {
        OrdersByDateTotalAmount orderRequest = buildOrderByDateTotalAmount();
        orderRequest.setTotalAmount(-2);
        mockMvc.perform(post("/orders/getOrdersByDateAndGrTotalAmount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("MethodArgumentNotValidException"))
                .andExpect(jsonPath("$.errorMessage").value("totalAmount must be greater than or equal to 0"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrdersByDateAndGrTotalAmount_throwValidExceptionDate() throws Exception {
        OrdersByDateTotalAmount orderRequest = new OrdersByDateTotalAmount();
        orderRequest.setTotalAmount(-2);
        mockMvc.perform(post("/orders/getOrdersByDateAndGrTotalAmount")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("HttpMessageNotReadableException"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: orderDate is marked non-null but is null"))
                .andDo(print())
                .andReturn();
    }

    @Test
    public void getOrdersByDatePeriodAndNotProduct_returnOrderList() throws Exception {
        OrdersByDatePeriodNotProduct orderRequest = buildOrderByDatePeriodNotProduct();
        MvcResult mvcResult = mockMvc.perform(post("/orders/getOrdersByDatePeriodAndNotProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().is(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.length()").value(2))
                .andDo(print())
                .andReturn();
        TypeReference<List<OrderResponse>> typeRef = new TypeReference<>() {
        };
        List<OrderResponse> actualOrders = objectMapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                typeRef);
        assertNotNull(actualOrders.get(0));
        assertNotEquals(orderRequest.getOrderProduct().getArticle(), actualOrders.get(0).getOrderProducts().get(0).getArticle());
        assertNotEquals(orderRequest.getOrderProduct().getName(), actualOrders.get(0).getOrderProducts().get(0).getName());
        assertNotEquals(orderRequest.getOrderProduct().getAmount(), actualOrders.get(0).getOrderProducts().get(0).getAmount());
        assertNotEquals(orderRequest.getOrderProduct().getQuantity(), actualOrders.get(0).getOrderProducts().get(0).getQuantity());
    }

    @Test
    public void getOrdersByDatePeriodAndNotProduct_throwValidExceptionDate() throws Exception {
        OrdersByDatePeriodNotProduct orderRequest = new OrdersByDatePeriodNotProduct();
        MvcResult mvcResult = mockMvc.perform(post("/orders/getOrdersByDatePeriodAndNotProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.errorName").value("HttpMessageNotReadableException"))
                .andExpect(jsonPath("$.errorMessage").value("JSON parse error: from is marked non-null but is null"))
                .andDo(print())
                .andReturn();
    }
}
