package org.example.infrastructure.adapter.out.aemet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.model.Municipality;
import org.example.domain.port.out.MunicipalityRepository;
import org.example.infrastructure.adapter.out.aemet.dto.AemetApiResponse;
import org.example.infrastructure.adapter.out.aemet.dto.AemetMunicipalityDto;
import org.example.infrastructure.config.AemetProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Arrays;
import java.util.List;

// Obtiene la lista de municipios desde la API de AEMET
@Component
public class AemetMunicipalityAdapter implements MunicipalityRepository {

    private final RestClient aemetRestClient;
    private final AemetProperties properties;
    private final ObjectMapper objectMapper;

    public AemetMunicipalityAdapter(RestClient aemetRestClient,
                                    AemetProperties properties,
                                    ObjectMapper objectMapper) {
        this.aemetRestClient = aemetRestClient;
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<Municipality> findAll() {
        // Pedimos a AEMET la URL donde están los municipios
        AemetApiResponse apiResponse = aemetRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maestro/municipios")
                        .queryParam("api_key", properties.apiKey())
                        .build())
                .retrieve()
                .body(AemetApiResponse.class);

        if (apiResponse == null || apiResponse.datos() == null) {
            return List.of();
        }

        // Descargamos los municipios desde esa URL
        // AEMET devuelve texto plano en lugar de JSON, así que lo leemos como String y lo parseamos a mano
        String rawJson = RestClient.create()
                .get()
                .uri(apiResponse.datos())
                .retrieve()
                .body(String.class);

        if (rawJson == null || rawJson.isBlank()) {
            return List.of();
        }

        try {
            AemetMunicipalityDto[] dtos = objectMapper.readValue(rawJson, AemetMunicipalityDto[].class);
            return Arrays.stream(dtos)
                    // AEMET devuelve el código con el prefijo "id" (ej: "id28079"), lo quitamos
                    .map(dto -> new Municipality(dto.id().replaceFirst("^id", ""), dto.nombre()))
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar los municipios de AEMET", e);
        }
    }
}
