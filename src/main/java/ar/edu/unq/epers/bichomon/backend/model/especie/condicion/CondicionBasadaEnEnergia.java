package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class CondicionBasadaEnEnergia extends CondicionDeEvolucion{

	public CondicionBasadaEnEnergia(int energia) {
		super(energia);
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getEnergia() > this.cantidad;
	}

}
