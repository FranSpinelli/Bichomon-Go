package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.BusquedaHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.BusquedaHelperMock;

public interface BusquedaHelperDAO {
    void guardar(BusquedaHelperMock busqedaHelper);

    BusquedaHelperMock recuperar(Integer id);
}
