package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.BusquedaHelper;
import java.util.List;

public interface BusquedaHelperDAO {
    void guardar(BusquedaHelper busqedaHelper);

    BusquedaHelper recuperar(Integer id);

    void guardarTodos(List<BusquedaHelper> busquedaHelpers);
}
