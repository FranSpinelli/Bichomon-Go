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
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.assertNotEquals;

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
    private DueloHelper dueloHelper;
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
        this.dueloHelper = new DueloHelper();
        this.dojo = new Dojo(dueloHelper);
        this.pueblo = new Pueblo();

        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.bichoService = new BichoServiceImpl(entrenadorDAO, bichoDAO);
    }

    @Test
    public void testAbandonar(){
        this.ash.setUbicacionActual(this.guarderia);
        run(() -> this.especieDAO.guardar(this.picachu));
        run(() -> this.especieDAO.guardar(this.charizard));
        run(() -> this.especieDAO.guardar(this.squirtle));
        run(() -> this.ubicacionDAO.guardar(this.guarderia));
        run(() -> this.entrenadorDAO.guardar(this.ash));
        this.ash.addBicho(this.bichoPicachu);
        this.ash.addBicho(this.bichoCharizard1);
        this.ash.addBicho(this.bichoCharizard2);
        this.ash.addBicho(this.bichoSquirtle);
        run(() -> this.bichoDAO.guardar(this.bichoPicachu));
        run(() -> this.bichoDAO.guardar(this.bichoCharizard1));
        run(() -> this.bichoDAO.guardar(this.bichoCharizard2));
        run(() -> this.bichoDAO.guardar(this.bichoSquirtle));


        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());

        Set<Bicho> bichos = new HashSet<Bicho>();
        assertNotEquals(bichos, this.ash.getInventarioDeBichos());
    }
}
