package com.suhareva.order_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.validation.annotation.Validated;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Validated
@Schema(description = "Идентификатор заказа, возвращаемый после сохранения")
public class OrderNumber {

    @Size(max = 13)
    @NonNull
    @Schema(description = "Уникальный идентификатор заказа", example = "7097020241118")
    private String number;
}
