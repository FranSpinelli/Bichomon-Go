package ar.edu.unq.epers.bichomon.backend.model.camino;

public class Aereo implements TipoCamino {
    @Override
    public int getPrecio() {
        return 5;
    }

    @Override
    public String getName() {
        return "Aereo";
    }
}