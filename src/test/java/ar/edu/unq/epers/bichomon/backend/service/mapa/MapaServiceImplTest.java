package ar.edu.unq.epers.bichomon.backend.service.mapa;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoNoUtilizado;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.exceptions.DojoSinCampeon;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.MapaServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static org.junit.Assert.*;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class MapaServiceImplTest {
    private EntrenadorDAO entrenadorDAO;
    private UbicacionDAO ubicacionDAO;
    private BichoDAO bichoDAO;
    private EspecieDAO especieDAO;
    private CampeonDAO campeonDAO;
    private Entrenador ash, ashRecuperado, brook, misty;
    private Dojo dojo, dojoRecuperado;
    private Guarderia guarderia;
    private Pueblo pueblo;
    private MapaService mapaService;
    private Bicho bichoPicachu, bichoCharizard, bichoSquirtle;
    private Especie picachu, charizard, squirtle;
    private AbstractNivel nivel;
    private NivelDAO nivelDAO;

    @Before
    public void crearModelo(){
        run(() -> {
            this.crearDAOs();
            this.crearEntrenadores();
            this.crearEspecies();
            this.crearBichos();
            this.crearUbicaciones();
        });
        this.mapaService = new MapaServiceImpl(entrenadorDAO, ubicacionDAO);
    }

    @After
    public void limpiarEscenario(){
        run(SessionFactoryProvider::destroy);
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

    @Test
    public void testMover(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
            this.ashRecuperado.setUbicacionActual(this.dojo);
        });
        this.mapaService.mover(this.ash.getNombre(), this.guarderia.getNombre());
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar(this.ash.getNombre());
        });
        assertEquals(this.guarderia, this.ashRecuperado.getUbicacionActual());
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
        });
        assertEquals(this.bichoPicachu, this.mapaService.campeon(this.dojo.getNombre()));
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getNombre());
            this.dojoRecuperado.setCampeonActual(this.bichoCharizard);
        });
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
        });
        Campeon campeonActual = this.dojoRecuperado.getCampeonActual();
        campeonActual.setFechaDeInicio(fechaInicio);
        campeonActual.setFechaDeFin(fechaFin);
        run(() -> this.campeonDAO.guardar(campeonActual));
    }

    private void crearDAOs() {
        this.nivelDAO = new HibernateNivelDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.campeonDAO = new HibernateCampeonDAO();
    }

    private void crearUbicaciones() {
        this.dojo = new Dojo("Dojo");
        this.guarderia = new Guarderia("Guarderia");
        this.pueblo = new Pueblo("Pueblo");
        this.ubicacionDAO.guardarTodos(this.listaDeUbicaciones());
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
        this.entrenadorDAO.guardarTodos(this.listaDeEntrenadores());
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        ubicaciones.add(this.dojo);
        ubicaciones.add(this.guarderia);
        ubicaciones.add(this.pueblo);
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
