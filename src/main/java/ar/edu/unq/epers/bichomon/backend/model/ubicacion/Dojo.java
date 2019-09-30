package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dojo extends Ubicacion {

    @OneToOne
    private Campeon campeonActual;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Campeon> campeonesDelPasado;
    @OneToOne
    private DueloHelper dueloHelper;

    public Dojo(){}
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
    public ResultadoCombate realizarDuelo(Bicho bichoRetador) {
        return this.dueloHelper.realizarDuelo(bichoRetador, this);

    /*public Dojo(BusquedaHelper busquedaHelper) {
        super(busquedaHelper);
    }*/
    }
}