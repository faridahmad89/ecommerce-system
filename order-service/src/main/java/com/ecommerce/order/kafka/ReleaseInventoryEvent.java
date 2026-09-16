package com.ecommerce.order.kafka;

import java.util.UUID;

public record ReleaseInventoryEvent(
        UUID eventId,
        Long orderId,
        Long productId,
        Integer quantity
) {
}