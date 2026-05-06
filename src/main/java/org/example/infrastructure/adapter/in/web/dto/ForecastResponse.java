package org.example.infrastructure.adapter.in.web.dto;

import java.util.List;

public record ForecastResponse(double mediaTemperatura,
                               String unidadTemperatura,
                               List<PrecipitationProbabilityResponse> probPrecipitacion) {
}
