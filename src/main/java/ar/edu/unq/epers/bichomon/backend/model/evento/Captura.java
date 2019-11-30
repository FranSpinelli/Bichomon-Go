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
    public String getEspecie(){return this.especie;}
    public void setEspecie(String newEspecie){this.especie = newEspecie;}
    public int getbichoId(){return this.bichoId;}
    public void setbichoId(int newBichoId){this.bichoId = newBichoId;}
    public String getUbicacion(){return this.ubicacion;}
    public void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
}
