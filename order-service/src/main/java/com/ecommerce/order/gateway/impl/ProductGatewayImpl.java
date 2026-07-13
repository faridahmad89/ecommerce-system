package com.ecommerce.order.gateway.impl;

import com.ecommerce.order.client.ProductFeignClient;
import com.ecommerce.order.client.dto.ProductDto;
import com.ecommerce.order.exception.ServiceUnavailableException;
import com.ecommerce.order.gateway.ProductGateway;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ProductGatewayImpl
        implements ProductGateway {

    private final ProductFeignClient productFeignClient;

    @Override
    @CircuitBreaker(
            name = "productService",
            fallbackMethod = "fallbackProductPrice"
    )
    public BigDecimal getProductPrice(Long productId) {

        try {

            ProductDto product =
                    productFeignClient.getProduct(productId);

            return product.getPrice();

        }

        catch (FeignException.NotFound ex) {

            throw new EntityNotFoundException(
                    "Product not found with id : " + productId);

        }

        catch (FeignException ex) {

            throw new ServiceUnavailableException(
                    "Product Service is temporarily unavailable.");

        }

    }

    public BigDecimal fallbackProductPrice(
            Long productId,
            Exception ex) {

        throw new ServiceUnavailableException(
                "Product service is temporarily unavailable. Please try again later."
        );
    }

}