package com.suhareva.order_service.repository.order;

import com.suhareva.order_service.dto.OrderNumber;
import com.suhareva.order_service.dto.OrdersByDatePeriodNotProduct;
import com.suhareva.order_service.dto.OrdersByDateTotalAmount;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.model.entity.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.ArrayList;
import java.util.List;

import static com.suhareva.order_service.testEntityFactory.TestEntityFactory.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class OrderRepositoryTest {
    @InjectMocks
    private OrderRepositoryJdbcImpl orderRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Captor
    private ArgumentCaptor<Object[]> objCap;

    @Test
    public void persist_shouldReturnOrderNumber() throws DaoException {
        Order order = buildOrder("1838020241120");
        Mockito.when(jdbcTemplate.update(anyString(), objCap.capture())).thenReturn(1);
        Order orderActual = orderRepository.persist(order);
        Assertions.assertNotNull(orderActual);
        Assertions.assertEquals(order.getNumber(), orderActual.getNumber());
    }

    @Test
    public void getOrderByNumber_shouldReturnOrder() throws DaoException {
        OrderNumber number = buildNumber();
        List<Order> orders = new ArrayList<>();
        Order order = buildOrder(number.getNumber());
        orders.add(order);
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Order>>any(), ArgumentMatchers.any())).thenReturn(orders);
        Order orderActual = orderRepository.getOrderByNumber(number.getNumber());
        Assertions.assertNotNull(orderActual);
        Assertions.assertEquals(order.getNumber(), orderActual.getNumber());
    }

    @Test
    public void getOrdersByDateAndGrAmount_shouldReturnOrders() throws DaoException {
        OrdersByDateTotalAmount order = buildOrderByDateTotalAmount();
        List<Order> orders = buildOrderList();
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Order>>any(), objCap.capture())).thenReturn(orders);
        List<Order> ordersActual = orderRepository.getOrdersByDateAndGrAmount(order.getOrderDate(), order.getOrderDate(), order.getTotalAmount());
        Assertions.assertNotNull(ordersActual);
        Assertions.assertTrue(order.getTotalAmount() < ordersActual.get(0).getTotalAmount());
    }

    @Test
    public void getOrdersByDatePeriod_shouldReturnOrders() throws DaoException {
        OrdersByDatePeriodNotProduct order = buildOrderByDatePeriodNotProduct();
        List<Order> orders = buildOrderList();
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Order>>any(), objCap.capture())).thenReturn(orders);
        List<Order> ordersActual = orderRepository.getOrdersByDatePeriod(order.getFrom(), order.getTo());
        Assertions.assertNotNull(ordersActual);
    }
}
