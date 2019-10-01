package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.CampeonDAO;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;

public class HibernateCampeonDAO extends HibernateDAO<Campeon> implements CampeonDAO {

    public HibernateCampeonDAO() {
        super(Campeon.class);
    }
}
