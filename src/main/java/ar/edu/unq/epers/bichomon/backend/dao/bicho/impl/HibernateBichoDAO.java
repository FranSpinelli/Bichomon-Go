package ar.edu.unq.epers.bichomon.backend.dao.bicho.impl;

import org.hibernate.Session;

import ar.edu.unq.epers.bichomon.backend.dao.bicho.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.service.runner.Runner;

public class HibernateBichoDAO implements BichoDAO{

	@Override
	public Bicho recuperar(int id) {
		Session session = Runner.getCurrentSession();
		return session.get(Bicho.class, id);
	}

	@Override
	public void guardarBicho(Bicho bicho){
        Session session = Runner.getCurrentSession();
        session.save(bicho);
		
	}

	@Override
	public Bicho updatePorEvolucion(int idDeBicho) {
		Bicho bicho;
		Session session = Runner.getCurrentSession();
		bicho = session.get(Bicho.class, idDeBicho);
		bicho.evolucionar();
		session.update(bicho);
		return bicho;
	}
}
