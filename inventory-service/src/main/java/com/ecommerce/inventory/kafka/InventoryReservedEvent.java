package com.ecommerce.inventory.kafka;

import java.util.UUID;

public record InventoryReservedEvent(
        UUID eventId,
        Long orderId,
        Long productId,
        Integer quantity
) {
}