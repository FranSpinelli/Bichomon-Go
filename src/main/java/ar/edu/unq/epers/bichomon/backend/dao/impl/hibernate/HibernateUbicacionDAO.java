package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoNoUtilizado;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoSinCampeon;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import org.hibernate.Session;
import org.hibernate.query.Query;

import javax.persistence.NoResultException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class HibernateUbicacionDAO extends HibernateDAO<Ubicacion> implements UbicacionDAO {
    public HibernateUbicacionDAO() {
        super(Ubicacion.class);
    }

    @Override
    public Ubicacion recuperar(String ubicacion) {
        Session session = TransactionRunner.getCurrentSession();
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
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select count(entrenador) from Entrenador entrenador join entrenador.ubicacionActual ubicacion where ubicacion.nombre = :nombre";

        Query<Long> query = session.createQuery(hql);
        query.setParameter("nombre", ubicacion);
        query.setMaxResults(1);
        return query.getSingleResult().intValue();
    }

    @Override
    public Bicho getCampeon(String dojo) {
        if(this.recuperar(dojo) == null){
            throw new UbicacionInexistente("La ubicacion no existe");
        }
        Session session = TransactionRunner.getCurrentSession();

        String hql = "select bicho from Ubicacion ubicacion join ubicacion.campeonActual campeon join campeon.bicho bicho where ubicacion.nombre = :nombre";

        Query<Bicho> query = session.createQuery(hql);
        query.setParameter("nombre", dojo);
        query.setMaxResults(1);
        try{
            return query.getSingleResult();
        }catch (NoResultException ex){
            throw new DojoSinCampeon("El dojo no tiene campeon actualmente");
        }
    }

    @Override
    public Bicho getCampeonHistorico(String dojo) {
        Dojo dojoActual = (Dojo) this.recuperar(dojo);
        if(dojoActual == null){
            throw new UbicacionInexistente("La ubicacion no existe");
        }
        if(dojoActual.getListaDeCampeones().isEmpty()){
            throw new DojoNoUtilizado("El dojo no tuvo campeones");
        }
        Session session = TransactionRunner.getCurrentSession();
        //TODO: Preguntar como implementar la query con fechas
        /*

        String hql = "select bicho from Ubicacion ubicacion join ubicacion.campeonActual campeon join ubicacion.campeonesDelPasado campeonDelPasado join campeon.bicho bicho join campeonDelPasado.bicho bichoDelPasado where ubicacion.nombre = :nombre";//Ordenar por diferencia entre fechas

        Query<Bicho> query = session.createQuery(hql);
        query.setParameter("nombre", dojo);
        query.setMaxResults(1);
        return query.getSingleResult();
*/
        // TODO: Solucion provisoria no definitiva
        return dojoActual.getListaDeCampeones().stream().max(Comparator.comparing(Campeon::cantidadDiasDeCampeonato)).get().getBicho();
    }
}
