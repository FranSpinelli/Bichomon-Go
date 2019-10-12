package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static org.junit.Assert.*;

public abstract class EspecieDAOTest {

    protected EspecieDAO dao;
    protected Especie pacacho;
    private Especie charmandar;
    private Especie charmilian;
    private Especie chorizard;
    private Especie otroPacacho;
    private List<Especie> especies = new ArrayList<Especie>();
    private List<Especie> especiesRecuperadas = new ArrayList<Especie>();
    private Especie pacachoRecuperado;
    private Especie charmandarRecuperado;
    private Especie charmilianRecuperado;
    private Especie chorizardRecuperado;

    @Before
    public void crearModelo() {
        this.definirDAO();
        this.pacacho = crearEspecie("Pacachu", ELECTRICIDAD, 400, 5, "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png", 10);
        this.charmandar = crearEspecie( "Charmandar", FUEGO, 400, 5, "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png", 10);
        this.charmilian = crearEspecie( "Charmilian", FUEGO, 400, 5, "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png", 10);
        this.chorizard = crearEspecie( "Chorizard", FUEGO, 400, 5, "https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png", 10);
    }

    protected abstract void definirDAO();

    @After
    public void limpiarEscenario(){
        this.correr(() -> this.dao.eliminarTodos());
    }

    @Test
    public void al_guardar_y_luego_recuperar_se_obtiene_objetos_similares() {
        this.correr(() -> this.dao.guardar(this.pacacho));
        this.correr(() -> {this.otroPacacho = this.dao.recuperar("Pacachu");});
        assertEquals(this.pacacho.getNombre(), otroPacacho.getNombre());
        assertEquals(this.pacacho.getAltura(), otroPacacho.getAltura());
        assertEquals(this.pacacho.getPeso(), otroPacacho.getPeso());
        assertEquals(this.pacacho.getUrlFoto(), otroPacacho.getUrlFoto());
        assertEquals(this.pacacho.getEnergiaInicial(), otroPacacho.getEnergiaInicial());
        assertEquals(this.pacacho.getTipo(), otroPacacho.getTipo());
        assertEquals(this.pacacho.getCantidadBichos(), otroPacacho.getCantidadBichos());
        assertEquals(this.pacacho.getId(), otroPacacho.getId());

        assertNotSame(this.pacacho, otroPacacho);


    }

//    @Rule
//    public ExpectedException thrown= ExpectedException.none();



    @Test
    public void testActualizarCasoFeliz() {
        this.correr(() -> this.dao.guardar(this.pacacho));
        this.correr(() -> {this.pacachoRecuperado = this.dao.recuperar("Pacachu");});
            this.comprobarEspeciesSimilares(pacacho, pacachoRecuperado);

            //Modifico los atributos de el original
            this.pacacho.setCantidadBichos(20);
            this.pacacho.setNombre("Pacacho");
            this.pacacho.setAltura(10);
            this.pacacho.setPeso(30);
            this.pacacho.setEnergiaInicial(100);
            this.pacacho.setUrlFoto("hola");
            this.pacacho.setTipo(AGUA);
            this.correr(() -> {
                //Compruebo que los atributos no coincidan
                pacachoRecuperado = this.dao.recuperar("Pacachu");
            });

            assertNotEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
            assertNotEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
            assertNotEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
            assertNotEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
            assertNotEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
            assertNotEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
            assertNotEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());

            //Actualizo los datos
            correr(() -> this.dao.actualizar(this.pacacho));
            correr(() -> {pacachoRecuperado = this.dao.recuperar("Pacacho");});
            this.comprobarEspeciesSimilares(pacacho, pacachoRecuperado);

