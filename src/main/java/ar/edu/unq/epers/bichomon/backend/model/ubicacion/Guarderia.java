package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import java.util.HashSet;

import java.util.Set;

public class Guarderia extends Ubicacion{

    private Set<Bicho> bichosAbandonados;

    public Guarderia(){this.bichosAbandonados = new HashSet();}

    public void recibirBicho(Bicho bichoAbandonado){
        this.bichosAbandonados.add(bichoAbandonado);
    }

	@Override
	void buscar() {
		// TODO Auto-generated method stub
		
	}
}

