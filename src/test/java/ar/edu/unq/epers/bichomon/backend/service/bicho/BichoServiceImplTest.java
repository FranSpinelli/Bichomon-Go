
package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnEdad;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.*;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoAjeno;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoInexistente;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.*;

public class BichoServiceImplTest {

    private Entrenador ash;
    private Entrenador brook;
    private Especie pichu;
    private Especie picachu;
    private Especie charizard;
    private Especie squirtle;
    private Especie raychu;
    private Bicho bichoPicachu;
    private Bicho bichoCharizard1;
    private Bicho bichoCharizard2;
    private Bicho bichoSquirtle;
    private Guarderia guarderia;
    private DueloHelper dueloHelper;
    private Dojo dojo;
    private Pueblo pueblo;
    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;
    private EspecieDAO especieDAO;
    private BichoServiceImpl bichoService;
    private UbicacionDAO ubicacionDAO;
    private Entrenador ashRecuperado;
    private Bicho bichoPicachuRecuperado;
    private Guarderia guarderiaRecuperada;
    private BusquedaHelperDAO busquedaHelperDAO;
    private BusquedaHelperMock busquedaHelper;
    private Dojo dojoRecuperado;
    private CondicionDeEvolucion condicion;
    private HibernateDAO<CondicionDeEvolucion> condicionDAO;
    //private CampeonDAO campeonDAO;

