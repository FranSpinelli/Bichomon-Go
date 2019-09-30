package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.Entity;

@Entity
public class CondicionBasadaEnEdad extends CondicionDeEvolucion{

	public CondicionBasadaEnEdad(){}

	public CondicionBasadaEnEdad(int edad) {
		super(edad);
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		System.out.println(this.cantidad);
		System.out.println(bicho.getEdad());
		System.out.println(bicho.getEdad() > this.cantidad);
		return bicho.getEdad() > this.cantidad;
	}

}
