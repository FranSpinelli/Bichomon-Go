package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;

public class Coronacion extends Evento {


    private String entrenadorPerdedor;
    private String dojo;
    private String especieGanador;
    private int bichoGanadorId;
    private String especiePerdedor;
    private int bichoPerdedorId;

    public Coronacion(){}

    public Coronacion(String entrenadorCoronado, String entrenadorDescoronado, String dojo, String especieGanador, int bichoGanador, String especiePerdedor,  int bichoPerdedor) {
        super(entrenadorCoronado);
        this.entrenadorPerdedor = entrenadorDescoronado;
        this.dojo = dojo;
        this.especieGanador = especieGanador;
        this.bichoGanadorId = bichoGanador;
        this.especiePerdedor = especiePerdedor;
        this.bichoPerdedorId = bichoPerdedor;
    }
}
