package ar.edu.unq.epers.bichomon.backend.service.feed;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.mongoDB.MongoDBEventoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j.Neo4jMapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.camino.Terrestre;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.evento.*;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.*;
import ar.edu.unq.epers.bichomon.backend.service.bicho.BichoServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.feed.impl.FeedServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.mapa.MapaService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.MapaServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.Neo4jTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    private BichoServiceImpl bichoService;

    private Transaction hibernateTransaction = new HibernateTransaction();
    private TransactionManager transactionManager = new TransactionManager().addPossibleTransaction(this.hibernateTransaction).addPossibleTransaction(new Neo4jTransaction());
    private BichoDAO bichoDAO;
    private Dojo dojo;
    private Bicho bichoPicachu;
    private Especie picachu;
    private EspecieDAO especieDAO;
    private Dojo dojo1;
    private Bicho bichoCharizard;
    private Bicho bichoSquirtle;
    private Especie charizard;
    private Especie pichu;
    private Especie squirtle;
    private Entrenador brook;
    private Entrenador misty;
    private Bicho bichoPichu;
    private BusquedaHelperMock busquedaHelper;
    private HibernateBusquedaHelperDAO busquedaHelperDAO;
    private Especie charmilion;
    private Bicho bichoCharmilion;

    @Before
    public void crearModelo() {
        run(() -> {
            this.crearDAOs();
            this.crearHelpers();
            this.crearService();
            this.crearEspecies();
            this.crearBichos();
            this.crearUbicaciones();
            this.crearEntrenadores();
        }, HIBERNATE, NEO4J);
    }

    @After
    public void limpiarModelo(){
        run(() -> {
                    SessionFactoryProvider.destroy();
                    this.neo4jMapaDAO.deleteAll();
                    this.eventoDAO.deleteAll();
                }
                , HIBERNATE, NEO4J);
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testFeedEntrenadorElEntrenadorNoExiste(){
        this.feedService.feedEntrenador("Ashe");
    }

    @Test
    public void testFeedEntrenador() {
        List<Evento> resultados = feedService.feedEntrenador(ash.getNombre());
        assertEquals(new ArrayList<>(), feedService.feedEntrenador(ash.getNombre()));

        mapaService.mover(ash.getNombre(), puebloB.getNombre());
        assertEquals(1, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), puebloB, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        mapaService.moverMasCorto(ash.getNombre(), puebloA.getNombre());
        System.out.println(this.ash.getUbicacionActual().getNombre());
        assertEquals(2, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), puebloA, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        bichoService.buscar(this.ash.getNombre());
        assertEquals(3, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertCaptura(this.ash.getNombre(), this.picachu, puebloA, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        mapaService.mover(ash.getNombre(), guarderia.getNombre());
        assertEquals(4, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), guarderia, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        bichoService.abandonar(this.ash.getNombre(), this.bichoCharmilion.getId());
        assertEquals(5, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertAbandono(this.ash.getNombre(), this.bichoCharmilion, guarderia, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        mapaService.mover(ash.getNombre(), dojo.getNombre());
        assertEquals(6, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), dojo, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        bichoService.duelo(this.ash.getNombre(), this.bichoPicachu.getId());
        assertEquals(7, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertCoronacion(this.ash.getNombre(), this.bichoPicachu, this.bichoPichu, dojo, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        mapaService.mover(ash.getNombre(), dojo1.getNombre());
        assertEquals(8, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), dojo1, this.feedService.feedEntrenador(this.ash.getNombre()),0);

        bichoService.duelo(this.ash.getNombre(),this.bichoPicachu.getId());
        assertEquals(8, feedService.feedEntrenador(ash.getNombre()).size());
        this.assertArribo(this.ash.getNombre(), dojo1, this.feedService.feedEntrenador(this.ash.getNombre()),0);
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testFeedUbicacionElEntrenadorNoExiste(){
        this.feedService.feedUbicacion("Ashe");
    }

    @Test
    public void testFeedUbicacion(){
        this.mapaService.mover(this.misty.getNombre(),this.guarderia.getNombre());
        this.mapaService.mover(this.brook.getNombre(),this.puebloB.getNombre());
        this.bichoService.buscar(this.brook.getNombre());
        this.mapaService.mover(this.brook.getNombre(),this.dojo.getNombre());
        this.bichoService.duelo(this.brook.getNombre(), this.bichoCharizard.getId());
        this.mapaService.moverMasCorto(this.brook.getNombre(),this.guarderia.getNombre());
        this.bichoService.abandonar(this.brook.getNombre(),this.bichoCharizard.getId());

        List<Evento> resultados = this.feedService.feedUbicacion(this.ash.getNombre());
        assertEquals(7, resultados.size());
        assertAbandono(this.brook.getNombre(), this.bichoCharizard, this.guarderia, resultados, 0);
        assertArribo(this.brook.getNombre(),this.guarderia,resultados,1);
        assertCoronacion(this.brook.getNombre(),this.bichoCharizard,this.bichoPichu,this.dojo,resultados, 2);
        assertArribo(this.brook.getNombre(),this.dojo,resultados, 3);
        assertCaptura(this.brook.getNombre(),this.charizard,this.puebloB,resultados, 4);
        assertArribo(this.brook.getNombre(),this.puebloB,resultados, 5);
        assertArribo(this.misty.getNombre(),this.guarderia,resultados, 6);

        resultados = this.feedService.feedUbicacion(this.brook.getNombre());
        assertEquals(3, resultados.size());
        assertAbandono(this.brook.getNombre(), this.bichoCharizard, this.guarderia, resultados, 0);
        assertArribo(this.brook.getNombre(), this.guarderia, resultados, 1);
        assertArribo(this.misty.getNombre(), this.guarderia, resultados, 2);

        resultados = this.feedService.feedUbicacion(this.misty.getNombre());
        assertEquals(3, resultados.size());
        assertAbandono(this.brook.getNombre(), this.bichoCharizard, this.guarderia, resultados, 0);
        assertArribo(this.brook.getNombre(), this.guarderia, resultados, 1);
        assertArribo(this.misty.getNombre(), this.guarderia, resultados, 2);
    }

    private void assertArribo(String entrenador, Ubicacion ubicacionDeArribo, List<Evento> eventos, Integer posicion){
        assertEquals(entrenador, eventos.get(posicion).getEntrenador());
        assertEquals(ubicacionDeArribo.getNombre(), ((Arribo) eventos.get(posicion)).getUbicacion());
    }

    private void assertCaptura(String entrenador, Especie especieCapturada, Ubicacion ubicacionDeCaptura, List<Evento> eventos, Integer posicion){
        assertEquals(entrenador, eventos.get(posicion).getEntrenador());
        assertEquals(especieCapturada.getNombre(), ((Captura) eventos.get(posicion)).getEspecie());
        assertEquals(ubicacionDeCaptura.getNombre(), ((Captura) eventos.get(posicion)).getUbicacion());
    }

    private void assertAbandono(String entrenador, Bicho bichoAbandonado, Ubicacion ubicacionDeAbandono, List<Evento> eventos, Integer posicion){
        assertEquals(entrenador, eventos.get(posicion).getEntrenador());
        assertEquals(bichoAbandonado.getEspecie().getNombre(), ((Abandono) eventos.get(posicion)).getEspecie());
        assertEquals(bichoAbandonado.getId(), ((Abandono) eventos.get(posicion)).getbichoId());
        assertEquals(ubicacionDeAbandono.getNombre(), ((Abandono) eventos.get(posicion)).getUbicacion());
    }

    private void assertCoronacion(String entrenador, Bicho bichoGanador, Bicho bichoPerdedor, Ubicacion ubicacionDeDuelo, List<Evento> eventos, Integer posicion) {
        assertEquals(entrenador, eventos.get(posicion).getEntrenador());
        assertEquals(bichoPerdedor.getId(), ((Coronacion) eventos.get(posicion)).getBichoPerdedorId());
        assertEquals(bichoPerdedor.getEntrenador().getNombre(), ((Coronacion) eventos.get(posicion)).getEntrenadorPerdedor());
        assertEquals(bichoPerdedor.getEspecie().getNombre(), ((Coronacion) eventos.get(posicion)).getEspeciePerdedor());
        assertEquals(bichoGanador.getId(), ((Coronacion) eventos.get(posicion)).getGanadorId());
        assertEquals(bichoGanador.getEspecie().getNombre(), ((Coronacion) eventos.get(posicion)).getEspecieGanador());
        assertEquals(ubicacionDeDuelo.getNombre(), ((Coronacion) eventos.get(posicion)).getUbicacion());
    }

    /*    Create the model functions    */
    private void crearHelpers(){
        this.busquedaHelper = new BusquedaHelperMock(true,true,true,true,this.picachu);
        this.busquedaHelperDAO.guardar(busquedaHelper);
    }

    private void crearUbicaciones() {
        this.puebloA = new Pueblo("PuebloA");
        this.puebloA.setBusquedaHelper(this.busquedaHelper);
        this.puebloA.addEspecieHabitante(this.picachu, 100);
        this.puebloB = new Pueblo("PuebloB");
        this.puebloB.setBusquedaHelper(this.busquedaHelper);
        this.puebloB.addEspecieHabitante(this.charizard, 100);
        this.dojo = new Dojo("Dojo");
        this.dojo1 = new Dojo("Dojo1");
        this.guarderia = new Guarderia("Guarderia");
        this.persistirUbicaciones(this.listaDeUbicaciones());
        this.conectarUbicaciones();
    }

    private void crearService() {
        this.feedService = new FeedServiceImpl(entrenadorDAO, neo4jMapaDAO, eventoDAO);
        this.mapaService = new MapaServiceImpl(entrenadorDAO, ubicacionDAO, neo4jMapaDAO, eventoDAO);
        this.bichoService = new BichoServiceImpl(this.entrenadorDAO, this.bichoDAO, this.eventoDAO);
    }

    private void crearDAOs() {
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.neo4jMapaDAO = new Neo4jMapaDAO();
        this.nivelDAO = new HibernateNivelDAO();
        this.eventoDAO = new MongoDBEventoDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.busquedaHelperDAO = new HibernateBusquedaHelperDAO();
    }

    private void conectarUbicaciones(){
        this.mapaService.conectar(this.puebloA.getNombre(), this.puebloB.getNombre(), new Terrestre());
        this.mapaService.conectar(this.puebloA.getNombre(), this.dojo.getNombre(), new Terrestre());
        this.mapaService.conectar(this.puebloA.getNombre(), this.dojo1.getNombre(), new Terrestre());
        this.mapaService.conectar(this.puebloA.getNombre(), this.guarderia.getNombre(), new Terrestre());
        this.mapaService.conectar(this.puebloB.getNombre(), this.puebloA.getNombre(), new Terrestre());
        this.mapaService.conectar(this.dojo.getNombre(), this.puebloA.getNombre(), new Terrestre());
        this.mapaService.conectar(this.dojo1.getNombre(), this.puebloA.getNombre(), new Terrestre());
        this.mapaService.conectar(this.guarderia.getNombre(), this.puebloA.getNombre(), new Terrestre());

    }

    private void crearEspecies(){
        this.picachu = new Especie("Picachu", TipoBicho.ELECTRICIDAD);
        this.pichu = new Especie("Pichu", TipoBicho.ELECTRICIDAD);
        this.charizard = new Especie("Charizard", TipoBicho.FUEGO);
        this.charmilion = new Especie("Charmilion", TipoBicho.FUEGO);
        this.squirtle = new Especie("Squirtle", TipoBicho.AGUA);
        this.picachu.setEnergiaInicial(100);
        this.charizard.setEnergiaInicial(1000);
        this.pichu.setEnergiaInicial(10);
        this.especieDAO.guardarTodos(this.listaDeEspecies());
    }

    private List<Especie> listaDeEspecies() {
        List<Especie> especies = new ArrayList<>();
        especies.add(this.picachu);
        especies.add(this.pichu);
        especies.add(this.charizard);
        especies.add(this.charmilion);
        especies.add(this.squirtle);
        return especies;
    }

    private void crearBichos(){
        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoPichu = new Bicho(this.pichu);
        this.bichoCharizard = new Bicho(this.charizard);
        this.bichoCharmilion = new Bicho(this.charmilion);
        this.bichoSquirtle = new Bicho(this.squirtle);
        this.bichoDAO.guardarTodos(this.listaDeBichos());
    }

    private List<Bicho> listaDeBichos() {
        List<Bicho> bichos = new ArrayList<>();
        bichos.add(this.bichoPicachu);
        bichos.add(this.bichoPichu);
        bichos.add(this.bichoCharizard);
        bichos.add(this.bichoCharmilion);
        bichos.add(this.bichoSquirtle);
        return bichos;
    }

    private void crearEntrenadores() {
        this.nivel      = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.ash        = new Entrenador("Ash", nivel);
        this.ash.addBicho(this.bichoPicachu);
        this.ash.addBicho(this.bichoCharmilion);
        this.ash.setUbicacionActual(this.puebloA);
        this.ash.addMonedas(50);

        this.nivel      = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.brook        = new Entrenador("Brook", nivel);
        this.brook.addBicho(this.bichoCharizard);
        this.brook.setUbicacionActual(this.dojo1);
        this.dojo1.setCampeonActual(this.bichoCharizard);
        this.brook.addMonedas(50);

        this.nivel      = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.misty        = new Entrenador("Misty", nivel);
        this.misty.addBicho(this.bichoPichu);
        this.misty.setUbicacionActual(this.dojo);
        this.dojo.setCampeonActual(this.bichoPichu);
        this.misty.addMonedas(50);

        this.entrenadorDAO.guardarTodos(Arrays.asList(ash,brook,misty));
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        ubicaciones.add(this.puebloA);
        ubicaciones.add(this.puebloB);
        ubicaciones.add(this.dojo);
        ubicaciones.add(this.dojo1);
        ubicaciones.add(this.guarderia);
        return ubicaciones;
    }

    private void persistirUbicaciones(List<Ubicacion> ubicaciones){
        for(Ubicacion ubicacion : ubicaciones){
            this.mapaService.crearUbicacion(ubicacion);
        }
    }
}
