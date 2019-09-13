package ar.edu.unq.epers.bichomon.backend.service.data;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;

public class DataServiceImpl implements DataService {

    private EspecieDAO daoDelDataService;

    public DataServiceImpl(EspecieDAO unDAO){

        daoDelDataService = unDAO;
    }

    @Override
    public void eliminarDatos() {

        daoDelDataService.eliminarTodos();
    }

    @Override
    public void crearSetDatosIniciales() {

        Especie especie1 = new Especie(1,"especie1", TipoBicho.FUEGO);
        especie1.setAltura(100);
        especie1.setPeso(100);
        especie1.setEnergiaIncial(100);
        especie1.setUrlFoto("/image/respecie1.jpg");

        Especie especie2 = new Especie(2,"especie2", TipoBicho.AGUA);
        especie2.setAltura(200);
        especie2.setPeso(200);
        especie2.setEnergiaIncial(200);
        especie2.setUrlFoto("/image/especie2.jpg");

        Especie especie3 = new Especie(3,"especie3", TipoBicho.AIRE);
        especie3.setAltura(300);
        especie3.setPeso(300);
        especie3.setEnergiaIncial(300);
        especie3.setUrlFoto("/image/especie3.jpg");

        Especie especie4 = new Especie(4,"especie4", TipoBicho.ELECTRICIDAD);
        especie4.setAltura(400);
        especie4.setPeso(400);
        especie4.setEnergiaIncial(400);
        especie4.setUrlFoto("/image/especie4.jpg");

        Especie especie5 = new Especie(5,"especie5", TipoBicho.PLANTA);
        especie5.setAltura(500);
        especie5.setPeso(500);
        especie5.setEnergiaIncial(500);
        especie5.setUrlFoto("/image/especie5.jpg");

        Especie especie6 = new Especie(6,"especie6", TipoBicho.TIERRA);
        especie6.setAltura(600);
        especie6.setPeso(600);
        especie6.setEnergiaIncial(600);
        especie6.setUrlFoto("/image/especie6.jpg");

        Especie especie7 = new Especie(7,"especie7", TipoBicho.CHOCOLATE);
        especie7.setAltura(700);
        especie7.setPeso(700);
        especie7.setEnergiaIncial(700);
        especie7.setUrlFoto("/image/especie7.jpg");


        daoDelDataService.guardar(especie1);
        daoDelDataService.guardar(especie2);
        daoDelDataService.guardar(especie3);
        daoDelDataService.guardar(especie4);
        daoDelDataService.guardar(especie5);
        daoDelDataService.guardar(especie6);
        daoDelDataService.guardar(especie7);
    }
}
