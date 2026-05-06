package org.example.domain.model;

import java.util.List;

// Predicción ya procesada lista para devolver al cliente
public record Forecast(double mediaTemperatura,
                       TemperatureUnit unidadTemperatura,
                       List<PrecipitationProbability> probPrecipitacion) {
}
