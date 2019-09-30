package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.*;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.*;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;
import static org.junit.Assert.*;

import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionBasadaEnEdad;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Dojo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Pueblo;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.runner.SessionFactoryProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class BichoServiceImplTest {

    private Entrenador ash;
    private Especie picachu;
    private Especie raychu;
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
    private CondicionDeEvolucion condicion;
    private HibernateDAO<CondicionDeEvolucion> condicionDAO;
    //private CondicionDAO condicionDAO;

    @After
    public void cleanUp() {
        SessionFactoryProvider.destroy();
    }

    @Before
    public void crearModelo(){
        this.ash = new Entrenador("Ash");
        this.picachu = new Especie("Picachu", ELECTRICIDAD);
        this.raychu = new Especie("Raychu", ELECTRICIDAD);
        this.charizard = new Especie("Charizard", FUEGO);
        this.squirtle = new Especie("Squirtle", AGUA);

        ArrayList<CondicionDeEvolucion> condiciones = new ArrayList<>();
        this.condicion = new CondicionBasadaEnEdad(2);
        condiciones.add(this.condicion);
        this.picachu.setEspecieAEvolucionar(raychu, condiciones);

        this.bichoPicachu = new Bicho(this.picachu);
        this.bichoCharizard1 = new Bicho(this.charizard);
        this.bichoCharizard2 = new Bicho(this.charizard);
        this.bichoSquirtle = new Bicho(this.squirtle);

        this.guarderia = new Guarderia();
        this.dojo = new Dojo("Gimnasio Agua");
        this.pueblo = new Pueblo("Paleta");

        this.ubicacionDAO = new HibernateUbicacionDAO();
        this.condicionDAO = new HibernateDAO<CondicionDeEvolucion>(CondicionDeEvolucion.class);
        this.entrenadorDAO = new HibernateEntrenadorDAO();
        this.bichoDAO = new HibernateBichoDAO();
        this.especieDAO = new HibernateEspecieDAO();
        this.bichoService = new BichoServiceImpl(entrenadorDAO, bichoDAO);

        this.ash.setUbicacionActual(this.guarderia);

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

    @Test
    public void testNoPuedeEvolucionar(){
        Entrenador ashe = run(()->this.entrenadorDAO.recuperar("Ash"));
        assertFalse(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testPuedeEvolucionar(){
        Entrenador ashe = run(()->this.entrenadorDAO.recuperar("Ash"));
        bichoPicachu.setEdad(5);
        run(()->this.bichoDAO.guardar(bichoPicachu));
        assertTrue(this.bichoService.puedeEvolucionar("Ash", this.bichoPicachu.getId()));
    }

    @Test
    public void testEvolucionar(){
        Entrenador ashe = run(()->this.entrenadorDAO.recuperar("Ash"));
        Bicho bichoEvolucionado = this.bichoService.evolucionar("Ash", this.bichoPicachu.getId());
        assertEquals(bichoEvolucionado.getEspecie().getNombre(), this.raychu.getNombre());
    }

    @Test
    public void testAbandonar(){

        this.bichoService.abandonar("Ash", this.bichoPicachu.getId());

        Entrenador ashe = run(()->this.entrenadorDAO.recuperar("Ash"));
        Set<Bicho> bichos = ashe.getInventarioDeBichos();
        assertNotEquals(bichos, this.ash.getInventarioDeBichos());
    }

}
