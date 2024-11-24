package com.suhareva.order_service.dto;

import com.suhareva.order_service.model.entity.Product;
import com.suhareva.order_service.model.enums.PaymentType;
import com.suhareva.order_service.model.enums.DeliveryType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Заказ который необходимо сохранить")
@Builder
@Validated
public class SaveOrder {
    @Min(0)
    @Schema(description = "Общая сумма заказа", example = "10000")
    private Integer totalAmount;
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
    @NonNull
    private List<OrderProduct> orderProducts;

    public List<Product> takeProductList() {
        return orderProducts.stream().map(Product::new).toList();
    }
}
