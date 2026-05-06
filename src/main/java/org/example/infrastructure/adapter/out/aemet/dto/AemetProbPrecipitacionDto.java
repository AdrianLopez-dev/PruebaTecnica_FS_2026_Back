package org.example.infrastructure.adapter.out.aemet.dto;

// AEMET llama "value" al porcentaje de lluvia, nosotros lo renombramos a "probabilidad" en nuestro modelo
public record AemetProbPrecipitacionDto(Integer value, String periodo) {
}
