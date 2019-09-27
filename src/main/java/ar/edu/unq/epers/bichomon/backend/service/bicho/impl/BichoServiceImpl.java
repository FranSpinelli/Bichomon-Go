package ar.edu.unq.epers.bichomon.backend.service.bicho.impl;

import ar.edu.unq.epers.bichomon.backend.dao.bicho.BichoDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.service.bicho.BichoService;

public class BichoServiceImpl implements BichoService{
	
	private BichoDAO bichoDAO;

    public BichoServiceImpl(BichoDAO dAO) {
        this.bichoDAO = dAO;
    }

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
    
    public Bicho getBicho(int idDeBicho){
        return Runner.runInSession(() ->{
        	return this.bichoDAO.recuperar(idDeBicho);
        		});
    }
    
    private Boolean esBichoDeEntrenador(Entrenador entrenador, Bicho bicho){
        return entrenador.tieneBicho(bicho);
    }
    
    public boolean puedeEvolucionar(String entrenador, int idDeBicho){
    	Bicho bichoRecuperado;
    	bichoRecuperado = Runner.runInSession(() ->{
        	return this.bichoDAO.recuperar(idDeBicho);
		});
		return bichoRecuperado.puedeEvolucionar();}
    
    public Bicho evolucionar(String entrenador, int idDeBicho){
    	return Runner.runInSession(() ->{
    		return this.bichoDAO.updatePorEvolucion(idDeBicho);
			});
		}

	@Override
	public Entrenador buscar(String entrenador) {
		// TODO Auto-generated method stub
		return null;
	}
}
