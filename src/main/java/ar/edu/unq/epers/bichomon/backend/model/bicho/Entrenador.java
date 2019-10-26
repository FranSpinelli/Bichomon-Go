package ar.edu.unq.epers.bichomon.backend.model.bicho;


import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.DueloHelper;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoAjeno;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
	private int xp;
	@OneToMany(mappedBy = "entrenador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private Set<Bicho> inventarioDeBichos = new HashSet<>();
	@ManyToOne
	private Ubicacion ubicacionActual;
	@ManyToOne
	private AbstractNivel nivel;

	public Entrenador(){}

	public Entrenador(String nombre, AbstractNivel nivel){
		this.nombre = nombre;
		this.inventarioDeBichos = new HashSet();
		this.xp = 0;
		this.nivel = nivel;
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
		return this.nivel.getNivel();
	}

	public void setNivel(AbstractNivel nivel) {
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

	public void setUbicacionActual(Ubicacion ubicacionActual) {
		this.ubicacionActual = ubicacionActual;
	}

    public void abandonar(Bicho bicho) {
        if(! this.tieneBicho(bicho)){ throw new BichoAjeno("No se pueden abandonar bichos ajenos"); }
        if(!(this.getCantidadDeBichos() > 1)){ throw new BichosInsuficientes("El entrenador debe tener al menos un bicho luego de abandonar"); }
        this.ubicacionActual.recibirBicho(bicho);
        bicho.agregarEx(this);
		this.inventarioDeBichos.remove(bicho);
    }

    public void addXp(int experienciaGanada){
		this.setXp(this.getXp() + experienciaGanada);
		this.setNivel(this.nivel.eval(this.getXp()));
	}

    public Bicho buscar(){
		if (this.inventarioDeBichos.size() >= this.maximoNumeroDeBichos()){
			throw new InventarioCompleto("Tu inventario de bichos esta al maximo");}
		Bicho bicho = this.ubicacionActual.buscar(this);
        this.addBicho(bicho);
        return bicho;
	}

	private int maximoNumeroDeBichos() {
		return this.nivel.getNroBichos();
	}

	public Integer getCantidadDeBichos(){
		return this.inventarioDeBichos.size();
	}

	public Boolean tieneBicho(Bicho bicho) {
		return inventarioDeBichos.contains(bicho);
	}

	public void addBicho(Bicho bicho) {
		this.inventarioDeBichos.add(bicho);
		bicho.setEntrenador(this);
	}

	public Ubicacion getUbicacionActual() {
		return this.ubicacionActual;
	}

	public ResultadoCombate desafiarCampeonActualCon(Bicho bicho){
        if (!this.tieneBicho(bicho)) { throw new BichoAjeno("No se puede retar a duelo con un bicho ajeno"); }
		Estrategia dueloHelper = new DueloHelper();
		return this.ubicacionActual.realizarDuelo(bicho,dueloHelper);
	}

	public Bicho hacerEvolucionar(Bicho bicho) {
		if (!this.tieneBicho(bicho)) { throw new BichoAjeno("No se puede hacer evolucionar a un bicho ajeno"); }
		return bicho.evolucionar();
	}

	public Boolean estaEn(Ubicacion ubicacion) {
		return ubicacion.equals(this.ubicacionActual);
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
