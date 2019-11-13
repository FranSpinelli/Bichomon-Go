package ar.edu.unq.epers.bichomon.backend.service.mapa.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoSinCampeon;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.mapa.MapaService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionActualException;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class MapaServiceImpl implements MapaService {
	private EntrenadorDAO entrenadorDAO;
	private UbicacionDAO ubicacionDAO;
	private Transaction hibernateTransaction = new HibernateTransaction();

	public MapaServiceImpl(EntrenadorDAO entrenadorDAO, UbicacionDAO ubicacionDAO) {
		this.entrenadorDAO = entrenadorDAO;
		this.ubicacionDAO = ubicacionDAO;
	}

	@Override
	public void mover(String entrenador, String ubicacion) {
		run(() -> {
			Entrenador entrenadorActual = this.getEntrenador(entrenador);
			Ubicacion ubicacionActual = this.getUbicacion(ubicacion);
			if(entrenadorActual.estaEn(ubicacionActual)){
				throw new UbicacionActualException("El entrenador no se puede mover a la ubicacion pues ya se encuentra alli");
			}
			entrenadorActual.setUbicacionActual(ubicacionActual);
		}, this.hibernateTransaction);
	}

	@Override
	public int cantidadEntrenadores(String ubicacion) {
		return run(() -> this.ubicacionDAO.cantidadEntrenadoresEn(ubicacion), this.hibernateTransaction);
	}

	@Override
	public Bicho campeon(String nombreDelDojo) {
		Ubicacion dojoRecuperado = run(() -> this.ubicacionDAO.recuperar(nombreDelDojo), this.hibernateTransaction);
		if(dojoRecuperado == null){throw new UbicacionInexistente("La ubicacion no existe");}

		Campeon campeonActual = dojoRecuperado.getCampeonActual();
		if(campeonActual == null){throw new DojoSinCampeon("El dojo no tiene campeon actualmente");}

		return campeonActual.getBicho();
	}

	@Override
	public Bicho campeonHistorico(String dojo) {
		return run(() -> this.ubicacionDAO.getCampeonHistorico(dojo), this.hibernateTransaction);
	}

	//PRIVATE FUNCTIONS---------------------------------------------------------------------------------------------------------------------
	private Entrenador getEntrenador(String nombreDeEntrenador){
		Entrenador entrenador = this.entrenadorDAO.recuperar(nombreDeEntrenador);
		if(entrenador == null){
			throw new EntrenadorInexistente(nombreDeEntrenador);
		}
		return entrenador;
	}

	private Ubicacion getUbicacion(String nombreDeUbicacion){
		Ubicacion ubicacion = this.ubicacionDAO.recuperar(nombreDeUbicacion);
		if(ubicacion == null){
			throw new UbicacionInexistente("La ubicacion no existe");
		}
		return ubicacion;
	}

}
