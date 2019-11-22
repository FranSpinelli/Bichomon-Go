package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public abstract class Evento {
    private Entrenador entrenador;

    public Evento(Entrenador entrenador){
        this.entrenador = entrenador;
    }

    public Entrenador getEntrenador() {
        return this.entrenador;
    }
}
