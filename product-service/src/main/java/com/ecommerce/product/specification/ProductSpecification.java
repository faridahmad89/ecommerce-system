package com.ecommerce.product.specification;

import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpecification {

    public static Specification<Product> hasCategory(
            String category) {

        return (root, query, cb) ->

                cb.equal(
                        root.get("category"),
                        category
                );

    }

    public static Specification<Product> isActive(Boolean active) {

        return (root, query, cb) ->
                cb.equal(root.get("active"), active);

    }

    public static Specification<Product> hasPriceGreaterThan(
            BigDecimal price) {

        return (root, query, cb) ->
                cb.greaterThan(
                        root.get("price"),
                        price
                );

    }

    public static Specification<Product> hasPriceLessThan(
            BigDecimal price) {

        return (root, query, cb) ->
                cb.lessThan(
                        root.get("price"),
                        price
                );

    }

}