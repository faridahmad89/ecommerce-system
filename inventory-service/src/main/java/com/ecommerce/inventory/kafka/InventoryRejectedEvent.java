package com.ecommerce.inventory.kafka;

import java.util.UUID;

public record InventoryRejectedEvent(
        UUID eventId,
        Long orderId,
        Long productId,
        Integer requestedQuantity,
        String reason
) {
}