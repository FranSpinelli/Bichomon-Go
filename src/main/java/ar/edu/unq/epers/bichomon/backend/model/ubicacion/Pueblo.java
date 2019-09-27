package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

public class Pueblo extends Ubicacion {
	
	private String nombre;
	
	public Pueblo(String nombre) {
		this.setNombre(nombre);
	}

	@Override
	void buscar() {
		// TODO Auto-generated method stub
		
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}
