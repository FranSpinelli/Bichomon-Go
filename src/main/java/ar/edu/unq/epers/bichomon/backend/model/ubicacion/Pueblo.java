package ar.edu.unq.epers.bichomon.backend.model.ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.Entity;
import java.util.List;

@Entity
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
public class Pueblo extends Ubicacion{
    //probabilidad suma 100
    private List<EspecieEncontrable> especiesHabitantes;

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

    /*public Pueblo(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
    }*/
}
