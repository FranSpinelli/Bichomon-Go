package ar.edu.unq.epers.bichomon.backend.model.condicionDeEvolucion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnVictorias;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CondicionBasadaEnVictoriasTest{
	
	private CondicionBasadaEnVictorias condicion;
	private Bicho bicho;
	
	@Before
	public void setUp() {
		condicion = new CondicionBasadaEnVictorias(4);
		bicho = mock(Bicho.class);
	}
	
	@Test
	public void recibeUnBichoQuePuedeEvolucionar() {
		when(bicho.getVictorias()).thenReturn(5);
		assertTrue(condicion.puedeEvolucionar(bicho));
		}
	
	@Test
	public void recibeUnBichoQueNoPuedeEvolucionar() {
		when(bicho.getVictorias()).thenReturn(0);
		assertFalse(condicion.puedeEvolucionar(bicho));
		}

}
