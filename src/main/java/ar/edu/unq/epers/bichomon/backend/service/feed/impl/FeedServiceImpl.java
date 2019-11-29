package ar.edu.unq.epers.bichomon.backend.service.feed.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EventoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.MapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.evento.Evento;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.feed.FeedService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FeedServiceImpl implements FeedService {
    EntrenadorDAO entrenadorDAO;
    MapaDAO mapaDAO;
    EventoDAO eventoDAO;

    public FeedServiceImpl(EntrenadorDAO entrenadorDAO, MapaDAO mapaDAO, EventoDAO eventoDAO){
        this.entrenadorDAO  = entrenadorDAO ;
        this.eventoDAO      = eventoDAO     ;
        this.mapaDAO        = mapaDAO       ;
    }

    @Override
    public List<Evento> feedEntrenador(String entrenador) {
        return eventoDAO.getEventList(entrenador);
    }

    @Override
    public List<Evento> feedUbicacion(String entrenadorName) {
        Set<String> ubicaciones = new HashSet<>();
        Entrenador entrenador = entrenadorDAO.recuperar(entrenadorName);
        if (entrenador != null) {
            String ubicacion = entrenador.getUbicacionActual().getNombre();
            ubicaciones.add(ubicacion);
            ubicaciones.addAll(mapaDAO.conectados(ubicacion, "Aereo"));
            ubicaciones.addAll(mapaDAO.conectados(ubicacion, "Terrestre"));
            ubicaciones.addAll(mapaDAO.conectados(ubicacion, "Maritimo"));
            return eventoDAO.getEventForUbicactionList(ubicaciones);
        }
        else{
            throw new EntrenadorInexistente(entrenadorName);
        }
    }
}