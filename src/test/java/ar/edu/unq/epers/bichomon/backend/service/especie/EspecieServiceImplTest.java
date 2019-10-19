package ar.edu.unq.epers.bichomon.backend.service.especie;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class EspecieServiceImplTest {

    EspecieServiceImpl especieService;
    EspecieDAO especieDAOMock;
    Especie especieMock;

    @Before
    public void SetUp(){

        especieDAOMock = Mockito.mock(HibernateEspecieDAO.class);
        especieService = new EspecieServiceImpl(especieDAOMock);

        especieMock = Mockito.mock(Especie.class);

        /*
        * mockeo el especie dao, ya que en esta test suite solo chequeo que el service delegue
        * correctamente la tarea al dao (que ese es su comportamiento) las queries a la BD se realizan
        * en la test suite del HibernateEspecieDao (que esa es su tarea, realizar las queries a la bd)
        */
    }

    @Test
    public void crearEspecie() {

        especieService.crearEspecie(especieMock);
        Mockito.verify(especieDAOMock).guardar(especieMock);
    }

    @Test
    public void getEspecie() {

        Mockito.doReturn(especieMock).when(especieDAOMock).recuperar("pikachu");

        Especie especie = especieService.getEspecie("pikachu");

        Mockito.verify(especieDAOMock).recuperar("pikachu");
        assertEquals(especieMock, especie);
    }

    @Test(expected = EspecieNoExistente.class)
    public void getEspecieCasoNoFeliz() {

        Mockito.doReturn(null).when(especieDAOMock).recuperar("pikachu");

        Especie especie = especieService.getEspecie("pikachu");

        /*se espera que se lanze una exception*/
    }

    @Test
    public void getAllEspecies() {

        List<Especie> especies = especieService.getAllEspecies();

        Mockito.verify(especieDAOMock).recuperarTodos();
    }

    @Test
    public void crearBicho() {

        Bicho bichoMock = Mockito.mock(Bicho.class);

        Mockito.when(especieDAOMock.recuperar("pikachu")).thenReturn(especieMock);
        Mockito.when(especieMock.crearBicho()).thenReturn(bichoMock);

        especieService.crearBicho("pikachu");

        Mockito.verify(especieDAOMock).recuperar("pikachu");
        Mockito.verify(especieMock).crearBicho();
    }

    @Test
    public void populares() {

        List<Especie> especies = especieService.populares();

        Mockito.verify(especieDAOMock).getMasPopulares();

    }

    @Test
    public void impopulares() {

        List<Especie> especies = especieService.impopulares();

        Mockito.verify(especieDAOMock).getMasImpopulares();
    }
}
