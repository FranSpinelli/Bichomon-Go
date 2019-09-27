package ar.edu.unq.epers.bichomon.backend.service.leaderboard;

import java.util.List;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

public interface LeaderboardService {

	List<Entrenador> campeones();
	
	Especie especieLider();
	
	List<Entrenador> lideres();
}
