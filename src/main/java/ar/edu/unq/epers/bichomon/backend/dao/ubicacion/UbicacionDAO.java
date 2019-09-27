package ar.edu.unq.epers.bichomon.backend.dao.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

public interface UbicacionDAO {

	Ubicacion recuperar(int id);

	void guardarUbicacion(Ubicacion ubicacion);

}
