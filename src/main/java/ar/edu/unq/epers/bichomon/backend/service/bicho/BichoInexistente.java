package ar.edu.unq.epers.bichomon.backend.service.bicho;

public class BichoInexistente extends RuntimeException {
    public BichoInexistente(){
        super("No existe el bicho");
    }
}
