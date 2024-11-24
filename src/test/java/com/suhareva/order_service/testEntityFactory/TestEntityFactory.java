package com.suhareva.order_service.testEntityFactory;

import com.suhareva.order_service.dto.OrderNumber;
import com.suhareva.order_service.dto.OrderProduct;
import com.suhareva.order_service.dto.OrderResponse;
import com.suhareva.order_service.dto.OrdersByDatePeriodNotProduct;
import com.suhareva.order_service.dto.OrdersByDateTotalAmount;
import com.suhareva.order_service.dto.SaveOrder;
import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.model.enums.DeliveryType;
import com.suhareva.order_service.model.enums.PaymentType;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TestEntityFactory {
    public static OrderNumber buildNumber() {
        return new OrderNumber("5077320241121");
    }

    public static SaveOrder buildSaveOrder() {
        return SaveOrder.builder()
                .totalAmount(1000)
                .deliveryAddress("Alexandria")
                .client("Daryl Dixon")
                .deliveryType(DeliveryType.PICKUP)
                .paymentType(PaymentType.CARD)
                .orderProducts(buildOrderProductList())
                .build();
    }

    public static Order buildOrder(String number) {
        return Order.builder()
                .number(number)
                .totalAmount(1000)
                .orderDate(new Date(2024,11,20))
                .deliveryAddress("Alexandria")
                .client("Daryl Dixon")
                .paymentType(PaymentType.CARD.name())
                .deliveryType(DeliveryType.PICKUP.name())
                .build();
    }

    public static List<Order> buildOrderList() {
        List<Order> orders = new ArrayList<>();
        orders.add(buildOrder("5077320241121"));
        orders.add(buildOrder("5775120241122"));
        return orders;
    }

    public static List<Product> buildListProduct() {
        List<Product> products = new ArrayList<>();
        Product product1 = Product.builder()
                .article(12345678)
                .amount(1000)
                .quantity(1)
                .name("crossbow")
                .build();
        Product product2 = Product.builder()
                .article(12345678)
                .amount(1000)
                .quantity(1)
                .name("Knife")
                .build();
        products.add(product1);
        products.add(product2);
        return products;
    }

    public static OrderResponse buildOrderResponse() {
        return OrderResponse.builder()
                .number("5077320241121")
                .totalAmount(1000)
                .client("Daryl Dixon")
                .orderDate(new Date())
                .deliveryAddress("Alexandria")
                .paymentType(PaymentType.CARD)
                .deliveryType(DeliveryType.PICKUP)
                .orderProducts(buildOrderProductList())
                .build();
    }

    public static List<OrderProduct> buildOrderProductList() {
        List<OrderProduct> products = new ArrayList<>();
        OrderProduct product1 = OrderProduct.builder()
                .article(12345678)
                .amount(1000)
                .quantity(1)
                .name("crossbow")
                .build();
        OrderProduct product2 = OrderProduct.builder()
                .article(12345678)
                .amount(1000)
                .quantity(1)
                .name("Knife")
                .build();
        products.add(product1);
        products.add(product2);
        return products;
    }

    public static OrdersByDateTotalAmount buildOrderByDateTotalAmount() {
        return OrdersByDateTotalAmount.builder()
                .orderDate(new Date(124, Calendar.NOVEMBER,20))
                .totalAmount(900)
                .build();
    }

    public static OrdersByDatePeriodNotProduct buildOrderByDatePeriodNotProduct() {
        return OrdersByDatePeriodNotProduct.builder()
                .from(new Date(124, Calendar.NOVEMBER,20))
                .to(new Date(124, Calendar.NOVEMBER,21))
                .orderProduct(OrderProduct.builder()
                        .article(123456789)
                        .amount(1000)
                        .quantity(1)
                        .name("crossbow")
                        .build())
                .build();
    }

    public static List<OrderResponse> builOrderResponseList() {
        List<OrderResponse> responses = new ArrayList<>();
        responses.add(buildOrderResponse());
        responses.add(buildOrderResponse());
        return responses;
    }
}
