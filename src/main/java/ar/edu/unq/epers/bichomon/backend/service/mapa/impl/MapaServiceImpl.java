package ar.edu.unq.epers.bichomon.backend.service.mapa.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoSinCampeon;
import ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j.Neo4jMapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.MonedasInsuficientesException;
import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.mapa.MapaService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionActualException;
import ar.edu.unq.epers.bichomon.backend.service.mapa.UbicacionInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.Neo4jTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.TransactionManager;

import java.util.List;
import java.util.function.Supplier;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.HIBERNATE;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.NEO4J;


public class MapaServiceImpl implements MapaService {
	private Neo4jMapaDAO neo4jMapaDAO;
	private EntrenadorDAO entrenadorDAO;
	private UbicacionDAO ubicacionDAO;
	private Transaction hibernateTransaction = new HibernateTransaction();
	private Transaction neo4jTransaction = new Neo4jTransaction();
	private TransactionManager transactionManager;

	public MapaServiceImpl(EntrenadorDAO entrenadorDAO, UbicacionDAO ubicacionDAO, Neo4jMapaDAO neo4jMapaDAO) {
		this.entrenadorDAO = entrenadorDAO;
		this.ubicacionDAO = ubicacionDAO;
		this.neo4jMapaDAO = neo4jMapaDAO;
		this.transactionManager = new TransactionManager().addPossibleTransaction(this.hibernateTransaction).addPossibleTransaction(this.neo4jTransaction);
	}

	@Override
	public void mover(String entrenador, String ubicacion) {
		run(() -> {
			Entrenador entrenadorActual = this.getEntrenador(entrenador);
			Ubicacion ubicacionActual = this.getUbicacion(ubicacion);
			this.moverse(entrenadorActual, ubicacionActual ,() -> this.neo4jMapaDAO.costoCaminoMasBarato(entrenadorActual.getUbicacionActual(), ubicacionActual));
		}, this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
	}

	@Override
	public void moverMasCorto(String entrenador, String ubicacion) {
		run(() -> {
			Entrenador entrenadorActual = this.getEntrenador(entrenador);
			Ubicacion ubicacionActual = this.getUbicacion(ubicacion);
			this.moverse(entrenadorActual, ubicacionActual ,() -> this.neo4jMapaDAO.costoCaminoMasCorto(entrenadorActual.getUbicacionActual().getNombre(), ubicacion));
		}, this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
	}

	@Override
	public List<Ubicacion> conectados(String ubicacion, String tipoCamino) {
		return run(() -> {
					this.getUbicacion(ubicacion);
					return this.ubicacionDAO.recuperarTodos(this.neo4jMapaDAO.conectados(ubicacion,tipoCamino));
				}
		, this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
	}

	@Override
	public void crearUbicacion(Ubicacion ubicacion) {
		run(() -> {
			try{
				this.ubicacionDAO.guardar(ubicacion);
				this.neo4jMapaDAO.create(ubicacion);
			}catch(RuntimeException e){
				throw new CreacionException("No se pudo crear la ubicacion");
			}

		}, this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
	}

	@Override
	public void conectar(String ubicacion1, String ubicacion2, TipoCamino tipoCamino) {
		run(() -> {
			Ubicacion ubicacionOrigen= this.getUbicacion(ubicacion1);
			Ubicacion ubicacionDestino= this.getUbicacion(ubicacion2);

			this.neo4jMapaDAO.conectar(ubicacionOrigen, ubicacionDestino, tipoCamino);
		}, this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
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

	private List<Ubicacion> getUbicaciones(List<String> nombreUbicaciones){

		return this.ubicacionDAO.recuperarTodos(nombreUbicaciones);
	}

	private Integer getCosto(Supplier<Integer> calcularCosto) {
//		Integer costo = this.neo4jMapaDAO.costoCaminoMasBarato(origen, destino);
		Integer costo = calcularCosto.get();
		if(costo == null){
			throw new UbicacionMuyLejana("No hay un camino hacia la ubicacion de destino");
		}
		return costo;
	}

	private void moverse(Entrenador entrenador, Ubicacion ubicacion, Supplier<Integer> calcularCosto){
			if(entrenador.estaEn(ubicacion)){
				throw new UbicacionActualException("El entrenador no se puede mover a la ubicacion pues ya se encuentra alli");
			}
			Integer costo = this.getCosto(calcularCosto);
			try{
				entrenador.mover(ubicacion, costo);
			}catch(MonedasInsuficientesException e){
				throw new CaminoMuyCostoso("El entrenador no puede pagar el costo del camino");
			}
	}

}
