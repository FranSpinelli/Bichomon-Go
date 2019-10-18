package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import ar.edu.unq.epers.bichomon.backend.dao.impl.EspecieDAOTest;

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


    /*private EspecieDAO dao = new JDBCEspecieDAO();
    private Especie pacacho;
    private Especie charmandar;
    private Especie charmilian;
    private Especie chorizard;

    @After
    public void limpiarEscenario(){
        run(SessionFactoryProvider::destroy);
    }

    @Test
    public void actualizar() {

    }

    @Test
    public void recuperar() {
    }

    @Test
    public void recuperarTodos() {
    }*/
}
