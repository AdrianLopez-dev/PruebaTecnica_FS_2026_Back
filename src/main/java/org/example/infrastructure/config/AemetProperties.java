package org.example.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// Propiedades del bloque "aemet" en application.yml
@ConfigurationProperties(prefix = "aemet")
public record AemetProperties(String apiKey, String baseUrl) {
}
