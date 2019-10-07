package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Guarderia extends Ubicacion{
    @OneToMany(fetch = FetchType.EAGER)//TODO: ¿Como implementar lazy?
    private Set<Bicho> bichosAbandonados;

    public Guarderia(){
        bichosAbandonados = new HashSet<Bicho>();
    }

    public Guarderia(BusquedaHelper busquedaHelper){
        super(busquedaHelper);
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
    public Bicho generarBicho(){
        if(this.bichosAbandonados.isEmpty()){
            throw new BusquedaNoExitosa("No hay bichos que puedas adoptar en esta guarderia");
        }
        return this.bichosAbandonados.iterator().next();
    }
}

