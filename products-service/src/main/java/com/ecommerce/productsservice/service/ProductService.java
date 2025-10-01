package com.ecommerce.productsservice.service;
import com.ecommerce.Product;
import com.ecommerce.productsservice.exception.ProductNotFoundException;
import com.ecommerce.productsservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final ElasticSearchService elasticsearchService;

    public List<Product> findAll() {
        var res = elasticsearchService.findAll();

        return res;
    }

    @Transactional
    public Product createProduct(Product product) {
        log.info("Creating product: {}", product.getProductId());

        // Ensure ID is null for new products
        if (product.getId() != null) {
            product.setId(null);
        }

        Product savedProduct = productRepository.save(product);

        // Index in Elasticsearch
        try {
            elasticsearchService.indexProduct(savedProduct);
        } catch (Exception e) {
            log.error("Error indexing product in Elasticsearch", e);
        }

        log.info("Product created with ID: {}", savedProduct.getId());
        return savedProduct;
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#id")
    public Product getProduct(Long id) {
        log.debug("Fetching product by ID: {}", id);
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "products", key = "#productId")
    public Product getProductByProductId(String productId) {
        log.debug("Fetching product by productId: {}", productId);
        return productRepository.findByProductId(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + productId));
    }

    @Transactional(readOnly = true)
    public Page<Product> getAllProducts(Pageable pageable) {
        log.debug("Fetching all active products");
        return productRepository.findByActiveTrue(pageable);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByCategory(String category) {
        log.debug("Fetching products by category: {}", category);
        return productRepository.findByCategory(category);
    }

    @Transactional(readOnly = true)
    public List<Product> getProductsByBrand(String brand) {
        log.debug("Fetching products by brand: {}", brand);
        return productRepository.findByBrand(brand);
    }

    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String category, String brand,
                                        BigDecimal minPrice, BigDecimal maxPrice,
                                        Pageable pageable) {
        log.debug("Searching products with filters - category: {}, brand: {}, price range: {}-{}",
                category, brand, minPrice, maxPrice);
        return productRepository.searchProducts(category, brand, minPrice, maxPrice, pageable);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product updateProduct(Long id, Product product) {
        log.info("Updating product: {}", id);

        Product existingProduct = getProduct(id);

        // Update fields
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setBrand(product.getBrand());
        existingProduct.setImageUrl(product.getImageUrl());
        existingProduct.setActive(product.getActive());

        Product updatedProduct = productRepository.save(existingProduct);

        // Update in Elasticsearch
        try {
            elasticsearchService.indexProduct(updatedProduct);
        } catch (Exception e) {
            log.error("Error updating product in Elasticsearch", e);
        }

        log.info("Product updated: {}", id);
        return updatedProduct;
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        log.info("Deleting product: {}", id);

        Product product = getProduct(id);
        productRepository.delete(product);

        // Delete from Elasticsearch
        try {
            elasticsearchService.deleteProduct(id.toString());
        } catch (Exception e) {
            log.error("Error deleting product from Elasticsearch", e);
        }

        log.info("Product deleted: {}", id);
    }

    @Transactional
    @CacheEvict(value = "products", key = "#id")
    public Product deactivateProduct(Long id) {
        log.info("Deactivating product: {}", id);

        Product product = getProduct(id);
        product.setActive(false);

        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<String> getAllCategories() {
        return productRepository.findAllCategories();
    }

    @Transactional(readOnly = true)
    public List<String> getAllBrands() {
        return productRepository.findAllBrands();
    }
}
