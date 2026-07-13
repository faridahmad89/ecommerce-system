package com.ecommerce.order.client;

import com.ecommerce.order.client.dto.ProductDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "product-service")
public interface ProductFeignClient {

    @GetMapping("/api/v1/products/{id}")
    ProductDto getProduct(
            @PathVariable Long id
    );

}