package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class GuarderiaTest extends UbicacionTest {

    Guarderia guarderia;
    Bicho bichoMock1;
    Bicho bichoMock2;

    @Override
    @Before
    public void crearEscenario(){
        guarderia = new Guarderia();
        super.crearEscenario();
        bichoMock1 = Mockito.mock(Bicho.class);
        bichoMock2 = Mockito.mock(Bicho.class);
    }

    @Override
    @Test
    public void testRecibirBicho() {

        assertEquals(guarderia.getBichosEnGuarderia().size(), 0);
        guarderia.recibirBicho(bichoMock1);
        assertEquals(guarderia.getBichosEnGuarderia().size(),1);
    }

    @Override
    protected Ubicacion ubicacion() {
        return this.guarderia;
    }

    @Override
    protected void definirGeneracionDeBicho() {
        when(this.bichoMock1.getEspecie()).thenReturn(this.especieEncontrada1);
        when(this.bichoMock1.tieneExDuenio(this.entrenador)).thenReturn(false);
        when(this.bichoMock2.getEspecie()).thenReturn(this.especieEncontrada2);
        when(this.bichoMock2.tieneExDuenio(this.entrenador)).thenReturn(true);
        this.guarderia.recibirBicho(this.bichoMock1);
        this.guarderia.recibirBicho(this.bichoMock2);
    }

    @Override
    protected void definirGeneracionDeBichoImposibilitada(){
        when(this.bichoMock1.getEspecie()).thenReturn(this.especieEncontrada1);
        when(this.bichoMock1.tieneExDuenio(this.entrenador)).thenReturn(true);
        this.guarderia.recibirBicho(this.bichoMock1);
    }


}