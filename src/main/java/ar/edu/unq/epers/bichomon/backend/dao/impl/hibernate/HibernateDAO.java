package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import org.hibernate.Session;


import javax.persistence.Query;
import java.util.List;

public class HibernateDAO<T> {

    private Class<T> entityType;

    public HibernateDAO(Class<T> entityType){
        this.entityType = entityType;
    }

    public void guardar(T item) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);
        session.save(item);
    }

    public T recuperar(Integer id) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);
        return session.get(entityType, id);
    }

    public void eliminarTodos() {
        String myTable = entityType.getName();
        String hql = String.format("delete from %s", myTable);
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.HIBERNATE);
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate();
    }

    public void guardarTodos(List<T> items){
        for(T item : items){
            this.guardar(item);
        }
    }
}
