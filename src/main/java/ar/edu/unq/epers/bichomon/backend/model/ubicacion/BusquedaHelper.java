package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;


public interface BusquedaHelper {

    Boolean factorTiempo(Entrenador entrenador);

    Boolean factorNivel(Entrenador entrenador);

    Boolean factorPoblacion(Ubicacion ubicacion);

    Boolean factorRandom();

    Bicho generarBicho(Ubicacion ubicacion);
}
