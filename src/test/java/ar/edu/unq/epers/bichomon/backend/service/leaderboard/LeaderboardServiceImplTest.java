package ar.edu.unq.epers.bichomon.backend.service.leaderboard;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.*;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.service.especie.NullEspecieLeaderException;
import ar.edu.unq.epers.bichomon.backend.service.leaderboard.impl.LeaderboardServiceImpl;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.ELECTRICIDAD;
import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.FUEGO;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.assertEquals;

public class LeaderboardServiceImplTest {

    private Entrenador ash;
    private Entrenador misty;
    private Entrenador brook;

    private Especie picachu;
    private Especie charizard;
    private Especie especieLider;

    private Bicho bichoPicachu;
    private Bicho bichoCharizard1;
    private Bicho bichoCharizard2;

    private Dojo dojo;
    private Dojo dojo1;
    private Dojo dojo2;

    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;
    private EspecieDAO especieDAO;
    private UbicacionDAO ubicacionDAO;
    private CampeonDAO campeonDAO;
    private NivelDAO nivelDAO;

    private Entrenador ashRecuperado;
    private Bicho bichoPicachuRecuperado;
    private Dojo dojoRecuperado;

    private List<Entrenador> entrenadores;

    private LeaderboardService leaderboardService;

    private AbstractNivel nivel;

    Transaction hibernateTransaction = new HibernateTransaction();

    @Before
    public void crearModelo() {
        this.nivel = new UltimoNivel(10,10, 100);

        this.ash = new Entrenador("Ash", nivel);
        this.misty = new Entrenador("Misty", nivel);
        this.brook = new Entrenador("Brook", nivel);

        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.picachu.setEnergiaInicial(1000);
        this.charizard = new Especie("Charizard", FUEGO);
        this.charizard.setEnergiaInicial(100);

        this.bichoPicachu = new Bicho(picachu);
        this.bichoCharizard1 = new Bicho(charizard);
        this.bichoCharizard2 = new Bicho(charizard);

        this.dojo = new Dojo("dojo1");
        this.dojo1 = new Dojo("dojo2");
        this.dojo2 = new Dojo("dojo3");

        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.campeonDAO = new HibernateCampeonDAO();

        this.nivelDAO = new HibernateNivelDAO();

        this.leaderboardService = new LeaderboardServiceImpl(entrenadorDAO, campeonDAO);
    }

    @After
    public void limpiarEscenario(){
        run(SessionFactoryProvider::destroy, this.hibernateTransaction);
    }

    @Test
    public void campeonesDeUnModeloNuevoEsUnaListaVacia(){
        run(() -> {
            this.entrenadores = leaderboardService.campeones();
        }, this.hibernateTransaction);


    }

    @Test
    public void campeonesEstanOrdenadosCorrectamente(){
        run(() -> {
       this.dojo.setCampeonActual(bichoCharizard1);
        this.dojo1.setCampeonActual(bichoCharizard2);
        this.dojo2.setCampeonActual(bichoPicachu);

        this.especieDAO.guardar(this.charizard);
        this.especieDAO.guardar(this.picachu);

        this.nivelDAO.guardar(nivel);

        this.entrenadorDAO.guardar(this.ash);
        this.entrenadorDAO.guardar(this.brook);
        this.entrenadorDAO.guardar(this.misty);

        this.ash.addBicho(bichoCharizard1);
        this.brook.addBicho(bichoCharizard2);
        this.misty.addBicho(bichoPicachu);

        this.bichoDAO.guardar(bichoCharizard1);
        this.bichoDAO.guardar(bichoCharizard2);
        this.bichoDAO.guardar(bichoPicachu);



        this.ubicacionDAO.guardar(this.dojo);
        this.ubicacionDAO.guardar(this.dojo1);
        this.ubicacionDAO.guardar(this.dojo2);
    }, this.hibernateTransaction);
        run(() -> {
            this.entrenadores = leaderboardService.campeones();
            }, this.hibernateTransaction);
        List<String> rto = entrenadores.stream().map(Entrenador::getNombre).collect(Collectors.toList());
        List<String> rtoEsperado =  new ArrayList<String>();
        rtoEsperado.add(ash.getNombre());
        rtoEsperado.add(brook.getNombre());
        rtoEsperado.add(misty.getNombre());
        assertEquals(rto, rtoEsperado);
    }

    @Test(expected = NullEspecieLeaderException.class)
    public void testEspecieLiderFails(){
        run(() -> {
            leaderboardService.especieLider();
        }, this.hibernateTransaction);
    }

    @Test
    public void testEspecieLider(){
        run(() -> {
            this.nivelDAO.guardar(nivel);

            this.ash.addBicho(bichoCharizard1);
            this.ash.addBicho(bichoCharizard2);
            this.ash.addBicho(bichoPicachu);

            this.dojo.setCampeonActual(bichoCharizard1);
            this.dojo1.setCampeonActual(bichoCharizard2);
            this.dojo2.setCampeonActual(bichoPicachu);

            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.picachu);

            this.bichoDAO.guardar(bichoCharizard1);
            this.bichoDAO.guardar(bichoCharizard2);
            this.bichoDAO.guardar(bichoPicachu);

            this.entrenadorDAO.guardar(this.ash);

            this.ubicacionDAO.guardar(this.dojo);
            this.ubicacionDAO.guardar(this.dojo1);
            this.ubicacionDAO.guardar(this.dojo2);
        }, this.hibernateTransaction);
        run(() -> {
            especieLider = leaderboardService.especieLider();
        }, this.hibernateTransaction);
        assertEquals(this.charizard.getNombre(), especieLider.getNombre());
    }



    @Test
    public void testLideresDeUnModeloVacioEsUnaListaVacia(){
        run(() -> {
            entrenadores = leaderboardService.lideres();
        }, this.hibernateTransaction);
        assertEquals(entrenadores, new ArrayList<Entrenador>());
    }

    @Test
    public void testLideres(){
        run(() -> {
            this.ash.addBicho(bichoCharizard1);
            this.brook.addBicho(bichoCharizard2);
            this.misty.addBicho(bichoPicachu);

            this.dojo.setCampeonActual(bichoCharizard1);
            this.dojo1.setCampeonActual(bichoCharizard2);
            this.dojo2.setCampeonActual(bichoPicachu);

            this.nivelDAO.guardar(nivel);

            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.picachu);

            this.bichoDAO.guardar(bichoCharizard1);
            this.bichoDAO.guardar(bichoCharizard2);
            this.bichoDAO.guardar(bichoPicachu);

            this.entrenadorDAO.guardar(this.ash);
            this.entrenadorDAO.guardar(this.brook);
            this.entrenadorDAO.guardar(this.misty);

            this.ubicacionDAO.guardar(this.dojo);
            this.ubicacionDAO.guardar(this.dojo1);
            this.ubicacionDAO.guardar(this.dojo2);
        }, this.hibernateTransaction);
        run(() -> {
            entrenadores = leaderboardService.lideres();
        }, this.hibernateTransaction);
        List<String> rto = entrenadores.stream().map(Entrenador::getNombre).collect(Collectors.toList());
        List<String> rtoEsperado =  new ArrayList<String>();
        rtoEsperado.add(misty.getNombre());
        rtoEsperado.add(ash.getNombre());
        rtoEsperado.add(brook.getNombre());
        assertEquals(rtoEsperado, rto);
    }
}
