	package ar.edu.unq.epers.bichomon.backend.service.especie;

import java.util.List;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;


    public class EspecieServiceImpl implements EspecieService {

	private EspecieDAO especieDAO;

	public EspecieServiceImpl(EspecieDAO dao){
		this.especieDAO = dao;
	}
	

	@Override
	public void crearEspecie(Especie especie){
		especieDAO.guardar(especie);
	}
	

	@Override
	public Especie getEspecie(String nombreEspecie){
		Especie especie = especieDAO.recuperar(nombreEspecie);
		if(especie == null){
			throw new EspecieNoExistente(nombreEspecie);
		}
		return especie;
	}


	@Override
	public List<Especie> getAllEspecies(){
		return especieDAO.recuperarTodos();
	}


	@Override
	public Bicho crearBicho(String nombreEspecie, String nombreBicho){
		Especie especie = especieDAO.recuperar(nombreEspecie);
		Bicho bicho = especie.crearBicho();
		especieDAO.actualizar(especie);
		return bicho;
	}


	@Override
	public List<Especie> populares() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<Especie> impopulares() {
		// TODO Auto-generated method stub
		return null;
	}

}
