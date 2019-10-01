package ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;

public interface Estrategia {

    ResultadoCombate calcularDuelo(Bicho bichoReador, Dojo lugarDeBatalla);
}
