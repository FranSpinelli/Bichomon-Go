package ar.edu.unq.epers.bichomon.backend.dao.ubicacion.impl;

import org.hibernate.Session;

import ar.edu.unq.epers.bichomon.backend.dao.ubicacion.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.runner.Runner;

public class HibernateUbicacionDAO implements UbicacionDAO {
	
	@Override
	public Ubicacion recuperar(int id) {
		Session session = Runner.getCurrentSession();
		return session.get(Ubicacion.class, id);
	}

	@Override
	public void guardarUbicacion(Ubicacion ubicacion){
        Session session = Runner.getCurrentSession();
        session.save(ubicacion);
		
	}

}
