package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.*;

@Entity
public class BusquedaHelperMock extends BusquedaHelper {
    private Boolean factorTiempo;
    private Boolean factorNivel;
    private Boolean factorPoblacion;
    private Boolean factorRandom;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Especie especie;

    public BusquedaHelperMock(){}

    public BusquedaHelperMock(Boolean factorTiempo, Boolean factorNivel, Boolean factorPoblacion, Boolean factorRandom, Especie especie){
        this.factorTiempo = factorTiempo;
        this.factorNivel = factorNivel;
        this.factorPoblacion = factorPoblacion;
        this.factorRandom = factorRandom;
        this.especie = especie;
    }

    public Boolean factorTiempo(Entrenador entrenador) {
        return this.factorTiempo;
    }

    public Boolean factorNivel(Entrenador entrenador) {
        return this.factorNivel;
    }

    public Boolean factorPoblacion(Ubicacion ubicacion) {
        return this.factorPoblacion;
    }

    public Boolean factorRandom() {
        return this.factorRandom;
    }

    public Bicho generarBicho(Ubicacion ubicacion) {
        Bicho bicho = new Bicho();
        bicho.setEspecie(this.especie);
        return bicho;
    }

    public Boolean getFactorTiempo() {
        return factorTiempo;
    }

    public void setFactorTiempo(Boolean factorTiempo) {
        this.factorTiempo = factorTiempo;
    }

    public Boolean getFactorNivel() {
        return factorNivel;
    }

    public void setFactorNivel(Boolean factorNivel) {
        this.factorNivel = factorNivel;
    }

    public Boolean getFactorPoblacion() {
        return factorPoblacion;
    }

    public void setFactorPoblacion(Boolean factorPoblacion) {
        this.factorPoblacion = factorPoblacion;
    }

    public Boolean getFactorRandom() {
        return factorRandom;
    }

    public void setFactorRandom(Boolean factorRandom) {
        this.factorRandom = factorRandom;
    }

}
