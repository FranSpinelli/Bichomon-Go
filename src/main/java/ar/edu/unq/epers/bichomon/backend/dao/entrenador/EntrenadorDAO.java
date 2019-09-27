package ar.edu.unq.epers.bichomon.backend.dao.entrenador;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public interface EntrenadorDAO {

	Entrenador recuperar(int id);

	void guardarEntrenador(Entrenador entrenador);

}
