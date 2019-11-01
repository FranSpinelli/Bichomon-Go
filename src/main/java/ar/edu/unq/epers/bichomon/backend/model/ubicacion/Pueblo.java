package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pueblo extends Ubicacion{
    @OneToMany(cascade = CascadeType.ALL)
    private List<EspecieEncontrable> especiesHabitantes = new ArrayList();


    public Pueblo(){
        super();
    }

    public Pueblo(String nombre) {
        super(nombre);
    }

    public Pueblo(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
    }

    @Override
    protected Especie elegirEspecie() {
        Integer random = (new Aleatorio()).nextInt(100);
        Integer tope = 0;
        for(EspecieEncontrable especieHabitante : this.especiesHabitantes){
            tope += especieHabitante.getProbabilidad();
            if(tope > random){
               return especieHabitante.getEspecie();
            }
        }
        //No deberia llegar hasta aca
        return null;
    }

    @Override
    protected Boolean esBusquedaExitosaPosible(Entrenador entrenador) {
        return !this.especiesHabitantes.isEmpty();
    }

    @Override
    protected String mensajeBusquedaExitosaNoPosible() {
        return "No habitan especies en este pueblo";
    }

    public List<EspecieEncontrable> getEspeciesHabitantes() {
        return this.especiesHabitantes;
    }

    public void setEspeciesHabitantes(List<EspecieEncontrable> especiesHabitantes) {
        this.especiesHabitantes = especiesHabitantes;
    }

    public void addEspecieHabitante(Especie especie, Integer probabilidad){
        this.especiesHabitantes.add(new EspecieEncontrable(especie, probabilidad));
    }

}
