package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public class HibernateEntrenadorDAO extends HibernateDAO<Entrenador> implements EntrenadorDAO {
    public HibernateEntrenadorDAO() {
        super(Entrenador.class);
    }

    @Override
    public Entrenador recuperar(String nombre) {
        return null;
    }
}
