package ar.edu.unq.epers.bichomon.backend.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.unq.epers.bichomon.backend.dao.bicho.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.bicho.impl.HibernateBichoDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnEdad;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.service.bicho.BichoService;
import ar.edu.unq.epers.bichomon.backend.service.bicho.impl.BichoServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class BichoServiceTest {

	private BichoDAO dao;
	private BichoService service;
	private Especie picachu;
	private Especie raychu;
	private Bicho pacachu;
	private CondicionDeEvolucion condicion;
	private Entrenador ash;
	
	
	@Before
	public void setUp() {
		dao = new HibernateBichoDAO();
		service = new BichoServiceImpl(dao);
		picachu = new Especie("Picachu", ELECTRICIDAD);
		raychu = new Especie("Raychu", ELECTRICIDAD);
		condicion = new CondicionBasadaEnEdad(2);
		ArrayList<CondicionDeEvolucion> condiciones = new ArrayList<CondicionDeEvolucion>();
		condiciones.add(condicion);
		picachu.setEspecieAEvolucionar(raychu, condiciones);
		pacachu = new Bicho(picachu);
		ash = new Entrenador("ash");
		ash.capturarBicho(pacachu);		
	}

    @After
    public void cleanup() {
       SessionFactoryProvider.destroy();
    }

	@Test
	public void testUnBichoNoPuedeEvolucionar() {
		boolean rto = service.puedeEvolucionar(ash.getNombre(), pacachu.getId());
		assertFalse(rto);
	}

	@Test
	public void testUnBichoPuedeEvolucionar() {
		assertTrue(service.puedeEvolucionar(ash.getNombre(), pacachu.getId()));
	}
	
	@Test
	public void testUnBichoEvoluciona() {
		assertEquals(service.evolucionar(ash.getNombre(), pacachu.getId()).getEspecie(), raychu);
	}
	
}
