package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;

public class Coronacion extends Evento {


    private String entrenadorPerdedor;
    private String ubicacion;
    private String especieGanador;
    private int bichoGanadorId;
    private String especiePerdedor;
    private int bichoPerdedorId;

    public Coronacion(){}

    public Coronacion(String entrenadorCoronado, String entrenadorDescoronado, String dojo, String especieGanador, int bichoGanador, String especiePerdedor,  int bichoPerdedor) {
        super(entrenadorCoronado);
        this.entrenadorPerdedor = entrenadorDescoronado;
        this.ubicacion = dojo;
        this.especieGanador = especieGanador;
        this.bichoGanadorId = bichoGanador;
        this.especiePerdedor = especiePerdedor;
        this.bichoPerdedorId = bichoPerdedor;
    }
    int getGanadorId(){return this.bichoGanadorId;}
    void setGanadorId(int newBichoId){this.bichoGanadorId = newBichoId;}
    String getEspecieGanador(){return this.especieGanador;}
    void setEspecieGanador(String newEspecie){this.especieGanador = newEspecie;}
    int getBichoPerdedorId(){return this.bichoPerdedorId;}
    void setBichoPerdedorId(int newBichoId){this.bichoPerdedorId = newBichoId;}
    String getEspeciePerdedor(){return this.especiePerdedor;}
    void setEspeciePerdedor(String newEspecie){this.especiePerdedor = newEspecie;}
    String getEntrenadorPerdedor(){return this.entrenadorPerdedor;}
    void setEntrenadorPerdedor(String newEntrenador){this.entrenadorPerdedor = newEntrenador;}
    String getUbicacion(){return this.ubicacion;}
    void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
}
