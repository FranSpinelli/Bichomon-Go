package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

public class HibernateEspecieDAO extends HibernateDAO<Especie> implements EspecieDAO {

    public HibernateEspecieDAO() {
        super(Especie.class);
    }

    public void actualizar(Especie especie) {
        this.guardar(especie);
    }

    public Especie recuperar(String nombreEspecie) {
        Session session = TransactionRunner.getCurrentSession();
        String hql = "from Especie e where e.nombre = :nombre";

        Query<Especie> query = session.createQuery(hql, Especie.class);
        query.setParameter("nombre", nombreEspecie);
        query.setMaxResults(1);

        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            return null;
        }
    }

    public List<Especie> recuperarTodos() {
        Session session = TransactionRunner.getCurrentSession();

        String hql = "from Especie e "
                + "order by e.nombre asc";

        Query<Especie> query = session.createQuery(hql,  Especie.class);
        return query.getResultList();
    }

    @Override
    public List<Especie> getMasPopulares() {
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select especie from Entrenador entrenador join entrenador.inventarioDeBichos bicho join " +
                "bicho.especie especie group by especie order by count(bicho)";

        Query<Especie> query = session.createQuery(hql,  Especie.class);
        query.setMaxResults(10);
        return query.getResultList();


    @Override
    public List<Especie> getMasImpopulares() {
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select especie from Guarderia guarderia join guarderia.bichosAbandonados bicho join " +
                "bicho.especie especie group by especie order by count(bicho)";

        Query<Especie> query = session.createQuery(hql,  Especie.class);
        query.setMaxResults(10);
        return query.getResultList();
    }
}

