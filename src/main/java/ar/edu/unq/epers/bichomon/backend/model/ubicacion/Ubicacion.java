package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public abstract class Ubicacion {

	public void recibirBicho(Bicho bicho) throws UbicacionIncorrectaException {
		throw new UbicacionIncorrectaException("");
	}
	
	abstract void buscar();
}
