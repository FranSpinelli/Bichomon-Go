package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GuarderiaTest extends UbicacionTest {

    Guarderia guarderia;
    Bicho bichoMock;

    @Override
    @Before
    public void crearEscenario(){
        guarderia = new Guarderia();
        super.crearEscenario();
        bichoMock = Mockito.mock(Bicho.class);
    }

    @Override
    @Test
    public void testRecibirBicho() {

        assertEquals(guarderia.getBichosEnGuarderia().size(), 0);
        guarderia.recibirBicho(bichoMock);
        assertEquals(guarderia.getBichosEnGuarderia().size(),1);
    }

    @Override
    protected Ubicacion ubicacion() {
        return this.guarderia;
    }

    @Override
    protected void definirGeneracionDeBicho() {
        when(this.bichoMock.getEspecie()).thenReturn(this.especieEncontrada);
        this.guarderia.recibirBicho(this.bichoMock);
    }
}
