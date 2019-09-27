package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ResultadoCombateTest {

    private ResultadoCombate contenedor;
    private Bicho bichoMock1;

    @Before
    public void setUp(){
        contenedor = new ResultadoCombate();
        Bicho bichoMock1 = Mockito.mock(Bicho.class);
    }

    @Test
    public void getGanadorDelDuelo() {

        assertEquals(contenedor.getGanadorDelDuelo(), null);
    }

    @Test
    public void setGanadorDelDuelo() {
        assertEquals(contenedor.getGanadorDelDuelo(), null);
        contenedor.setGanadorDelDuelo(bichoMock1);
        assertEquals(contenedor.getGanadorDelDuelo(), bichoMock1);
    }

    @Test
    public void addDanhoRecibidoPorBichoCampeon() {
        contenedor.addDanhoRecibidoPorBichoCampeon(11.0);
        assertEquals(contenedor.getDanhoTotalRecibidoPorCampeon(), 11.0, 0.0);
    }

    @Test
    public void addDanhoRecibidoPorBichoRetador() {
        contenedor.addDanhoRecibidoPorBichoRetador(11.0);
        assertEquals(contenedor.getDanhoTotalRecibidoPorRetador(), 11.0, 0.0);
    }

    @Test
    public void getDanhoTotalRecibidoPorCampeon() {
        assertEquals(contenedor.getDanhoTotalRecibidoPorCampeon(), 0, 0.0);
        contenedor.addDanhoRecibidoPorBichoCampeon(1.0);
        assertEquals(contenedor.getDanhoTotalRecibidoPorCampeon(), 1, 0.0);
    }

    @Test
    public void getDanhoTotalRecibidoPorRetador() {
        assertEquals(contenedor.getDanhoTotalRecibidoPorRetador(), 0, 0.0);
        contenedor.addDanhoRecibidoPorBichoRetador(1.0);
        assertEquals(contenedor.getDanhoTotalRecibidoPorRetador(), 1, 0.0);
    }

    @Test
    public void getListaDeDanhoRecibidoPorBichoCampeon(){
        assertTrue(contenedor.getListaDeDanhoRecibidoPorBichoCampeon().isEmpty());
    }

    @Test
    public void getListaDeDanhoRecibidoPorBichoRetador(){
        assertTrue(contenedor.getListaDeDanhoRecibidoPorBichoRetador().isEmpty());
    }
}
