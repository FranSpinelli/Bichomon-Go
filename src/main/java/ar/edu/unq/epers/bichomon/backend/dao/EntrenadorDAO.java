package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Entrenador;

import java.util.List;

public interface EntrenadorDAO {

    void guardar(Entrenador entrenador);

    Entrenador recuperar(String nombre);

    void guardarTodos(List<Entrenador> entrenadores);
}
