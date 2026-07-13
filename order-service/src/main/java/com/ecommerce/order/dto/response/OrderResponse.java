package com.ecommerce.order.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private BigDecimal productPrice;

    private BigDecimal totalPrice;

    private String status;

    private LocalDateTime createdAt;

}