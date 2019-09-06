package ar.edu.unq.epers.bichomon.backend.service.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import connection.ConnectionBlock;

public class DataServiceImpl implements DataService {

    @Override
    public void eliminarDatos() {

        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM especie");
            ps.execute();

            if (ps.getUpdateCount() != 0) {
                throw new RuntimeException("No se pudo eliminar los datos de la tabla");
            }
            ps.close();

            return null;
        });
    }

    @Override
    public void crearSetDatosIniciales() {}

    //PRIVATE FUNCTIONS---------------------------------------------------------------------------------------------------

    private <T> T executeWithConnection(ConnectionBlock<T> bloque) {
        Connection connection = this.openConnection();
        try {
            return bloque.executeWith(connection);
        } catch (SQLException e) {
            throw new RuntimeException("Error no esperado", e);
        } finally {
            this.closeConnection(connection);
        }
    }

    private Connection openConnection() {
        try {
            /*todo: se puede mejorar el metodo para que la URL no aparezca aca?*/

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/epers_persistiendoConEstilo_jdbc?user=root&password=root&serverTimezone=UTC");
        } catch (SQLException e) {
            throw new RuntimeException("No se puede establecer una conexion", e);
        }
    }

    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar la conexion", e);
        }
    }
}
