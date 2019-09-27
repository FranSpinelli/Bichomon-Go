package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.ArrayList;
import java.util.List;



public class Guarderia extends Ubicacion{
	
	
    private String nombre;
	
    private List<Bicho> bichosAbandonados;

    public Guarderia(){
    	this.bichosAbandonados = new ArrayList<Bicho>();
    }

    @Override
    public void recibirBicho(Bicho bichoAbandonado){

        this.bichosAbandonados.add(bichoAbandonado);
    }

	@Override
	void buscar() {
		// TODO Auto-generated method stub
		
	}
}
