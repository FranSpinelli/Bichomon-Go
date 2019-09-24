package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {
    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }
}
