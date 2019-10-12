package ar.edu.unq.epers.bichomon.backend.service.especie;

public class NullEspecieLeaderException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NullEspecieLeaderException() {
        super("No existe una especie lider");
    }
}
