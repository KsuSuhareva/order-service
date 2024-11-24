package com.suhareva.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
@Schema(description = "Запрос получения списка заказов, не содержащих заданный товар и поступивших в заданный временной период")
public class OrdersByDatePeriodNotProduct {

    @NonNull
    @Schema(description = "Начало периода", example = "2024-11-18T16:56:43.053+00:00")
    private Date from;
    @Schema(description = "Конец периода", example = "2024-11-18T16:56:43.053+00:00")
    private Date to;
    @Schema(description = "Товар, который следует исключить из поиска")
    @NonNull
    private OrderProduct orderProduct;
}
