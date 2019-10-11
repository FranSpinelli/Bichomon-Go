package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

public class HibernateEntrenadorDAO extends HibernateDAO<Entrenador> implements EntrenadorDAO {
    public HibernateEntrenadorDAO() {
        super(Entrenador.class);
    }

    @Override
    public Entrenador recuperar(String nombre) {
        Session session = TransactionRunner.getCurrentSession();
        String hql = "from Entrenador e where e.nombre = :nombre";

        Query<Entrenador> query = session.createQuery(hql, Entrenador.class);
        query.setParameter("nombre", nombre);
        query.setMaxResults(1);

        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            return null;
        }
    }
    public List<Entrenador> lideres(){
        Session session = TransactionRunner.getCurrentSession();
        String hql ="select entrenador from Entrenador entrenador join entrenador.inventarioDeBichos bicho "+
                "group by entrenador "+
                "order by sum(bicho.energia) desc";

        Query<Entrenador> query = session.createQuery(hql, Entrenador.class);
        query.setMaxResults(10);

        try{
            return query.list();
        }catch (NoResultException ex){
            return null;
        }
    }
}
