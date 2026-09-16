package com.ecommerce.payment.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentFailedEvent(
        UUID eventId,
        Long orderId,
        BigDecimal amount,
        String reason
) {
}