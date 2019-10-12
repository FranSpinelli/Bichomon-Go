package ar.edu.unq.epers.bichomon.backend.dao.impl.jdbc;

import ar.edu.unq.epers.bichomon.backend.dao.impl.EspecieDAOTest;
import ar.edu.unq.epers.bichomon.backend.dao.impl.JDBCEspecieDAO;
import org.junit.Test;

public class JDBCEspecieDAOTest extends EspecieDAOTest {

    @Test(expected = RuntimeException.class)
    public void testActualizarCasoNoFeliz(){
        this.correr(() -> {
            //        thrown.expect(RuntimeException.class);
//        thrown.expectMessage("No existe el personaje " + pacacho);
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
}

