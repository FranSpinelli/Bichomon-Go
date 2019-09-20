package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

/**
 * Un {@link Bicho} existente en el sistema, el mismo tiene un nombre
 * y pertenece a una {@link Especie} en particular.
 * 
 * @author Charly Backend
 */
public class Bicho {

	private Especie especie;
	private int energia;
	private int edad;
	private int cantidadDeVictorias;
	private Entrenador entrenador;
	
	public Bicho(Especie especie) {
		this.especie = especie;
		this.edad = 0;
		this.cantidadDeVictorias = 0;
		this.energia = especie.getEnergiaInicial();
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

	public void evolucionar() {
		this.especie.evolucionar(this);
	}

	public int getEdad() {
		return this.edad;
	}

	public int getVictorias() {
		// TODO Auto-generated method stub
		return this.cantidadDeVictorias;
	}

	public Entrenador getEntrenador() {
		return this.entrenador;
	}

	public void setEntrenador(Entrenador entrenador) {
		this.entrenador = entrenador;
	}

	public int getNivelDelEntrenador() {
		// TODO Auto-generated method stub
		return this.entrenador.getNivel();
	}

}
