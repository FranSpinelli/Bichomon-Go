package ar.edu.unq.epers.bichomon.backend.service.data;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;

import java.util.ArrayList;

public class DataServiceImpl implements DataService {

    private EspecieDAO daoDelDataService;

    public void DataService(EspecieDAO unDAO){

        daoDelDataService = unDAO;
    }

    @Override
    public void eliminarDatos() {

        daoDelDataService.eliminarTodos();
    }

    @Override
    public void crearSetDatosIniciales() {

        Especie especie1 = new Especie("especie1", TipoBicho.FUEGO);
        Especie especie2 = new Especie("especie2", TipoBicho.AGUA);
        Especie especie3 = new Especie("especie3", TipoBicho.AIRE);
        Especie especie4 = new Especie("especie4", TipoBicho.ELECTRICIDAD);
        Especie especie5 = new Especie("especie5", TipoBicho.PLANTA);
        Especie especie6 = new Especie("especie6", TipoBicho.TIERRA);
        Especie especie7 = new Especie("especie7", TipoBicho.CHOCOLATE);

        daoDelDataService.guardar(especie1);
        daoDelDataService.guardar(especie2);
        daoDelDataService.guardar(especie3);
        daoDelDataService.guardar(especie4);
        daoDelDataService.guardar(especie5);
        daoDelDataService.guardar(especie6);
        daoDelDataService.guardar(especie7);
    }

    //PRIVATE FUNCTIONS---------------------------------------------------------------------------------------------------
}
