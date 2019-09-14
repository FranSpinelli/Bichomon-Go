package ar.edu.unq.epers.bichomon.backend.model.especie;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import org.junit.Before;
import org.junit.Test;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EspecieTest {

    public Especie especie;
    public Especie otraEspecie;
    public Bicho bicho;
    public CondicionDeEvolucion condicion;
	
    @Before
	public void setUp() {
    	condicion = mock(CondicionDeEvolucion.class);
    	especie = new Especie(1,"nombreEspecie", AIRE);
    	otraEspecie = mock (Especie.class);
    	bicho= mock(Bicho.class);
    	when(condicion.puedeEvolucionar(bicho)).thenReturn(true);
    }
    
    @Test
    public void test() {
    	especie.evolucionar(bicho);
    	verify(bicho).setEspecie(otraEspecie);
    }
	
}
