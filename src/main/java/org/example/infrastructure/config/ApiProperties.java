package org.example.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

// Propiedades del bloque "api" en application.yml
@ConfigurationProperties(prefix = "api")
public record ApiProperties(Cors cors) {

    public record Cors(List<String> allowedOrigins) {
    }
}
