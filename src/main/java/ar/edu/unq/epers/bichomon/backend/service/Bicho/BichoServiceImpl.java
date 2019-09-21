package ar.edu.unq.epers.bichomon.backend.service.Bicho;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

public class BichoServiceImpl {

    public void abandonar(String nombreEntrenador, int idBicho){

        Entrenador entrenador = this.getEntrenador(nombreEntrenador);

        Bicho bicho= this.getBicho(idBicho);

        if(! this.esBichoDeEntrenador(entrenador,bicho)){/*exeption*/}

        if(entrenador.getCantidadDeBichos() <= 1){/*exception2*/}

        entrenador.abandonar(bicho);

    }

    private Entrenador getEntrenador(String nombreDeEntrenador){
        return null;
    }

    private Bicho getBicho(Integer idDeBicho){
        return null;
    }

    private Boolean esBichoDeEntrenador(Entrenador entrenador, Bicho bicho){

        return entrenador.tieneBicho(bicho);
    }
}
