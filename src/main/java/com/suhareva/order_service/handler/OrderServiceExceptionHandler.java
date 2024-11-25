package com.suhareva.order_service.handler;

import com.suhareva.order_service.handler.exception.DaoException;
import com.suhareva.order_service.handler.exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
@Slf4j
public class OrderServiceExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMessage> catchDaoException(MethodArgumentNotValidException e) {
        log.error("Catch exception methodArgumentNotValidException with cause: {}", Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        Optional<String> massage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + " " + error.getDefaultMessage())
                .findFirst();
        return new ResponseEntity<>(new ErrorMessage(e, massage.orElseGet(e::getMessage)), BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<ErrorMessage> catchDaoException(HttpMessageNotReadableException e) {
        log.error("Catch exception httpMessageNotReadableException with cause: {}", Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessage(e), BAD_REQUEST);
    }

    @ExceptionHandler(DaoException.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> catchDaoException(DaoException e) {
        log.error("Catch exception daoException with cause: {}", Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessage(e), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientRequestException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> catchWebClientRequestException(WebClientRequestException e) {
        log.error("Catch exception webClientResponseException with cause:{} ", e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(WebClientResponseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> catchWebClientResponseException(WebClientException e) {
        log.error("Catch exception webClientResponseException with cause:{} ", e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<ErrorMessage> catchOrderNotFoundException(OrderNotFoundException e) {
        log.error("Catch exception orderNotFoundException with cause:{} ", e.getMessage());
        return new ResponseEntity<>(new ErrorMessage(e), NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorMessage> catchException(Exception e) {
        log.error("Catch {} with cause:{} ", e.getClass().getSimpleName(), Arrays.toString(e.getStackTrace()));
        e.printStackTrace();
        return new ResponseEntity<>(new ErrorMessage(e), INTERNAL_SERVER_ERROR);
    }
}
