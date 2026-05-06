package org.example.domain.port.out;

import org.example.domain.model.Municipality;

import java.util.List;

// Lo que necesita la aplicación de fuera: una forma de obtener municipios
// La implementación concreta está en el adaptador de AEMET
public interface MunicipalityRepository {

    List<Municipality> findAll();
}
