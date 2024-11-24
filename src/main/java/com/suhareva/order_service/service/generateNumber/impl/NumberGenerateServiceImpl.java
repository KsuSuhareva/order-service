package com.suhareva.order_service.service.generateNumber.impl;

import com.suhareva.order_service.dto.OrderNumber;
import com.suhareva.order_service.service.generateNumber.NumberGenerateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NumberGenerateServiceImpl implements NumberGenerateService {
    private final WebClient webClient;

    @Override
    public OrderNumber generate() {
        return webClient
                .post()
                .uri("/numbers/generateNumber")
                .retrieve()
                .bodyToMono(OrderNumber.class)
                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
    }
}
