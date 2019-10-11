package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BichoTest {

    public Especie especie;
    public Especie otraEspecie;
    public Entrenador entrenador;
    public Bicho bicho;
	
    @Before
	public void setUp() {
    	especie = mock(Especie.class);
    	otraEspecie = mock(Especie.class);
    	bicho= new Bicho(especie);
    	entrenador = mock(Entrenador.class);
    }
    @Test
    public void unBichoMandaElMensajeEvolucionarASuEspecie(){
        bicho.evolucionar();
        verify(especie).evolucionar(bicho);
        
    }
    
    @Test
    public void unBichoCambiaDeUnaEspecieAOtra(){
        bicho.setEspecie(otraEspecie);
        assertEquals(otraEspecie, bicho.getEspecie());
        
    }
    
    @Test
    public void unBichoConoceASuEntrenador() {
    	bicho.setEntrenador(entrenador);
    	assertEquals(entrenador, bicho.getEntrenador());
    }
    
    @Test
    public void unBichoTePuedeDevolverElNivelDeSuEntrenador(){
    	bicho.setEntrenador(entrenador);
    	when(entrenador.getNivel()).thenReturn(5);
    	assertEquals(bicho.getNivelDelEntrenador(), 5);
    }

    @Test
    public void agregarExTest(){

        assertEquals(bicho.getExDuenhos().size(),0);
        bicho.agregarEx(entrenador);
        assertEquals(bicho.getExDuenhos().size(),1);
    }

    @Test
    public void getEdad(){
        bicho.setFechaDeNacimiento(LocalDate.of(2019,9,4));

        assertEquals(30,bicho.getEdadConRespectoAlDia(LocalDate.of(2019,10,4)));
    }
}
