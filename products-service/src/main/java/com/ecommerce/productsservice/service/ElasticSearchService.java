package com.ecommerce.productsservice.service;

import co.elastic.clients.elasticsearch.core.DeleteResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ecommerce.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ElasticSearchService {
    private final ElasticsearchClient elasticSearchClient;
    private static final String INDEX_NAME = "products";

    public void indexProduct(Product product) {
        try {
            IndexResponse response = elasticSearchClient.index(i -> i
                    .index(INDEX_NAME)
                    .id(product.getId().toString())
                    .document(product)
            );
            log.debug("Product indexed: {}", response.id());
        } catch (Exception e) {
            log.error("Error indexing product: {}", e.getMessage(), e);
        }
    }

    public List<Product> findAll(){
        try {
            SearchResponse<Product> response = elasticSearchClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q.matchAll(m -> m)),  // Match all documents
                    Product.class);
            return response.hits().hits().stream()
                    .map(Hit::source)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        catch (Exception e) {
            return  Collections.emptyList();
        }
    }

    public List<Product> searchProducts(String query) {
        try {
            var response = elasticSearchClient.search(s -> s
                            .index(INDEX_NAME)
                            .query(q -> q
                                    .multiMatch(m -> m
                                            .query(query)
                                            .fields("name", "description", "category", "brand")
                                    )
                            ),
                    Product.class
            );

            return response.hits().hits().stream()
                    .map(Hit::source)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error searching products: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public void deleteProduct(String productId) {
        try {
            DeleteResponse response = elasticSearchClient.delete(d -> d
                    .index(INDEX_NAME)
                    .id(productId)
            );
            log.debug("Product deleted from index: {}", response.id());
        } catch (Exception e) {
            log.error("Error deleting product: {}", e.getMessage(), e);
        }
    }
}
