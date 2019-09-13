package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.data.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;

public class JDBCEspecieDAOTest {
    private EspecieDAO dao = new JDBCEspecieDAO();
    private Especie pacacho;

    @Before
    public void crearModelo() {
        this.dao.eliminarTodos();
        this.pacacho = new Especie(0,"Pacachu", ELECTRICIDAD);
        this.pacacho.setAltura(400);//en cm
        this.pacacho.setPeso(5);
        this.pacacho.setUrlFoto("https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png");
        this.pacacho.setEnergiaIncial(10);
        this.pacacho.setCantidadBichos(0);
    }

    @Test
    public void testActualizar() {
        try{
            this.dao.actualizar(this.pacacho);
            fail("Deberia lanzar RuntimeException");
        }catch(RuntimeException ex){
            assertEquals("No existe el personaje " + pacacho,ex.getMessage());
        }

        this.dao.guardar(this.pacacho);
        Especie pacachoRecuperado = this.dao.recuperar("Pacachu");
        this.comprobarEspeciesSimilares(pacacho, pacachoRecuperado);

        //Compruebo que los atributos coincidan
        /*
        assertEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
        assertEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
        assertEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
        assertEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
        assertEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
        assertEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
        assertEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());
        */

        //Modifico los atributos de el original
        this.pacacho.setCantidadBichos(20);
        this.pacacho.setNombre("Pacacho");
        this.pacacho.setAltura(10);
        this.pacacho.setPeso(30);
        this.pacacho.setEnergiaIncial(100);
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

        //Compruebo que los atributos coincidan
        /*
        assertEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
        assertEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
        assertEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
        assertEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
        assertEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
        assertEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
        assertEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());
        */
        //==================Caso Feliz==================
        //Guardar
        //Recuperar
        //Comprobar instancias similares
        //Modificar
        //Comprobar instancias no similares
        //Actualizar
        //Comprobar instancias similares
        //=================Caso No Feliz================
        //Modificar
        //Actualizar
        //Comprobar excepcion


    }

    @Test
    public void testRecuperarTodos(){
        //Crear lista vacia
        //RecuperarTodos
        //Comparar que sean iguales
        //Crear y Guardar una especie
        //Agregar especie a la lista
        //RecuperarTodos y comparar que sean listas similares
        //Crear y Guardar mas especies
        //Agragar las especies a la lista
        //RecuperarTodos y comparar que sean listas similares
        Especie charmandar = new Especie(1, "Charmandar", FUEGO);
        Especie charmilian = new Especie(2, "Charmilian", FUEGO);
        Especie chorizard = new Especie(3, "Chorizard", FUEGO);
        List<Especie> especies = new ArrayList<Especie>();

        assertEquals(especies, this.dao.recuperarTodos());

        especies.add(charmandar);
        this.dao.guardar(charmandar);
        this.comprobarListasSimilares(especies, this.dao.recuperarTodos());

        especies.add(charmilian);
        especies.add(chorizard);
        this.dao.guardar(charmilian);
        this.dao.guardar(chorizard);
        this.comprobarListasSimilares(especies, this.dao.recuperarTodos());
    }

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
