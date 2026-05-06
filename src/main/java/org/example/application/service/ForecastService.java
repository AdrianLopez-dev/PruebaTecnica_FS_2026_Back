package org.example.application.service;

import org.example.domain.model.Forecast;
import org.example.domain.model.ForecastDay;
import org.example.domain.model.PrecipitationProbability;
import org.example.domain.model.TemperatureUnit;
import org.example.domain.port.in.GetForecastUseCase;
import org.example.domain.port.out.ForecastRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForecastService implements GetForecastUseCase {

    // Solo queremos las franjas de 6 horas (00-06, 06-12, 12-18, 18-24)
    private static final int SIX_HOUR_PERIOD = 6;

    private final ForecastRepository forecastRepository;

    public ForecastService(ForecastRepository forecastRepository) {
        this.forecastRepository = forecastRepository;
    }

    /**
     * Obtenemos la predicción del tiempo para un municipio
     * @param municipalityCode
     * @param unit
     * @return {@link Forecast}
     */
    @Override
    public Forecast getForecast(String municipalityCode, TemperatureUnit unit) {
        ForecastDay day = forecastRepository.findNextDay(municipalityCode);

        // La media es la suma de máxima y mínima dividida entre dos
        double avgCelsius = (day.maxTemperatureCel() + day.minTemperatureCel()) / 2.0;

        // Convertimos si el usuario pidió Fahrenheit, si no dejamos en Celsius
        double converted = unit == TemperatureUnit.G_FAH ? toFahrenheit(avgCelsius) : avgCelsius;

        // Redondeamos a un decimal
        double rounded = Math.round(converted * 10.0) / 10.0;

        List<PrecipitationProbability> filtered = filterSixHourPeriods(day.probPrecipitacion());

        return new Forecast(rounded, unit, filtered);
    }

    // Descartamos las franjas de 12 o 24 horas que también devuelve AEMET
    private List<PrecipitationProbability> filterSixHourPeriods(List<PrecipitationProbability> all) {
        return all.stream()
                .filter(p -> isSixHourPeriod(p.periodo()))
                .toList();
    }

    // Comprobamos que la franja dure exactamente 6 horas restando inicio y fin
    private boolean isSixHourPeriod(String periodo) {
        String[] parts = periodo.split("-");
        if (parts.length != 2) return false;
        try {
            int start = Integer.parseInt(parts[0]);
            int end = Integer.parseInt(parts[1]);
            return (end - start) == SIX_HOUR_PERIOD;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private double toFahrenheit(double celsius) {
        return celsius * 9.0 / 5.0 + 32.0;
    }
}
