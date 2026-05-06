package org.example.domain.model;

import java.util.List;

// Datos del tiempo para un día
public record ForecastDay(double maxTemperatureCel,
                          double minTemperatureCel,
                          List<PrecipitationProbability> probPrecipitacion) {
}
