package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class EntrenadorTest {

    Estrategia estrategiaMock;
    Bicho bichoMock;
    Entrenador entrenador;
    Ubicacion ubicacion;

    @Before
    public void setUp(){

        estrategiaMock = Mockito.mock(Estrategia.class);
        ubicacion = Mockito.mock(Ubicacion.class);

        entrenador = new Entrenador("pepe");
        entrenador.setUbicacionActual(ubicacion);

        bichoMock = Mockito.mock(Bicho.class);
    }

    @Test
    public void addXp() {

        assertEquals(entrenador.getXp(), 0);
        entrenador.addXp(10);
        assertEquals(entrenador.getXp(), 10);
    }

    @Test (expected = BichosInsuficientes.class)
    public void abandonar() {

        assertEquals(ubicacion, entrenador.getUbicacionActual());

        assertEquals(entrenador.getCantidadDeBichos(), 0, 0);
        entrenador.addBicho(bichoMock);
        assertEquals(entrenador.getCantidadDeBichos(), 1, 0);

        Mockito.doNothing().when(ubicacion).recibirBicho(bichoMock);
        Mockito.doNothing().when(bichoMock).agregarEx(entrenador);
        entrenador.abandonar(bichoMock);

        Mockito.verify(ubicacion).recibirBicho(bichoMock);
        Mockito.verify(bichoMock).agregarEx(entrenador);

        assertEquals(entrenador.getCantidadDeBichos(), 0, 0);
    }

    @Test
    public void getCantidadDeBichos() {

        assertEquals(entrenador.getCantidadDeBichos(),0, 0);
    }

    @Test
    public void tieneBicho() {

        assertFalse(entrenador.tieneBicho(bichoMock));
        entrenador.addBicho(bichoMock);
        assertTrue(entrenador.tieneBicho(bichoMock));
    }
}
