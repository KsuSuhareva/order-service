package com.suhareva.order_service.controller;

import com.suhareva.order_service.dto.*;
import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/orders")
@Tag(name = "OrderController",
        description = "Сохраняет заказы и возвращает информацию о заказах")
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/saveOrder")
    @Operation(summary = "Сохранение заказа", description = "Сохранение заказа, возвращает уникальный идентификатор")
    public ResponseEntity<OrderNumber> saveOrder(@Valid @RequestBody SaveOrder request) throws DaoException {
        return new ResponseEntity<>(orderService.createOrder(request), HttpStatus.ACCEPTED);
    }

    @PostMapping("/getOrderByNumber")
    @Operation(summary = "Получение заказа по индивидуальному идентификатору", description = "Возвращает заказ")
    public ResponseEntity<OrderResponse> detOrderByNumber(@Valid @RequestBody OrderNumber orderNumber) throws DaoException {
        return new ResponseEntity<>(orderService.getOrderByNumber(orderNumber), HttpStatus.OK);
    }

    @PostMapping("/getOrdersByDateAndGrTotalAmount")
    @Operation(summary = "Получение заказа за заданную дату и больше заданной общей суммы заказа", description = "Возвращает список заказов")
    public ResponseEntity<List<OrderResponse>> getOrdersByDateAndGrTotalAmount(@Valid @RequestBody OrdersByDateTotalAmount order) throws DaoException {
        return new ResponseEntity<>(orderService.getOrdersByDateAndGrTotalAmount(order), HttpStatus.OK);
    }

    @PostMapping("/getOrdersByDatePeriodAndNotProduct")
    @Operation(summary = "Получение списка заказов, не содержащих заданный товар и поступивших в заданный временной период", description = "Возвращает список заказов")
    public ResponseEntity<List<OrderResponse>> getOrdersByDatePeriodAndNotProduct(@Valid @RequestBody OrdersByDatePeriodNotProduct order) throws DaoException {
        return new ResponseEntity<>(orderService.getOrdersByDatePeriodAndNotProduct(order), HttpStatus.OK);
    }
}
