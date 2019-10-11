package ar.edu.unq.epers.bichomon.backend.model.ubicacion;
import javax.persistence.Entity;

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
    public Pueblo(String nombre){super(nombre);}

    /*public Pueblo(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
    }*/
}
