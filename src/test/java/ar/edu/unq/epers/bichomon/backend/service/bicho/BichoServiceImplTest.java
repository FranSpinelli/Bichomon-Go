package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.*;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.*;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BichoServiceImplTest {

    private Entrenador ash;
    private Especie picachu;
    private Especie charizard;
    private Especie squirtle;
    private Bicho bichoPicachu;
    private Bicho bichoCharizard1;
    private Bicho bichoCharizard2;
    private Bicho bichoSquirtle;
    private Ubicacion guarderia;
    private Ubicacion dojo;
    private Ubicacion pueblo;
    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;
    private EspecieDAO especieDAO;
    private BichoServiceImpl bichoService;
    private UbicacionDAO ubicacionDAO;
    private Entrenador ashRecuperado;
    private Bicho bichoPicachuRecuperado;
    private Guarderia guarderiaRecuperada;
    private BusquedaHelperDAO busquedaHelperDAO;
    private BusquedaHelper busquedaHelper;

    @Before
    public void crearModelo(){
        this.ash = new Entrenador("Ash");
        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.charizard = new Especie("Charizard", FUEGO);
        this.squirtle = new Especie("Squirtle", AGUA);

        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoCharizard1 = new Bicho(this.charizard);
        this.bichoCharizard2 = new Bicho(this.charizard);
        this.bichoSquirtle = new Bicho(this.squirtle);

        this.guarderia = new Guarderia();
        this.dojo = new Dojo();
        this.pueblo = new Pueblo();

        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.busquedaHelperDAO = new HibernateBusquedaHelperDAO();
        this.busquedaHelper = new BusquedaHelperMock(true,true,true,true,this.picachu);
        this.bichoService = new BichoServiceImpl(entrenadorDAO, bichoDAO);
    }

    @After
    public void limpiarEscenario(){
        SessionFactoryProvider.destroy();
    }

    @Test
    public void testAbandonar(){
        run(() -> {
            this.especieDAO.guardarTodos(this.listaDeEspecies());
            this.ubicacionDAO.guardarTodos(this.listaDeUbicaciones());
            this.ash.setUbicacionActual(this.guarderia);
            this.entrenadorDAO.guardarTodos(this.listaDeEntrenadores());
            this.ash.addBicho(this.bichoPicachu);
            this.ash.addBicho(this.bichoCharizard1);
            this.ash.addBicho(this.bichoCharizard2);
            this.ash.addBicho(this.bichoSquirtle);
            this.bichoDAO.guardarTodos(this.listaDeBichos());
        });
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        });

        assertEquals(new Integer(4), this.ashRecuperado.getCantidadDeBichos());
        assertEquals(ashRecuperado, this.bichoPicachuRecuperado.getEntrenador());
        assertEquals(0, this.guarderiaRecuperada.getBichosAbandonados().size());

        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());

        run(() -> {
            ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
            bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        });
        assertEquals(new Integer(3), ashRecuperado.getCantidadDeBichos());
        assertEquals(null, bichoPicachuRecuperado.getEntrenador());
        assertEquals(1, guarderiaRecuperada.getBichosAbandonados().size());
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testAbandonarNoEncuentraEntrenador(){
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = BichoInexistente.class)
    public void testAbandonarNoEncuentraBicho(){
        run(() -> this.entrenadorDAO.guardar(this.ash));
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = BichoAjeno.class)
    public void testAbandonarElBichoNoEsDelEntrenador(){
        run(() -> {
            this.especieDAO.guardar(this.picachu);
            this.bichoDAO.guardar(this.bichoPicachu);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = BichosInsuficientes.class)
    public void testAbandonarElEntrenadorNoTieneBichosSuficientes(){
        run(() -> {
            this.especieDAO.guardar(this.picachu);
            this.bichoDAO.guardar(this.bichoPicachu);
            this.ash.addBicho(this.bichoPicachu);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void testAbandonarElEntrenadorNoEstaEnUnaGuarderia(){
        run(() -> {
            this.ubicacionDAO.guardar(this.pueblo);
            this.especieDAO.guardar(this.picachu);
            this.especieDAO.guardar(this.squirtle);
            this.bichoDAO.guardar(this.bichoPicachu);
            this.ash.setUbicacionActual(this.pueblo);
            this.ash.addBicho(this.bichoPicachu);
            this.ash.addBicho(this.bichoSquirtle);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test
    public void testBuscar(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.guarderia.setBusquedaHelper(this.busquedaHelper);
            this.especieDAO.guardar(this.picachu);
            this.ubicacionDAO.guardar(this.guarderia);
            this.ash.setUbicacionActual(this.guarderia);
            this.entrenadorDAO.guardar(this.ash);
        });
        run(() -> {
            ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
        });
        assertEquals(new Integer(0), ashRecuperado.getCantidadDeBichos());
        this.bichoService.buscar("Ash");
        run(() -> {
            ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
        });
        assertEquals(new Integer(1), ashRecuperado.getCantidadDeBichos());
        assertEquals(this.picachu, this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie());
    }

    private List<Bicho> listaDeBichos() {
        List<Bicho> bichos = new ArrayList();
        bichos.add(this.bichoPicachu);
        bichos.add(this.bichoCharizard1);
        bichos.add(this.bichoCharizard2);
        bichos.add(this.bichoSquirtle);
        return bichos;
    }

    private List<Entrenador> listaDeEntrenadores() {
        List<Entrenador> entrenadores = new ArrayList();
        entrenadores.add(this.ash);
        return entrenadores;
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList();
        ubicaciones.add(this.guarderia);
        return ubicaciones;
    }

    private List<Especie> listaDeEspecies() {
        List<Especie> especies = new ArrayList();
        especies.add(this.picachu);
        especies.add(this.charizard);
        especies.add(this.squirtle);
        return especies;
    }
}
