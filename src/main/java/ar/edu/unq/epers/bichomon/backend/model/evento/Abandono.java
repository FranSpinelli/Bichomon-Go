package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;

public class Abandono extends Evento{

    private final Guarderia guarderia;
    private final Bicho bicho;

    public Abandono(Entrenador entrenador, Bicho bichoAbandonado, Guarderia guarderia) {
        super(entrenador);
        this.bicho = bichoAbandonado;
        this.guarderia = guarderia;
    }
}
