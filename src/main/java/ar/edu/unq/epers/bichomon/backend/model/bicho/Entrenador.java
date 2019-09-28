package ar.edu.unq.epers.bichomon.backend.model.bicho;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.UbicacionIncorrectaException;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Entrenador {

	@Id
	private int id;
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true)
	private String nombre;
	private int nivel;
	private int xp;
	@OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Bicho> inventarioDeBichos = new HashSet<>();
	@ManyToOne
	private Ubicacion ubicacionActual;

	public Entrenador(){}

	public Entrenador(String nombre){
		this.nombre = nombre;
		this.inventarioDeBichos = new HashSet();
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

	public Set<Bicho> getInventarioDeBichos() {
		return this.inventarioDeBichos;
	}

	public void setInventarioDeBichos(Set<Bicho> inventarioDeBichos) {
		this.inventarioDeBichos = inventarioDeBichos;
	}

	public Ubicacion getUbicacionActual() {
		return this.ubicacionActual;
	}

	public void setUbicacionActual(Ubicacion ubicacionActual) {
		this.ubicacionActual = ubicacionActual;
	}

    public void abandonar(Bicho bicho) throws UbicacionIncorrectaException {
		try {
			ubicacionActual.recibirBicho(bicho);
			this.inventarioDeBichos.remove(bicho);
			bicho.agregarEx(this);
		} catch (UbicacionIncorrectaException e) {
			e.printStackTrace();
		}

		this.ubicacionActual.recibirBicho(bicho);
        bicho.agregarEx(this);
		this.inventarioDeBichos.remove(bicho);
    }

    public Integer getCantidadDeBichos(){

		return this.inventarioDeBichos.size();
	}

	public Boolean tieneBicho(Bicho bicho) {

		return inventarioDeBichos.contains(bicho);
	}
	
	public void capturarBicho(Bicho bicho) {
		bicho.serCapturadoPor(this);
		this.inventarioDeBichos.add(bicho);
	}
	public void addBicho(Bicho bicho) {
		this.inventarioDeBichos.add(bicho);
		bicho.setEntrenador(this);
	}
}
