package ar.edu.unq.epers.bichomon.backend.service.runner;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.SessionatorType;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;

import java.util.function.Supplier;

//import org.hibernate.Transaction;

public class TransactionRunner {
    private static Transaction tx;

    public static void run(Runnable bloque, Transaction transaction) {
        run(()->{
            bloque.run();
            return null;
        },
            transaction);
    }

/*
    public static <T> T run(Supplier<T> bloque) {
        Transaction tx = null;

        try {
            session = SessionFactoryProvider.getInstance().createSession();
            tx = session.beginTransaction();

            //codigo de negocio
            T resultado = bloque.get();

            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            //solamente puedo cerrar la transaccion si fue abierta antes,
            //puede haberse roto el metodo ANTES de abrir una transaccion
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (session != null) {
                session.close();
                session = null;
            }
        }
    }*/

    public static <T> T run(Supplier<T> bloque, Transaction transaction) {
        tx = transaction;
        try {
            tx.begin();

            //codigo de negocio
            T resultado = bloque.get();

            tx.commit();
            return resultado;
        } catch (RuntimeException e) {
            //solamente puedo cerrar la transaccion si fue abierta antes,
            //puede haberse roto el metodo ANTES de abrir una transaccion
            tx.rollback();
            throw e;
        } finally {
            tx.close();
        }
    }

    public static Object getCurrentSession(SessionatorType sessionatorType) {
        return tx.getCurrentSession(sessionatorType);
    }
}
