package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoAjeno;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class EntrenadorTest {

    Estrategia estrategiaMock;
    Bicho bichoMock1;
    Bicho bichoMock2;
    Entrenador entrenador;
    Ubicacion ubicacion1;
    AbstractNivel nivel;
    private Dojo dojo;

    @Before
    public void setUp(){
        nivel = Mockito.mock(AbstractNivel.class);

        estrategiaMock = Mockito.mock(Estrategia.class);
        ubicacion1 = Mockito.mock(Ubicacion.class);
        dojo = Mockito.spy(Dojo.class);

        entrenador = new Entrenador("pepe", nivel);
        entrenador.setUbicacionActual(ubicacion1);

        bichoMock1 = Mockito.mock(Bicho.class);
        bichoMock2 = Mockito.mock(Bicho.class);
    }

    @Test
    public void testAddXp() {
        when(nivel.eval(10)).thenReturn(nivel);
        assertEquals(0, entrenador.getXp());
        entrenador.addXp(10);
        assertEquals(10, entrenador.getXp());
    }
    @Test
    public void testAddXpYNoSubeDeNivel() {
        when(nivel.eval(10)).thenReturn(nivel);
        when(nivel.getNivel()).thenReturn(1);
        assertEquals(0, entrenador.getXp());
        entrenador.addXp(10);
        assertEquals(1, entrenador.getNivel());
    }

    @Test
    public void testAddXpYSubeDeNivel() {
        AbstractNivel nivel2 = Mockito.mock(AbstractNivel.class);
        when(nivel.eval(100)).thenReturn(nivel2);
        when(nivel2.getNivel()).thenReturn(2);
        assertEquals(0, entrenador.getXp());
        entrenador.addXp(100);
        assertEquals(2, entrenador.getNivel());
    }

    @Test
    public void testAbandonar(){

        assertEquals(ubicacion1, entrenador.getUbicacionActual());

        assertEquals(0, entrenador.getCantidadDeBichos(), 0);
        entrenador.addBicho(bichoMock1);
        entrenador.addBicho(bichoMock2);
        assertEquals(2, entrenador.getCantidadDeBichos(), 0);

        Mockito.doNothing().when(ubicacion1).recibirBicho(bichoMock1);
        Mockito.doNothing().when(bichoMock1).agregarEx(entrenador);
        entrenador.abandonar(bichoMock1);

        Mockito.verify(ubicacion1).recibirBicho(bichoMock1);
        Mockito.verify(bichoMock1).agregarEx(entrenador);

        assertEquals(1, entrenador.getCantidadDeBichos(), 0);
    }

    @Test(expected = BichoAjeno.class)
    public void testAbandonarBichoAjeno(){
        entrenador.abandonar(bichoMock1);
    }

    @Test (expected = BichosInsuficientes.class)
    public void testAbandonarElUnicoBichoQueTiene() {

        assertEquals(ubicacion1, entrenador.getUbicacionActual());

        assertEquals(0, entrenador.getCantidadDeBichos(), 0);
        entrenador.addBicho(bichoMock1);
        assertEquals(1, entrenador.getCantidadDeBichos(), 0);

        Mockito.doNothing().when(ubicacion1).recibirBicho(bichoMock1);
        Mockito.doNothing().when(bichoMock1).agregarEx(entrenador);
        entrenador.abandonar(bichoMock1);
    }

    @Test
    public void testGetCantidadDeBichos() {

        assertEquals(0, entrenador.getCantidadDeBichos(), 0);
    }

    @Test
    public void testTieneBicho() {

        assertFalse(entrenador.tieneBicho(bichoMock1));
        entrenador.addBicho(bichoMock1);
        assertTrue(entrenador.tieneBicho(bichoMock1));
    }

    @Test(expected = BichoAjeno.class)
    public void testDesafiarCampeonActualConBichoAjeno(){
        this.entrenador.desafiarCampeonActualCon(this.bichoMock1);
    }

    @Test
    public void testhacerEvolucionar() {
        this.entrenador.addBicho(this.bichoMock1);
        when(this.bichoMock1.evolucionar()).thenReturn(this.bichoMock2);
        assertEquals(this.bichoMock2, this.entrenador.hacerEvolucionar(this.bichoMock1));
    }

    @Test(expected = BichoAjeno.class)
    public void testhacerEvolucionarBichoAjeno() {
        this.entrenador.hacerEvolucionar(this.bichoMock1);
    }
}
