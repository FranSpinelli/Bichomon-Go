package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;

import java.util.List;

public interface CampeonDAO {
    void guardar(Campeon campeon);

    Campeon recuperar(Integer id);

    List<Entrenador> campeones();

    Especie especieLider();

    void guardarTodos(List<Campeon> campeones);
}
