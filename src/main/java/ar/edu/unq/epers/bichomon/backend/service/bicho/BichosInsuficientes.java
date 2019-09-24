package ar.edu.unq.epers.bichomon.backend.service.bicho;

public class BichosInsuficientes extends RuntimeException {
    public BichosInsuficientes(String mensaje){
        super(mensaje);
    }
}
