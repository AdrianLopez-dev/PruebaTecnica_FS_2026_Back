package org.example.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "api")
public record ApiProperties(Security security, Cors cors) {

    public record Security(String token, String headerName) {
    }

    public record Cors(List<String> allowedOrigins) {
    }
}
