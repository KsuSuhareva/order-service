package com.suhareva.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Запрос получения заказа за заданную дату и больше заданной общей суммы заказа")
public class OrdersByDateTotalAmount {
    @NonNull
    @Schema(description = "Дата заказа", example = "2024-11-18T16:56:43.053+00:00")
    private Date orderDate;

    @NonNull
    @Min(0)
    @Schema(description = "Общая сумма заказа", example = "10000")
    private Integer totalAmount;
}
