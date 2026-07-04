package com.ecommerce.product.controller;

import com.ecommerce.product.config.CompanyProperties;
import com.ecommerce.product.dto.request.CreateProductRequest;
import com.ecommerce.product.dto.request.UpdateProductRequest;
import com.ecommerce.product.dto.response.PagedResponse;
import com.ecommerce.product.dto.response.ProductResponse;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    private final ProductService productService;

    private final CompanyProperties companyProperties;

    public ProductController(ProductService service,
                             CompanyProperties companyProperties) {

        this.productService = service;
        this.companyProperties = companyProperties;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse createProduct(
            @Valid @RequestBody CreateProductRequest request) {

        return productService.createProduct(request);
    }

    @GetMapping("/{id}")
    public ProductResponse getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/company")
    public CompanyProperties getCompanyName() {
        return companyProperties;
    }

    @GetMapping("/company-info")
    public String getCompanyInfo() {
        return companyProperties.getName() + " | " + companyProperties.getEmail()+ " | " + companyProperties.getPhone();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public PagedResponse<ProductResponse> getAllProducts(

            @PageableDefault(
                    page = 0,
                    size = 10,
                    sort = "name"
            )
            Pageable pageable) {

        return productService.getAllProducts(pageable);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        return productService.updateProduct(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}