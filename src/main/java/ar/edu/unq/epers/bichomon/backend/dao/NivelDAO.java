package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.bicho.AbstractNivel;

public interface NivelDAO {

    void guardar(AbstractNivel condicion);

    AbstractNivel recuperar(Integer id);
}
