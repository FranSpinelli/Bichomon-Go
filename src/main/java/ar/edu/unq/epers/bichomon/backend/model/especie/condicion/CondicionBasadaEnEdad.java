package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.Entity;

@Entity
public class CondicionBasadaEnEdad extends CondicionDeEvolucion{

	public CondicionBasadaEnEdad(int edad) {
		super(edad);
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getEdad() > this.cantidad;
	}

}
