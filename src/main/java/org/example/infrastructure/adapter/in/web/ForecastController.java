package org.example.infrastructure.adapter.in.web;

import org.example.domain.model.Forecast;
import org.example.domain.model.TemperatureUnit;
import org.example.domain.port.in.GetForecastUseCase;
import org.example.infrastructure.adapter.in.web.dto.ForecastResponse;
import org.example.infrastructure.adapter.in.web.dto.PrecipitationProbabilityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ForecastController {

    private final GetForecastUseCase getForecastUseCase;

    public ForecastController(GetForecastUseCase getForecastUseCase) {
        this.getForecastUseCase = getForecastUseCase;
    }

    // unidadTemperatura es opcional, si no se indica se usa Celsius por defecto
    @GetMapping("/prediccion/{codigoMunicipio}")
    public ForecastResponse getForecast(
            @PathVariable String codigoMunicipio,
            @RequestParam(required = false, defaultValue = "G_CEL") TemperatureUnit unidadTemperatura) {

        Forecast forecast = getForecastUseCase.getForecast(codigoMunicipio, unidadTemperatura);
        return toResponse(forecast);
    }

    private ForecastResponse toResponse(Forecast forecast) {
        List<PrecipitationProbabilityResponse> precip = forecast.probPrecipitacion().stream()
                .map(p -> new PrecipitationProbabilityResponse(p.probabilidad(), p.periodo()))
                .toList();
        return new ForecastResponse(forecast.mediaTemperatura(), forecast.unidadTemperatura().name(), precip);
    }
}
