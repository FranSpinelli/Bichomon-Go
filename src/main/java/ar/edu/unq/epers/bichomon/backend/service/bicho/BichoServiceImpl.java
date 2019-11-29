package ar.edu.unq.epers.bichomon.backend.service.bicho;

import ar.edu.unq.epers.bichomon.backend.dao.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EventoDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.evento.Abandono;
import ar.edu.unq.epers.bichomon.backend.model.evento.Captura;
import ar.edu.unq.epers.bichomon.backend.model.evento.Coronacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.ResultadoCombate;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.BichoInexistente;
import ar.edu.unq.epers.bichomon.backend.service.bicho.serviceExeptions.EntrenadorInexistente;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl.HibernateTransaction;

import static ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner.run;

public class BichoServiceImpl {

    private EntrenadorDAO entrenadorDAO;
    private BichoDAO bichoDAO;
    private EventoDAO eventoDAO;
    private Transaction hibernateTransaction = new HibernateTransaction();

    public BichoServiceImpl(EntrenadorDAO entrenadorDAO, BichoDAO bichoDAO, EventoDAO eventoDAO){
        this.entrenadorDAO = entrenadorDAO;
        this.bichoDAO = bichoDAO;
        this.eventoDAO = eventoDAO;
    }

    public Bicho buscar(String entrenador){
       return run(() -> {
           Entrenador entrenador1 = this.getEntrenador(entrenador);
           Bicho bichoEncontrado = entrenador1.buscar();
           eventoDAO.save(new Captura(entrenador1.getNombre(), bichoEncontrado.getId(),
                                        bichoEncontrado.getEspecie().getNombre(), entrenador1.getUbicacionActual().getNombre()));
           return bichoEncontrado;
       }, this.hibernateTransaction);
    }

    public void abandonar(String nombreEntrenador, int idBicho){
        run(() -> {
               Entrenador entrenador = this.getEntrenador(nombreEntrenador);
               Bicho bicho = this.getBicho(idBicho);
               entrenador.abandonar(bicho);
               eventoDAO.save(new Abandono(entrenador.getUbicacionActual().getNombre(), nombreEntrenador, bicho.getEspecie().getNombre(), idBicho));
        }, this.hibernateTransaction);
    }

    public ResultadoCombate duelo(String nombreEntrenador, int idBicho){

        return run(() -> {
            Entrenador entrenador = this.getEntrenador(nombreEntrenador);
            Bicho bicho = this.getBicho(idBicho);
            Bicho bichoCampeonAnterior = entrenador.getUbicacionActual().getCampeonActual().getBicho();
            Entrenador entrenadorCampeonAnterior = bichoCampeonAnterior.getEntrenador();
            ResultadoCombate resultado = entrenador.desafiarCampeonActualCon(bicho);
            if (entrenadorCampeonAnterior != resultado.getGanadorDelDuelo().getEntrenador()){
                eventoDAO.save(new Coronacion(nombreEntrenador, entrenadorCampeonAnterior.getNombre(),entrenador.getUbicacionActual().getNombre(),bicho.getEspecie().getNombre(), idBicho, bichoCampeonAnterior.getEspecie().getNombre(), bichoCampeonAnterior.getId()));
            }
            return resultado;
        }, this.hibernateTransaction);
    }

    public boolean puedeEvolucionar(String entrenadorNombre, int idDeBicho) {
        return run(() -> {
            Bicho bicho = this.getBicho(idDeBicho);
            Entrenador entrenador = this.getEntrenador(entrenadorNombre);
            return entrenador.tieneBicho(bicho) && bicho.puedeEvolucionar();
        }, this.hibernateTransaction);
    }

    public Bicho evolucionar(String entrenadorNombre, int idDeBicho) {
        return run(() -> {
            Bicho bicho = this.getBicho(idDeBicho);
            Entrenador entrenador = this.getEntrenador(entrenadorNombre);
            return entrenador.hacerEvolucionar(bicho);
        }, this.hibernateTransaction);
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
