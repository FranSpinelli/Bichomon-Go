package ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.RelacionadoADojo.Campeon;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;

public class CampeonTest {

    Bicho bichoMock1;
    LocalDate fecha;
    Campeon campeon;

    @Before
    public void setUp(){
        bichoMock1 = Mockito.mock(Bicho.class);
        fecha = LocalDate.now();
        campeon = new Campeon(bichoMock1, fecha);
    }

    @Test
    public void getBicho() {
        assertEquals(campeon.getBicho(), bichoMock1);
    }

    @Test
    public void getFechaDeInicio() {
        assertEquals(campeon.getFechaDeInicio(), fecha);
    }

    @Test
    public void getFechaDeFin() {
        assertEquals(campeon.getFechaDeFin(), null);
    }

    @Test
    public void setFechaDeFin() {
        assertEquals(campeon.getFechaDeFin(), null);
        campeon.setFechaDeFin(fecha);
        assertEquals(campeon.getFechaDeFin(),fecha);
    }
}
