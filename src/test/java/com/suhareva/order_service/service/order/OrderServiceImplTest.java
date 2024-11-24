package com.suhareva.order_service.service.order;

import com.suhareva.order_service.dto.OrderNumber;
import com.suhareva.order_service.dto.OrderProduct;
import com.suhareva.order_service.dto.OrderResponse;
import com.suhareva.order_service.dto.OrdersByDatePeriodNotProduct;
import com.suhareva.order_service.dto.OrdersByDateTotalAmount;
import com.suhareva.order_service.dto.SaveOrder;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.handler.exception.OrderNotFoundException;
import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.repository.order.OrderRepository;
import com.suhareva.order_service.repository.product.ProductRepository;
import com.suhareva.order_service.service.generateNumber.NumberGenerateService;
import com.suhareva.order_service.service.order.impl.OrderServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;

import static com.suhareva.order_service.testEntityFactory.TestEntityFactory.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {
    @InjectMocks
    private OrderServiceImpl orderService;
    @Mock
    private NumberGenerateService generateNumberService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;

    @Test
    public void createOrder_shouldReturnOrderNumber() throws DaoException {
        SaveOrder saveOrder = buildSaveOrder();
        OrderNumber number = buildNumber();
        Order order = buildOrder(number.getNumber());
        Mockito.when(generateNumberService.generate()).thenReturn(number);
        Mockito.when(orderRepository.persist(any(Order.class))).thenReturn(order);
        Mockito.when(productRepository.persist(anyList(), any(Long.class))).thenReturn(buildListProduct());
        OrderNumber result = orderService.createOrder(saveOrder);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(number, result);
    }

    @Test
    public void createOrder_throwDaoExceptionOrder() throws DaoException {
        SaveOrder saveOrder = buildSaveOrder();
        OrderNumber number = buildNumber();
        Mockito.when(generateNumberService.generate()).thenReturn(number);
        Mockito.when(orderRepository.persist(any(Order.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.createOrder(saveOrder));
    }

    @Test
    public void createOrder_throwDaoExceptionProduct() throws DaoException {
        SaveOrder saveOrder = buildSaveOrder();
        OrderNumber number = buildNumber();
        Order order = buildOrder(number.getNumber());
        Mockito.when(generateNumberService.generate()).thenReturn(number);
        Mockito.when(orderRepository.persist(any(Order.class))).thenReturn(order);
        Mockito.when(productRepository.persist(anyList(), any(Long.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.createOrder(saveOrder));
    }

    @Test
    public void orderByNumber_shouldReturnOrderResponse() throws DaoException {
        OrderNumber number = buildNumber();
        Order order = buildOrder(number.getNumber());
        Mockito.when(orderRepository.getOrderByNumber(any(String.class))).thenReturn(order);
        Mockito.when(productRepository.getProductsByOrderId(any(Long.class))).thenReturn(buildListProduct());
        OrderResponse response = buildOrderResponse();
        OrderResponse result = orderService.getOrderByNumber(number);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(response.getNumber(), result.getNumber());
        Assertions.assertEquals(response.getOrderProducts(), result.getOrderProducts());
        Assertions.assertEquals(response.getOrderProducts(), result.getOrderProducts());
        Assertions.assertEquals(response.getDeliveryType(), result.getDeliveryType());
        Assertions.assertEquals(response.getDeliveryAddress(), result.getDeliveryAddress());
        Assertions.assertEquals(response.getTotalAmount(), result.getTotalAmount());
    }

    @Test
    public void orderByNumber_throwOrderNotFoundException() throws DaoException {
        OrderNumber number = buildNumber();
        Mockito.when(orderRepository.getOrderByNumber(any(String.class))).thenThrow(OrderNotFoundException.class);
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrderByNumber(number));
    }

    @Test
    public void orderByNumber_throwDaoExceptionOrder() throws DaoException {
        OrderNumber number = buildNumber();
        Mockito.when(orderRepository.getOrderByNumber(any(String.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrderByNumber(number));
    }

    @Test
    public void orderByNumber_throwDaoExceptionProduct() throws DaoException {
        OrderNumber number = buildNumber();
        Mockito.when(orderRepository.getOrderByNumber(any(String.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrderByNumber(number));
    }

    @Test
    public void ordersByDateAndGrTotalAmount_shouldReturnOrderResponseList() throws DaoException {
        OrdersByDateTotalAmount ordersByDateTotalAmount = buildOrderByDateTotalAmount();
        List<Order> orders = buildOrderList();
        Mockito.when(orderRepository.getOrdersByDateAndGrAmount(any(Date.class), any(Date.class), any(Integer.class))).thenReturn(orders);
        Mockito.when(productRepository.getProductsByOrderIds(any(List.class))).thenReturn(buildListProduct());
        List<OrderResponse> result = orderService.getOrdersByDateAndGrTotalAmount(ordersByDateTotalAmount);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.size(), builOrderResponseList().size());
        Assertions.assertEquals(2, result.size());
        Assertions.assertTrue(result.get(0).getTotalAmount() > ordersByDateTotalAmount.getTotalAmount());
        Assertions.assertTrue(result.get(1).getTotalAmount() > ordersByDateTotalAmount.getTotalAmount());
    }

    @Test
    public void ordersByDateAndGrTotalAmount_throwOrderNotFoundException() throws DaoException {
        OrdersByDateTotalAmount ordersByDateTotalAmount = buildOrderByDateTotalAmount();
        Mockito.when(orderRepository.getOrdersByDateAndGrAmount(any(Date.class), any(Date.class), any(Integer.class))).thenThrow(OrderNotFoundException.class);
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByDateAndGrTotalAmount(ordersByDateTotalAmount));
    }

    @Test
    public void ordersByDateAndGrTotalAmount_throwDaoExceptionOrder() throws DaoException {
        OrdersByDateTotalAmount ordersByDateTotalAmount = buildOrderByDateTotalAmount();
        Mockito.when(orderRepository.getOrdersByDateAndGrAmount(any(Date.class), any(Date.class), any(Integer.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrdersByDateAndGrTotalAmount(ordersByDateTotalAmount));
    }

    @Test
    public void ordersByDateAndGrTotalAmount_throwDaoExceptionProduct() throws DaoException {
        OrdersByDateTotalAmount ordersByDateTotalAmount = buildOrderByDateTotalAmount();
        List<Order> orders = buildOrderList();
        Mockito.when(orderRepository.getOrdersByDateAndGrAmount(any(Date.class), any(Date.class), any(Integer.class))).thenReturn(orders);
        Mockito.when(productRepository.getProductsByOrderIds(any(List.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrdersByDateAndGrTotalAmount(ordersByDateTotalAmount));
    }

    @Test
    public void ordersByDateNotLkeProduct_shouldReturnOrderResponseList() throws DaoException {
        OrdersByDatePeriodNotProduct orderRequest = buildOrderByDatePeriodNotProduct();
        List<Order> orders = buildOrderList();
        Mockito.when(orderRepository.getOrdersByDatePeriod(any(Date.class), any(Date.class))).thenReturn(orders);
        Mockito.when(productRepository.getProductsByDatePeriod(any(List.class), any(OrderProduct.class))).thenReturn(buildListProduct());
        List<OrderResponse> result = orderService.getOrdersByDatePeriodAndNotProduct(orderRequest);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(2, result.size());
        Assertions.assertNotEquals(orderRequest.getOrderProduct(), result.get(0).getOrderProducts().get(0));
        Assertions.assertNotEquals(orderRequest.getOrderProduct(), result.get(1).getOrderProducts().get(0));
    }

    @Test
    public void ordersByDateNotLkeProduct_throwOrderNotFoundException() throws DaoException {
        OrdersByDatePeriodNotProduct orderRequest = buildOrderByDatePeriodNotProduct();
        Mockito.when(orderRepository.getOrdersByDatePeriod(any(Date.class), any(Date.class))).thenThrow(OrderNotFoundException.class);
        Assertions.assertThrows(OrderNotFoundException.class, () -> orderService.getOrdersByDatePeriodAndNotProduct(orderRequest));
    }

    @Test
    public void ordersByDateNotLkeProduct_throwDaoExceptionOrder() throws DaoException {
        OrdersByDatePeriodNotProduct orderRequest = buildOrderByDatePeriodNotProduct();
        Mockito.when(orderRepository.getOrdersByDatePeriod(any(Date.class), any(Date.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrdersByDatePeriodAndNotProduct(orderRequest));
    }

    @Test
    public void ordersByDateNotLkeProduct_throwDaoExceptionProduct() throws DaoException {
        OrdersByDatePeriodNotProduct orderRequest = buildOrderByDatePeriodNotProduct();
        List<Order> orders = buildOrderList();
        Mockito.when(orderRepository.getOrdersByDatePeriod(any(Date.class), any(Date.class))).thenReturn(orders);
        Mockito.when(productRepository.getProductsByDatePeriod(any(List.class), any(OrderProduct.class))).thenThrow(DaoException.class);
        Assertions.assertThrows(DaoException.class, () -> orderService.getOrdersByDatePeriodAndNotProduct(orderRequest));
    }
}