package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public class Captura extends Evento{

    private final Object bicho;

    public Captura(Entrenador entrenador, Bicho bichoCapturado) {
        super(entrenador);
        this.bicho = bichoCapturado;
    }
}
