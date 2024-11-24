package com.suhareva.order_service.service.order.impl;

import com.suhareva.order_service.dto.*;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.handler.exception.OrderNotFoundException;
import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.repository.order.OrderRepository;
import com.suhareva.order_service.repository.product.ProductRepository;
import com.suhareva.order_service.service.generateNumber.NumberGenerateService;
import com.suhareva.order_service.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final NumberGenerateService generateNumberService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderNumber createOrder(SaveOrder saveOrder) throws DaoException {
        String number = generateNumberService.generate().getNumber();
        String orderNumber = saveOrder(saveOrder, number);
        return new OrderNumber(orderNumber);
    }

    @Override
    @Transactional
    public OrderResponse getOrderByNumber(OrderNumber orderNumber) throws DaoException {
        Order order = orderRepository.getOrderByNumber(orderNumber.getNumber());
        List<Product> productList = productRepository.getProductsByOrderId(order.getId());
        return new OrderResponse(order, productList);
    }

    @Override
    @Transactional
    public List<OrderResponse> getOrdersByDateAndGrTotalAmount(OrdersByDateTotalAmount order) throws DaoException {
        Date from = DateUtils.truncate(order.getOrderDate(), Calendar.DATE);
        Date to = DateUtils.addDays(from, 1);
        List<Order> orders = orderRepository.getOrdersByDateAndGrAmount(from, to, order.getTotalAmount());
        List<Product> products = productRepository.getProductsByOrderIds(orders);
        return getOrderResponseList(orders, products);
    }

    @Override
    @Transactional
    public List<OrderResponse> getOrdersByDatePeriodAndNotProduct(OrdersByDatePeriodNotProduct request) throws DaoException {
        OrderProduct orderProduct = request.getOrderProduct();
        List<Order> orders = orderRepository.getOrdersByDatePeriod(request.getFrom(), request.getTo());
        List<Product> products = productRepository.getProductsByDatePeriod(orders, orderProduct).stream()
                .filter(product -> !orderProduct.equals(new OrderProduct(product)))
                .toList();
        if (orders.isEmpty() || products.isEmpty()) {
            throw new OrderNotFoundException(ORDERS_NOT_FOUND_MESSAGE);
        }
        return getOrderResponseList(orders, products);
    }

    @Transactional
    private String saveOrder(SaveOrder saveOrder, String number) throws DaoException {
        Order result = orderRepository.persist(new Order(saveOrder, number));
        productRepository.persist(saveOrder.takeProductList(), result.getId());
        return number;
    }

    private List<OrderResponse> getOrderResponseList(List<Order> orders, List<Product> products) {
        Map<Long, List<Product>> productMap = new HashMap<>();
        for (Product product : products) {
            long key = product.getOrderId();
            List<Product> values = productMap.getOrDefault(key, new ArrayList<>());
            values.add(product);
            productMap.put(key, values);
        }
        List<OrderResponse> orderResponceList = new ArrayList<>();
        orders.forEach(o -> orderResponceList.add(new OrderResponse(o, productMap.get(o.getId()))));
        return orderResponceList;
    }
}
