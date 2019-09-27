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
    	return run(() ->{
    		Entrenador e = this.entrenadorDAO.recuperar(nombreDeEntrenador);
    		if (e == null) {
    			throw new EntrenadorInexistente(nombreDeEntrenador);
    		}
    		return e;
    	});
    }
    
    public Bicho getBicho(int idDeBicho){
        return run(() ->{
    		Bicho b = this.bichoDAO.recuperar(idDeBicho);
    		if (b == null) {
    			throw new BichoInexistente(idDeBicho);
    		}
    		return b;
    	});
    }
    
    private Boolean esBichoDeEntrenador(Entrenador entrenador, Bicho bicho){
        return entrenador.tieneBicho(bicho);
    }
    
    public boolean puedeEvolucionar(String entrenador, int idDeBicho){
    	Bicho bicho = this.getBicho(idDeBicho);
    	return bicho.puedeEvolucionar();
    }
    
    
    /*{
    	Bicho bichoRecuperado;
    	bichoRecuperado = Runner.runInSession(() ->{
        	return this.bichoDAO.recuperar(idDeBicho);
		});
		return bichoRecuperado.puedeEvolucionar();}
    */
    public Bicho evolucionar(String entrenador, int idDeBicho){
    	Bicho bicho = this.getBicho(idDeBicho);
    	bicho.evolucionar();
    	return bicho;
		}

	@Override
	public Entrenador buscar(String entrenador) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

