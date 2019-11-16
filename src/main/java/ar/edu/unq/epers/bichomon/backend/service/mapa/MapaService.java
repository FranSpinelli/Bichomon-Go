package ar.edu.unq.epers.bichomon.backend.service.mapa;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;

import java.util.List;

public interface MapaService {

	void mover(String entrenador, String ubicacion);
	
	int cantidadEntrenadores(String ubicacion);
	
	Bicho campeon(String dojo);
	
	Bicho campeonHistorico(String dojo);

	void moverMasCorto(String entrenador, String ubicacion);

	List<Ubicacion> conectados(String ubicacion, String tipoCamino);

	void crearUbicacion(Ubicacion ubicacion);

	void conectar(String ubicacion1, String ubicacion2, TipoCamino tipoCamino);
}
