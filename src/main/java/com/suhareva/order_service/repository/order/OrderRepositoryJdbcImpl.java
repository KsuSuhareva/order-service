package com.suhareva.order_service.repository.order;

import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.handler.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

import static com.suhareva.order_service.repository.constants.SqlRequest.*;
import static com.suhareva.order_service.service.order.impl.OrderServiceImpl.*;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryJdbcImpl implements com.suhareva.order_service.repository.order.OrderRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Order> rowMapper = new BeanPropertyRowMapper<>(Order.class);

    @Override
    public Order persist(Order order) throws DaoException {
        try {
            int result = jdbcTemplate.update(INSERT_ORDER, order.getId(), order.getNumber(), order.getTotalAmount(), order.getOrderDate(),
                    order.getDeliveryAddress(), order.getClient(), order.getPaymentType(), order.getDeliveryType());
            if (result == 0) {
                throw new DaoException(INTERNAL_SERVER_ERROR_MESSAGE);
            }
            return order;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public Order getOrderByNumber(String number) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_ORDER_BY_NUMBER, rowMapper, number)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new OrderNotFoundException(ORDER_NOT_FOUND_MESSAGE + number));
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> getOrdersByDateAndGrAmount(Date from, Date to, Integer totalAmount) throws DaoException {
        try {
            List<Order> orders = jdbcTemplate.query(SELECT_ORDER_BY_DATE_AND_GREAT_TOTAL_AMOUNT, rowMapper, from, to, totalAmount);
            if (orders.isEmpty()) {
                throw new OrderNotFoundException(ORDERS_NOT_FOUND_MESSAGE);
            }
            return orders;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Order> getOrdersByDatePeriod(Date from, Date to) throws DaoException {
        try {
            List<Order> orders = jdbcTemplate.query(SELECT_ORDER_BY_PERIOD_DATE, rowMapper, from, to);
            if (orders.isEmpty()) {
                throw new OrderNotFoundException(ORDERS_NOT_FOUND_MESSAGE);
            }
            return orders;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
