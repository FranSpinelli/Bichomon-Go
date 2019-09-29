package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    //private BusquedaHelperMock busquedaHelperMock;

    public Ubicacion(){}

    /*public Ubicacion(BusquedaHelperMock busquedaHelper){
        this.busquedaHelperMock = busquedaHelper;
    }*/

    /*public BusquedaHelperMock getBusquedaHelperMock() {
        return busquedaHelperMock;
    }*/

    /*public void setBusquedaHelperMock(BusquedaHelperMock busquedaHelper) {
        this.busquedaHelperMock = busquedaHelper;
    }*/

    public void recibirBicho(Bicho bichoAbandonado){
        throw new UbicacionIncorrectaException("No se puede abandonar un bicho en esta ubicacion");
    }

    public ResultadoCombate realizarDuelo(Bicho bichoRetador){
        throw new UbicacionIncorrectaException("No se puede desafiar a duelo en esta ubicacion");
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    /*public Bicho buscar(Entrenador entrenador){
        Bicho bicho = null;
        if(this.esBusquedaExitosa(entrenador)){
            bicho = this.generarBicho();
        }
        return bicho;
    }*/

    /*protected Boolean esBusquedaExitosa(Entrenador entrenador){
        return this.busquedaHelperMock.factorTiempo(entrenador) && this.busquedaHelperMock.factorNivel(entrenador) && this.busquedaHelperMock.factorPoblacion(this) && this.busquedaHelperMock.factorRandom();
    }

    protected Bicho generarBicho(){
        return this.busquedaHelperMock.generarBicho(this);
    }*/
}
