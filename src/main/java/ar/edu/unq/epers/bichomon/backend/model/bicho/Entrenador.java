package ar.edu.unq.epers.bichomon.backend.model.bicho;


import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Guarderia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.UbicacionIncorrectaException;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import javax.persistence.*;

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
	private Set<Bicho> inventarioDeBichos = new HashSet<>();
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

	public void setUbicacionActual(Ubicacion ubicacionActual) {
		this.ubicacionActual = ubicacionActual;
	}

//TODO
    /*public void abandonar(Bicho bicho) throws UbicacionIncorrectaException {
		try {
			ubicacionActual.recibirBicho(bicho);
			this.inventarioDeBichos.remove(bicho);
			bicho.agregarEx(this);
		} catch (UbicacionIncorrectaException e) {
			e.printStackTrace();
		}*/


    public void abandonar(Bicho bicho) {
		this.ubicacionActual.recibirBicho(bicho);
        bicho.agregarEx(this);
		this.inventarioDeBichos.remove(bicho);
    }

    public Bicho buscar(){
	    Bicho bicho = this.ubicacionActual.buscar(this);
        if(bicho != null){
            this.addBicho(bicho);
        }
        return bicho;
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

	public Ubicacion getUbicacionActual() {
		return this.ubicacionActual;
	}

	public ResultadoCombate desafiarCampeonActualCon(Bicho bicho){

		Estrategia dueloHelper = new DueloHelper();
		return this.ubicacionActual.realizarDuelo(bicho,dueloHelper);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entrenador that = (Entrenador) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
