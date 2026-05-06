package org.example.infrastructure.adapter.in.web;

import org.example.domain.port.in.GetMunicipalitiesUseCase;
import org.example.infrastructure.adapter.in.web.dto.MunicipalityResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class MunicipalityController {

    private final GetMunicipalitiesUseCase getMunicipalitiesUseCase;

    public MunicipalityController(GetMunicipalitiesUseCase getMunicipalitiesUseCase) {
        this.getMunicipalitiesUseCase = getMunicipalitiesUseCase;
    }

    @GetMapping("/municipios")
    public List<MunicipalityResponse> getMunicipalities() {
        return getMunicipalitiesUseCase.getMunicipalities().stream()
                .map(m -> new MunicipalityResponse(m.id(), m.nombre()))
                .toList();
    }
}
