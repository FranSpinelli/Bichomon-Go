package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.impl.EspecieDAOTest;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class HibernateEspecieDAOTest extends EspecieDAOTest {

    private Transaction hibernateTransaction = new HibernateTransaction();

    @Override
    protected void definirDAO() {
        this.dao = new HibernateEspecieDAO();
    }

    @Override
    protected void correr(Runnable bloque) {
        run(bloque, this.hibernateTransaction);
    }

    @Override
    public void limpiarEscenario(){
        this.correr(SessionFactoryProvider::destroy);
    }
}
