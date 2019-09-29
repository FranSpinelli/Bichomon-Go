package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    private BusquedaHelper busquedaHelper;

    public Ubicacion(){}

    public Ubicacion(BusquedaHelper busquedaHelper){
        this.busquedaHelper = busquedaHelper;
    }

    public BusquedaHelper getBusquedaHelper() {
        return busquedaHelper;
    }

    public void setBusquedaHelper(BusquedaHelper busquedaHelper) {
        this.busquedaHelper = busquedaHelper;
    }

    public void recibirBicho(Bicho bichoAbandonado){
        throw new UbicacionIncorrectaException("No se puede abandonar un bicho en esta ubicacion");
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Bicho buscar(Entrenador entrenador){
        Bicho bicho = null;
        if(this.esBusquedaExitosa(entrenador)){
            bicho = this.generarBicho();
        }
        return bicho;
    }

    protected Boolean esBusquedaExitosa(Entrenador entrenador){
        return this.busquedaHelper.factorTiempo(entrenador) && this.busquedaHelper.factorNivel(entrenador) && this.busquedaHelper.factorPoblacion(this) && this.busquedaHelper.factorRandom();
    }

    protected Bicho generarBicho(){
        return this.busquedaHelper.generarBicho(this);
    }
}
