package ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class BichoAjeno extends RuntimeException {
    public BichoAjeno(String mensaje){
        super(mensaje);
    }
}
