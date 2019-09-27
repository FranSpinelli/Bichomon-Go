package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public interface BichoService {

	Entrenador buscar(String entrenador);
	
	void abandonar(String entrenador, int bicho);
	
	boolean puedeEvolucionar(String entrenador, int bicho);
	
	Bicho evolucionar(String entrenador, int bicho);
}
