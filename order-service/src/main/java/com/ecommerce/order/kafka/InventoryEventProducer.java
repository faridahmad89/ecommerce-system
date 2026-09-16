package com.ecommerce.order.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishReleaseInventory(
            ReleaseInventoryEvent event) {

        kafkaTemplate.send(
                "release-inventory-events",
                event.orderId().toString(),
                event
        );

        System.out.println(
                "Published ReleaseInventoryEvent: " + event
        );
    }
}