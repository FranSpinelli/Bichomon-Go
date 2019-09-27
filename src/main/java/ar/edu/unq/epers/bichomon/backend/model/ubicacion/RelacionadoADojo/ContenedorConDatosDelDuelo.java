package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.ArrayList;
import java.util.List;

public class ContenedorConDatosDelDuelo {

    private Bicho ganadorDelDuelo;

    private List<Double>danhoRecibidoPorBichoCampeon;
    private List<Double>danhoRecibidoPorBichoRetador;

    public ContenedorConDatosDelDuelo(){
        this.ganadorDelDuelo = null;
        this.danhoRecibidoPorBichoCampeon = new ArrayList<Double>();
        this.danhoRecibidoPorBichoRetador = new ArrayList<Double>();
    }

    public Bicho getGanadorDelDuelo() {
        return ganadorDelDuelo;
    }

    public void setGanadorDelDuelo(Bicho campeonDelDuelo) {
        this.ganadorDelDuelo = campeonDelDuelo;
    }

    public void addDanhoRecibidoPorBichoCampeon(Double danhoRecibido){
        this.danhoRecibidoPorBichoCampeon.add(danhoRecibido);
    }

    public void addDanhoRecibidoPorBichoRetador(Double danhoRecibido){
        this.danhoRecibidoPorBichoRetador.add(danhoRecibido);
    }

    public Double getDanhoTotalRecibidoPorCampeon(){
        return danhoRecibidoPorBichoCampeon.stream().mapToDouble(Double::doubleValue).sum();
    }

    public Double getDanhoTotalRecibidoPorRetador(){
        return danhoRecibidoPorBichoRetador.stream().mapToDouble(Double::doubleValue).sum();
    }

    public List<Double> getListaDeDanhoRecibidoPorBichoCampeon(){
        return this.danhoRecibidoPorBichoCampeon;
    }

    public List<Double> getListaDeDanhoRecibidoPorBichoRetador(){
        return this.danhoRecibidoPorBichoRetador;
    }
}
