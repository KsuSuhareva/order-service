package com.suhareva.order_service.repository.order;

import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.handler.exception.DaoException;

import java.util.Date;
import java.util.List;

public interface OrderRepository {
    Order persist(Order order) throws DaoException;

    Order getOrderByNumber(String orderNumber) throws DaoException;

    List<Order> getOrdersByDateAndGrAmount(Date from, Date to, Integer totalAmount) throws DaoException;

    List<Order> getOrdersByDatePeriod(Date from, Date to) throws DaoException;
}
