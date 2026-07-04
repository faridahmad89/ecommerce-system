package com.ecommerce.product.service.impl;

import com.ecommerce.product.dto.request.CreateProductRequest;
import com.ecommerce.product.dto.request.UpdateProductRequest;
import com.ecommerce.product.dto.response.PagedResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.service.ProductService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class DummyProductService implements ProductService {
    @Override
    public ProductResponse createProduct(CreateProductRequest request) {
        return new ProductResponse();
    }

    @Override
    public ProductResponse getProductById(Long id) {
        return new ProductResponse();
    }

    @Override
    public PagedResponse<ProductResponse> getAllProducts(Pageable pageable) {
        return null;
    }

    @Override
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        return null;
    }

    @Override
    public void deleteProduct(Long id) {

    }
    // return dummy values
}