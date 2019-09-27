package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import javax.persistence.Entity;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dojo extends Ubicacion {

    private Campeon campeonActual;
    private List<Campeon> campeonesDelPasado;
    private DueloHelper dueloHelper;

    public Dojo(DueloHelper dueloHelper){

        this.campeonesDelPasado = new ArrayList<Campeon>();
        this.campeonActual = null;
        this.dueloHelper = dueloHelper;
    }

    public Campeon getCampeonActual() {
        return campeonActual;
    }

    public void setCampeonActual(Bicho bichoCampeon) {
        Campeon nuevoCampeon = new Campeon(bichoCampeon, LocalDate.now());
        Campeon campeonActualAGuardar = this.campeonActual;

        if(campeonActualAGuardar != null ){
            campeonActualAGuardar.setFechaDeFin(LocalDate.now());
            campeonesDelPasado.add(campeonActualAGuardar);
        }
        this.campeonActual = nuevoCampeon;
    }

    public DueloHelper getDueloHelper() {
        return dueloHelper;
    }

    public List<Campeon> getListaDeCampeones(){
        List<Campeon> listaAEntregar = new ArrayList<Campeon>();

        if(campeonActual != null) {
            listaAEntregar.addAll(campeonesDelPasado);
            listaAEntregar.add(campeonActual);
        }

        return listaAEntregar;
    }

    @Override
    public ResultadoCombate realizarDuelo(Bicho bichoRetador){

        return this.dueloHelper.realizarDuelo(bichoRetador, this);
    }
}