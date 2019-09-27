package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Entrenador {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	private String nombre;
	private int nivel;
	private int xp;
	@OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Bicho> inventarioDeBichos;
	@ManyToOne
	private Ubicacion ubicacionActual;

	public Entrenador(){}

	public Entrenador(String nombre){
		this.nombre = nombre;
		this.inventarioDeBichos = new HashSet();
		this.xp = 0;
		this.nivel = 0;
		/* todo: falta ver como setear la ubicacion */
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getNivel() {
		return this.nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public int getXp() {
		return this.xp;
	}

	public void setXp(int xp) {
		this.xp = xp;
	}

	public void addXp(int xpAAgregar){
		this.xp = xp + xpAAgregar;
	}

	public Set<Bicho> getInventarioDeBichos() {
		return this.inventarioDeBichos;
	}

	public void setInventarioDeBichos(Set<Bicho> inventarioDeBichos) {
		this.inventarioDeBichos = inventarioDeBichos;
	}

	public void abandonar(Bicho bicho) {

		this.ubicacionActual.recibirBicho(bicho);
		this.inventarioDeBichos.remove(bicho);
		bicho.agregarEx(this);
    }

    public Integer getCantidadDeBichos(){

		return this.inventarioDeBichos.size();
	}

	public Boolean tieneBicho(Bicho bicho) {

		return inventarioDeBichos.contains(bicho);
	}

	public void addBicho(Bicho bicho) {

		bicho.setEntrenador(this);
		this.inventarioDeBichos.add(bicho);
	}

	public Ubicacion getUbicacionActual() {
		return this.ubicacionActual;
	}

	public void setUbicacionActual(Ubicacion ubicacionActual) {
		this.ubicacionActual = ubicacionActual;
	}

	public void desafiarCampeonActualCon(Bicho bicho){
		this.ubicacionActual.realizarDuelo(bicho);
	}
}
