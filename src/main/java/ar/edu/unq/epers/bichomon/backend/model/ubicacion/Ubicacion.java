package ar.edu.unq.epers.bichomon.backend.model.ubicacion;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public abstract class Ubicacion {

    public void recibirBicho(Bicho bichoAbandonado){
        throw new UbicacionIncorrectaException("No se puede abandonar un bicho en esta ubicacion");
    }

}
