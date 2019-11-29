package ar.edu.unq.epers.bichomon.backend.service.feed;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EventoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.NivelDAO;
import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateNivelDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateUbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.mongoDB.MongoDBEventoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j.Neo4jMapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.camino.Terrestre;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.feed.impl.FeedServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.mapa.MapaService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.MapaServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.Neo4jTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.TransactionManager;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.HIBERNATE;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.NEO4J;
import static org.junit.Assert.assertEquals;

public class FeedServiceTest {

    private EntrenadorDAO entrenadorDAO;
    private UbicacionDAO ubicacionDAO;
    private Neo4jMapaDAO neo4jMapaDAO;
    private NivelDAO nivelDAO;
    private EventoDAO eventoDAO;

    private AbstractNivel nivel;

    private Entrenador ash;

    private Pueblo puebloA, puebloB;
    private Guarderia guarderia;


    private FeedService feedService;
    private MapaService mapaService;

    private Transaction hibernateTransaction = new HibernateTransaction();
    private TransactionManager transactionManager = new TransactionManager().addPossibleTransaction(this.hibernateTransaction).addPossibleTransaction(new Neo4jTransaction());

    @Before
    public void crearModelo() {
        run(() -> {
            this.crearDAOs();
            this.crearService();
            this.crearUbicaciones();
            this.crearEntrenadores();
            this.crearEspecies();
            this.crearBichos();
        }, HIBERNATE, NEO4J);
    }

    @Test
    public void testFeedEntrenadorYFeedEntrenadorVacio(){
        assertEquals(0, feedService.feedEntrenador(ash.getNombre()));
        assertEquals(0, feedService.feedEntrenador(puebloA.getNombre()));
    }

    @Test
    public void testEventoDeArriboAPuebloB(){
        mapaService.mover(ash.getNombre(), puebloB.getNombre());
        assertEquals(1, feedService.feedEntrenador(ash.getNombre()));
    }

    /*    Create the model functions    */
    private void crearUbicaciones() {
        this.puebloA = new Pueblo("PuebloA");
        this.puebloB = new Pueblo("PuebloB");
        this.persistirUbicaciones(this.listaDeUbicaciones());
        this.conectarUbicaciones();
    }

    private void crearService() {
        this.feedService = new FeedServiceImpl(entrenadorDAO, neo4jMapaDAO, eventoDAO);
        this.mapaService = new MapaServiceImpl(entrenadorDAO, ubicacionDAO, neo4jMapaDAO, eventoDAO);
    }

    private void crearDAOs() {
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.neo4jMapaDAO = new Neo4jMapaDAO();
        this.nivelDAO = new HibernateNivelDAO();
        this.eventoDAO = new MongoDBEventoDAO();
    }

    private void conectarUbicaciones(){
        this.mapaService.conectar(this.puebloA.getNombre(), this.puebloB.getNombre(), new Terrestre());
        this.mapaService.conectar(this.puebloB.getNombre(), this.puebloA.getNombre(), new Terrestre());

    }

    private void crearEspecies(){}

    private void crearBichos(){}

    private void crearEntrenadores() {
        this.nivel      = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.ash        = new Entrenador("Ash", nivel);
        this.ash.setUbicacionActual(this.puebloA);
        this.ash.addMonedas(20);
        this.entrenadorDAO.guardar(ash);
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        ubicaciones.add(this.puebloA);
        ubicaciones.add(this.puebloB);
        return ubicaciones;
    }

    private void persistirUbicaciones(List<Ubicacion> ubicaciones){
        for(Ubicacion ubicacion : ubicaciones){
            this.mapaService.crearUbicacion(ubicacion);
        }
    }
}
