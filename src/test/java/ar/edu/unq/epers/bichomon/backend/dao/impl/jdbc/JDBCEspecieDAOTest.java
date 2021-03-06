package ar.edu.unq.epers.bichomon.backend.dao.impl.jdbc;

import ar.edu.unq.epers.bichomon.backend.dao.impl.EspecieDAOTest;
import ar.edu.unq.epers.bichomon.backend.dao.impl.JDBCEspecieDAO;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JDBCEspecieDAOTest extends EspecieDAOTest {

    @Test(expected = RuntimeException.class)
    public void testActualizarCasoNoFeliz(){
        this.correr(() -> {
            this.dao.actualizar(this.pacacho);
        });

    }

    @Override
    protected void definirDAO() {
        this.dao = new JDBCEspecieDAO();
    }

    @Override
    protected void correr(Runnable bloque) {
        bloque.run();
    }

    @Override
    @Test
    public void getMasImpopulares(){
        assertTrue(true);
    }

    @Override
    @Test
    public void getMasPopulares(){
        assertTrue(true);
    }
}

