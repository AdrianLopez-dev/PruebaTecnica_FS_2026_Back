package org.example.infrastructure.adapter.out.aemet.dto;

// Municipio tal como lo devuelve AEMET, con el prefijo "id" en el código (ej: "id28079")
public record AemetMunicipalityDto(String id, String nombre) {
}
