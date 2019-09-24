package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class BichoServiceImpl {

    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;

    public BichoServiceImpl(EntrenadorDAO entrenadorDAO, BichoDAO bichoDAO){
        this.entrenadorDAO = entrenadorDAO;
        this.bichoDAO = bichoDAO;
    }

    public void abandonar(String nombreEntrenador, int idBicho){
        run(() -> {
            Entrenador entrenador = this.getEntrenador(nombreEntrenador);

            Bicho bicho= this.getBicho(idBicho);

            if(! this.esBichoDeEntrenador(entrenador,bicho)){ throw new BichoAjeno("No se pueden abandonar bichos ajenos"); }

            if(entrenador.getCantidadDeBichos() <= 1){ throw new BichosInsuficientes("El entrenador debe tener al menos un bicho luego de abandonar"); }

            entrenador.abandonar(bicho);
        });
    }

    private Entrenador getEntrenador(String nombreDeEntrenador){
        return run(() -> {
            Entrenador entrenador = this.entrenadorDAO.recuperar(nombreDeEntrenador);
            if(entrenador == null){
                throw new EntrenadorInexistente(nombreDeEntrenador);
            }
            return entrenador;
        });
    }

    private Bicho getBicho(Integer idDeBicho){
        return run(() -> {
            Bicho bicho = this.bichoDAO.recuperar(new Long(idDeBicho));
            if(bicho == null){
                throw new BichoInexistente();
            }
            return bicho;
        });
    }

    private Boolean esBichoDeEntrenador(Entrenador entrenador, Bicho bicho){
        return run(() -> entrenador.tieneBicho(bicho));
    }
}
