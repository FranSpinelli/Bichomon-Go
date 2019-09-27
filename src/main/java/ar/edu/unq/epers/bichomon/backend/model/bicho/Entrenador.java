package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.UbicacionIncorrectaException;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;


@Entity
public class Entrenador {

	@Id
	private String nombre;
	private int nivel;
	@OneToMany
	private List<Bicho> inventarioDeBichos;
	@ManyToOne
	private Ubicacion ubicacionActual;

	public Entrenador(String nombre){
		this.nombre = nombre;
		this.inventarioDeBichos = new ArrayList<Bicho>();
	}

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
