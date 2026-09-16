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
import java.util.UUID;

@Component
@RequiredArgsConstructor
@KafkaListener(
        topics = "payment-events",
        groupId = "order-service"
)
public class PaymentEventConsumer {

    private final OrderRepository orderRepository;
    private final ProcessedEventRepository processedEventRepository;
    private final InventoryEventProducer inventoryEventProducer;


    @KafkaHandler
    @Transactional
    public void handlePaymentSuccess(
            PaymentSuccessEvent event) {

        System.out.println(
                "Received PaymentSuccessEvent: " + event
        );

        if (processedEventRepository.existsById(event.eventId())) {

            System.out.println(
                    "Payment event already processed: "
                            + event.eventId()
            );

            return;
        }

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: "
                                        + event.orderId()
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
                "Payment successful. Order "
                        + order.getId()
                        + " confirmed. Amount: "
                        + event.amount()
        );
    }


    @KafkaHandler
    @Transactional
    public void handlePaymentFailed(
            PaymentFailedEvent event) {

        System.out.println(
                "Received PaymentFailedEvent: " + event
        );

        if (processedEventRepository.existsById(event.eventId())) {

            System.out.println(
                    "Payment event already processed: "
                            + event.eventId()
            );

            return;
        }

        Order order = orderRepository.findById(event.orderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found: "
                                        + event.orderId()
                        ));

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);


        ReleaseInventoryEvent releaseEvent =
                new ReleaseInventoryEvent(
                        UUID.randomUUID(),
                        event.orderId(),
                        order.getProductId(),
                        order.getQuantity()
                );

        inventoryEventProducer.publishReleaseInventory(
                releaseEvent
        );


        processedEventRepository.save(
                ProcessedEvent.builder()
                        .eventId(event.eventId())
                        .processedAt(LocalDateTime.now())
                        .build()
        );

        System.out.println(
                "Payment failed. Order "
                        + order.getId()
                        + " cancelled. "
                        + "Inventory release requested."
        );
    }
}