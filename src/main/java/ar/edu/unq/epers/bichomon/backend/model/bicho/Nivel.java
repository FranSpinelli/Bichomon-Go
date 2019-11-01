package ar.edu.unq.epers.bichomon.backend.model.bicho;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class Nivel extends AbstractNivel {

    @OneToOne
    private AbstractNivel siguiente;
    private int maximoDeBichos;
    private int nivel;
    private int limite;

    public Nivel(){}

    public Nivel(int nivel, int limite, int maximoDeBichos){
        setSiguiente(null);
        setNivel(nivel);
        setLimite(limite);
        setmaximoDeBichos(maximoDeBichos);
    }

    public Nivel(int nivel, int limite, AbstractNivel siguiente, int maximoDeBichos){
        setSiguiente(siguiente);
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
        return limite;
    }

    public void setLimite(int limite) {
        this.limite = limite;
    }

    public AbstractNivel eval(int xp) {
        AbstractNivel retorno;
        if (this.siguiente == null){
            throw new UndefinedNivelError("Proximo nivel no seteado");
        }
        if(xp >= this.getSiguiente().getLimite()){
            retorno = this.getSiguiente();
        }else{
            retorno = this;
        }
        return retorno;
    }
    private AbstractNivel getSiguiente(){
        return this.siguiente;
    }
    public void setSiguiente(AbstractNivel siguiente){
        this.siguiente = siguiente;
    }
}
