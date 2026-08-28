package com.ecommerce.order.event;

import java.math.BigDecimal;

public record OrderCreatedEvent(
        Long orderId,
        Long productId,
        Integer quantity,
        BigDecimal productPrice,
        BigDecimal totalPrice
) {
}