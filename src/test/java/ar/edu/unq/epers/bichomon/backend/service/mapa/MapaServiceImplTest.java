package ar.edu.unq.epers.bichomon.backend.service.mapa;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoNoUtilizado;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoSinCampeon;
import ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j.Neo4jMapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.camino.Aereo;
import ar.edu.unq.epers.bichomon.backend.model.camino.Maritimo;
import ar.edu.unq.epers.bichomon.backend.model.camino.Terrestre;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.CaminoMuyCostoso;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.MapaServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.UbicacionMuyLejana;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.Neo4jTransaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.TransactionManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.HIBERNATE;
import static ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType.NEO4J;
import static org.junit.Assert.assertEquals;

public class MapaServiceImplTest {
    private EntrenadorDAO entrenadorDAO;
    private UbicacionDAO ubicacionDAO;
    private Neo4jMapaDAO neo4jMapaDAO;
    private BichoDAO bichoDAO;
    private EspecieDAO especieDAO;
    private CampeonDAO campeonDAO;
    private Entrenador ash, ashRecuperado, brook, misty;
    private Dojo dojo, dojoRecuperado, dojo1, dojo2, dojo3, dojo4;
    private Guarderia guarderia, guarderia1, guarderia2, guarderia3, guarderia4;
    private Pueblo pueblo, pueblo1, puebloOrigen, puebloDestino, puebloSinSalida, puebloPorDefecto;
    private MapaService mapaService;
    private Bicho bichoPicachu, bichoCharizard, bichoSquirtle;
    private Especie picachu, charizard, squirtle;
    private AbstractNivel nivel;
    private NivelDAO nivelDAO;
    private Transaction hibernateTransaction = new HibernateTransaction();
    private TransactionManager transactionManager = new TransactionManager().addPossibleTransaction(this.hibernateTransaction).addPossibleTransaction(new Neo4jTransaction());

    @Before
    public void crearModelo(){
        run(() -> {
            this.crearDAOs();
            this.crearUbicaciones();
            this.crearEntrenadores();//Los entrenadores estan por defecto en 'PuebloPorDefecto'
            this.crearEspecies();
            this.crearBichos();
        }, this.hibernateTransaction);
        this.mapaService = new MapaServiceImpl(entrenadorDAO, ubicacionDAO, neo4jMapaDAO);
    }

    @After
    public void limpiarEscenario(){
        run(() -> {
            SessionFactoryProvider.destroy();
            this.neo4jMapaDAO.deleteAll();
        }
        , this.transactionManager.addTransaction(HIBERNATE).addTransaction(NEO4J));
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testMoverNoEncuentraEntrenador(){
        this.mapaService.mover("Ashe", this.dojo.getNombre());
    }

    @Test(expected = UbicacionInexistente.class)
    public void testMoverNoEncuentraUbicacion(){
        this.mapaService.mover(this.ash.getNombre(), "Dojito");
    }

    @Test(expected = UbicacionActualException.class)
    public void testMoverElEntrenadorYaEstaEnLaUbicacion(){
        this.mapaService.mover(this.ash.getNombre(), this.dojo.getNombre());
        this.mapaService.mover(this.ash.getNombre(), this.dojo.getNombre());
    }

    @Test(expected = UbicacionMuyLejana.class)
    public void testMoverNoHayUnCaminoHaciaLaUbicacion(){
        this.mapaService.mover(this.ash.getNombre(), this.puebloOrigen.getNombre());
        this.mapaService.mover(this.ash.getNombre(), this.dojo.getNombre());
    }

    @Test(expected = CaminoMuyCostoso.class)
    public void testMoverElEntrenadorNoPuedeCostearElCamino(){
        this.ash.gastarMonedas(this.ash.getCantidadDeMonedas());
        this.mapaService.mover(this.ash.getNombre(), this.puebloOrigen.getNombre());
    }

    @Test
    public void testMover(){
        /*run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
            this.ashRecuperado.setUbicacionActual(this.dojo);
        }, this.hibernateTransaction);*/
        this.mapaService.mover(this.ash.getNombre(), this.puebloOrigen.getNombre());//Una moneda
        this.mapaService.mover(this.ash.getNombre(), this.puebloDestino.getNombre());//4 monedas
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
        }, this.hibernateTransaction);
        assertEquals(this.puebloDestino, this.ashRecuperado.getUbicacionActual());
        assertEquals(15, this.ashRecuperado.getCantidadDeMonedas());
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testMoverMasCortoNoEncuentraEntrenador(){
        this.mapaService.moverMasCorto("Ashe", this.dojo.getNombre());
    }

