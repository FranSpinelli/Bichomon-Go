package ar.edu.unq.epers.bichomon.backend.dao.bicho;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public interface BichoDAO {

	Bicho recuperar(int id);

	void guardarBicho(Bicho bicho);

	Bicho updatePorEvolucion(int idBicho);

}
