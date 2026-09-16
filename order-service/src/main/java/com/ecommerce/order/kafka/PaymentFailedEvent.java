package com.ecommerce.order.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID eventId,
        Long orderId,
        BigDecimal amount,
        String reason
) {
}