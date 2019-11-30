package ar.edu.unq.epers.bichomon.backend.model.evento;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Abandono.class, name = "abandono"),
        @JsonSubTypes.Type(value = Arribo.class, name = "arribo")
})
public abstract class Evento {

    @MongoId
    @MongoObjectId
    private String id;
    private String entrenador;

    public Evento(){}

    public Evento(String entrenador){
        this.entrenador = entrenador;
    }

    public String getEntrenador() {
        return this.entrenador;
    }

    public String getId() {
        return this.id;
    }

    }

