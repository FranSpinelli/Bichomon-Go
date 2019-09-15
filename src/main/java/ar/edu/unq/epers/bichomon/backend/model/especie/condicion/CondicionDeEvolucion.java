package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class CondicionDeEvolucion {
	
	protected int cantidad;
	
	public CondicionDeEvolucion(int cantidad){
		this.cantidad = cantidad;
	}

	public boolean puedeEvolucionar(Bicho bicho) {
		// TODO Auto-generated method stub
		return false;
	}

}
