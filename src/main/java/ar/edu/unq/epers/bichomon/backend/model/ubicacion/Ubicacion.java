package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
<<<<<<< HEAD

public abstract class Ubicacion {
=======

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    public void recibirBicho(Bicho bichoAbandonado){
        throw new UbicacionIncorrectaException("No se puede abandonar un bicho en esta ubicacion");
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }
>>>>>>> development

	public void recibirBicho(Bicho bicho) throws UbicacionIncorrectaException {
		throw new UbicacionIncorrectaException("");
	}
	
	abstract void buscar();
}