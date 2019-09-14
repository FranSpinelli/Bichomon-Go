package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.data.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;

public class JDBCEspecieDAOTest {
    private EspecieDAO dao = new JDBCEspecieDAO();
    private Especie pacacho;
    private Especie charmandar;
    private Especie charmilian;
    private Especie chorizard;

    @Before
    public void crearModelo() {
        this.dao.eliminarTodos();
        this.pacacho = new Especie("Pacachu", ELECTRICIDAD);
        this.pacacho.setAltura(400);//en cm
        this.pacacho.setPeso(5);
        this.pacacho.setUrlFoto("https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png");
        this.pacacho.setEnergiaInicial(10);
        this.pacacho.setCantidadBichos(0);
        this.charmandar = new Especie( "Charmandar", FUEGO);
        this.charmilian = new Especie( "Charmilian", FUEGO);
        this.chorizard = new Especie( "Chorizard", FUEGO);
    }

    @Test
    public void al_guardar_y_luego_recuperar_se_obtiene_objetos_similares() {
        this.dao.guardar(this.pacacho);

        //Los personajes son iguales
        Especie otroPacacho = this.dao.recuperar("Pacachu");
        assertEquals(this.pacacho.getNombre(), otroPacacho.getNombre());
        assertEquals(this.pacacho.getAltura(), otroPacacho.getAltura());
        assertEquals(this.pacacho.getPeso(), otroPacacho.getPeso());
        assertEquals(this.pacacho.getUrlFoto(), otroPacacho.getUrlFoto());
        assertEquals(this.pacacho.getEnergiaInicial(), otroPacacho.getEnergiaInicial());
        assertEquals(this.pacacho.getTipo(), otroPacacho.getTipo());
        assertEquals(this.pacacho.getCantidadBichos(), otroPacacho.getCantidadBichos());
        assertEquals(this.pacacho.getId(), otroPacacho.getId());

        //Pero no son el mismo objeto =(
        //A esto nos referimos con "perdida de identidad"

        assertTrue(this.pacacho != otroPacacho);

        this.dao.eliminarTodos();
    }

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @Test
    public void testActualizarCasoNoFeliz(){
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("No existe el personaje " + pacacho);
        this.dao.actualizar(this.pacacho);
        //=================Caso No Feliz================
        //Modificar -> En el crearModelo()
        //Actualizar
        //Comprobar excepcion
    }

    @Test
    public void testActualizarCasoFeliz() {

        this.dao.guardar(this.pacacho);
        Especie pacachoRecuperado = this.dao.recuperar("Pacachu");
        this.comprobarEspeciesSimilares(pacacho, pacachoRecuperado);

        //Modifico los atributos de el original
        this.pacacho.setCantidadBichos(20);
        this.pacacho.setNombre("Pacacho");
        this.pacacho.setAltura(10);
        this.pacacho.setPeso(30);
        this.pacacho.setEnergiaInicial(100);
        this.pacacho.setUrlFoto("hola");
        this.pacacho.setTipo(AGUA);
        //Compruebo que los atributos no coincidan
        pacachoRecuperado = this.dao.recuperar("Pacachu");
        assertNotEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
        assertNotEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
        assertNotEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
        assertNotEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
        assertNotEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
        assertNotEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
        assertNotEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());

        //Actualizo los datos
        this.dao.actualizar(this.pacacho);
        pacachoRecuperado = this.dao.recuperar("Pacacho");
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
        List<Especie> especies = new ArrayList<Especie>();

        assertEquals(especies, this.dao.recuperarTodos());

        especies.add(this.charmandar);
        this.dao.guardar(this.charmandar);
        List<Especie> especies2 = this.dao.recuperarTodos();
        this.comprobarListasSimilares(especies, especies2);

        especies.add(this.charmilian);
        especies.add(this.chorizard);
        this.dao.guardar(this.charmilian);
        this.dao.guardar(this.chorizard);
        this.comprobarListasSimilares(especies, this.dao.recuperarTodos());
    }

    @Test
    public void testEliminarTodos(){
        //Guardo especies
        this.dao.guardar(this.pacacho);
        this.dao.guardar(this.charmandar);
        this.dao.guardar(this.charmilian);
        this.dao.guardar(this.chorizard);
        //Compruebo que esten
        this.comprobarEspeciesSimilares(this.pacacho, this.dao.recuperar(this.pacacho.getNombre()));
        this.comprobarEspeciesSimilares(this.charmandar, this.dao.recuperar(this.charmandar.getNombre()));
        this.comprobarEspeciesSimilares(this.charmilian, this.dao.recuperar(this.charmilian.getNombre()));
        this.comprobarEspeciesSimilares(this.chorizard, this.dao.recuperar(this.chorizard.getNombre()));
        //Elimino todas las especies de la base
        this.dao.eliminarTodos();
        //Compruebo que no esten
        assertNull(this.dao.recuperar(this.pacacho.getNombre()));
        assertNull(this.dao.recuperar(this.charmilian.getNombre()));
        assertNull(this.dao.recuperar(this.charmandar.getNombre()));
        assertNull(this.dao.recuperar(this.chorizard.getNombre()));
        //Compruebo que no haya nada
        assertEquals(new ArrayList<Especie>(), this.dao.recuperarTodos());
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
        assertNotEquals(especie, otraEspecie);
    }
}
