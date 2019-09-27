package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

<<<<<<< HEAD
@SuppressWarnings("serial")
public class UbicacionIncorrectaException extends Exception {

	public UbicacionIncorrectaException(String string) {
		super(string);
	}
	
=======
public class UbicacionIncorrectaException extends RuntimeException{
    public UbicacionIncorrectaException(String mensaje){
        super(mensaje);
    }
>>>>>>> development
}
