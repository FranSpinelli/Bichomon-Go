package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PuebloTest extends UbicacionTest {

    private Pueblo pueblo;

    @Override
    public void crearEscenario(){
        this.pueblo = new Pueblo();
        super.crearEscenario();
    }

    @Test
    public void testAddEspecieHabitante(){
        assertEquals(this.pueblo.getEspeciesHabitantes().size(), 0);
        this.pueblo.addEspecieHabitante(this.especieEncontrada, 100);
        assertEquals(this.pueblo.getEspeciesHabitantes().size(),1);
    }

    @Override
    protected void definirGeneracionDeBicho() {
        this.pueblo.addEspecieHabitante(this.especieEncontrada, 100);
    }

    @Override
    protected Ubicacion ubicacion() {
        return this.pueblo;
    }

}
