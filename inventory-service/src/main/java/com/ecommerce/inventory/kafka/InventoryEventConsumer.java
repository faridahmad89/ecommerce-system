package com.ecommerce.inventory.kafka;

import com.ecommerce.inventory.entity.ProcessedOrder;
import com.ecommerce.inventory.repository.ProcessedOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import com.ecommerce.inventory.repository.InventoryRepository;
import com.ecommerce.inventory.entity.Inventory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final InventoryRepository inventoryRepository;
    private final ProcessedOrderRepository processedOrderRepository;
    private final InventoryEventProducer inventoryEventProducer;

    @Transactional
    @KafkaListener(
            topics = "order-events",
            groupId = "inventory-service"
    )
    public void consume(OrderEvent event) {

        System.out.println("Received OrderEvent: " + event);

        // 1. Check duplicate
        if (processedOrderRepository.existsById(event.orderId())) {

            System.out.println(
                    "Order already processed: " + event.orderId()
            );

            return;
        }

        // 2. Find inventory
        Inventory inventory = inventoryRepository
                .findByProductId(event.productId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Inventory not found for product: "
                                        + event.productId()
                        ));

        // 3. Check stock
        if (inventory.getQuantity() < event.quantity()) {

            System.out.println(
                    "Insufficient inventory for product: "
                            + event.productId()
            );

            InventoryRejectedEvent rejectedEvent =
                    new InventoryRejectedEvent(
                            UUID.randomUUID(),
                            event.orderId(),
                            event.productId(),
                            event.quantity(),
                            "INSUFFICIENT_STOCK"
                    );

            inventoryEventProducer.publishInventoryRejected(rejectedEvent);

            return;
        }

        // 4. Decrease inventory
        inventory.setQuantity(
                inventory.getQuantity() - event.quantity()
        );

        inventory.setUpdatedAt(LocalDateTime.now());

        inventoryRepository.save(inventory);

        InventoryReservedEvent reservedEvent = new InventoryReservedEvent(
                UUID.randomUUID(),
                event.orderId(),
                event.productId(),
                event.quantity()
        );

        inventoryEventProducer.publishInventoryReserved(
                reservedEvent
        );

        // 5. Mark order as processed
        processedOrderRepository.save(
                new ProcessedOrder(
                        event.orderId(),
                        LocalDateTime.now()
                )
        );

        System.out.println(
                "Inventory updated. Product: "
                        + event.productId()
                        + ", New quantity: "
                        + inventory.getQuantity()
        );
    }
}