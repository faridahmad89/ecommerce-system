package com.ecommerce.payment.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishPaymentSuccess(PaymentSuccessEvent event) {

        kafkaTemplate.send(
                "payment-events",
                event.orderId().toString(),
                event
        );
    }

    public void publishPaymentFailed(PaymentFailedEvent event) {

        kafkaTemplate.send(
                "payment-events",
                event.orderId().toString(),
                event
        );
    }
}