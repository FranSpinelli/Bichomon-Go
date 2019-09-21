package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import java.util.Set;

public class Entrenador {

	private int nivel;
	private Set<Bicho> inventarioDeBichos;
	private Ubicacion ubicacionActual;

	public Entrenador(){}

	public int getNivel() {

		return this.nivel;
	}

    public void abandonar(Bicho bicho) {

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
}
