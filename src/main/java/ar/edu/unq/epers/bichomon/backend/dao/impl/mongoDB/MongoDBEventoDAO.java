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
import java.util.Set;

public class MongoDBEventoDAO extends GenericMongoDAO<Evento> implements EventoDAO{

    protected MongoCollection mongoCollection;

    public MongoDBEventoDAO() {
        super(Evento.class);
    }

    public void deleteAll() {
        this.mongoCollection.drop();
    }

    public List<Evento> getAll() {
        return this.find("{}");
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

    public List<Evento> getEventList(String entrenador) {
        List<Evento> respuesta;
        try {
            MongoCursor<Evento> all = this.mongoCollection.find("{ $or: [{ entrenador: # },{ entrenadorPerdedor: # } ] }", entrenador,entrenador)
                    .sort("{ timestamp : -1}")
                    .as(Evento.class);
            List<Evento> result = this.copyToList(all);
            all.close();
            respuesta = result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return respuesta;
    }

    @Override
    public List<Evento> getEventForUbicactionList(Set<String> ubicaciones) {
        List<Evento> respuesta;
        try {
            MongoCursor<Evento> all = this.mongoCollection.find("{ ubicacion: { $in: # } }", ubicaciones)
                    .sort("{ timestamp : -1}")
                    .as(Evento.class);
            List<Evento> result = this.copyToList(all);
            all.close();

            respuesta = result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return respuesta;
    }
}
