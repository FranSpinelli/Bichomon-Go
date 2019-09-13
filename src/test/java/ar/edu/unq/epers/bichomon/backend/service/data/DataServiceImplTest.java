package ar.edu.unq.epers.bichomon.backend.service.data;

import ar.edu.unq.epers.bichomon.backend.dao.impl.JDBCEspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.data.DataServiceImpl;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class DataServiceImplTest {
    JDBCEspecieDAO jdbcDelDao = new JDBCEspecieDAO();
    DataServiceImpl dataServiceDePrueva = new DataServiceImpl(jdbcDelDao);

    @Test
    public void testMetodoCrearSetDeDatosInicialesCarga7EspeciesEnLaBaseDeDatos(){

        dataServiceDePrueva.crearSetDatosIniciales();

        List<Especie> especiesPersistidas = jdbcDelDao.recuperarTodos();

        assertEquals(especiesPersistidas.size(),7);
    }

    @Test
    public void testMetodoEliminarDatosVaciaLaTablaEspecieDeLaBaseDeDatos(){

        assertEquals(jdbcDelDao.recuperarTodos().size(),7);

        dataServiceDePrueva.eliminarDatos();

        assertEquals(jdbcDelDao.recuperarTodos().size(),0);
    }
}
