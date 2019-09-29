package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Guarderia extends Ubicacion{
    @OneToMany(fetch = FetchType.EAGER)//TODO: ¿Como implementar lazy?
    private Set<Bicho> bichosAbandonados;

    public Guarderia(){}

    /*public Guarderia(BusquedaHelperMock busquedaHelper){
        super(busquedaHelper);
    }
*/
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
}
