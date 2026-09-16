package com.ecommerce.payment.kafka;

public record InventoryReservedEvent(
        String eventId,
        Long orderId,
        Long productId,
        Integer quantity
) {
}