    @Before
    public void crearModelo(){
        this.ash = new Entrenador("Ash");
        this.brook = new Entrenador("Brook");
        this.pichu = new Especie("Pichu", ELECTRICIDAD);
        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.picachu.setEnergiaInicial(1000);
        this.charizard = new Especie("Charizard", FUEGO);
        this.charizard.setEnergiaInicial(100);
        this.squirtle = new Especie("Squirtle", AGUA);
        this.raychu = new Especie("Raychu", ELECTRICIDAD);

        this.condicion = new CondicionBasadaEnEdad(2);

        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoCharizard1 = new Bicho(this.charizard);
        this.bichoCharizard2 = new Bicho(this.charizard);
        this.bichoSquirtle = new Bicho(this.squirtle);

        this.guarderia = new Guarderia();
        this.dueloHelper = new DueloHelper();
        this.dojo = new Dojo();
        this.pueblo = new Pueblo();

        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.busquedaHelperDAO = new HibernateBusquedaHelperDAO();
        this.condicionDAO = new HibernateDAO<CondicionDeEvolucion>(CondicionDeEvolucion.class);
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

    // TODO: Hacer nuevos casos de error de busqueda (con busqueda no exitosa y con busqueda imposibilitada)
    // TODO: Hacer casos de busqueda en todas las ubicaciones
    // TODO: Mejorar forma de creacion guardado (DataService)
    @Test
    public void testBuscarEnGuarderia(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.guarderia.setBusquedaHelper(this.busquedaHelper);
            this.especieDAO.guardar(this.picachu);
            this.ubicacionDAO.guardar(this.guarderia);
            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.squirtle);
            this.bichoDAO.guardar(this.bichoCharizard1);
            this.bichoDAO.guardar(this.bichoSquirtle);
            this.ash.setUbicacionActual(this.guarderia);
            this.brook.setUbicacionActual(this.guarderia);
            this.brook.addBicho(this.bichoCharizard1);
            this.brook.addBicho(this.bichoSquirtle);
            this.entrenadorDAO.guardar(this.ash);
            this.entrenadorDAO.guardar(this.brook);
        });
        this.bichoService.abandonar("Brook", this.bichoCharizard1.getId());
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(0), ashRecuperado.getCantidadDeBichos());
        this.bichoService.buscar("Ash");
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(1), ashRecuperado.getCantidadDeBichos());
        assertEquals(this.charizard, this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie());
    }

    @Test
    public void testBuscarEnDojo(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.dojo.setBusquedaHelper(this.busquedaHelper);
            this.dojo.setCampeonActual(this.bichoPicachu);
            this.picachu.setEvolucionRaiz(this.pichu);
            this.especieDAO.guardar(this.pichu);
            this.especieDAO.guardar(this.picachu);
            this.ubicacionDAO.guardar(this.dojo);
            this.ash.setUbicacionActual(this.dojo);
            this.entrenadorDAO.guardar(this.ash);
        });
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(0), ashRecuperado.getCantidadDeBichos());
        this.bichoService.buscar("Ash");
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(1), ashRecuperado.getCantidadDeBichos());
        assertEquals(this.pichu, this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie());
    }

    @Test
    public void testBuscarEnPueblo(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.pueblo.setBusquedaHelper(this.busquedaHelper);
            this.pueblo.addEspecieHabitante(this.charizard, 100);
            this.especieDAO.guardar(this.charizard);
            this.ubicacionDAO.guardar(this.pueblo);
            this.ash.setUbicacionActual(this.pueblo);
            this.entrenadorDAO.guardar(this.ash);
        });
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(0), ashRecuperado.getCantidadDeBichos());
        this.bichoService.buscar("Ash");
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        });
        assertEquals(new Integer(1), ashRecuperado.getCantidadDeBichos());
        assertEquals(this.charizard, this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie());
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarNoEncuentraBicho(){
        run(() -> {
            this.busquedaHelper.setFactorRandom(false);
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.pueblo.setBusquedaHelper(this.busquedaHelper);
            this.pueblo.addEspecieHabitante(this.charizard, 100);
            this.especieDAO.guardar(this.charizard);
            this.ubicacionDAO.guardar(this.pueblo);
            this.ash.setUbicacionActual(this.pueblo);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnGuarderiaSinBichosAbandonados(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.guarderia.setBusquedaHelper(this.busquedaHelper);
            this.especieDAO.guardar(this.picachu);
            this.ubicacionDAO.guardar(this.guarderia);
            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.squirtle);
            this.ash.setUbicacionActual(this.guarderia);
            this.brook.setUbicacionActual(this.guarderia);
            this.brook.addBicho(this.bichoCharizard1);
            this.brook.addBicho(this.bichoSquirtle);
            this.entrenadorDAO.guardar(this.ash);
            this.entrenadorDAO.guardar(this.brook);
        });
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnPuebloSinEspeciesHabitantes(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.pueblo.setBusquedaHelper(this.busquedaHelper);
            this.ubicacionDAO.guardar(this.pueblo);
            this.ash.setUbicacionActual(this.pueblo);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnPuebloSinCampeon(){
        run(() -> {
            this.busquedaHelperDAO.guardar(this.busquedaHelper);
            this.dojo.setBusquedaHelper(this.busquedaHelper);
            this.ubicacionDAO.guardar(this.dojo);
            this.ash.setUbicacionActual(this.dojo);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.buscar("Ash");
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testBuscarNoEncuentraEntrenador(){
        this.bichoService.buscar("Ash");
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testdueloNoEncuentraEntrenador(){
        this.bichoService.duelo("Ash", bichoPicachu.getId());
    }
    @Test(expected = BichoInexistente.class)
    public void testDueloNoEncuentraBicho(){
        run(() -> this.entrenadorDAO.guardar(this.ash));

        this.bichoService.duelo("Ash", bichoPicachu.getId());
    }

    @Test(expected = BichoAjeno.class)
    public void testDueloBichoNoEsDelEntrenador(){
        run(() -> {
            this.especieDAO.guardar(this.picachu);
            this.bichoDAO.guardar(this.bichoPicachu);
            this.entrenadorDAO.guardar(this.ash);
        });
        this.bichoService.duelo("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void testDueloEntrenadorNoEstaEnUnDojo(){
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

        this.bichoService.duelo("Ash", bichoPicachu.getId());
    }

    @Test
    public void testDuelo(){
        run(() -> {
            this.especieDAO.guardarTodos(this.listaDeEspecies());
            this.ubicacionDAO.guardarTodos(this.listaDeUbicaciones());
            this.ash.setUbicacionActual(this.dojo);
            this.entrenadorDAO.guardarTodos(this.listaDeEntrenadores());
            this.brook.addBicho(this.bichoCharizard1);
            this.ash.addBicho(this.bichoPicachu);
            this.ash.addBicho(this.bichoCharizard2);
            this.ash.addBicho(this.bichoSquirtle);
            this.bichoDAO.guardarTodos(this.listaDeBichos());
        });
        run(() -> {
            //this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            //this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        });
        assertEquals(0, dojoRecuperado.getListaDeCampeones().size());
        assertNull(this.dojoRecuperado.getCampeonActual());
        run(() -> {
            //this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            this.dojoRecuperado.setCampeonActual(this.bichoCharizard1);
            //this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        });

        assertEquals(1, dojoRecuperado.getListaDeCampeones().size());
        assertEquals(this.bichoCharizard1, this.dojoRecuperado.getCampeonActual().getBicho());

        this.bichoService.duelo("Ash", this.bichoPicachu.getId());

        run(() -> {
            //ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            //this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        });
        assertEquals(2, dojoRecuperado.getListaDeCampeones().size());
        assertEquals(bichoPicachu,this.dojoRecuperado.getCampeonActual().getBicho());

    }

    @Test
    public void testNoPuedeEvolucionar(){
        this.guardarParaTestsEvolucionar();
        run(() -> {
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
            this.bichoPicachuRecuperado.setFechaDeNacimiento(LocalDate.now());
        });
        assertFalse(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testPuedeEvolucionar(){
        this.guardarParaTestsEvolucionar();
        run(() -> this.bichoDAO.guardar(bichoPicachu));
        assertTrue(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testEvolucionar(){
        // TODO: Tirar excepcion si no evoluciona
        // TODO: Borrar prints
        this.guardarParaTestsEvolucionar();
        Bicho bichoEvolucionado = this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
        assertEquals(bichoEvolucionado.getEspecie().getNombre(), this.raychu.getNombre());
    }

//PRIVATE FUCTIONS------------------------------------------------------------------------------------

    private void guardarParaTestsEvolucionar(){
        ArrayList<CondicionDeEvolucion> condiciones = new ArrayList<>();
        condiciones.add(this.condicion);
        this.bichoPicachu.setFechaDeNacimiento(LocalDate.of(2019,10,2));
        this.picachu.setEspecieAEvolucionar(raychu, condiciones);
        run(() -> {
            this.especieDAO.guardar(this.picachu);
            this.especieDAO.guardar(this.raychu);
            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.squirtle);

            this.ubicacionDAO.guardar(this.guarderia);

            this.entrenadorDAO.guardar(this.ash);
            this.condicionDAO.guardar(this.condicion);

            this.ash.addBicho(this.bichoPicachu);
            this.ash.addBicho(this.bichoCharizard1);
            this.ash.addBicho(this.bichoCharizard2);
            this.ash.addBicho(this.bichoSquirtle);

            this.bichoDAO.guardar(this.bichoPicachu);
            this.bichoDAO.guardar(this.bichoCharizard1);
            this.bichoDAO.guardar(this.bichoCharizard2);
            this.bichoDAO.guardar(this.bichoSquirtle);
        });
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
        entrenadores.add(this.brook);
        return entrenadores;
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList();
        ubicaciones.add(this.guarderia);
        ubicaciones.add(this.dojo);
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