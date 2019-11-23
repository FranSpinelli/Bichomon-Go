package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;

public class Abandono extends Evento{

    private String guarderia;
    private String especie;
    private int bichoId;

    public Abandono(){}

    public Abandono(String guarderia, String entrenador, String especie, int bicho) {
        super(entrenador);
        this.especie = especie;
        this.bichoId = bicho;
        this.guarderia = guarderia;
    }
}
