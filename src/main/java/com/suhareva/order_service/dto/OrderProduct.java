package com.suhareva.order_service.dto;

import com.suhareva.order_service.model.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Товар")
@Builder
@Validated
public class OrderProduct {
    @NotNull
    @Min(100000000)
    @Schema(description = "Артикул товара", example = "123456789")
    private long article;
    @NotNull
    @Schema(description = "Название товара", example = "блокнот")
    private String name;

    @Min(1)
    @Schema(description = "Количество товара", example = "2")
    private int quantity;

    @Min(0)
    @Schema(description = "Сумма товара", example = "1000")
    private long amount;

    public OrderProduct(Product product) {
        this.article = product.getArticle();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.amount = product.getAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return article == that.article && quantity == that.quantity
                && amount == that.amount && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article, name, quantity, amount);
    }
}
