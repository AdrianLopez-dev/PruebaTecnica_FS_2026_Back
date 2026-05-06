package org.example.infrastructure.adapter.out.aemet.dto;

// Temperaturas máxima y mínima del día. Pueden ser null si AEMET no tiene el dato
public record AemetTemperaturaDto(Double maxima, Double minima) {
}
