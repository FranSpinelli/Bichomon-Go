package ar.edu.unq.epers.bichomon.backend.service.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import connection.ConnectionBlock;

public class DataServiceImpl implements DataService {

    private EspecieDAO daoDelDataService;

    public void DataService(EspecieDAO unDAO){

        daoDelDataService = unDAO;
    }

    @Override
    public void eliminarDatos() {

//        this.executeWithConnection(conn -> {
//            PreparedStatement ps = conn.prepareStatement("DELETE FROM especie");
//            ps.execute();

//            if (ps.getUpdateCount() != 0) {
//                throw new RuntimeException("No se pudo eliminar los datos de la tabla");
//            }
//            ps.close();

//            return null;
//        });
    }

    @Override
    public void crearSetDatosIniciales() {


    }

    //PRIVATE FUNCTIONS---------------------------------------------------------------------------------------------------
}
