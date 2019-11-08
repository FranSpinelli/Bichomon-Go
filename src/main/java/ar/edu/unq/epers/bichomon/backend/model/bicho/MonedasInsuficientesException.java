package ar.edu.unq.epers.bichomon.backend.model.bicho;

public class MonedasInsuficientesException extends RuntimeException{

    public MonedasInsuficientesException(String mensaje){
        super(mensaje);
    }
}
