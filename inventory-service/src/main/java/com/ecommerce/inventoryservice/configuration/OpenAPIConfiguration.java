package com.ecommerce.inventoryservice.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import io.swagger.v3.oas.models.ExternalDocumentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Value("${server.port:8082}")
    private String serverPort;

    @Value("${spring.application.name:inventory-service}")
    private String applicationName;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(apiInfo())
                .servers(serverList())
                .tags(tagList())
                .externalDocs(externalDocumentation());
    }

    private Info apiInfo() {
        return new Info()
                .title("Inventory Service API")
                .description("""
                    # Inventory Management Microservice
                    
                    This service manages product inventory with the following features:
                    - Real-time inventory tracking
                    - Inventory reservation for orders
                    - Stock management (add/release)
                    - Integration with RabbitMQ for event-driven updates
                    
                    ## Key Operations
                    - **GET** `/api/inventory/{productId}` - Get inventory details
                    - **POST** `/api/inventory` - Create new inventory
                    - **POST** `/api/inventory/{productId}/reserve` - Reserve stock
                    - **POST** `/api/inventory/{productId}/release` - Release reserved stock
                    
                    ## Business Rules
                    - Reserved quantity cannot exceed total quantity
                    - Available quantity = Total - Reserved
                    - All operations are transactional
                    """)
                .version("1.0.0")
                .contact(contact())
                .license(license());
    }

    private Contact contact() {
        return new Contact()
                .name("E-Commerce Team")
                .email("inventory-team@ecommerce.com")
                .url("https://ecommerce.com/inventory-service");
    }

    private License license() {
        return new License()
                .name("Apache 2.0")
                .url("https://www.apache.org/licenses/LICENSE-2.0.html");
    }

    private List<Server> serverList() {
        return List.of(
                new Server()
                        .url("http://localhost:" + serverPort)
                        .description("Local Development Server"),
                new Server()
                        .url("http://localhost:8080/inventory")
                        .description("API Gateway (Local)"),
                new Server()
                        .url("https://api-dev.ecommerce.com/inventory")
                        .description("Development Environment"),
                new Server()
                        .url("https://api.ecommerce.com/inventory")
                        .description("Production Environment")
        );
    }

    private List<Tag> tagList() {
        return List.of(
                new Tag()
                        .name("Inventory Management")
                        .description("CRUD operations for inventory"),
                new Tag()
                        .name("Stock Operations")
                        .description("Reserve, release, and add stock"),
                new Tag()
                        .name("Query")
                        .description("Query inventory and availability")
        );
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
                .description("Complete API Documentation")
                .url("https://docs.ecommerce.com/inventory-service");
    }
}