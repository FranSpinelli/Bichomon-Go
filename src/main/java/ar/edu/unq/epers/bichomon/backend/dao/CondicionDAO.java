package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;

public interface CondicionDAO {

    void guardar(CondicionDeEvolucion condicion);

    CondicionDeEvolucion recuperar(int id);
}
