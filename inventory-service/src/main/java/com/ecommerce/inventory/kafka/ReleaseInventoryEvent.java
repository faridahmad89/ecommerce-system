package com.ecommerce.inventory.kafka;

public record ReleaseInventoryEvent(
        String eventId,
        Long orderId,
        Long productId,
        Integer quantity
) {
}