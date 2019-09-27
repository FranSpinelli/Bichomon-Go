package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import java.util.HashSet;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Guarderia extends Ubicacion{

    @OneToMany(fetch = FetchType.EAGER)//TODO: ¿Como implementar lazy?
    private Set<Bicho> bichosAbandonados;

    public Guarderia(){this.bichosAbandonados = new HashSet();}

    @Override
    public void recibirBicho(Bicho bichoAbandonado){
        this.bichosAbandonados.add(bichoAbandonado);
    }

	@Override
	void buscar() {
		// TODO Auto-generated method stub
		
	}
}

