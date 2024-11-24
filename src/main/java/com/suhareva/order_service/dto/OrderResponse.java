package com.suhareva.order_service.dto;

import com.suhareva.order_service.model.entity.Order;
import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.model.enums.DeliveryType;
import com.suhareva.order_service.model.enums.PaymentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос по уникальному идентификатору")
@Builder
public class OrderResponse {
    @Schema(description = "Уникальный номер заказа", example = "8817020241120")
    private String number;
    @Schema(description = "Общая сумма заказа", example = "10000")
    private Integer totalAmount;

    @Schema(description = "Дата заказа", example = "2024-11-18T16:56:43.053+00:00")
    private Date orderDate;
    @NotNull
    @Schema(description = "Адрес заказа", example = "Гомель, Советская 1")
    private String deliveryAddress;
    @NotNull
    @Schema(description = "ФИО клиента", example = "Сухарева Ксения")
    private String client;
    @NotNull
    @Schema(description = "Тип оплаты, возможные значения: CARD, CASH", example = "CARD")
    private PaymentType paymentType;
    @NotNull
    @Schema(description = "Тип доставки заказа, возможные значения: PICKUP, DOOR_TO_DOOR", example = "PICKUP")
    private DeliveryType deliveryType;
    @Schema(description = "Список товаров")
    private List<OrderProduct> orderProducts;

    public OrderResponse(Order order, List<Product> products) {
        this.number = order.getNumber();
        this.totalAmount = order.getTotalAmount();
        this.orderDate = order.getOrderDate();
        this.deliveryAddress = order.getDeliveryAddress();
        this.client = order.getClient();
        this.paymentType = PaymentType.valueOf(order.getPaymentType());
        this.deliveryType = DeliveryType.valueOf(order.getDeliveryType());
        this.orderProducts = getOrderProductList(products);
    }

    public List<OrderProduct> getOrderProductList(List<Product> products) {
        return products.stream().map(OrderProduct::new).toList();
    }
}
