package ar.edu.unq.epers.bichomon.backend.service.especie;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.NivelDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.bicho.UltimoNivel;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class EspecieServiceImplTest {

    EspecieServiceImpl especieService;
    EspecieDAO especieDAO;
    Especie especie1;
    Especie especieRecuperada;
    List<Especie> especies;
    Bicho bichoCreado;
    Transaction hibernateTransaction = new HibernateTransaction();

    @Before
    public void SetUp(){
        especieDAO = Mockito.spy(HibernateEspecieDAO.class);
        especieService = new EspecieServiceImpl(especieDAO);

        especie1 = new Especie("especie1", TipoBicho.FUEGO);
        especie1.setEnergiaInicial(1000);
        especie1.setAltura(100);
        especie1.setPeso(10);
        especie1.setUrlFoto("foto");
    }

    @After
    public void limpiarEscenario(){
        run(SessionFactoryProvider::destroy, this.hibernateTransaction);
    }

    @Test
    public void crearEspecieYRecuperarla() {

        run(() -> {
                especieService.crearEspecie(especie1);
                especieRecuperada = especieService.getEspecie("especie1");
        }, this.hibernateTransaction);

        assertEquals(especie1.getNombre(),especieRecuperada.getNombre());
        assertEquals(especie1.getEnergiaInicial(), especieRecuperada.getEnergiaInicial());
        assertEquals(especie1.getAltura(), especieRecuperada.getAltura());
        assertEquals(especie1.getPeso(), especieRecuperada.getPeso());
        assertEquals(especie1.getUrlFoto(), especieRecuperada.getUrlFoto());
    }

    @Test(expected = EspecieNoExistente.class)
    public void getEspecieCasoNoFeliz() {

        run(() -> {
            especieRecuperada = especieService.getEspecie("especie1");
        }, this.hibernateTransaction);

    }

    @Test
    public void getAllEspecies() {

        assertNull(especies);

        run(() -> {
            especieService.crearEspecie(especie1);
            especies = especieService.getAllEspecies();
        }, this.hibernateTransaction);

        assertEquals(especies.size(),1);

    }

    @Test
    public void crearBicho() {

        run(() -> {
            especieService.crearEspecie(especie1);
            especieRecuperada = especieService.getEspecie("especie1");
        }, this.hibernateTransaction);

        assertEquals(especieRecuperada.getCantidadBichos(),0);

        run(() -> {
           bichoCreado = especieService.crearBicho("especie1");
           especieRecuperada = especieService.getEspecie("especie1");
        }, this.hibernateTransaction);

        assertEquals(especieRecuperada.getCantidadBichos(),1);
        assertEquals(bichoCreado.getEspecie().getNombre(), "especie1");
    }

    @Test
    public void populares() {
        NivelDAO nivelDAO = new HibernateNivelDAO();
        AbstractNivel nivel = new UltimoNivel(10,10,10);
        HibernateBichoDAO bichoDAO = new HibernateBichoDAO();
        HibernateEntrenadorDAO entrenadorDAO = new HibernateEntrenadorDAO();
        Entrenador entrenador1 = new Entrenador("Ash", nivel);
        Bicho bicho1 = new Bicho(especie1);
        entrenador1.addBicho(bicho1);

        run(() -> {
            especieService.crearEspecie(especie1);
            nivelDAO.guardar(nivel);
            bichoDAO.guardar(bicho1);
            entrenadorDAO.guardar(entrenador1);
        }, this.hibernateTransaction);

        run(() -> {
            especies = especieService.populares();
        }, this.hibernateTransaction);

        assertEquals(especies.size(),1);
        assertEquals(especies.get(0),especie1);
    }

    @Test
    public void impopulares() {

        HibernateUbicacionDAO guarderiaDAO= new HibernateUbicacionDAO();
        HibernateBichoDAO bichoDAO = new HibernateBichoDAO();
        Bicho bicho1 = new Bicho(especie1);
        Guarderia guarderia1 = new Guarderia();
        guarderia1.recibirBicho(bicho1);

        run(() -> {
            especieService.crearEspecie(especie1);
            bichoDAO.guardar(bicho1);
            guarderiaDAO.guardar(guarderia1);
        }, this.hibernateTransaction);

        run(() -> {
            especies = especieService.impopulares();
        }, this.hibernateTransaction);

        assertEquals(especies.size(),1);
        assertEquals(especies.get(0),especie1);
    }
}
