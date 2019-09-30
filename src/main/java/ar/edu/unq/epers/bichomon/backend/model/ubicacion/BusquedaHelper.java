package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class BusquedaHelper {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public abstract Boolean factorTiempo(Entrenador entrenador);

    public abstract Boolean factorNivel(Entrenador entrenador);

    public abstract Boolean factorPoblacion(Ubicacion ubicacion);

    public abstract Boolean factorRandom();

    public abstract Bicho generarBicho(Ubicacion ubicacion);

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
