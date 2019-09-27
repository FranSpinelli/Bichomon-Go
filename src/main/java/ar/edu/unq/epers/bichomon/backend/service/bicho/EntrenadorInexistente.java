package ar.edu.unq.epers.bichomon.backend.service.bicho;

public class EntrenadorInexistente extends RuntimeException {
    public EntrenadorInexistente(String nombreEntrenador){
        super("No existe un entrenador con nombre '" + nombreEntrenador + "'");
    }
}
