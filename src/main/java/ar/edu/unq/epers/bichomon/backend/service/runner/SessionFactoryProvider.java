package ar.edu.unq.epers.bichomon.backend.service.runner;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.SessionatorType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class SessionFactoryProvider {

    private static SessionFactoryProvider INSTANCE;

    private SessionFactory sessionFactory;

    public static SessionFactoryProvider getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionFactoryProvider();
        }
        return INSTANCE;
    }

    public static void destroy() {
        Session session = (Session) TransactionRunner.getCurrentSession(SessionatorType.HIBERNATE);
        List tablas = session.createNativeQuery("show tables").getResultList();
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=0;").executeUpdate();
        tablas.forEach(tabla ->{
            if(tabla != "hibernate_secuence"){
                session.createNativeQuery("TRUNCATE TABLE "+tabla).executeUpdate();
            }
        });
        session.createNativeQuery("SET FOREIGN_KEY_CHECKS=1;").executeUpdate();
    }

    /*if (INSTANCE != null && INSTANCE.sessionFactory != null) {
            INSTANCE.sessionFactory.close();
        }
        INSTANCE = null;*/

    private SessionFactoryProvider() {
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        this.sessionFactory = configuration.buildSessionFactory();
    }

    public Session createSession() {
        return this.sessionFactory.openSession();
    }



}