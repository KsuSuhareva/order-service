package com.suhareva.order_service.model.entity;

import com.suhareva.order_service.dto.OrderProduct;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    private static AtomicLong counter = new AtomicLong(1);
    private long id;
    private long article;
    private String name;
    private int quantity;
    private long amount;
    private long orderId;

    public Product(OrderProduct productDto) {
        this.id = counter.getAndIncrement() + System.currentTimeMillis();
        this.article = productDto.getArticle();
        this.name = productDto.getName();
        this.quantity = productDto.getQuantity();
        this.amount = productDto.getAmount();
    }
}
