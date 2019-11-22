package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

public class Arribo extends Evento{

    private final Ubicacion ubicacion;

    public Arribo(Entrenador entrenador, Ubicacion ubicacion) {
        super(entrenador);
        this.ubicacion = ubicacion;
    }
}
