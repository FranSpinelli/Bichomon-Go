package ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions;

public class BichosInsuficientes extends RuntimeException {
    public BichosInsuficientes(String mensaje){
        super(mensaje);
    }
}
