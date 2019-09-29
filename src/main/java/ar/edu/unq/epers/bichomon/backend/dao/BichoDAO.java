package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;

import java.util.List;

public interface BichoDAO {

    void guardar(Bicho bicho);

    Bicho recuperar(Integer id);

    void guardarTodos(List<Bicho> bichos);
}
