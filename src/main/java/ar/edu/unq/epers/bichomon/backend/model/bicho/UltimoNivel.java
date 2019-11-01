package ar.edu.unq.epers.bichomon.backend.model.bicho;

import javax.persistence.Entity;

@Entity
public class UltimoNivel extends AbstractNivel{

    private int nivel;
    private int maximoDeBichos;
    private int limite;

    public UltimoNivel(){}

    public UltimoNivel(int nivel, int limite,int maximoDeBichos) {
        setNivel(nivel);
        setLimite(limite);
        setmaximoDeBichos(maximoDeBichos);
    }

    public int getNivel() {
        return this.nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public int getNroBichos() {
        return this.maximoDeBichos;
    }

    public void setmaximoDeBichos(int maximoDeBichos) {
        this.maximoDeBichos = maximoDeBichos;
    }

    public int getLimite() {
        return this.limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public AbstractNivel eval(int xp) {
        return this;
    }



}
