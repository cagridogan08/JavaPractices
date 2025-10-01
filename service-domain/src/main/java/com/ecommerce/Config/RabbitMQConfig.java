package com.ecommerce.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queues
    public static final String ORDER_QUEUE = "order.queue";
    public static final String INVENTORY_QUEUE = "inventory.queue";
    public static final String PRODUCT_QUEUE = "product.queue";

    // Exchanges
    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String INVENTORY_EXCHANGE = "inventory.exchange";
    public static final String PRODUCT_EXCHANGE = "product.exchange";

    // Routing Keys
    public static final String ORDER_ROUTING_KEY = "order.routing.key";
    public static final String INVENTORY_ROUTING_KEY = "inventory.routing.key";
    public static final String PRODUCT_ROUTING_KEY = "product.routing.key";

    // ========== ORDER ==========
    @Bean
    public Queue orderQueue() {
        return new Queue(ORDER_QUEUE, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderBinding(
            @Qualifier("orderQueue") Queue orderQueue,
            @Qualifier("orderExchange") TopicExchange orderExchange) {
        return BindingBuilder.bind(orderQueue)
                .to(orderExchange)
                .with(ORDER_ROUTING_KEY);
    }

    // ========== INVENTORY ==========
    @Bean
    public Queue inventoryQueue() {
        return new Queue(INVENTORY_QUEUE, true);
    }

    @Bean
    public TopicExchange inventoryExchange() {
        return new TopicExchange(INVENTORY_EXCHANGE);
    }

    @Bean
    public Binding inventoryBinding(
            @Qualifier("inventoryQueue") Queue inventoryQueue,
            @Qualifier("inventoryExchange") TopicExchange inventoryExchange) {
        return BindingBuilder.bind(inventoryQueue)
                .to(inventoryExchange)
                .with(INVENTORY_ROUTING_KEY);
    }

    // ========== PRODUCT ==========
    @Bean
    public Queue productQueue() {
        return new Queue(PRODUCT_QUEUE, true);
    }

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(PRODUCT_EXCHANGE);
    }

    @Bean
    public Binding productBinding(
            @Qualifier("productQueue") Queue productQueue,
            @Qualifier("productExchange") TopicExchange productExchange) {
        return BindingBuilder.bind(productQueue)
                .to(productExchange)
                .with(PRODUCT_ROUTING_KEY);
    }

    // ========== MESSAGE CONVERTER & TEMPLATE ==========
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}