package ar.edu.unq.epers.bichomon.backend.model.camino;

public class Terrestre implements TipoCamino {
    @Override
    public int getPrecio() {
        return 1;
    }

    @Override
    public String getName() {
        return "Terrestre";
    }
}