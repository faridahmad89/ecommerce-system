package com.ecommerce.payment.kafka;

import com.ecommerce.payment.entity.Payment;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;

    @KafkaListener(
            topics = "inventory-reserved-events",
            groupId = "payment-service"
    )
    @Transactional
    public void consume(InventoryReservedEvent event) {

        System.out.println(
                "Received InventoryReservedEvent: " + event
        );

        // Prevent duplicate payment
        if (paymentRepository.findByOrderId(event.orderId()).isPresent()) {

            System.out.println(
                    "Payment already exists for order: "
                            + event.orderId()
            );

            return;
        }

        /*
         * For now amount is hardcoded.
         * Later this will come from Order Service.
         */
        BigDecimal amount = BigDecimal.valueOf(100);

        // 1. Create payment as PENDING
        Payment payment = Payment.builder()
                .orderId(event.orderId())
                .amount(amount)
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        payment = paymentRepository.save(payment);

        // 2. Process payment
        boolean paymentSuccessful = true;

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        if (paymentSuccessful) {

            // 3. Mark payment SUCCESS
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // 4. Publish PaymentSuccessEvent
            PaymentSuccessEvent successEvent =
                    new PaymentSuccessEvent(
                            UUID.randomUUID(),
                            event.orderId(),
                            payment.getAmount()
                    );

            paymentEventProducer.publishPaymentSuccess(successEvent);

            System.out.println(
                    "Payment successful for order: "
                            + event.orderId()
            );

        } else {

            // 3. Mark payment FAILED
            payment.setStatus(PaymentStatus.FAILED);
            payment.setUpdatedAt(LocalDateTime.now());

            paymentRepository.save(payment);

            // 4. Publish PaymentFailedEvent
            PaymentFailedEvent failedEvent =
                    new PaymentFailedEvent(
                            UUID.randomUUID(),
                            event.orderId(),
                            payment.getAmount(),
                            "PAYMENT_FAILED"
                    );

            paymentEventProducer.publishPaymentFailed(failedEvent);

            System.out.println(
                    "Payment failed for order: "
                            + event.orderId()
            );
        }
    }
}