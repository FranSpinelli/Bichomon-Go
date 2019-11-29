package ar.edu.unq.epers.bichomon.backend.model.evento;


public class Captura extends Evento{
    private String especie;
    private int bichoId;
    private String ubicacion;

    public Captura(){}

    public Captura(String entrenador, int bicho, String especie, String ubicacion){
        super(entrenador);
        this.bichoId = bicho;
        this.especie = especie;
        this.ubicacion = ubicacion;
    }
    String getEspecie(){return this.especie;}
    void setEspecie(String newEspecie){this.especie = newEspecie;}
    int getbichoId(){return this.bichoId;}
    void setbichoId(int newBichoId){this.bichoId = newBichoId;}
    String getUbicacion(){return this.ubicacion;}
    void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
}
