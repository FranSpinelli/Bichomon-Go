package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Guarderia extends Ubicacion{
    @OneToMany(fetch = FetchType.EAGER)//TODO: ¿Como implementar lazy?
    private Set<Bicho> bichosAbandonados;

    public Guarderia(String nombre){
        super(nombre);
        bichosAbandonados = new HashSet<Bicho>();
    }

    public Guarderia(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
    }

    public Guarderia() {
        bichosAbandonados = new HashSet<Bicho>();
    }

    public Set<Bicho> getBichosAbandonados() {
        return bichosAbandonados;
    }

    public void setBichosAbandonados(Set<Bicho> bichosAbandonados) {
        this.bichosAbandonados = bichosAbandonados;
    }

    @Override
    public void recibirBicho(Bicho bichoAbandonado){

        this.bichosAbandonados.add(bichoAbandonado);
    }

    public Set<Bicho> getBichosEnGuarderia(){
        return this.bichosAbandonados;
    }

    @Override
    public Bicho generarBicho(Entrenador entrenador){
        return this.bichosAbandonados.stream().filter(bicho -> !bicho.tieneExDuenio(entrenador)).findFirst().get();
    }

    @Override
    protected Boolean esBusquedaExitosaPosible(Entrenador entrenador) {
        return this.hayBichoAbandonadoPorOtro(entrenador);
    }

    private Boolean hayBichoAbandonadoPorOtro(Entrenador entrenador){
        return this.bichosAbandonados.stream().filter(bicho -> !bicho.tieneExDuenio(entrenador)).count() > 0;
    }

    @Override
    protected String mensajeBusquedaExitosaNoPosible() {
        return "No hay bichos que puedas adoptar en esta guarderia";
    }
}

