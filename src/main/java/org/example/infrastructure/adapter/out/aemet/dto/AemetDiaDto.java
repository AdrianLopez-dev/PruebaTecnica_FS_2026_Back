package org.example.infrastructure.adapter.out.aemet.dto;

import java.util.List;

// Datos del tiempo para un día concreto según AEMET
public record AemetDiaDto(String fecha,
                          AemetTemperaturaDto temperatura,
                          List<AemetProbPrecipitacionDto> probPrecipitacion) {
}
