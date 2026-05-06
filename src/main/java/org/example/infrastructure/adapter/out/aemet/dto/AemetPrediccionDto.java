package org.example.infrastructure.adapter.out.aemet.dto;

import java.util.List;

// Contiene la lista de días con su predicción
public record AemetPrediccionDto(List<AemetDiaDto> dia) {
}
