package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

public class BusquedaNoExitosa extends RuntimeException {
    public BusquedaNoExitosa(String mensaje){
        super(mensaje);
    }
}
