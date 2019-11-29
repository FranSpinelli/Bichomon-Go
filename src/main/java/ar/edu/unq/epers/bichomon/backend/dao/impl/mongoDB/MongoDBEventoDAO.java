package ar.edu.unq.epers.bichomon.backend.dao.impl.mongoDB;

import ar.edu.unq.epers.bichomon.backend.dao.EventoDAO;
import ar.edu.unq.epers.bichomon.backend.model.evento.Evento;
import org.bson.types.ObjectId;
import org.jongo.Aggregate;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.jongo.MongoCursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MongoDBEventoDAO extends GenericMongoDAO<Evento> {

    protected MongoCollection mongoCollection;

    public MongoDBEventoDAO() {
        super(Evento.class);
    }

    public void deleteAll() {
        this.mongoCollection.drop();
    }

    public void save(Evento object) {
        this.mongoCollection.insert(object);
    }

    public void save(List<Evento> eventos) {
        this.mongoCollection.insert(eventos.toArray());
    }

    public Evento get(String id) {
        ObjectId objectId = new ObjectId(id);
        System.out.println(this.mongoCollection.findOne(objectId));
        return this.mongoCollection.findOne(objectId).as(Evento.class);
    }

    public List<Evento> find(String query, Object... parameters) {
        try {
            MongoCursor<Evento> all = this.mongoCollection.find(query, parameters).as(Evento.class);

            List<Evento> result = this.copyToList(all);
            all.close();

            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Copia el contenido de un iterable en una lista
     */
    protected <X> List<X> copyToList(Iterable<X> iterable) {
        List<X> result = new ArrayList<>();
        iterable.forEach(x -> result.add(x));
        return result;
    }

    public List<Evento> getEventForUbicactionList(List<String> ubicaciones) {
        ArrayList<Evento> events = new ArrayList<>();
        for (String ubicacion : ubicaciones) {
            events.addAll(this.find());
        }
        return events;
    }

    public List<Evento> getEventList(String entrenador) {
        Aggregate.ResultsIterator<Evento> result = this.mongoCollection.aggregate("{ $match: {entrenador: # }}",entrenador)
            .and("{$sort:{fechaEvento : -1 } }")
            .as(Evento.class);

        List<Evento> eventos = this.copyToList(result);
        return eventos;
    }
}
