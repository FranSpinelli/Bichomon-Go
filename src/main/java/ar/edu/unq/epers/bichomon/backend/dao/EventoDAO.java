package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.evento.Captura;
import ar.edu.unq.epers.bichomon.backend.model.evento.Evento;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import java.util.List;
import java.util.Set;

public interface EventoDAO {

    List<Evento> getEventList(String entrenador);

    List<Evento> getEventForUbicactionList(Set<String> ubicaciones);

    void save(Evento evento);

    void deleteAll();
}