    @Test(expected = UbicacionInexistente.class)
    public void testMoverMasCortoNoEncuentraUbicacion(){
        this.mapaService.moverMasCorto(this.ash.getNombre(), "Dojito");
    }

    @Test(expected = UbicacionActualException.class)
    public void testMoverMasCortoElEntrenadorYaEstaEnLaUbicacion(){
        this.mapaService.mover(this.ash.getNombre(), this.dojo.getNombre());
        this.mapaService.moverMasCorto(this.ash.getNombre(), this.dojo.getNombre());
    }

    @Test(expected = UbicacionMuyLejana.class)
    public void testMoverMasCortoNoHayUnCaminoHaciaLaUbicacion(){
        this.mapaService.mover(this.ash.getNombre(), this.puebloOrigen.getNombre());
        this.mapaService.moverMasCorto(this.ash.getNombre(), this.dojo.getNombre());
    }

    @Test(expected = CaminoMuyCostoso.class)
    public void testMoverMasCortoElEntrenadorNoPuedeCostearElCamino(){
        this.ash.gastarMonedas(this.ash.getCantidadDeMonedas());
        this.mapaService.moverMasCorto(this.ash.getNombre(), this.puebloOrigen.getNombre());
    }

    @Test
    public void testMoverMasCorto(){
        /*run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
            this.ashRecuperado.setUbicacionActual(this.dojo);
        }, this.hibernateTransaction);*/
        this.mapaService.moverMasCorto(this.ash.getNombre(), this.puebloOrigen.getNombre());//Una moneda
        this.mapaService.moverMasCorto(this.ash.getNombre(), this.puebloDestino.getNombre());//10 monedas
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
        }, this.hibernateTransaction);
        assertEquals(this.puebloDestino, this.ashRecuperado.getUbicacionActual());
        assertEquals(9, this.ashRecuperado.getCantidadDeMonedas());
    }

    @Test(expected = UbicacionInexistente.class)
    public void testCantidadDeEntrenadoresNoEncuentraUbicacion(){
        this.mapaService.cantidadEntrenadores("Pueblito");
    }

    @Test
    public void testCantidadDeEntrenadores(){
        assertEquals(0, this.mapaService.cantidadEntrenadores(this.pueblo.getNombre()));
        this.mapaService.mover(this.ash.getNombre(), this.pueblo.getNombre());
        this.mapaService.mover(this.brook.getNombre(), this.pueblo.getNombre());
        assertEquals(2, this.mapaService.cantidadEntrenadores(this.pueblo.getNombre()));
        this.mapaService.mover(this.misty.getNombre(), this.pueblo.getNombre());
        assertEquals(3, this.mapaService.cantidadEntrenadores(this.pueblo.getNombre()));
        this.mapaService.mover(this.ash.getNombre(), this.guarderia.getNombre());
        this.mapaService.mover(this.brook.getNombre(), this.dojo.getNombre());
        assertEquals(1, this.mapaService.cantidadEntrenadores(this.pueblo.getNombre()));
        this.mapaService.mover(this.misty.getNombre(), this.dojo.getNombre());
        assertEquals(0, this.mapaService.cantidadEntrenadores(this.pueblo.getNombre()));
    }

    @Test(expected = UbicacionInexistente.class)
    public void testCampeonNoEncuentraUbicacion(){
        this.mapaService.campeon("Dojito");
    }

    @Test(expected = DojoSinCampeon.class)
    public void testCampeonElDojoNoTieneCampeon(){
        this.mapaService.campeon(this.dojo.getNombre());
    }

    @Test
    public void testCampeon(){
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getNombre());
            this.dojoRecuperado.setCampeonActual(this.bichoPicachu);
        }, this.hibernateTransaction);
        assertEquals(this.bichoPicachu, this.mapaService.campeon(this.dojo.getNombre()));
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getNombre());
            this.dojoRecuperado.setCampeonActual(this.bichoCharizard);
        }, this.hibernateTransaction);
        assertEquals(this.bichoCharizard, this.mapaService.campeon(this.dojo.getNombre()));
    }

    @Test(expected = UbicacionInexistente.class)
    public void testCampeonHistoricoNoEncuentraDojo() {
        this.mapaService.campeonHistorico("Dojito");
    }

    @Test(expected = DojoNoUtilizado.class)
    public void testCampeonHistoricoNoHuboCampeonesEnElDojo() {
        this.mapaService.campeonHistorico(this.dojo.getNombre());
    }

    @Test
    public void testCampeonHistorico(){
        this.crearCampeonato(this.bichoCharizard, LocalDate.now().minusDays(30), LocalDate.now().minusDays(28));
        this.crearCampeonato(this.bichoPicachu, LocalDate.now().minusDays(28), LocalDate.now().minusDays(2));
        this.crearCampeonato(this.bichoSquirtle, LocalDate.now().minusDays(2), null);
        assertEquals(this.bichoPicachu, this.mapaService.campeonHistorico(this.dojoRecuperado.getNombre()));
    }

