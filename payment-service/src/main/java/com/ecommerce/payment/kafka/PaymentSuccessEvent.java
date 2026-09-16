package com.ecommerce.payment.kafka;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentSuccessEvent(
        UUID eventId,
        Long orderId,
        BigDecimal amount
) {
}