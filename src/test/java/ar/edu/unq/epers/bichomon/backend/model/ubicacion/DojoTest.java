package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;

public class DojoTest {

    DueloHelper dueloHelperMock;
    Dojo dojo;
    Bicho bichoMock;

    @Before
    public void setUp(){

        dojo = new Dojo("dojo");
        dueloHelperMock = Mockito.mock(DueloHelper.class);
        bichoMock = Mockito.mock(Bicho.class);
    }

    @Test
    public void getCampeonActual() {
        assertNull(dojo.getCampeonActual());
    }

    @Test
    public void setCampeonActual() {
        //CASO1= el dojo no tenia ningun campeon
        assertEquals(dojo.getListaDeCampeones().size(), 0);

        dojo.setCampeonActual(bichoMock);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 1);
        assertEquals(dojo.getListaDeCampeones().get(dojo.getListaDeCampeones().size()-1).getBicho(), bichoMock);
    }

    @Test
    public void setCampeonActualCaso2() {
        //CASO2= el dojo no tenia ningun campeon

        assertEquals(dojo.getListaDeCampeones().size(), 0);

        dojo.setCampeonActual(bichoMock);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 1);
        assertEquals(dojo.getListaDeCampeones().get(dojo.getListaDeCampeones().size()-1).getBicho(), bichoMock);

        Bicho bichoMock2 = Mockito.mock(Bicho.class);
        dojo.setCampeonActual(bichoMock2);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock2);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 2);
        assertEquals(dojo.getListaDeCampeones().get(0).getFechaDeFin(), LocalDate.now());
    }

    @Test
    public void realizarDuelo() {

        ResultadoCombate datosMock = Mockito.mock(ResultadoCombate.class);

        Mockito.doReturn(datosMock).when(dueloHelperMock).calcularDuelo(bichoMock,dojo);
        dojo.realizarDuelo(bichoMock,dueloHelperMock);
        verify(dueloHelperMock).calcularDuelo(bichoMock,dojo);
    }

}
