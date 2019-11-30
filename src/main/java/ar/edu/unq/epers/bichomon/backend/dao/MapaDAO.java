package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import java.util.List;

public interface MapaDAO {

    String recuperar(String ubicacion);
    Integer costoCaminoMasBarato(Ubicacion origen, Ubicacion destino);
    Integer costoCaminoMasCorto(String origenNombre, String destinoNombre);
    List<String> conectados(String nombreOrigen, String tipoCamino);
    void create(Ubicacion ubicacion);
    void conectar(Ubicacion origen, Ubicacion destino, TipoCamino tipoCamino);
    void deleteAll();
}
