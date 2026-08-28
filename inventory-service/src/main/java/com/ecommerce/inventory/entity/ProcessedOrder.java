package com.ecommerce.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "processed_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedOrder {

    @Id
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "processed_at", nullable = false)
    private LocalDateTime processedAt;
}