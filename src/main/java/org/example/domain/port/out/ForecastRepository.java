package org.example.domain.port.out;

import org.example.domain.model.ForecastDay;

// Lo que necesita la aplicación de fuera: una forma de obtener la predicción del tiempo
// La implementación concreta está en el adaptador de AEMET
public interface ForecastRepository {

    ForecastDay findNextDay(String municipalityCode);
}
