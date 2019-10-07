package ar.edu.unq.epers.bichomon.backend.model.especie;

public class EvolucionNoPermitida extends RuntimeException {
    public EvolucionNoPermitida(String mensaje) {
        super(mensaje);
    }
}
