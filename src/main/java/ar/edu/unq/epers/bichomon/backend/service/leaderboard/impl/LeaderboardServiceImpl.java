package ar.edu.unq.epers.bichomon.backend.service.leaderboard.impl;

import java.util.List;

import ar.edu.unq.epers.bichomon.backend.dao.CampeonDAO;
import ar.edu.unq.epers.bichomon.backend.dao.EntrenadorDAO;
import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.leaderboard.LeaderboardService;

public class LeaderboardServiceImpl implements LeaderboardService {

	private EntrenadorDAO entrenadorDAO;
    private CampeonDAO campeonDAO;

    public LeaderboardServiceImpl(EntrenadorDAO entrenadorDAO, CampeonDAO campeonDAO){
        this.entrenadorDAO = entrenadorDAO;
        this.campeonDAO = campeonDAO;
    }

	@Override
	public List<Entrenador> campeones() {
		return campeonDAO.campeones();
	}

	@Override
	public Especie especieLider() {
		/*retorna la especie que tenga mas bichos que haya sido
		 *campeones de cualquier dojo. Cada bicho deberá ser
		 *contando una sola vez (distinct) (independientemente de si haya
		 *sido coronado campeon mas de una vez o en mas de un Dojo)
		 */
		return campeonDAO.especieLider(); //ya esta
	}

	@Override
	public List<Entrenador> lideres() {
		//retorna los diez primeros entrenadores para los cuales
		//el valor de poder combinado de todos sus bichos sea superior
		return entrenadorDAO.lideres();
	}

}
