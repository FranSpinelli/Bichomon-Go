package ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class DueloHelperTest {

    Especie especieMock;
    Especie especieMock2;
    Bicho bicho1;
    Bicho bicho2;
    Dojo dojo;
    DueloHelper dueloHelperSpy;
    Entrenador entrenador1;
    Entrenador entrenador2;

    @Before
    public void setUp(){
        ///dueloHelper = new DueloHelper();
        dojo = new Dojo("dojo");

        especieMock = Mockito.mock(Especie.class);
        Mockito.when(especieMock.getEnergiaInicial()).thenReturn(10);

        especieMock2 = Mockito.mock(Especie.class);
        Mockito.when(especieMock2.getEnergiaInicial()).thenReturn(1000);

        entrenador1 = new Entrenador("pepe");
        entrenador2 = new Entrenador("roco");


        bicho1 = new Bicho(especieMock);
        bicho1.setEntrenador(entrenador1);

        bicho2 = new Bicho(especieMock2);
        bicho2.setEntrenador(entrenador2);

        //este dueloHelper es usado para testear los casos en el que hace falta setear el random (caso2,caso3,caso4)
        dueloHelperSpy = Mockito.spy(new DueloHelper());
    }

    @Test
    public void getRandom(){

        DueloHelper dueloHelper = new DueloHelper();

        Double azar1 = dueloHelperSpy.getRandomDouble(0.5,1.0);
        Double azar2 = dueloHelperSpy.getRandomDouble(0.5,1.0);
        Double azar3 = dueloHelperSpy.getRandomDouble(0.5,1.0);
        Double azar4 = dueloHelperSpy.getRandomDouble(0.5,1.0);

        //uso mas de un decimo, a la hora de hacer el assert, por que podia pasar que el random me de un 1.002... y lo tome como valido
        assertTrue(azar1 >= 0.5 && azar1 <= 1.000);
        assertTrue(azar2 >= 0.5 && azar1 <= 1.000);
        assertTrue(azar3 >= 0.5 && azar1 <= 1.000);
        assertTrue(azar4 >= 0.5 && azar1 <= 1.000);
    }

    @Test
    public void realizarDueloCaso1() {
        /*CASO1: el dojo no tiene ningun campeon actual, por lo tanto el retador automaticamente se convierte en actual campeon, recibe energia plus
                    y su entrenador, xp plus*/

        assertNull(dojo.getCampeonActual());

        assertEquals(entrenador1.getXp(), 0);
        assertEquals(bicho1.getEnergia(),10);

        Mockito.when(dueloHelperSpy.getRandomDouble(1.0, 5.0)).thenReturn(3.0);
        ResultadoCombate datos = dueloHelperSpy.calcularDuelo(bicho1,dojo);

        assertEquals(dojo.getCampeonActual().getBicho(), bicho1);
        assertEquals(datos.getGanadorDelDuelo(), bicho1);

        assertEquals(entrenador1.getXp(), 10);
        assertEquals(bicho1.getEnergia(), 13);
    }

    @Test
    public void realizarDueloCaso2(){
        /*CASO2: el dojo tiene un campeon actual y este le gana al retador*/

        assertNull(dojo.getCampeonActual());
        dojo.setCampeonActual(bicho2);
        assertEquals(dojo.getCampeonActual().getBicho(), bicho2);

        assertEquals(dojo.getCampeonActual().getBicho().getEnergia(), 1000);

        Mockito.when(dueloHelperSpy.getRandomDouble(0.5, 1.0)).thenReturn(1.0);
        Mockito.when(dueloHelperSpy.getRandomDouble(1.0, 5.0)).thenReturn(3.0);

        ResultadoCombate datos = dueloHelperSpy.calcularDuelo(bicho1,dojo);

        assertEquals(dojo.getCampeonActual().getBicho(),bicho2);

        assertEquals(bicho2.getEnergia(), 1003);
        assertEquals(bicho1.getEnergia(), 13);

        assertEquals(entrenador2.getXp(), 10);
        assertEquals(entrenador1.getXp(), 10);
    }

    @Test
    public void realizarDueloCaso3(){
        //CASO3: el dojo tiene un campeon pero el retador no llega a ganarle en los 10 turnos del duelo (los 5 que le correspondian a el)

        dojo.setCampeonActual(bicho2);
        assertEquals(dojo.getCampeonActual().getBicho(), bicho2);

        Bicho bicho3 = new Bicho(especieMock2);
        bicho3.setEntrenador(entrenador1);

        Mockito.when(dueloHelperSpy.getRandomDouble(0.5, 1.0)).thenReturn(0.01);
        Mockito.when(dueloHelperSpy.getRandomDouble(1.0, 5.0)).thenReturn(3.0);

        ResultadoCombate datos = dueloHelperSpy.calcularDuelo(bicho3,dojo);

        assertEquals(dojo.getCampeonActual().getBicho(),bicho2);
        assertEquals(datos.getGanadorDelDuelo(), bicho2);

        assertEquals(bicho2.getEnergia(), 1003);
        assertEquals(bicho3.getEnergia(), 1003);

        assertEquals(entrenador2.getXp(), 10);
        assertEquals(entrenador1.getXp(), 10);
    }

    @Test
    public void realizarDueloCaso4(){
        //CASO4: el dojo tiene un campeon y este pierde, el titulo pasa a manos del retador

        dojo.setCampeonActual(bicho1);
        assertEquals(dojo.getCampeonActual().getBicho(), bicho1);

        Mockito.when(dueloHelperSpy.getRandomDouble(0.5, 1.0)).thenReturn(1.0);
        Mockito.when(dueloHelperSpy.getRandomDouble(1.0, 5.0)).thenReturn(3.0);

        ResultadoCombate datos = dueloHelperSpy.calcularDuelo(bicho2,dojo);

        assertEquals(dojo.getCampeonActual().getBicho(),bicho2);

        assertEquals(bicho2.getEnergia(), 1003);
        assertEquals(bicho1.getEnergia(), 13);

        assertEquals(entrenador2.getXp(), 10);
        assertEquals(entrenador1.getXp(), 10);
    }
}
