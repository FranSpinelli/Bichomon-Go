package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class CondicionBasadaEnVictorias extends CondicionDeEvolucion{
	

	public CondicionBasadaEnVictorias(int victorias) {
		super(victorias);
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getVictorias() > this.cantidad;
	}

}
