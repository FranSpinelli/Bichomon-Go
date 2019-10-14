package ar.edu.unq.epers.bichomon.backend.service.mapa;

public class UbicacionInexistente extends RuntimeException {
    public UbicacionInexistente(String mensaje) {
        super(mensaje);
    }
}