//PRIVATE FUCTIONS------------------------------------------------------------------------------------

    private void crearCampeonato(Bicho bicho, LocalDate fechaInicio, LocalDate fechaFin){
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            this.dojoRecuperado.setCampeonActual(bicho);
        }, this.hibernateTransaction);
        Campeon campeonActual = this.dojoRecuperado.getCampeonActual();
        campeonActual.setFechaDeInicio(fechaInicio);
        campeonActual.setFechaDeFin(fechaFin);
        run(() -> this.campeonDAO.guardar(campeonActual), this.hibernateTransaction);
    }

    private void crearDAOs() {
        this.nivelDAO = new HibernateNivelDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.neo4jMapaDAO = new Neo4jMapaDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.campeonDAO = new HibernateCampeonDAO();
    }

    private void crearUbicaciones() {
        this.dojo = new Dojo("Dojo");
        this.dojo1 = new Dojo("Dojo1");
        this.dojo2 = new Dojo("Dojo2");
        this.dojo3 = new Dojo("Dojo3");
        this.dojo4 = new Dojo("Dojo4");
        this.guarderia = new Guarderia("Guarderia");
        this.guarderia1 = new Guarderia("Guarderia1");
        this.guarderia2 = new Guarderia("Guarderia2");
        this.guarderia3 = new Guarderia("Guarderia3");
        this.guarderia4 = new Guarderia("Guarderia4");
        this.pueblo = new Pueblo("Pueblo");
        this.pueblo1 = new Pueblo("Pueblo1");
        this.puebloOrigen = new Pueblo("PuebloOrigen");
        this.puebloDestino = new Pueblo("PuebloDestino");
        this.puebloSinSalida = new Pueblo("PuebloSinSalida");
        this.puebloPorDefecto = new Pueblo("PuebloPorDefecto");
        this.persistirUbicaciones(this.listaDeUbicaciones());
        this.conectarUbicaciones();
        //        this.ubicacionDAO.guardarTodos(this.listaDeUbicaciones());
    }

    private void conectarUbicaciones(){
        this.mapaService.conectar(this.puebloPorDefecto.getNombre(), this.dojo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloPorDefecto.getNombre(), this.pueblo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloPorDefecto.getNombre(), this.guarderia.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloPorDefecto.getNombre(), this.puebloOrigen.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.dojo.getNombre(), this.guarderia.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.dojo.getNombre(), this.pueblo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.guarderia.getNombre(), this.pueblo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.guarderia.getNombre(), this.dojo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.pueblo.getNombre(), this.dojo.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.pueblo.getNombre(), this.guarderia.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.puebloOrigen.getNombre(), this.guarderia4.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloOrigen.getNombre(), this.guarderia2.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloOrigen.getNombre(), this.dojo2.getNombre(), new Aereo().getName());
        this.mapaService.conectar(this.puebloOrigen.getNombre(), this.guarderia1.getNombre(), new Maritimo().getName());
        this.mapaService.conectar(this.puebloOrigen.getNombre(), this.puebloSinSalida.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.guarderia4.getNombre(), this.dojo4.getNombre(), new Maritimo().getName());

        this.mapaService.conectar(this.dojo4.getNombre(), this.pueblo1.getNombre(), new Aereo().getName());

        this.mapaService.conectar(this.pueblo1.getNombre(), this.puebloDestino.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.guarderia2.getNombre(), this.dojo3.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.dojo3.getNombre(), this.guarderia3.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.guarderia3.getNombre(), this.puebloDestino.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.guarderia3.getNombre(), this.dojo2.getNombre(), new Aereo().getName());

        this.mapaService.conectar(this.puebloDestino.getNombre(), this.guarderia3.getNombre(), new Maritimo().getName());
        this.mapaService.conectar(this.puebloDestino.getNombre(), this.dojo1.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.puebloDestino.getNombre(), this.puebloSinSalida.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.dojo2.getNombre(), this.puebloDestino.getNombre(), new Aereo().getName());
        this.mapaService.conectar(this.dojo2.getNombre(), this.puebloOrigen.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.guarderia1.getNombre(), this.puebloOrigen.getNombre(), new Aereo().getName());
        this.mapaService.conectar(this.guarderia1.getNombre(), this.dojo1.getNombre(), new Terrestre().getName());

        this.mapaService.conectar(this.dojo1.getNombre(), this.guarderia1.getNombre(), new Terrestre().getName());
        this.mapaService.conectar(this.dojo1.getNombre(), this.puebloDestino.getNombre(), new Maritimo().getName());

    }

    private void persistirUbicaciones(List<Ubicacion> ubicaciones){
        for(Ubicacion ubicacion : ubicaciones){
            this.mapaService.crearUbicacion(ubicacion);
        }
    }

    private void crearBichos() {
        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoCharizard = new Bicho(this.charizard);
        this.bichoSquirtle = new Bicho(this.squirtle);
        this.bichoDAO.guardarTodos(this.listaDeBichos());
    }

    private void crearEspecies() {
        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.charizard = new Especie("Charizard", FUEGO);
        this.squirtle = new Especie("Squirtle", AGUA);
        this.especieDAO.guardarTodos(this.listaDeEspecies());
    }

    private void crearEntrenadores() {
        this.nivel = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.ash = new Entrenador("Ash", nivel);
        this.brook = new Entrenador("Brook", nivel);
        this.misty = new Entrenador("Misty", nivel);
        this.ash.setUbicacionActual(this.puebloPorDefecto);
        this.brook.setUbicacionActual(this.puebloPorDefecto);
        this.misty.setUbicacionActual(this.puebloPorDefecto);
        this.ash.addMonedas(20);
        this.brook.addMonedas(20);
        this.misty.addMonedas(20);
        this.entrenadorDAO.guardarTodos(this.listaDeEntrenadores());
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        ubicaciones.add(this.dojo);
        ubicaciones.add(this.guarderia);
        ubicaciones.add(this.pueblo);
        ubicaciones.add(this.pueblo1);
        ubicaciones.add(this.puebloOrigen);
        ubicaciones.add(this.puebloDestino);
        ubicaciones.add(this.puebloSinSalida);
        ubicaciones.add(this.puebloPorDefecto);
        ubicaciones.add(this.guarderia1);
        ubicaciones.add(this.guarderia2);
        ubicaciones.add(this.guarderia3);
        ubicaciones.add(this.guarderia4);
        ubicaciones.add(this.dojo1);
        ubicaciones.add(this.dojo2);
        ubicaciones.add(this.dojo3);
        ubicaciones.add(this.dojo4);
        return ubicaciones;
    }

    private List<Bicho> listaDeBichos() {
        List<Bicho> bichos = new ArrayList<>();
        bichos.add(this.bichoPicachu);
        bichos.add(this.bichoCharizard);
        bichos.add(this.bichoSquirtle);
        return bichos;
    }

    private List<Especie> listaDeEspecies() {
        List<Especie> especies = new ArrayList<>();
        especies.add(this.picachu);
        especies.add(this.charizard);
        especies.add(this.squirtle);
        return especies;
    }

    private List<Entrenador> listaDeEntrenadores() {
        List<Entrenador> entrenadores = new ArrayList<>();
        entrenadores.add(this.ash);
        entrenadores.add(this.brook);
        entrenadores.add(this.misty);
        return entrenadores;
    }
}
