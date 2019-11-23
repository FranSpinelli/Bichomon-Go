package ar.edu.unq.epers.bichomon.backend.service.feed.impl;

import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateEntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.hibernate.HibernateUbicacionDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.mongoDB.MongoDBEventoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j.Neo4jMapaDAO;
import ar.edu.unq.epers.bichomon.backend.model.camino.Aereo;
import ar.edu.unq.epers.bichomon.backend.model.camino.Maritimo;
import ar.edu.unq.epers.bichomon.backend.model.camino.Terrestre;
import ar.edu.unq.epers.bichomon.backend.model.evento.Evento;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.feed.FeedService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.MapaService;
import ar.edu.unq.epers.bichomon.backend.service.mapa.impl.MapaServiceImpl;

import java.util.ArrayList;
import java.util.List;

public class FeedServiceImpl implements FeedService {
    @Override
    public List<Evento> feedEntrenador(String entrenador) {
        return new MongoDBEventoDAO().getEventList(entrenador);
    }

    @Override
    public List<Evento> feedUbicacion(String entrenador) {
        HibernateEntrenadorDAO entrenadorDAO= new HibernateEntrenadorDAO();
        MapaService mapaService = new MapaServiceImpl(entrenadorDAO, new HibernateUbicacionDAO(), new Neo4jMapaDAO());
        Ubicacion ubicacion = entrenadorDAO.recuperar(entrenador).getUbicacionActual();
        List<String> ubicaciones = new Neo4jMapaDAO().conectados(ubicacion.getNombre(), new Terrestre().getName()) ;
        ubicaciones.addAll(new Neo4jMapaDAO().conectados(ubicacion.getNombre(), new Maritimo().getName()));
        ubicaciones.addAll(new Neo4jMapaDAO().conectados(ubicacion.getNombre(), new Aereo().getName()));
        List<Evento> eventos = new ArrayList<>();
        for (String ubicacion1 : ubicaciones){
            eventos.addAll(new MongoDBEventoDAO().getEventForUbicactionList(entrenador,ubicacion1));
        }
        return eventos;
    }
}
