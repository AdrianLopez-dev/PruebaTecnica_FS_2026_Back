package org.example.domain.port.in;

import org.example.domain.model.Forecast;
import org.example.domain.model.TemperatureUnit;

public interface GetForecastUseCase {

    Forecast getForecast(String municipalityCode, TemperatureUnit unit);
}
