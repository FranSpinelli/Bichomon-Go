package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.CampeonDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.especie.NullEspecieLeaderException;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;

public class HibernateCampeonDAO extends HibernateDAO<Campeon> implements CampeonDAO {

    public HibernateCampeonDAO() {
        super(Campeon.class);
    }

    public List<Entrenador> campeones(){
        Session session = TransactionRunner.getCurrentSession();
        String hql = "select distinct campeon.bicho.entrenador from Campeon campeon where campeon.fechaDeFin = null " +
                "group by campeon.bicho.entrenador " +
                "order by campeon.fechaDeInicio desc";

        Query<Entrenador> query = session.createQuery(hql, Entrenador.class);
        query.setMaxResults(10);

        try{
            return query.list();
        }catch (NoResultException ex){
            return null;
        }

    }

    public Especie especieLider() {
        Session session = TransactionRunner.getCurrentSession();
        String hql = "select laEspecie from Campeon as lider join lider.bicho as pokemon join pokemon.especie as laEspecie" +
                "        where lider.fechaDeFin = null " +
                "        group by laEspecie" +
                "        order by count(pokemon) desc";

        Query<Especie> query = session.createQuery(hql, Especie.class);
        query.setMaxResults(1);

        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            throw new NullEspecieLeaderException();
        }
    }
}
