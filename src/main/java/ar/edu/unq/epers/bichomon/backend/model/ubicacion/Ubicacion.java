package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Estrategia;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String nombre;
    @OneToOne(cascade = CascadeType.ALL)
    private BusquedaHelper busquedaHelper;

    public Ubicacion(String nombre) {
        this.nombre = nombre;
    }

    public Ubicacion(BusquedaHelper busquedaHelper){
        this.busquedaHelper = busquedaHelper;
    }

    public Ubicacion() {

    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public ResultadoCombate realizarDuelo(Bicho bichoRetador, Estrategia estrategia){
        throw new UbicacionIncorrectaException("No se puede desafiar a duelo en esta ubicacion");
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Bicho buscar(Entrenador entrenador){
        this.arrojarBusquedaNoExitosaSi(!this.esBusquedaExitosa(entrenador), "No se encontro ningun bicho");
        this.arrojarBusquedaNoExitosaSi(!this.esBusquedaExitosaPosible(entrenador), this.mensajeBusquedaExitosaNoPosible());
        return this.generarBicho(entrenador);
    }

    protected Boolean esBusquedaExitosa(Entrenador entrenador){
        return this.busquedaHelper.factorTiempo(entrenador) && this.busquedaHelper.factorNivel(entrenador) && this.busquedaHelper.factorPoblacion(this) && this.busquedaHelper.factorRandom();
    }

    protected Bicho generarBicho(Entrenador entrenador) {
        return new Bicho(this.elegirEspecie());
    }

    protected Especie elegirEspecie(){ throw new MetodoRestringido("No se puede usar este metodo"); }

    private void arrojarBusquedaNoExitosaSi(Boolean condicion, String mensajeDeError){
        if(condicion){
            throw new BusquedaNoExitosa(mensajeDeError);
        }
    }

    protected abstract Boolean esBusquedaExitosaPosible(Entrenador entrenador);

    protected abstract String mensajeBusquedaExitosaNoPosible();

    public Campeon getCampeonActual(){
        throw new UbicacionIncorrectaException("Esta ubicacion no tiene campeon");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ubicacion ubicacion = (Ubicacion) o;
        return id == ubicacion.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
