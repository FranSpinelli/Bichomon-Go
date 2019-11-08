package ar.edu.unq.epers.bichomon.backend.model.camino;


public class Maritimo implements TipoCamino {
    @Override
    public int getPrecio() {
        return 2;
    }

    @Override
    public String getName() {
        return "Maritimo";
    }
}
