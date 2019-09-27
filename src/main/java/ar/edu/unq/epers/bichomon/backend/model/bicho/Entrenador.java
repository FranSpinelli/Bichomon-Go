package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.UbicacionIncorrectaException;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;


@Entity
public class Entrenador {

	@Id
	private String nombre;
	private int nivel;
	@ManyToOne
	private Ubicacion ubicacionActual;
	private Set<Bicho> inventarioDeBichos;

	public Entrenador(String nombre){
		this.nombre = nombre;
		this.inventarioDeBichos = new HashSet<Bicho>();
	}


	public Entrenador(){}

	public int getNivel() {

		return this.nivel;
	}

    public void abandonar(Bicho bicho) {
		try {
			ubicacionActual.recibirBicho(bicho);
			this.inventarioDeBichos.remove(bicho);
			bicho.agregarEx(this);
		} catch (UbicacionIncorrectaException e) {
			e.printStackTrace();
		}

		this.inventarioDeBichos.remove(bicho);
		bicho.agregarEx(this);
		((Guarderia)this.ubicacionActual).recibirBicho(bicho);
    }

    public Integer getCantidadDeBichos(){

		return this.inventarioDeBichos.size();
	}

	public Boolean tieneBicho(Bicho bicho) {

		return inventarioDeBichos.contains(bicho);
	}

	public String getNombre() {
		return this.nombre;
	}
	
	public void capturarBicho(Bicho bicho) {
		bicho.serCapturadoPor(this);
		this.inventarioDeBichos.add(bicho);
	}
}
