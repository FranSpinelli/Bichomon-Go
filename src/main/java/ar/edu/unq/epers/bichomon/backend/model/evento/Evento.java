package ar.edu.unq.epers.bichomon.backend.model.evento;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.jongo.marshall.jackson.oid.MongoId;
import org.jongo.marshall.jackson.oid.MongoObjectId;

import java.util.Date;
import java.util.Objects;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Abandono.class, name = "abandono"),
        @JsonSubTypes.Type(value = Arribo.class, name = "arribo"),
        @JsonSubTypes.Type(value = Captura.class, name = "captura"),
        @JsonSubTypes.Type(value = Coronacion.class, name = "coronacion")
})
public abstract class Evento {

    @MongoId
    @MongoObjectId
    private String id;

    private long timestamp;

    private String entrenador;

    public Evento(){}

    public Evento(String entrenador){
        this.timestamp = new Date().toInstant().toEpochMilli();
        this.entrenador = entrenador;
    }

    public String getEntrenador() {
        return this.entrenador;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getId() {
        return this.id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(id, evento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    }

