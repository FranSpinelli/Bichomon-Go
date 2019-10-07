package ar.edu.unq.epers.bichomon.backend.model.ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/*
public class Pueblo extends Ubicacion {
	
	private String nombre;
	
	public Pueblo(String nombre) {
		this.setNombre(nombre);
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

*/
@Entity
public class Pueblo extends Ubicacion{
    @OneToMany(cascade = CascadeType.ALL)
    private List<EspecieEncontrable> especiesHabitantes = new ArrayList();

    public Pueblo(){}

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
        throw new BusquedaNoExitosa("No habitan especies en este pueblo");
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
/*public Pueblo(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
    }*/
}
