package ar.edu.unq.epers.bichomon.backend.model.especie.condicion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class CondicionDeEvolucion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	protected int cantidad;
	
	public CondicionDeEvolucion(int cantidad){
		this.cantidad = cantidad;
	}

	public abstract boolean puedeEvolucionar(Bicho bicho);

	public int getId(){
		return this.id;
	}
	 public void setId(int id){
		this.id = id;
	 }

	public int getCantidad(){
		return this.cantidad;
	}
	public void setCantidad(int cantidad){
		this.cantidad = cantidad;
	}

}
