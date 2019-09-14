package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.data.*;
import org.junit.Before;
import org.junit.Test;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BichoTest {

    public Especie especie;
    public Especie otraEspecie;
    public Bicho bicho;
	
    @Before
	public void setUp() {
    	especie = mock(Especie.class);
    	otraEspecie = mock(Especie.class);
    	bicho= new Bicho(especie);
    }
    @Test
    public void unBichoMandaElMensajeEvolucionarASuEspecie(){
        bicho.evolucionar();
        verify(especie).evolucionar(bicho);
        
    }
    
    @Test
    public void unBichoCambiaDeUnaEspecieAOtra(){
        bicho.setEspecie(otraEspecie);
        assertEquals(otraEspecie, bicho.getEspecie());
        
    }
    
    @Test
    public void unBichoConoceASuEntrenador() {
    	
    }
}
