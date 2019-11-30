package ar.edu.unq.epers.bichomon.backend.service.feed;

import ar.edu.unq.epers.bichomon.backend.model.evento.Evento;

import java.util.List;

public interface FeedService {

    List<Evento> feedEntrenador(String entrenador);

    List<Evento> feedUbicacion(String entrenador);
}
