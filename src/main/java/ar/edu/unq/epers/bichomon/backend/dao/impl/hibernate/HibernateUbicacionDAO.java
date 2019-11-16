package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoNoUtilizado;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;

public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {
    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }

    @Override
    public Ubicacion recuperar(String ubicacion) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);
        String hql = "from Ubicacion u where u.nombre = :nombre";

        Query<Ubicacion> query = session.createQuery(hql, Ubicacion.class);
        query.setParameter("nombre", ubicacion);
        query.setMaxResults(1);

        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            return null;
        }
    }

    @Override
    public int cantidadEntrenadoresEn(String ubicacion) {
        if(this.recuperar(ubicacion) == null){
            throw new UbicacionInexistente("La ubicacion no existe");
        }
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);

        String hql = "select count(entrenador) from Entrenador entrenador join entrenador.ubicacionActual ubicacion where ubicacion.nombre = :nombre";

        Query<Long> query = session.createQuery(hql);
        query.setParameter("nombre", ubicacion);
        query.setMaxResults(1);
        return query.getSingleResult().intValue();
    }

    @Override
    public Bicho getCampeonHistorico(String dojo) {
        Ubicacion dojoActual = this.recuperar(dojo);
        if(dojoActual == null){
            throw new UbicacionInexistente("La ubicacion no existe");
        }
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);

       String hql = "select bicho from Campeon campeon join campeon.bicho bicho " +
               "where campeon.id in (select campeon.id from Dojo ubicacion join ubicacion.campeonesDelPasado campeon where ubicacion.nombre = :nombre) " +
               "or campeon.id in (select campeon.id from Dojo ubicacion join ubicacion.campeonActual campeon where ubicacion.nombre = :nombre) " +
               "order by DATEDIFF(ifNull(campeon.fechaDeFin, NOW()), campeon.fechaDeInicio) desc";

        Query<Bicho> query = session.createQuery(hql);
        query.setParameter("nombre", dojo);
        query.setMaxResults(1);
        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            throw new DojoNoUtilizado("El dojo no tuvo campeones");
        }
    }

    @Override
    public List<Ubicacion> recuperarTodos(List<String> ubicaciones){
        return ubicaciones.stream().map(ubicacion -> this.recuperar(ubicacion)).collect(Collectors.toList());
    }
}