            //==================Caso Feliz==================
            //Guardar
            //Recuperar
            //Comprobar instancias similares
            //Modificar
            //Comprobar instancias no similares
            //Actualizar
            //Comprobar instancias similares

    }

    @Test
    public void testRecuperarTodos(){
        this.correr(() -> {
            this.especiesRecuperadas = this.dao.recuperarTodos();
        });
        //Compruebo que se devuelva una lista vacia cuando la base esta vacia
        assertEquals(especies, this.especiesRecuperadas);
        //Agrego un elemento y compruebo que se devuelve una lista con solo ese elemento
        especies.add(this.charmandar);
        this.correr(() -> this.dao.guardar(this.charmandar));
        this.correr(() -> {
            this.especiesRecuperadas = this.dao.recuperarTodos();
            this.comprobarListasSimilares(especies, especiesRecuperadas);
        });
        //Agrago el resto de especies a la lista de modo que queden en orden alfabetico
        especies.add(this.charmilian);
        especies.add(this.chorizard);
        correr(() -> {
            //Agrego el resto de las especies a la base de modo que no queden ordenadas alfabeticamente
            this.dao.guardar(this.chorizard);
            this.dao.guardar(this.charmilian);
        });
        correr(() -> {
            this.especiesRecuperadas = this.dao.recuperarTodos();
        });
            //Compruebo que recuperarTodos efectivamente devuelve todos ordenados alfabeticamente
            this.comprobarListasSimilares(especies, especiesRecuperadas);
    }

    @Test
    public void testEliminarTodos(){
        this.correr(() -> {
            //Guardo especies
            this.dao.guardar(this.pacacho);
            this.dao.guardar(this.charmandar);
            this.dao.guardar(this.charmilian);
            this.dao.guardar(this.chorizard);
        });
        this.correr(() -> {
            this.pacachoRecuperado = this.dao.recuperar(this.pacacho.getNombre());
            this.charmandarRecuperado = this.dao.recuperar(this.charmandar.getNombre());
            this.charmilianRecuperado = this.dao.recuperar(this.charmilian.getNombre());
            this.chorizardRecuperado = this.dao.recuperar(this.chorizard.getNombre());
        });
        //Compruebo que esten
        this.comprobarEspeciesSimilares(this.pacacho, this.pacachoRecuperado);
        this.comprobarEspeciesSimilares(this.charmandar, this.charmandarRecuperado);
        this.comprobarEspeciesSimilares(this.charmilian, this.charmilianRecuperado);
        this.comprobarEspeciesSimilares(this.chorizard, this.chorizardRecuperado);
        //Elimino todas las especies de la base
        this.correr(() -> this.dao.eliminarTodos());
        this.correr(() -> {
            this.pacachoRecuperado = this.dao.recuperar(this.pacacho.getNombre());
            this.charmandarRecuperado = this.dao.recuperar(this.charmandar.getNombre());
            this.charmilianRecuperado = this.dao.recuperar(this.charmilian.getNombre());
            this.chorizardRecuperado = this.dao.recuperar(this.chorizard.getNombre());
            this.especiesRecuperadas = this.dao.recuperarTodos();
        });
        //Compruebo que no esten
        assertNull(this.pacachoRecuperado);
        assertNull(this.charmilianRecuperado);
        assertNull(this.charmandarRecuperado);
        assertNull(this.chorizardRecuperado);
        //Compruebo que no haya nada
        assertEquals(new ArrayList<Especie>(), this.especiesRecuperadas);

    }
//PRIVATE FUNCTION---------------------------------------------------------------------------------------------

    private void comprobarListasSimilares(List<Especie> especies, List<Especie> otrasEspecies) {
        assertEquals(especies.size(), otrasEspecies.size());
        for(Integer i = 0; i < especies.size(); i++){
            this.comprobarEspeciesSimilares(especies.get(i), otrasEspecies.get(i));
        }
    }

    private void comprobarEspeciesSimilares(Especie especie, Especie otraEspecie) {
        assertEquals(especie.getId(), otraEspecie.getId());
        assertEquals(especie.getNombre(), otraEspecie.getNombre());
        assertEquals(especie.getAltura(), otraEspecie.getAltura());
        assertEquals(especie.getPeso(), otraEspecie.getPeso());
        assertEquals(especie.getTipo(), otraEspecie.getTipo());
        assertEquals(especie.getCantidadBichos(), otraEspecie.getCantidadBichos());
        assertEquals(especie.getEnergiaInicial(), otraEspecie.getEnergiaInicial());
        assertEquals(especie.getUrlFoto(), otraEspecie.getUrlFoto());
        assertNotSame(especie, otraEspecie);
    }

    private Especie crearEspecie(String nombre, TipoBicho tipo, int altura, int peso, String url, int energia) {
        Especie newEspecie;
        newEspecie = new Especie(nombre, tipo);
        newEspecie.setAltura(altura);
        newEspecie.setPeso(peso);
        newEspecie.setUrlFoto(url);
        newEspecie.setEnergiaInicial(energia);
        newEspecie.setCantidadBichos(0);
        return newEspecie;
    }

    protected abstract void correr(Runnable bloque);
}
