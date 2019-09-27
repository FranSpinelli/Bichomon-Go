package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import javax.persistence.Entity;

@Entity
public class Dojo extends Ubicacion {

    private Bicho campeonActual;
    private DueloHelper dueloHelper;

    public Dojo(DueloHelper dueloHelper){

        this.campeonActual = null;
        this.dueloHelper = dueloHelper;
    }

    public Bicho getCampeonActual(){
        return this.campeonActual;
    }

    public void setCampeonActual(Bicho nuevoCampeon){
        this.campeonActual = nuevoCampeon;
    }

    @Override
    public ContenedorConDatosDelDuelo realizarDuelo(Bicho bichoRetador){

        return this.dueloHelper.realizarDuelo(bichoRetador, this);
    }
}