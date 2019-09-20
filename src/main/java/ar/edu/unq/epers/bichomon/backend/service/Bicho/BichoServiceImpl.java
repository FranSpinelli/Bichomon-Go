package ar.edu.unq.epers.bichomon.backend.service.Bicho;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

public class BichoServiceImpl {

    public void abandonar(String nombreEntrenador, int idBicho){

        Entrenador entrenador = this.getEntrenador();

        Bicho bicho= this.getBicho();

        if(! this.esBichoDeEntrenador(entrenador,bicho)){/*exeption*/}

        if(entrenador.getCantidadDeBichos() <= 1){/*exception2*/}

        entrenador.abandonar();

    }
}
