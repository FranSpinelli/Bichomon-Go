package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.data.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;

public class JDBCEspecieDAOTest {
    private EspecieDAO dao = new JDBCEspecieDAO();
    private Especie pacacho;

    @Before
    public void crearModelo() {
        this.dao.eliminarTodos();
        this.pacacho = new Especie("Pacachu", ELECTRICIDAD);
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
        //Compruebo que los atributos coincidan
        assertEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
        assertEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
        assertEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
        assertEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
        assertEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
        assertEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
        assertEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());
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
        //Compruebo que los atributos coincidan
        assertEquals(pacachoRecuperado.getNombre(), pacacho.getNombre());
        assertEquals(pacachoRecuperado.getAltura(), pacacho.getAltura());
        assertEquals(pacachoRecuperado.getPeso(), pacacho.getPeso());
        assertEquals(pacachoRecuperado.getTipo(), pacacho.getTipo());
        assertEquals(pacachoRecuperado.getCantidadBichos(), pacacho.getCantidadBichos());
        assertEquals(pacachoRecuperado.getEnergiaInicial(), pacacho.getEnergiaInicial());
        assertEquals(pacachoRecuperado.getUrlFoto(), pacacho.getUrlFoto());

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


}
