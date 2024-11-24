package com.suhareva.order_service.handler;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
@Schema(description = "Сообщение об ошибке")
public class ErrorMessage {
    @Schema(description = "Название ошибки", example = "MethodArgumentNotValidException")
    private String errorName;
    @Schema(description = "Сообщение ошибки", example = "totalAmount must be greater than or equal to 0")
    private String errorMessage;

    @Schema(description = "Дата возникновения ошибки", example = "2024-11-18T16:56:43.053+00:00")
    private Date date;

    public ErrorMessage(Exception exception) {
        this.errorName = exception.getClass().getSimpleName();
        this.errorMessage = exception.getMessage();
        this.date = new Date();
    }

    public ErrorMessage(Exception exception, String message) {
        this.errorName = exception.getClass().getSimpleName();
        this.errorMessage = message;
        this.date = new Date();
    }
}
