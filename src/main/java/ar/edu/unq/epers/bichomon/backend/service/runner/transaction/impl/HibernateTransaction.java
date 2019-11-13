package ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.SessionatorType;
import org.hibernate.Session;

public class HibernateTransaction extends Sessionator implements Transaction {
    private static Session session;
    private static org.hibernate.Transaction tx = null;
    @Override
    public void commit() {
        tx.commit();
    }

    @Override
    public void begin() {
        session = SessionFactoryProvider.getInstance().createSession();
        tx = session.beginTransaction();
    }

    @Override
    public void rollback() {
        if (tx != null && tx.isActive()) {
            tx.rollback();
        }
    }

    @Override
    public void close() {
        if (session != null) {
            session.close();
            session = null;
        }
        tx = null;
    }

    @Override
    public SessionatorType getSessionatorType() {
        return SessionatorType.HIBERNATE;
    }

    @Override
    protected Object getSession() {
        return this.session;
    }
}
