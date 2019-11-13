
package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.EvolucionNoPermitida;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnEdad;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.*;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoAjeno;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoInexistente;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
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

    private Entrenador ash, brook, ashRecuperado, brookRecuperado;
    private Especie pichu, picachu, charizard, squirtle, raychu;
    private Bicho bichoPicachu, bichoCharizard1, bichoCharizard2, bichoSquirtle, bichoPicachuRecuperado;
    private Guarderia guarderia, guarderiaRecuperada; private Dojo dojo, dojoRecuperado; private Pueblo pueblo, puebloRecuperado;
    private BusquedaHelperMock busquedaHelper, busquedaHelperRecuperado;

    private EntrenadorDAO entrenadorDAO; private BichoDAO bichoDAO;
    private EspecieDAO especieDAO; private UbicacionDAO ubicacionDAO;
    private BusquedaHelperDAO busquedaHelperDAO;
    private NivelDAO nivelDAO;
    private CondicionDAO condicionDAO;

    private Transaction hibernateTransaction = new HibernateTransaction();

    private BichoServiceImpl bichoService;
    private CondicionDeEvolucion condicion;
    private AbstractNivel nivel;

    @Before
    public void crearModelo(){
        run(() -> {
            this.crearDAOs();
            this.crearEntrenadores();
            this.crearCondiciones();
            this.crearEspecies();
            this.crearBichos();
            this.crearHelpers();
            this.crearUbicaciones();
        }, this.hibernateTransaction);
        this.bichoService = new BichoServiceImpl(entrenadorDAO, bichoDAO);
    }

    @After
    public void limpiarEscenario(){
        run(SessionFactoryProvider::destroy, this.hibernateTransaction);
    }

    @Test
    public void testAbandonar(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.guarderia);
            this.ashRecuperado.addBicho(this.bichoPicachu);
            this.ashRecuperado.addBicho(this.bichoCharizard1);
            this.ashRecuperado.addBicho(this.bichoCharizard2);
            this.ashRecuperado.addBicho(this.bichoSquirtle);
        }, this.hibernateTransaction);
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        }, this.hibernateTransaction);
        assertTrue(this.ashRecuperado.tieneBicho(this.bichoPicachuRecuperado));
        assertEquals(new Integer(4), this.ashRecuperado.getCantidadDeBichos());
        assertEquals(this.ashRecuperado, this.bichoPicachuRecuperado.getEntrenador());
        assertEquals(0, this.guarderiaRecuperada.getBichosAbandonados().size());
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.guarderiaRecuperada = (Guarderia) this.ubicacionDAO.recuperar(this.guarderia.getId());
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
        }, this.hibernateTransaction);
        assertFalse(this.ashRecuperado.tieneBicho(this.bichoPicachuRecuperado));
        assertEquals(new Integer(3), this.ashRecuperado.getCantidadDeBichos());
        assertNull(this.bichoPicachuRecuperado.getEntrenador());
        assertEquals(1, this.guarderiaRecuperada.getBichosAbandonados().size());
        assertEquals(this.bichoPicachuRecuperado, this.guarderiaRecuperada.getBichosAbandonados().iterator().next());
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testAbandonarNoEncuentraEntrenador(){
        this.bichoService.abandonar("Ashe", this.bichoPicachu.getId());
    }

    @Test(expected = BichoInexistente.class)
    public void testAbandonarNoEncuentraBicho(){
        this.bichoService.abandonar("Ash", 0);
    }

    @Test(expected = BichoAjeno.class)
    public void testAbandonarElBichoNoEsDelEntrenador(){
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = BichosInsuficientes.class)
    public void testAbandonarElEntrenadorNoTieneBichosSuficientes(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.addBicho(this.bichoPicachu);
        }, this.hibernateTransaction);
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void testAbandonarElEntrenadorNoEstaEnUnaGuarderia(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.pueblo);
            this.ashRecuperado.addBicho(this.bichoPicachu);
            this.ashRecuperado.addBicho(this.bichoSquirtle);
        }, this.hibernateTransaction);
        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());
    }

    @Test
    public void testBuscarEnGuarderia(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.brookRecuperado = this.entrenadorDAO.recuperar("Brook");
            this.ashRecuperado.setUbicacionActual(this.guarderia);
            this.brookRecuperado.setUbicacionActual(this.guarderia);
            this.brookRecuperado.addBicho(this.bichoCharizard1);
            this.brookRecuperado.addBicho(this.bichoSquirtle);
        }, this.hibernateTransaction);
        this.bichoService.abandonar("Brook", this.bichoCharizard1.getId());
        this.comprobacionBusquedaEncuentraBichoDeEspecie(this.charizard);
    }

    @Test
    public void testBuscarEnDojo(){
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            this.dojoRecuperado.setCampeonActual(this.bichoPicachu);
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.dojoRecuperado);
        }, this.hibernateTransaction);
        this.comprobacionBusquedaEncuentraBichoDeEspecie(this.pichu);
    }

    @Test
    public void testBuscarEnPueblo(){
        run(() -> {
            this.puebloRecuperado = (Pueblo) this.ubicacionDAO.recuperar(this.pueblo.getId());
            this.puebloRecuperado.addEspecieHabitante(this.charizard, 55);
            this.puebloRecuperado.addEspecieHabitante(this.picachu, 45);
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.puebloRecuperado);
        }, this.hibernateTransaction);
        List<Especie> especies = new ArrayList<>();
        especies.add(this.charizard);
        especies.add(this.picachu);
        this.comprobacionBusquedaEncuentraBichoDeAlgunaEspecie(especies);
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarNoEncuentraBicho(){
        run(() -> {
            this.busquedaHelperRecuperado = (BusquedaHelperMock) this.busquedaHelperDAO.recuperar(this.busquedaHelper.getId());
            this.busquedaHelperRecuperado.setFactorRandom(false);
            this.puebloRecuperado = (Pueblo) this.ubicacionDAO.recuperar(this.pueblo.getId());
            this.puebloRecuperado.addEspecieHabitante(this.charizard, 100);
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.puebloRecuperado);
        }, this.hibernateTransaction);
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnGuarderiaSinBichosAbandonados(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.brookRecuperado = this.entrenadorDAO.recuperar("Brook");
            this.ashRecuperado.setUbicacionActual(this.guarderia);
            this.brookRecuperado.setUbicacionActual(this.guarderia);
            this.brookRecuperado.addBicho(this.bichoCharizard1);
            this.brookRecuperado.addBicho(this.bichoSquirtle);
        }, this.hibernateTransaction);
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnGuarderiaConBichosAbandonadosDelEntrenador(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.guarderia);
            this.ashRecuperado.addBicho(this.bichoCharizard1);
            this.ashRecuperado.addBicho(this.bichoSquirtle);
        }, this.hibernateTransaction);
        this.bichoService.abandonar("Ash", this.bichoCharizard1.getId());
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnPuebloSinEspeciesHabitantes(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.pueblo);
        }, this.hibernateTransaction);
        this.bichoService.buscar("Ash");
    }

    @Test(expected = BusquedaNoExitosa.class)
    public void testBuscarEnDojoSinCampeon(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.dojo);
        }, this.hibernateTransaction);
        this.bichoService.buscar("Ash");
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testBuscarNoEncuentraEntrenador(){
        this.bichoService.buscar("Ashe");
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testDueloNoEncuentraEntrenador(){
        this.bichoService.duelo("Ashe", bichoPicachu.getId());
    }

    @Test(expected = BichoInexistente.class)
    public void testDueloNoEncuentraBicho(){
        this.bichoService.duelo("Ash", 0);
    }

    @Test(expected = BichoAjeno.class)
    public void testDueloBichoNoEsDelEntrenador(){
        this.bichoService.duelo("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = UbicacionIncorrectaException.class)
    public void testDueloEntrenadorNoEstaEnUnDojo(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.pueblo);
            this.ashRecuperado.addBicho(this.bichoPicachu);
        }, this.hibernateTransaction);
        this.bichoService.duelo("Ash", this.bichoPicachu.getId());
    }

    @Test
    public void testDuelo(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.setUbicacionActual(this.dojo);
            this.brookRecuperado = this.entrenadorDAO.recuperar("Brook");
            this.brookRecuperado.addBicho(this.bichoCharizard1);
            this.ashRecuperado.addBicho(this.bichoPicachu);
            this.ashRecuperado.addBicho(this.bichoCharizard2);
            this.ashRecuperado.addBicho(this.bichoSquirtle);
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
        }, this.hibernateTransaction);
        assertEquals(0, this.dojoRecuperado.getListaDeCampeones().size());
        assertNull(this.dojoRecuperado.getCampeonActual());
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
            this.dojoRecuperado.setCampeonActual(this.bichoCharizard1);
        }, this.hibernateTransaction);
        assertEquals(1, this.dojoRecuperado.getListaDeCampeones().size());
        assertEquals(this.bichoCharizard1, this.dojoRecuperado.getCampeonActual().getBicho());
        this.bichoService.duelo("Ash", this.bichoPicachu.getId());
        run(() -> {
            this.dojoRecuperado = (Dojo) this.ubicacionDAO.recuperar(this.dojo.getId());
        }, this.hibernateTransaction);
        assertEquals(2, this.dojoRecuperado.getListaDeCampeones().size());
        assertEquals(this.bichoPicachu, this.dojoRecuperado.getCampeonActual().getBicho());
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testPuedeEvolucionarNoEncuentraEntrenador(){
        this.bichoService.puedeEvolucionar("Ashe", bichoPicachu.getId());
    }

    @Test(expected = BichoInexistente.class)
    public void testPuedeEvolucionarNoEncuentraBicho(){
        this.bichoService.puedeEvolucionar("Ash", 0);
    }


    @Test
    public void testPuedeEvolucionarNoCumpleCondiciones(){
        run(() -> {
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.addBicho(this.bichoPicachuRecuperado);
            this.bichoPicachuRecuperado.setFechaDeNacimiento(LocalDate.now());
        }, this.hibernateTransaction);
        assertFalse(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testPuedeEvolucionarElBichoNoEsDelEntrenador(){
        assertFalse(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testPuedeEvolucionar(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.addBicho(this.bichoPicachu);
        }, this.hibernateTransaction);
        assertTrue(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testEvolucionar(){
        // TODO: Hacer casos de tests con varias condiciones de evolucion
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.addBicho(this.bichoPicachu);
        }, this.hibernateTransaction);
        Bicho bichoEvolucionado = this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
        assertEquals(bichoEvolucionado.getEspecie().getNombre(), this.raychu.getNombre());
    }

    @Test(expected = EntrenadorInexistente.class)
    public void testEvolucionarNoEncuentraEntrenador(){
        this.bichoService.evolucionar("Ashe", bichoPicachu.getId());
    }

    @Test(expected = BichoInexistente.class)
    public void testEvolucionarNoEncuentraBicho(){
        this.bichoService.evolucionar("Ash", 0);
    }


    @Test(expected = BichoAjeno.class)
    public void testEvolucionarBichoNoEsDelEntrenador(){
        this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
    }

    @Test(expected = EvolucionNoPermitida.class)
    public void testEvolucionarBichoNoPuedeEvolucionar(){
        run(() -> {
            this.bichoPicachuRecuperado = this.bichoDAO.recuperar(this.bichoPicachu.getId());
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
            this.ashRecuperado.addBicho(this.bichoPicachuRecuperado);
            this.bichoPicachuRecuperado.setFechaDeNacimiento(LocalDate.now());
        }, this.hibernateTransaction);
        this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
    }

//PRIVATE FUCTIONS------------------------------------------------------------------------------------

    private void comprobacionBusquedaEncuentraBicho(){
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        }, this.hibernateTransaction);
        assertEquals(new Integer(0), ashRecuperado.getCantidadDeBichos());
        this.bichoService.buscar("Ash");
        run(() -> {
            this.ashRecuperado = this.entrenadorDAO.recuperar("Ash");
        }, this.hibernateTransaction);
        assertEquals(new Integer(1), ashRecuperado.getCantidadDeBichos());
    }

    private void comprobacionBusquedaEncuentraBichoDeEspecie(Especie especie){
        this.comprobacionBusquedaEncuentraBicho();
        assertEquals(especie, this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie());
    }

    private void comprobacionBusquedaEncuentraBichoDeAlgunaEspecie(List<Especie> especies){
        this.comprobacionBusquedaEncuentraBicho();
        assertTrue(especies.contains(this.ashRecuperado.getInventarioDeBichos().iterator().next().getEspecie()));
    }

    private List<Bicho> listaDeBichos() {
        List<Bicho> bichos = new ArrayList<>();
        bichos.add(this.bichoPicachu);
        bichos.add(this.bichoCharizard1);
        bichos.add(this.bichoCharizard2);
        bichos.add(this.bichoSquirtle);
        return bichos;
    }

    private List<Entrenador> listaDeEntrenadores() {
        List<Entrenador> entrenadores = new ArrayList<>();
        entrenadores.add(this.ash);
        entrenadores.add(this.brook);
        return entrenadores;
    }

    private List<Ubicacion> listaDeUbicaciones() {
        List<Ubicacion> ubicaciones = new ArrayList<>();
        ubicaciones.add(this.pueblo);
        ubicaciones.add(this.guarderia);
        ubicaciones.add(this.dojo);
        return ubicaciones;
    }

    private List<Especie> listaDeEspecies() {
        List<Especie> especies = new ArrayList<>();
        especies.add(this.pichu);
        especies.add(this.raychu);
        especies.add(this.picachu);
        especies.add(this.charizard);
        especies.add(this.squirtle);
        return especies;
    }

    private List<CondicionDeEvolucion> listaDeCondiciones() {
        List<CondicionDeEvolucion> condiciones = new ArrayList<>();
        condiciones.add(this.condicion);
        return condiciones;
    }

    private void crearDAOs(){
        this.nivelDAO = new HibernateNivelDAO();
        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.busquedaHelperDAO = new HibernateBusquedaHelperDAO();
        this.condicionDAO = new HibernateCondicionDAO();
    }

    private void crearEntrenadores(){
        this.nivel = new UltimoNivel(10,0, 100);
        this.nivelDAO.guardar(nivel);
        this.ash = new Entrenador("Ash", nivel);
        this.brook = new Entrenador("Brook", nivel);
        this.entrenadorDAO.guardarTodos(this.listaDeEntrenadores());
    }

    private void crearCondiciones(){
        this.condicion = new CondicionBasadaEnEdad(2);
        this.condicionDAO.guardarTodos(this.listaDeCondiciones());
    }

    private void crearEspecies(){
        this.pichu = new Especie("Pichu", ELECTRICIDAD);
        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.raychu = new Especie("Raychu", ELECTRICIDAD);
        this.charizard = new Especie("Charizard", FUEGO);
        this.squirtle = new Especie("Squirtle", AGUA);
        this.picachu.setEvolucionRaiz(this.pichu);
        this.raychu.setEvolucionRaiz(this.pichu);
        this.picachu.setEnergiaInicial(1000);
        this.charizard.setEnergiaInicial(100);
        ArrayList<CondicionDeEvolucion> condiciones = new ArrayList<>();
        condiciones.add(this.condicion);
        this.picachu.setEspecieAEvolucionar(raychu, condiciones);
        this.especieDAO.guardarTodos(this.listaDeEspecies());
    }

    private void crearBichos(){
        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoCharizard1 = new Bicho(this.charizard);
        this.bichoCharizard2 = new Bicho(this.charizard);
        this.bichoSquirtle = new Bicho(this.squirtle);
        this.bichoPicachu.setFechaDeNacimiento(LocalDate.of(2019,10,2));
        this.bichoDAO.guardarTodos(this.listaDeBichos());
    }

    private void crearHelpers(){
        this.busquedaHelper = new BusquedaHelperMock(true,true,true,true,this.picachu);
        this.busquedaHelperDAO.guardar(busquedaHelper);
    }

    private void crearUbicaciones(){
        this.guarderia = new Guarderia(this.busquedaHelper);
        this.dojo = new Dojo(this.busquedaHelper);
        this.pueblo = new Pueblo(this.busquedaHelper);
        this.ubicacionDAO.guardarTodos(this.listaDeUbicaciones());
    }
}