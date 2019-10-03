package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoAjeno;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoInexistente;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichosInsuficientes;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class BichoServiceImpl {

    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;

    public BichoServiceImpl(EntrenadorDAO entrenadorDAO, BichoDAO bichoDAO){
        this.entrenadorDAO = entrenadorDAO;
        this.bichoDAO = bichoDAO;
    }

    public Bicho buscar(String entrenador){
       return run(() -> {
           Entrenador entrenador1 = this.getEntrenador(entrenador);
           return entrenador1.buscar();
       });
    }

    public void abandonar(String nombreEntrenador, int idBicho){
        run(() -> {
               Entrenador entrenador = this.getEntrenador(nombreEntrenador);
               Bicho bicho = this.getBicho(idBicho);
               entrenador.abandonar(bicho);
        });
    }

    public ResultadoCombate duelo(String nombreEntrenador, int idBicho){
    /*todo: las primeras 3 lineas se repiten, se puede hacer refactor */

        return run(() -> {
            Entrenador entrenador = this.getEntrenador(nombreEntrenador);
            Bicho bicho = this.getBicho(idBicho);
            return entrenador.desafiarCampeonActualCon(bicho);
        });
    }





//PRIVATE FUNCTIONS---------------------------------------------------------------------------------------------------------------------
    private Entrenador getEntrenador(String nombreDeEntrenador){
            Entrenador entrenador = this.entrenadorDAO.recuperar(nombreDeEntrenador);
            if(entrenador == null){
                throw new EntrenadorInexistente(nombreDeEntrenador);
            }
            return entrenador;
    }

    private Bicho getBicho(Integer idDeBicho){
            Bicho bicho = this.bichoDAO.recuperar(idDeBicho);
            if(bicho == null){
                throw new BichoInexistente();
            }
            return bicho;
    }
}
