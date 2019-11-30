package ar.edu.unq.epers.bichomon.backend.model.evento;

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
    public String getEspecie(){return this.especie;}
    public void setEspecie(String newEspecie){this.especie = newEspecie;}
    public String getUbicacion(){return this.ubicacion;}
    public void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
    public int getbichoId(){return this.bichoId;}
    public void setbichoId(int newBichoId){this.bichoId = newBichoId;}
}
