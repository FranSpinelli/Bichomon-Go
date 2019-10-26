package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.NivelDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;

public class HibernateNivelDAO extends HibernateDAO<AbstractNivel> implements NivelDAO{
    public HibernateNivelDAO() {super(AbstractNivel.class);}
}
