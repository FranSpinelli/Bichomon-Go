package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;

public class Abandono extends Evento{

    private String ubicacion;
    private String especie;
    private int bichoId;

    public Abandono(){}

    public Abandono(String guarderia, String entrenador, String especie, int bicho) {
        super(entrenador);
        this.especie = especie;
        this.bichoId = bicho;
        this.ubicacion = guarderia;
    }
    String getEspecie(){return this.especie;}
    void setEspecie(String newEspecie){this.especie = newEspecie;}
    String getUbicacion(){return this.ubicacion;}
    void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
    int getbichoId(){return this.bichoId;}
    void setbichoId(int newBichoId){this.bichoId = newBichoId;}
}
