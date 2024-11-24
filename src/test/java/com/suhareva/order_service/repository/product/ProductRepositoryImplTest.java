package com.suhareva.order_service.repository.product;

import com.suhareva.order_service.dto.OrdersByDatePeriodNotProduct;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static com.suhareva.order_service.testEntityFactory.TestEntityFactory.*;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
public class ProductRepositoryImplTest {
    @InjectMocks
    private ProductRepositoryImpl productRepository;
    @Mock
    private JdbcTemplate jdbcTemplate;
    @Captor
    private ArgumentCaptor<Object[]> objCap;

    @Test
    public void persist_shouldReturnOrderNumber() throws DaoException {
        List<Product> products = buildListProduct();
        Mockito.when(jdbcTemplate.batchUpdate(anyString(), Mockito.any(BatchPreparedStatementSetter.class))).thenReturn(new int[2]);
        List<Product> actualProducts = productRepository.persist(products, 1L);
        Assertions.assertNotNull(actualProducts);
        Assertions.assertEquals(products, actualProducts);
    }

    @Test
    public void getProductsByOrderId_shouldReturnProducts() throws DaoException {
        List<Product> products = buildListProduct();
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Product>>any(), ArgumentMatchers.any())).thenReturn(products);
        List<Product> actualProducts = productRepository.getProductsByOrderId(1l);
        Assertions.assertNotNull(actualProducts);
        Assertions.assertEquals(products, actualProducts);
    }

    @Test
    public void getProductsByOrderIds_shouldReturnProducts() throws DaoException {
        List<Product> products = buildListProduct();
        List<Order> orders = buildOrderList();
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Product>>any(), objCap.capture())).thenReturn(products);
        List<Product> actualProducts = productRepository.getProductsByOrderIds(orders);
        Assertions.assertNotNull(actualProducts);
        Assertions.assertEquals(products, actualProducts);
    }

    @Test
    public void getOrdersByDatePeriod_shouldReturnOrders() throws DaoException {
        OrdersByDatePeriodNotProduct order = buildOrderByDatePeriodNotProduct();
        List<Product> products = buildListProduct();
        List<Order> orders = buildOrderList();
        Mockito.when(jdbcTemplate.query(anyString(), ArgumentMatchers.<RowMapper<Product>>any(), objCap.capture())).thenReturn(products);
        List<Product> actualProducts = productRepository.getProductsByDatePeriod(orders, order.getOrderProduct());
        Assertions.assertNotNull(actualProducts);
        Assertions.assertEquals(products, actualProducts);
    }
}
