package ar.edu.unq.epers.bichomon.backend.dao.entrenador.impl;

import org.hibernate.Session;

import ar.edu.unq.epers.bichomon.backend.dao.entrenador.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.service.runner.Runner;

public class HibernateEntrenadorDAO implements EntrenadorDAO {


	@Override
	public Entrenador recuperar(int id) {
		Session session = Runner.getCurrentSession();
		return session.get(Entrenador.class, id);
	}

	@Override
	public void guardarEntrenador(Entrenador entrenador){
        Session session = Runner.getCurrentSession();
        session.save(entrenador);
	}

}
