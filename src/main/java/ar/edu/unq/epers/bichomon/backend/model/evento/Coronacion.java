package ar.edu.unq.epers.bichomon.backend.model.evento;

public class Coronacion extends Evento {


    private String entrenadorPerdedor;
    private String ubicacion;
    private String especieGanador;
    private Integer bichoGanadorId;
    private String especiePerdedor;
    private Integer bichoPerdedorId;

    public Coronacion(){}

    public Coronacion(String entrenadorCoronado, String entrenadorDescoronado, String dojo, String especieGanador, Integer bichoGanador, String especiePerdedor,  Integer bichoPerdedor) {
        super(entrenadorCoronado);
        this.entrenadorPerdedor = entrenadorDescoronado;
        this.ubicacion = dojo;
        this.especieGanador = especieGanador;
        this.bichoGanadorId = bichoGanador;
        this.especiePerdedor = especiePerdedor;
        this.bichoPerdedorId = bichoPerdedor;
    }
    public int getGanadorId(){return this.bichoGanadorId;}
    public void setGanadorId(int newBichoId){this.bichoGanadorId = newBichoId;}
    public String getEspecieGanador(){return this.especieGanador;}
    public void setEspecieGanador(String newEspecie){this.especieGanador = newEspecie;}
    public int getBichoPerdedorId(){return this.bichoPerdedorId;}
    public void setBichoPerdedorId(int newBichoId){this.bichoPerdedorId = newBichoId;}
    public String getEspeciePerdedor(){return this.especiePerdedor;}
    public void setEspeciePerdedor(String newEspecie){this.especiePerdedor = newEspecie;}
    public String getEntrenadorPerdedor(){return this.entrenadorPerdedor;}
    public void setEntrenadorPerdedor(String newEntrenador){this.entrenadorPerdedor = newEntrenador;}
    public String getUbicacion(){return this.ubicacion;}
    public void setUbicacion(String newUbicacion){this.ubicacion = newUbicacion;}
}
