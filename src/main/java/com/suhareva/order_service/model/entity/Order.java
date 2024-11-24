package com.suhareva.order_service.model.entity;

import com.suhareva.order_service.dto.SaveOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order implements Serializable {
    private static AtomicLong counter = new AtomicLong();
    private long id;
    private String number;
    private Integer totalAmount;
    private Date orderDate;
    private String deliveryAddress;
    private String client;
    private String paymentType;
    private String deliveryType;


    public Order(SaveOrder request, String orderNumber) {
        this.id = counter.getAndIncrement() + System.currentTimeMillis();
        this.number = orderNumber;
        this.totalAmount = request.getTotalAmount();
        this.orderDate = new Date();
        this.deliveryAddress = request.getDeliveryAddress();
        this.client = request.getClient();
        this.paymentType = request.getPaymentType().name();
        this.deliveryType = request.getDeliveryType().name();
    }

//    public Order(String number, Integer totalAmount, Date orderDate, String deliveryAddress, String client, String paymentType, String deliveryType) {
//        this.id = counter.getAndIncrement() + System.currentTimeMillis();
//        this.number = number;
//        this.totalAmount = totalAmount;
//        this.orderDate = orderDate;
//        this.deliveryAddress = deliveryAddress;
//        this.client = client;
//        this.paymentType = paymentType;
//        this.deliveryType = deliveryType;
//    }
}
