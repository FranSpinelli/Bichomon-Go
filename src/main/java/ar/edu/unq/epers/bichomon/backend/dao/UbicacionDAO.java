package ar.edu.unq.epers.bichomon.backend.dao;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.relacionadoADojo.Campeon;

import java.util.List;

public interface UbicacionDAO {
    void guardar(Ubicacion ubicacion);

    Ubicacion recuperar(String ubicacion);

    Ubicacion recuperar(Integer id);

    void guardarTodos(List<Ubicacion> ubicaciones);

    int cantidadEntrenadoresEn(String ubicacion);

    Bicho getCampeonHistorico(String dojo);
}
