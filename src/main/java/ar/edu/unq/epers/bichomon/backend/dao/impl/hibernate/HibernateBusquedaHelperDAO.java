package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.BusquedaHelperDAO;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.BusquedaHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.BusquedaHelperMock;

public class HibernateBusquedaHelperDAO extends HibernateDAO<BusquedaHelper> implements BusquedaHelperDAO {
    public HibernateBusquedaHelperDAO() {
        super(BusquedaHelper.class);
    }


}
