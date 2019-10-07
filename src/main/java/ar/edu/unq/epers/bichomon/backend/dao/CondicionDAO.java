package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;

import java.util.List;

public interface CondicionDAO {

    void guardar(CondicionDeEvolucion condicion);

    CondicionDeEvolucion recuperar(Integer id);

    void guardarTodos(List<CondicionDeEvolucion> condiciones);
}
