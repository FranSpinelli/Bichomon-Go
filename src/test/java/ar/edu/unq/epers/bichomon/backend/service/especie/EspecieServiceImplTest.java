package ar.edu.unq.epers.bichomon.backend.service.especie;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateBichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateUbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
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
        run(SessionFactoryProvider::destroy);
    }

    @Test
    public void crearEspecieYRecuperarla() {

        run(() -> {
                especieService.crearEspecie(especie1);
                especieRecuperada = especieService.getEspecie("especie1");
        });

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
        });

    }

    @Test
    public void getAllEspecies() {

        assertNull(especies);

        run(() -> {
            especieService.crearEspecie(especie1);
            especies = especieService.getAllEspecies();
        });

        assertEquals(especies.size(),1);

    }

    @Test
    public void crearBicho() {

        run(() -> {
            especieService.crearEspecie(especie1);
            especieRecuperada = especieService.getEspecie("especie1");
        });

        assertEquals(especieRecuperada.getCantidadBichos(),0);

        run(() -> {
           bichoCreado = especieService.crearBicho("especie1");
           especieRecuperada = especieService.getEspecie("especie1");
        });

        assertEquals(especieRecuperada.getCantidadBichos(),1);
        assertEquals(bichoCreado.getEspecie().getNombre(), "especie1");
    }

    @Test
    public void populares() {

        HibernateBichoDAO bichoDAO = new HibernateBichoDAO();
        HibernateEntrenadorDAO entrenadorDAO = new HibernateEntrenadorDAO();
        Entrenador entrenador1 = new Entrenador("Ash");
        Bicho bicho1 = new Bicho(especie1);
        entrenador1.addBicho(bicho1);

        run(() -> {
            especieService.crearEspecie(especie1);
            bichoDAO.guardar(bicho1);
            entrenadorDAO.guardar(entrenador1);
        });

        run(() -> {
            especies = especieService.populares();
        });

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
        });

        run(() -> {
            especies = especieService.impopulares();
        });

        assertEquals(especies.size(),1);
        assertEquals(especies.get(0),especie1);
    }
}
