package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
public class Guarderia extends Ubicacion{

    @OneToMany(fetch = FetchType.EAGER)//TODO: ¿Como implementar lazy?
    private Set<Bicho> bichosAbandonados;

    public Guarderia(){}

    @Override
    public void recibirBicho(Bicho bichoAbandonado){

        this.bichosAbandonados.add(bichoAbandonado);
    }
}

