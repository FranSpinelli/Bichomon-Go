package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

public class UbicacionIncorrectaException extends RuntimeException{
    public UbicacionIncorrectaException(String mensaje){
        super(mensaje);
    }
}
