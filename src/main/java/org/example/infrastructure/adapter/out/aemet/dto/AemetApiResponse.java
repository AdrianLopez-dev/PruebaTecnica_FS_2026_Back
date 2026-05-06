package org.example.infrastructure.adapter.out.aemet.dto;

// Respuesta que devuelve AEMET en el primer paso: contiene la URL donde están los datos reales
public record AemetApiResponse(String descripcion, int estado, String datos, String metadatos) {
}
