package ar.edu.unq.epers.bichomon.backend.model.evento;

public class Arribo extends Evento{

    private String ubicacion;

    public Arribo(){}

    public Arribo(String entrenador, String ubicacion) {
        super(entrenador);
        this.ubicacion = ubicacion;
    }
    public String getUbicacion(){return this.ubicacion;}
    public void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
}
