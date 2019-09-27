package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.Entity;

@Entity
public class CondicionBasadaEnNivel extends CondicionDeEvolucion{

	public CondicionBasadaEnNivel(){}

	public CondicionBasadaEnNivel(int nivel){
		super(nivel);	
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getNivelDelEntrenador() > this.cantidad;
	}

}
