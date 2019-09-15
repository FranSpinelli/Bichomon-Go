package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class CondicionBasadaEnNivel extends CondicionDeEvolucion{
	
	public CondicionBasadaEnNivel(int nivel){
		super(nivel);	
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getEntrenador().getNivel() > this.cantidad;
	}

}
