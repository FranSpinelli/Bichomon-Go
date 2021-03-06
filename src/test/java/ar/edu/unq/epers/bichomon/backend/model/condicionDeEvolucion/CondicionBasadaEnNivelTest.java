package ar.edu.unq.epers.bichomon.backend.model.condicionDeEvolucion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnNivel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CondicionBasadaEnNivelTest{
	
	private CondicionBasadaEnNivel condicion;
	private Bicho bicho;
	
	@Before
	public void setUp() {
		condicion = new CondicionBasadaEnNivel(4);
		bicho = mock(Bicho.class);
	}
	
	@Test
	public void recibeUnBichoQuePuedeEvolucionar() {
		when(bicho.getNivelDelEntrenador()).thenReturn(5);
		assertTrue(condicion.puedeEvolucionar(bicho));
		}
	
	@Test
	public void recibeUnBichoQueNoPuedeEvolucionar() {
		when(bicho.getNivelDelEntrenador()).thenReturn(0);
		assertFalse(condicion.puedeEvolucionar(bicho));
		}

}
