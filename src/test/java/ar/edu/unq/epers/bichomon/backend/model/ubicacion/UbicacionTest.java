package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class UbicacionTest {
    private Bicho bicho;
    protected Entrenador entrenador;
    protected Especie especieEncontrada1;
    protected Especie especieEncontrada2;
    private Estrategia estrategia;
    private BusquedaHelper busquedaHelper;

    @Before
    public void crearEscenario(){
        this.bicho = mock(Bicho.class);
        this.entrenador = mock(Entrenador.class);
        this.especieEncontrada1 = mock(Especie.class);
        this.especieEncontrada2 = mock(Especie.class);
        this.estrategia = mock(Estrategia.class);
        this.busquedaHelper = mock(BusquedaHelper.class);
        when(this.busquedaHelper.factorNivel(this.entrenador)).thenReturn(true);
        when(this.busquedaHelper.factorPoblacion(this.ubicacion())).thenReturn(true);
        when(this.busquedaHelper.factorTiempo(this.entrenador)).thenReturn(true);
        when(this.busquedaHelper.factorRandom()).thenReturn(true);
        this.ubicacion().setBusquedaHelper(this.busquedaHelper);
    }

    protected abstract void definirGeneracionDeBicho();

    @Test(expected = UbicacionIncorrectaException.class)
    public void testRecibirBicho(){
        this.ubicacion().recibirBicho(bicho);
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void testRealizarDuelo(){
        this.ubicacion().realizarDuelo(bicho, estrategia);
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarNoTieneExito(){
        when(this.busquedaHelper.factorRandom()).thenReturn(false);
        this.ubicacion().buscar(this.entrenador);
    }

    @Test
    public void testBuscar(){
        this.definirGeneracionDeBicho();
        Bicho bichoEncontrado = this.ubicacion().buscar(this.entrenador);
        this.assertAlgunaEspecieEncontrada(this.posiblesEspeciesEncontradas(), bichoEncontrado);
    }

    protected List<Especie> posiblesEspeciesEncontradas(){
        List<Especie> especies = new ArrayList<>();
        especies.add(this.especieEncontrada1);
        return especies;
    }

    public void assertAlgunaEspecieEncontrada(List<Especie> posiblesEspeciesEncontradas, Bicho bichoEncontrado){
        assertTrue(posiblesEspeciesEncontradas.contains(bichoEncontrado.getEspecie()));
    }
    
    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarTieneExitoPeroNoPuedeGenerarElBicho(){
        this.definirGeneracionDeBichoImposibilitada();
        this.ubicacion().buscar(this.entrenador);
    }

    protected void definirGeneracionDeBichoImposibilitada(){}

    protected abstract Ubicacion ubicacion();
}
