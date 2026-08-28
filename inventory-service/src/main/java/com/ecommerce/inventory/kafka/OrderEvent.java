package com.ecommerce.inventory.kafka;

import java.math.BigDecimal;

public record OrderEvent(
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice
) {
}