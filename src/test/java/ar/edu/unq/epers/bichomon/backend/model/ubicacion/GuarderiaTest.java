package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class GuarderiaTest {

    Guarderia guarderia;
    Bicho bichoMock;

    @Before
    public void setUp(){
        guarderia = new Guarderia("guarderia");
        bichoMock = Mockito.mock(Bicho.class);
    }

    @Test
    public void recibirBicho() {

        assertEquals(guarderia.getBichosEnGuarderia().size(), 0);
        guarderia.recibirBicho(bichoMock);
        assertEquals(guarderia.getBichosEnGuarderia().size(),1);
    }
}
