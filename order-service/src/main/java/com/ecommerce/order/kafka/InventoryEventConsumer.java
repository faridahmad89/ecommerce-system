package com.ecommerce.order.kafka;

import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.ProcessedEvent;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.repository.ProcessedEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@KafkaListener(
        topics = "inventory-events",
        groupId = "order-service"
)
@RequiredArgsConstructor
public class InventoryEventConsumer {

    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaHandler
    @Transactional
    public void handleReserved(InventoryReservedEvent event) {

        if (processedEventRepository.existsById(event.eventId())) {
            System.out.println(
                    "Event already processed: " + event.eventId()
            );
            return;
        }

        System.out.println(
                "Received InventoryReservedEvent: " + event
        );

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + event.orderId()
                        ));

        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        processedEventRepository.save(
                ProcessedEvent.builder()
                        .eventId(event.eventId())
                        .processedAt(LocalDateTime.now())
                        .build()
        );

        System.out.println(
                "Order " + order.getId()
                        + " confirmed. Inventory reserved."
        );
    }

    @KafkaHandler
    @Transactional
    public void handleRejected(InventoryRejectedEvent event) {

        if (processedEventRepository.existsById(event.eventId())) {
            System.out.println(
                    "Event already processed: " + event.eventId()
            );
            return;
        }

        System.out.println(
                "Received InventoryRejectedEvent: " + event
        );

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: " + event.orderId()
                        ));

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        processedEventRepository.save(
                ProcessedEvent.builder()
                        .eventId(event.eventId())
                        .processedAt(LocalDateTime.now())
                        .build()
        );

        System.out.println(
                "Order " + order.getId()
                        + " cancelled because inventory was rejected."
        );
    }
}