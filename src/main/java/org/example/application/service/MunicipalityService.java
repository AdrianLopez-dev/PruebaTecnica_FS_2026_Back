package org.example.application.service;

import org.example.domain.model.Municipality;
import org.example.domain.port.in.GetMunicipalitiesUseCase;
import org.example.domain.port.out.MunicipalityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MunicipalityService implements GetMunicipalitiesUseCase {

    private final MunicipalityRepository municipalityRepository;

    public MunicipalityService(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    /**
     * Obtenemos todos los municipios
     * @return List of {@link Municipality}
     */
    @Override
    public List<Municipality> getMunicipalities() {
        return municipalityRepository.findAll();
    }
}
