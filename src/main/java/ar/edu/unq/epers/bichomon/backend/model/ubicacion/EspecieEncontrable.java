package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

public class EspecieEncontrable {
    private int id;
    private Especie especie;
    private Integer probabilidad;

    public EspecieEncontrable() {}

    public EspecieEncontrable(int id, Especie especie, Integer probabilidad) {
        this.id = id;
        this.especie = especie;
        this.probabilidad = probabilidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Especie getEspecie() {
        return especie;
    }

    public void setEspecie(Especie especie) {
        this.especie = especie;
    }

    public Integer getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(Integer probabilidad) {
        this.probabilidad = probabilidad;
    }
}
