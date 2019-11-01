package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import org.junit.Test;

import java.util.List;

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
        this.pueblo.addEspecieHabitante(this.especieEncontrada1, 100);
        assertEquals(this.pueblo.getEspeciesHabitantes().size(),1);
    }

    @Override
    protected void definirGeneracionDeBicho() {
        this.pueblo.addEspecieHabitante(this.especieEncontrada1, 70);
        this.pueblo.addEspecieHabitante(this.especieEncontrada2, 30);
    }

    @Override
    protected List<Especie> posiblesEspeciesEncontradas() {
        List<Especie> especies = super.posiblesEspeciesEncontradas();
        especies.add(this.especieEncontrada2);
        return especies;
    }

    @Override
    protected Ubicacion ubicacion() {
        return this.pueblo;
    }

}
