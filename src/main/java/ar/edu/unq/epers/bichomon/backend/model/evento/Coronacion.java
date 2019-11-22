package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;

public class Coronacion extends Evento {

    private final Entrenador entrenadorPerdedor;
    private final Dojo dojo;

    public Coronacion(Entrenador entrenadorCoronado, Entrenador entrenadorDescoronado, Dojo dojo) {
        super(entrenadorCoronado);
        this.entrenadorPerdedor = entrenadorDescoronado;
        this.dojo = dojo;
    }
}
