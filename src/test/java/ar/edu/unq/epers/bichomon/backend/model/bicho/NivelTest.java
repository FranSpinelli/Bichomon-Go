package ar.edu.unq.epers.bichomon.backend.model.bicho;

import com.mysql.cj.exceptions.FeatureNotAvailableException;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class NivelTest {

    public Nivel nivel0;
    public Nivel nivel1;
    public UltimoNivel lastLevel;
    public Entrenador entrenador;

    @Before
    public void setUp(){
        lastLevel = new UltimoNivel(2,20, 3);
        nivel0 = new Nivel(0, 1, 1);
        nivel1 = new Nivel(1,3, lastLevel, 1);
        entrenador = mock(Entrenador.class);
    }
    @Test(expected = UndefinedNivelError.class)
    public void unNivelPuedeSerCreadoSinUnSiguienteNivelYEsteFallaSoloCuandoSeLePideEval(){
        assertEquals(1, nivel0.getNroBichos());
        assertEquals(0, nivel0.getNivel());
        assertEquals(1, nivel0.getLimite());
        nivel0.eval(1);

    }
    @Test
    public void unNivelSeLePuedeSetearSuNivelDespuesDeSuCreacion(){
        nivel0.setSiguiente(nivel1);
        assertEquals(1, nivel0.getNroBichos());
        assertEquals(0, nivel0.getNivel());
        assertEquals(1, nivel0.getLimite());
        assertEquals(nivel0, nivel0.eval(1));
        assertEquals(nivel1, nivel0.eval(3));

    }
    @Test
    public void losNivelesTienenUnNumeroMaximoDeBichos(){
        assertEquals(1, nivel1.getNroBichos());
        assertEquals(3, lastLevel.getNroBichos());
    }
    @Test
    public void dadaUnaCantidadDeExperienciaLosNivelesEvaluanSiComillasSubisDeNivelComillas(){
        assertEquals(nivel1, nivel1.eval(0));
        assertEquals(lastLevel, nivel1.eval(100));
    }
    @Test
    public void evalDeUnUltimoNivelSiempreRetornaASiMismo(){
        assertEquals(lastLevel, lastLevel.eval(0));
        assertEquals(lastLevel, lastLevel.eval(100));
    }
}
