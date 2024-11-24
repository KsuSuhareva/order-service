package com.suhareva.order_service.repository.product;

import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.dto.OrderProduct;
import com.suhareva.order_service.handler.exception.DaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.suhareva.order_service.repository.constants.SqlRequest.*;
import static com.suhareva.order_service.service.order.impl.OrderServiceImpl.INTERNAL_SERVER_ERROR_MESSAGE;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Product> rowMapper = new BeanPropertyRowMapper<>(Product.class);

    @Override
    @Transactional
    public List<Product> persist(List<Product> productList, Long orderId) throws DaoException {
        try {
            int[] updateRows = jdbcTemplate.batchUpdate(INSERT_PRODUCT,
                    new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            Product product = productList.get(i);
                            ps.setLong(1, product.getId());
                            ps.setLong(2, product.getArticle());
                            ps.setString(3, product.getName());
                            ps.setInt(4, product.getQuantity());
                            ps.setLong(5, product.getAmount());
                            ps.setLong(6, orderId);
                        }

                        @Override
                        public int getBatchSize() {
                            return productList.size();
                        }
                    });
            if (updateRows.length == 0) {
                throw new DaoException(INTERNAL_SERVER_ERROR_MESSAGE);
            }
            return productList;
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> getProductsByOrderId(Long orderId) throws DaoException {
        try {
            return jdbcTemplate.query(SELECT_PRODUCT_LIST_BY_ORDER_ID, rowMapper, orderId);
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> getProductsByOrderIds(List<Order> orders) throws DaoException {
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        try {
            return jdbcTemplate.query(buildSelectProductsByOrderIds(orderIds.size()), rowMapper, orderIds.toArray());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public List<Product> getProductsByDatePeriod(List<Order> orders, OrderProduct product) throws DaoException {
        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        try {
            return jdbcTemplate.query(buildSelectProductsByOrderIds(orderIds.size()), rowMapper,
                    orderIds.toArray());
        } catch (DataAccessException e) {
            throw new DaoException(e);
        }
    }
}
