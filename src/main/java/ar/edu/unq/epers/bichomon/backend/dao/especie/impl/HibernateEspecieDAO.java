package ar.edu.unq.epers.bichomon.backend.dao.especie.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.query.Query;

import ar.edu.unq.epers.bichomon.backend.dao.especie.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.runner.Runner;

public class HibernateEspecieDAO implements EspecieDAO {

	@Override
	public void guardar(Especie especie) {
        Session session = Runner.getCurrentSession();
        session.save(especie);

	}

	@Override
	public void actualizar(Especie especie) {
		Session session = Runner.getCurrentSession();
		session.update(especie);

	}

	@Override
	public Especie recuperar(String nombreEspecie) {
		Session session = Runner.getCurrentSession();
		return session.get(Especie.class, nombreEspecie);
	}

	@Override
	public List<Especie> recuperarTodos() {
	Session session = Runner.getCurrentSession();
		
		String hql = "from Especie especie "
				+ "order by especie.nombre asc";
		
		Query<Especie> query = session.createQuery(hql,  Especie.class);
		return query.getResultList();
	}

	@Override
	public void eliminarTodos() {
		// TODO Auto-generated method stub

	}

}
