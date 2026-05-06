package org.example.infrastructure.adapter.out.aemet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.model.ForecastDay;
import org.example.domain.model.PrecipitationProbability;
import org.example.domain.port.out.ForecastRepository;
import org.example.infrastructure.adapter.out.aemet.dto.AemetApiResponse;
import org.example.infrastructure.adapter.out.aemet.dto.AemetDiaDto;
import org.example.infrastructure.adapter.out.aemet.dto.AemetForecastDto;
import org.example.infrastructure.config.AemetProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

// Obtiene la predicción del tiempo desde la API de AEMET
// Al igual que con los municipios, AEMET devuelve primero una URL y luego hay que ir a buscar los datos
@Component
public class AemetForecastAdapter implements ForecastRepository {

    private final RestClient aemetRestClient;
    private final AemetProperties properties;
    private final ObjectMapper objectMapper;

    public AemetForecastAdapter(RestClient aemetRestClient,
                                AemetProperties properties,
                                ObjectMapper objectMapper) {
        this.aemetRestClient = aemetRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public ForecastDay findNextDay(String municipalityCode) {
        // Pedimos a AEMET la URL donde está la predicción
        AemetApiResponse apiResponse = aemetRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/prediccion/especifica/municipio/diaria/{municipio}")
                        .queryParam("api_key", properties.apiKey())
                        .build(municipalityCode))
                .retrieve()
                .body(AemetApiResponse.class);

        if (apiResponse == null || apiResponse.datos() == null) {
            throw new RuntimeException("No hay datos de predicción para el municipio: " + municipalityCode);
        }

        // Descargamos la predicción desde esa URL y la parseamos a mano (viene como texto plano)
        String rawJson = RestClient.create()
                .get()
                .uri(apiResponse.datos())
                .retrieve()
                .body(String.class);

        try {
            AemetForecastDto[] forecasts = objectMapper.readValue(rawJson, AemetForecastDto[].class);
            AemetDiaDto targetDay = findTomorrow(forecasts);
            return toDomain(targetDay);
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la predicción de AEMET", e);
        }
    }

    // Buscamos el día de mañana entre todos los días que devuelve AEMET
    private AemetDiaDto findTomorrow(AemetForecastDto[] forecasts) {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return Arrays.stream(forecasts)
                .filter(f -> f.prediccion() != null && f.prediccion().dia() != null)
                .flatMap(f -> f.prediccion().dia().stream())
                .filter(dia -> dia.fecha() != null)
                .sorted(Comparator.comparing(dia -> LocalDateTime.parse(dia.fecha())))
                .filter(dia -> !LocalDateTime.parse(dia.fecha()).toLocalDate().isBefore(tomorrow))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No hay predicción disponible para mañana"));
    }

    // Convertimos los datos de AEMET a nuestro modelo interno
    // Si algún dato de temperatura viene vacío, usamos 0 como valor por defecto
    private ForecastDay toDomain(AemetDiaDto dia) {
        double maxima = dia.temperatura() != null && dia.temperatura().maxima() != null
                ? dia.temperatura().maxima() : 0.0;
        double minima = dia.temperatura() != null && dia.temperatura().minima() != null
                ? dia.temperatura().minima() : 0.0;

        List<PrecipitationProbability> precip = dia.probPrecipitacion() == null
                ? List.of()
                : dia.probPrecipitacion().stream()
                        .filter(p -> p.value() != null && p.periodo() != null)
                        .map(p -> new PrecipitationProbability(p.value(), p.periodo()))
                        .toList();

        return new ForecastDay(maxima, minima, precip);
    }
}
