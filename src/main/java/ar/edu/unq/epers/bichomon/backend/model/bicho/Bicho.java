package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Set;


/**
 * Un {@link Bicho} existente en el sistema, el mismo tiene un nombre
 * y pertenece a una {@link Especie} en particular.
 * 
 * @author Charly Backend
 */
@Entity
public class Bicho {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Entrenador> exDuenhos = new HashSet<>();
	@ManyToOne
	private Especie especie;

	private int energia;
	private LocalDate fechaDeNacimiento;
	private int cantidadDeVictorias;
	@ManyToOne(cascade = CascadeType.ALL)
	private Entrenador entrenador;

	public Bicho(){}

	public Bicho(Especie especie) {
		this.especie = especie;
		this.cantidadDeVictorias = 0;
		this.energia = especie.getEnergiaInicial();
		this.fechaDeNacimiento = LocalDate.now();
	}

	/**
	 * @return la especie a la que este bicho pertenece.
	 */
	public Especie getEspecie() {
		return this.especie;
	}
	
	public void setEspecie(Especie newEspecie) {
		this.especie = newEspecie;
	}
	/**
	 * @return la cantidad de puntos de energia de este bicho en
	 * particular. Dicha cantidad crecerá (o decrecerá) conforme
	 * a este bicho participe en combates contra otros bichomones.
	 */
	public int getEnergia() {
		return this.energia;
	}
	public void setEnergia(int energia) {
		this.energia = energia;
	}

	public int getEdad(){
		Long edadEnDias = ChronoUnit.DAYS.between(fechaDeNacimiento, LocalDate.now());
		return edadEnDias.intValue();
	}

	public LocalDate getFechaDeNacimiento() {
		return this.fechaDeNacimiento;
	}

	public void setFechaDeNacimiento(LocalDate fechaDeNacimiento) {this.fechaDeNacimiento = fechaDeNacimiento;}

	public int getVictorias() {
		return this.cantidadDeVictorias;
	}

	public Set<Entrenador> getExDuenhos() {
		return this.exDuenhos;
	}

	public void setExDuenhos(Set<Entrenador> exDuenhos) {
		this.exDuenhos = exDuenhos;
	}

	public Entrenador getEntrenador() {
		return this.entrenador;
	}

	public void setEntrenador(Entrenador nentrenador) {
		this.entrenador = nentrenador;
	}

	public int getNivelDelEntrenador() {
		return this.entrenador.getNivel();
	}

	public int getId(){
		return this.id;
	}

	public Set<Entrenador> getExEntrenadores() {
		return this.exDuenhos;
	}

	public void agregarEx(Entrenador nuevoExDuenho){
		this.entrenador = null;
		this.exDuenhos.add(nuevoExDuenho);
	}

	public void evolucionar() {
		this.especie.evolucionar(this);
	}

	public boolean puedeEvolucionar() {
		return this.especie.puedeEvolucionar(this);
	}

	public void serCapturadoPor(Entrenador entrenador) {this.entrenador = entrenador;}

	@Override
	public int hashCode(){
		int result = 145;
		result = 31 * result + this.id;
		return result;
	}

	@Override
	public boolean equals(Object o){
		return o instanceof Bicho && ((Bicho) o).getId() == this.id;
	}

}
