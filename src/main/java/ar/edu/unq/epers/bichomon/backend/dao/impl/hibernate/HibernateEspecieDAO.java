package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.especie.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import java.util.List;

public class HibernateEspecieDAO extends HibernateDAO<Especie> implements EspecieDAO {
    public HibernateEspecieDAO() {
        super(Especie.class);
    }

    @Override
    public void actualizar(Especie especie) {

    }

    @Override
    public Especie recuperar(String nombreEspecie) {
        return null;
    }

    @Override
    public List<Especie> recuperarTodos() {
        return null;
    }

    @Override
    public void eliminarTodos() {

    }
}
