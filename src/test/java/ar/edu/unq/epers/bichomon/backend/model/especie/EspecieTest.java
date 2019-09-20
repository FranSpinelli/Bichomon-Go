package ar.edu.unq.epers.bichomon.backend.model.especie;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import org.junit.Before;
import org.junit.Test;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import static org.mockito.Mockito.verifyZeroInteractions;

public class EspecieTest {

    public Especie especie;
    public Especie otraEspecie;
    public Bicho bicho;
    public ArrayList<CondicionDeEvolucion> condicion;
    public CondicionDeEvolucion condicion1;
	
    @Before
	public void setUp() {
    	condicion = new ArrayList<CondicionDeEvolucion>();
    	condicion1 = mock(CondicionDeEvolucion.class);
    	condicion.add(condicion1);
    	especie = new Especie("nombreEspecie", AIRE);
    	otraEspecie = new Especie("nombreOtraEspecie", AIRE);
    	otraEspecie = mock (Especie.class);
    	bicho= mock(Bicho.class);
    }
    
    @Test
    public void unaEspeciePuedeEvolucionarAOtra() {
    	especie.setEspecieAEvolucionar(otraEspecie,condicion);
    	when(condicion1.puedeEvolucionar(bicho)).thenReturn(true);
    	especie.evolucionar(bicho);
    	verify(bicho).setEspecie(otraEspecie);
    }
    
    @Test
    public void unaEspecieNoPuedeEvolucionarAOtraPorNoTenerEvolucion() {
    	especie.evolucionar(bicho);
    	verifyZeroInteractions(bicho);
    }
    
    @Test
    public void unaEspecieNoPuedeEvolucionarAOtraPorCondicion() {
    	especie.setEspecieAEvolucionar(otraEspecie,condicion);
    	when(condicion1.puedeEvolucionar(bicho)).thenReturn(false);
    	especie.evolucionar(bicho);
    	verifyZeroInteractions(bicho);
    }
	
    
    @Test
    public void unaEspecieNoPuedeEvolucionarAOtraPorNoTenerEvolucionNiCondicion() {
    	especie.evolucionar(bicho);
    	verifyZeroInteractions(bicho);
    }
	
}
