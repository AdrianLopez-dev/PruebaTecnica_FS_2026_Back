package org.example.domain.port.in;

import org.example.domain.model.Municipality;

import java.util.List;

public interface GetMunicipalitiesUseCase {

    List<Municipality> getMunicipalities();
}
