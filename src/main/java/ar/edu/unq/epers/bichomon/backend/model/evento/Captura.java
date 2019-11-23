package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

public class Captura extends Evento{
    private String especie;
    private int bichoId;

    public Captura(){}

    public Captura(String entrenador, int bicho, String especie) {
        super(entrenador);
        this.bichoId = bicho;
        this.especie = especie;
    }
}
