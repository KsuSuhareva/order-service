package com.suhareva.order_service.repository.product;

import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.dto.OrderProduct;
import com.suhareva.order_service.handler.exception.DaoException;

import java.util.List;

public interface ProductRepository {
    List<Product> persist(List<Product> productList, Long orderId) throws DaoException;

    List<Product> getProductsByOrderId(Long orderId) throws DaoException;

    List<Product> getProductsByOrderIds(List<Order> orders) throws DaoException;

    List<Product> getProductsByDatePeriod(List<Order> orders, OrderProduct product) throws DaoException;
}
