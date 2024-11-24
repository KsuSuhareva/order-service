package com.suhareva.order_service.service.order;

import com.suhareva.order_service.dto.*;
import com.suhareva.order_service.handler.exception.DaoException;

import java.util.List;

public interface OrderService {
    String INTERNAL_SERVER_ERROR_MESSAGE = "Сервис не отвечает";
    String ORDERS_NOT_FOUND_MESSAGE = "No orders found";
    String ORDER_NOT_FOUND_MESSAGE = "Order not found with order number ";

    OrderNumber createOrder(SaveOrder request) throws DaoException;

    OrderResponse getOrderByNumber(OrderNumber orderNumber) throws DaoException;

    List<OrderResponse> getOrdersByDateAndGrTotalAmount(OrdersByDateTotalAmount order) throws DaoException;

    List<OrderResponse> getOrdersByDatePeriodAndNotProduct(OrdersByDatePeriodNotProduct order) throws DaoException;
}
