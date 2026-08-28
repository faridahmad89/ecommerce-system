package com.ecommerce.inventory.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishInventoryRejected(
            InventoryRejectedEvent event) {

        kafkaTemplate.send(
                "inventory-events",
                event.orderId().toString(),
                event
        );
    }

    public void publishInventoryReserved(
            InventoryReservedEvent event) {

        kafkaTemplate.send(
                "inventory-events",
                event.orderId().toString(),
                event
        );
    }
}