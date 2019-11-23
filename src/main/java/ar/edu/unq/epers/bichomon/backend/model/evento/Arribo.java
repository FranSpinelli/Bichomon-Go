package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

public class Arribo extends Evento{

    private String ubicacion;

    public Arribo(){}

    public Arribo(String entrenador, String ubicacion) {
        super(entrenador);
        this.ubicacion = ubicacion;
    }
}
