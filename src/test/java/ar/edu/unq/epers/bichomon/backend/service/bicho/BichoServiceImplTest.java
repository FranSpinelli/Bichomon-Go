package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.UbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateBichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateUbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.*;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
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
        this.dojo = new Dojo("Gimnasio Agua");
        this.pueblo = new Pueblo("Paleta");

        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.bichoService = new BichoServiceImpl(entrenadorDAO, bichoDAO);

        this.ash.setUbicacionActual(this.guarderia);

        run(() -> {
            this.especieDAO.guardar(this.picachu);
            this.especieDAO.guardar(this.charizard);
            this.especieDAO.guardar(this.squirtle);
            this.ubicacionDAO.guardar(this.guarderia);
            this.entrenadorDAO.guardar(this.ash);

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

    @Test
    public void testAbandonar(){

        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());

        Entrenador ashe = run(()->this.entrenadorDAO.recuperar("Ash"));
        Set<Bicho> bichos = ashe.getInventarioDeBichos();
        assertNotEquals(bichos, this.ash.getInventarioDeBichos());
    }

    @Test
    public void testNoPuedeEvolucionar(){
        assertFalse(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testPuedeEvolucionar(){
        this.bichoDAO.recuperar(this.bichoPicachu.getId()).setEdad(5);
        assertTrue(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testEvolucionar(){
        Bicho bichoEvolucionado = this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
        assertTrue(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

}
