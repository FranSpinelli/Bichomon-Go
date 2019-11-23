package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

public abstract class Evento {

    @MongoId
    @MongoObjectId
    private String id;
    private String entrenador;
    private String type;

    public Evento(){}

    public Evento(String entrenador){
        this.entrenador = entrenador;
        this.setType(this.getClass().getName());
    }

    public String getEntrenador() {
        return this.entrenador;
    }

    public String getId() {
        return this.id;
    }

    public void setType(String typeName){
        this.type = typeName;
    }
}
