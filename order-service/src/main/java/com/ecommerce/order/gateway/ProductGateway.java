package com.ecommerce.order.gateway;

import java.math.BigDecimal;

public interface ProductGateway {

    BigDecimal getProductPrice(Long productId);

}