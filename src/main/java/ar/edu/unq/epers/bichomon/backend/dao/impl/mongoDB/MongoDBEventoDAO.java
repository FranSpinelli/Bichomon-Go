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

    public MongoDBEventoDAO() {
        super(Evento.class);
    }

    public List<Evento> getAll() {
        return this.find("{}");
    }

    public Evento get(String id) {
        ObjectId objectId = new ObjectId(id);
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
            System.out.println(result);
            respuesta = result;


            System.out.println("AAAAAAAAAAAa");
            System.out.println(result);
        } catch (IOException e) {
            System.out.println("ERRRRRRRRROR");
            throw new RuntimeException(e);
        }
        System.out.println(respuesta);
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
