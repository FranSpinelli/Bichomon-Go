package ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnEdad;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.service.bicho.impl.BichoServiceImpl;

import org.junit.Before;

import ar.edu.unq.epers.bichomon.backend.dao.bicho.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.bicho.impl.HibernateBichoDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

public class HibernateBichoDAOTest {

	private BichoDAO bichoDAO;
	private BichoServiceImpl service;
	private Bicho bichomon;
	private Entrenador entrenador;
	private Especie picachu;
	private Especie raychu;
	private CondicionDeEvolucion condicion;
	
	@Before
	public void setUp() {
		
		bichoDAO = new HibernateBichoDAO();
		service = new BichoServiceImpl(bichoDAO);
		
		condicion = new CondicionBasadaEnEdad(2);
		ArrayList<CondicionDeEvolucion> condiciones = new ArrayList<CondicionDeEvolucion>();
		condiciones.add(condicion);
		
		picachu = new Especie("Picachu", ELECTRICIDAD);
		raychu = new Especie("RayChu", ELECTRICIDAD);
		picachu.setEspecieAEvolucionar(raychu, condiciones);
		bichomon = new Bicho(picachu);
		entrenador = new Entrenador("Ash");
		entrenador.capturarBicho(bichomon);
		
	}
	
	public void unBichoPuedeEvolucionar() {
		assertTrue(service.puedeEvolucionar(entrenador.getNombre(), picachu.getId()));
		
	}
	
	public void unBichoNoPuedeEvolucionar() {
		assertTrue(service.puedeEvolucionar(entrenador.getNombre(), picachu.getId()));
		
	}
	
	public void unBichoEvoluciona() {
		service.evolucionar(entrenador.getNombre(), picachu.getId());
	}
}