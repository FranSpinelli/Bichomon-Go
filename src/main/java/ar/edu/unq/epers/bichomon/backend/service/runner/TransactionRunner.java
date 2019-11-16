package ar.edu.unq.epers.bichomon.backend.service.runner;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.Neo4jTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.TransactionManager;

import java.util.Arrays;
import java.util.function.Supplier;

//import org.hibernate.Transaction;

public class TransactionRunner {
    private final static TransactionManager tx = new TransactionManager()
            .addPossibleTransaction(new HibernateTransaction())
            .addPossibleTransaction(new Neo4jTransaction());
    private static Boolean isRunning = false;

    public static void run(Runnable bloque, TransactionType... tipos) {
        run(()->{
                    bloque.run();
                    return null;
                },
                tipos);
    }

    public static void run(Runnable bloque, Transaction transaction) {
        run(()->{
            bloque.run();
            return null;
        },
            transaction);
    }

    public  static <T> T run(Supplier<T> bloque, TransactionType... tipos) {
        if(isRunning){
            return  bloque.get();
        }

        Arrays.stream(tipos).forEach(transactionType -> tx.addTransaction(transactionType));
        try {
            isRunning = true;
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
            isRunning = false;
        }
    }

    public  static <T> T run(Supplier<T> bloque, Transaction transaction) {
        return run(bloque, TransactionType.HIBERNATE, TransactionType.NEO4J);
    }

    public static Object getCurrentSession(TransactionType transactionType) {
        return tx.getCurrentSession(transactionType);
    }
}
