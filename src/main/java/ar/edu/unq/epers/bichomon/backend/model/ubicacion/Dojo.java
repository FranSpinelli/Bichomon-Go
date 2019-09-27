package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class Dojo extends Ubicacion {

	private String nombre;
	private Bicho campeon;
	
	public Dojo(String nombre) {
		this.setNombre(nombre);
		this.setCampeon(null);
	}
	
	void pelear() {
		// TODO Auto-generated method stub
		
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

	public Bicho getCampeon() {
		return campeon;
	}

	public void setCampeon(Bicho campeon) {
		this.campeon = campeon;
	}
	
	public void AsignarBicho(Bicho bicho) {
		if (this.campeon == null || bicho.getEntrenador() == this.campeon.getEntrenador()) {
			this.setCampeon(bicho);
		}
		else {
			//error you dont have permision to do that
		}
	}
}
