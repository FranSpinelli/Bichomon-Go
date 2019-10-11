package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Dojo extends Ubicacion {
    @OneToOne(cascade = CascadeType.ALL)
    private Campeon campeonActual;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<Campeon> campeonesDelPasado;

    public Dojo(String nombre){
        super(nombre);
        this.campeonesDelPasado = new ArrayList<Campeon>();
        this.campeonActual = null;
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

    public List<Campeon> getListaDeCampeones(){
        List<Campeon> listaAEntregar = new ArrayList<Campeon>();

        if(campeonActual != null) {
            listaAEntregar.addAll(campeonesDelPasado);
            listaAEntregar.add(campeonActual);
        }

        return listaAEntregar;
    }

    @Override
    public ResultadoCombate realizarDuelo(Bicho bichoRetador, Estrategia estrategiaAUtilizar) {

        return estrategiaAUtilizar.calcularDuelo(bichoRetador, this);

    /*public Dojo(BusquedaHelper busquedaHelper) {
        super(busquedaHelper);
    }*/
    }
}
