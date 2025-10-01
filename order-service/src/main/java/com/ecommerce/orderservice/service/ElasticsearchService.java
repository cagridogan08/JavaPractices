package com.ecommerce.orderservice.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.ecommerce.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {

    private final ElasticsearchClient elasticsearchClient;
    private static final String INDEX_NAME = "orders";

    public void indexOrder(Order order) {
        try {
            IndexRequest<Order> request = IndexRequest.of(i -> i
                    .index(INDEX_NAME)
                    .id(order.getId().toString())
                    .document(order)
            );
            elasticsearchClient.index(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> searchOrders(String productId) {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index(INDEX_NAME)
                    .query(q -> q
                            .match(m -> m
                                    .field("productId")
                                    .query(productId)
                            )
                    )
            );

            SearchResponse<Order> response = elasticsearchClient.search(request, Order.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void deleteOrder(String orderId) {

    }
}
