
package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DojoTest extends UbicacionTest {

    DueloHelper dueloHelperMock;
    Dojo dojo;
    Bicho bichoMock;
    private Especie especie;

    @Override
    @Before
    public void crearEscenario(){

        dojo = new Dojo();
        this.especie = mock(Especie.class);
        dueloHelperMock = mock(DueloHelper.class);
        bichoMock = mock(Bicho.class);
        super.crearEscenario();
    }

    @Test
    public void testGetCampeonActual() {
        assertNull(dojo.getCampeonActual());
    }

    @Test
    public void testSetCampeonActual() {
        //CASO1= el dojo no tenia ningun campeon
        assertEquals(dojo.getListaDeCampeones().size(), 0);

        dojo.setCampeonActual(bichoMock);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 1);
        assertEquals(dojo.getListaDeCampeones().get(dojo.getListaDeCampeones().size()-1).getBicho(), bichoMock);
    }

    @Test
    public void testSetCampeonActualCaso2() {
        //CASO2= el dojo no tenia ningun campeon

        assertEquals(dojo.getListaDeCampeones().size(), 0);

        dojo.setCampeonActual(bichoMock);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 1);
        assertEquals(dojo.getListaDeCampeones().get(dojo.getListaDeCampeones().size()-1).getBicho(), bichoMock);

        Bicho bichoMock2 = mock(Bicho.class);
        dojo.setCampeonActual(bichoMock2);

        assertEquals(dojo.getCampeonActual().getBicho(), bichoMock2);
        assertEquals(dojo.getCampeonActual().getFechaDeInicio(), LocalDate.now());

        assertEquals(dojo.getListaDeCampeones().size(), 2);
        assertEquals(dojo.getListaDeCampeones().get(0).getFechaDeFin(), LocalDate.now());
    }

    @Override
    @Test
    public void testRealizarDuelo() {

        ResultadoCombate datosMock = mock(ResultadoCombate.class);

        Mockito.doReturn(datosMock).when(dueloHelperMock).calcularDuelo(bichoMock,dojo);
        dojo.realizarDuelo(bichoMock,dueloHelperMock);
        verify(dueloHelperMock).calcularDuelo(bichoMock,dojo);
    }

    @Override
    protected Ubicacion ubicacion() {
        return this.dojo;
    }

    @Override
    protected void definirGeneracionDeBicho() {
        when(this.bichoMock.getEspecie()).thenReturn(this.especie);
        when(this.especie.getEvolucionRaiz()).thenReturn(this.especieEncontrada);
        this.dojo.setCampeonActual(this.bichoMock);
    }
}
