package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.impl.EspecieDAOTest;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class HibernateEspecieDAOTest extends EspecieDAOTest {

    @Override
    protected void definirDAO() {
        this.dao = new HibernateEspecieDAO();
    }

    @Override
    protected void correr(Runnable bloque) {
        run(bloque);
    }

    @Override
    public void limpiarEscenario(){run(SessionFactoryProvider::destroy);}
}
