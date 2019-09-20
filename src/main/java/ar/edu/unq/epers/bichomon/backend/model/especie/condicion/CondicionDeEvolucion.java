package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public abstract class CondicionDeEvolucion {
	
	protected int cantidad;
	
	public CondicionDeEvolucion(int cantidad){
		this.cantidad = cantidad;
	}

	public abstract boolean puedeEvolucionar(Bicho bicho);

}
