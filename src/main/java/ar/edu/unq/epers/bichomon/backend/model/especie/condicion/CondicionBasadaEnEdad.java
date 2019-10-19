package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class CondicionBasadaEnEdad extends CondicionDeEvolucion{

	public CondicionBasadaEnEdad(){}

	public CondicionBasadaEnEdad(int edad) {
		super(edad);
	}
	
	@Override
	public boolean puedeEvolucionar(Bicho bicho) {
		return bicho.getEdadConRespectoAlDia(LocalDate.now()) > this.cantidad;
	}

}
