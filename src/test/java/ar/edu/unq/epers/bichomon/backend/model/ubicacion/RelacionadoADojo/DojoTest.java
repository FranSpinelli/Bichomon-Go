package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;

public class DojoTest {

    DueloHelper dueloHelperMock;
    Dojo dojo;
    Bicho bichoMock;

    @Before
    public void setUp(){

        dueloHelperMock = Mockito.mock(DueloHelper.class);
        dojo = new Dojo(dueloHelperMock);
        bichoMock = Mockito.mock(Bicho.class);
    }

    @Test
    public void getCampeonActual() {
        assertEquals(dojo.getCampeonActual(), null);
    }

    @Test
    public void setCampeonActual() {
        dojo.setCampeonActual(bichoMock);

        assertEquals(dojo.getCampeonActual(), bichoMock);
    }

    @Test
    public void realizarDuelo() {

        ContenedorConDatosDelDuelo datosMock = Mockito.mock(ContenedorConDatosDelDuelo.class);

        Mockito.doReturn(datosMock).when(dueloHelperMock).realizarDuelo(bichoMock,dojo);
        dojo.realizarDuelo(bichoMock);
        verify(dueloHelperMock).realizarDuelo(bichoMock,dojo);
    }
}
