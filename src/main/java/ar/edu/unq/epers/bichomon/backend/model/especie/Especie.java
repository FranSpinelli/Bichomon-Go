package ar.edu.unq.epers.bichomon.backend.model.especie;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.condicion.CondicionDeEvolucion;

import javax.persistence.*;

/**
 * Representa una {@link Especie} de bicho.
 * 
 * @author Charly Backend
 */

@Entity
public class Especie {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(unique = true)
	private String nombre;
	private int altura;
	private int peso;
	private TipoBicho tipo;
	private int energiaInicial;
	private String urlFoto;
	private int cantidadBichos;
	@OneToOne
	private Especie especieAEvolucionar;
	@OneToMany(fetch = FetchType.EAGER) //TODO: Suponiendo que la misma condicion(con mismo id) no puede ser compartida entre distintas especies
	private List<CondicionDeEvolucion> condicion;
	@ManyToOne
	private Especie evolucionRaiz;

	public Especie(){}

	public Especie(String nombre, TipoBicho tipo) {

		this.nombre = nombre;
		this.tipo = tipo;
		this.especieAEvolucionar = null;
		this.setEvolucionRaiz(this);
	}

	/**
	 * @return el nombre de la especie (por ejemplo: Perromon)
	 */
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * @return la altura de todos los bichos de esta especie
	 */
	public int getAltura() {
		return this.altura;
	}
	public void setAltura(int altura) {
		this.altura = altura;
	}
	
	/**
	 * @return el peso de todos los bichos de esta especie
	 */
	public int getPeso() {
		return this.peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	/**
	 * @return una url que apunta al un recurso imagen el cual será
	 * utilizado para mostrar un thumbnail del bichomon por el frontend.
	 */
	public String getUrlFoto() {
		return this.urlFoto;
	}
	public void setUrlFoto(String urlFoto) {
		this.urlFoto = urlFoto;
	}
	
	/**
	 * @return la cantidad de energia de poder iniciales para los bichos
	 * de esta especie.
	 */
	public int getEnergiaInicial() {
		return this.energiaInicial;
	}
	public void setEnergiaInicial(int energiaInicial) {
		this.energiaInicial = energiaInicial;
	}

	/**
	 * @return el tipo de todos los bichos de esta especie
	 */
	public TipoBicho getTipo() {
		return this.tipo;
	}
	public void setTipo(TipoBicho tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * @return la cantidad de bichos que se han creado para esta
	 * especie.
	 */
	public int getCantidadBichos() {
		return this.cantidadBichos;
	}
	public void setCantidadBichos(int i) {
		this.cantidadBichos = i;
	}

	public Bicho crearBicho(){
		this.cantidadBichos++;
		return new Bicho(this);
	}

	/**
	 * @return el id de la especie
	 */

	public int getId(){
		return this.id;
	}

	public void setId(int nuevoId){
		this.id=nuevoId;
	}

	public void evolucionar(Bicho bicho) {
		if (this.especieAEvolucionar != null && this.puedeEvolucionar(bicho)) {
			bicho.setEspecie(this.especieAEvolucionar);
		}
		
	}

	private boolean puedeEvolucionar(Bicho bicho) {
		return this.condicion.stream().allMatch(condition ->condition.puedeEvolucionar(bicho));
	}

	public void setCondicion(ArrayList<CondicionDeEvolucion> condicion) {
		this.condicion = condicion;
	}

	public Especie getEvolucionRaiz() {
		return evolucionRaiz;
	}

	public void setEvolucionRaiz(Especie evolucionRaiz) {
		
		this.evolucionRaiz = evolucionRaiz;
	}
	
	public void setEspecieAEvolucionar(Especie especie, ArrayList<CondicionDeEvolucion> condicion) {
		this.especieAEvolucionar = especie;
		this.setCondicion(condicion);
		especie.setEvolucionRaiz(this.evolucionRaiz);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Especie especie = (Especie) o;
		return id == especie.id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}
}


