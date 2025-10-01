package com.ecommerce.productsservice.exception;



public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id);
    }

    public ProductNotFoundException(String field, String value) {
        super(String.format("Product not found with %s: %s", field, value));
    }
}
