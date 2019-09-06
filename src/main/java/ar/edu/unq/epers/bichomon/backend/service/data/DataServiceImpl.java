package ar.edu.unq.epers.bichomon.backend.service.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataServiceImpl implements DataService {

    @Override
    public void eliminarDatos() {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO personaje (nombre, pesoMaximo, xp, vida) VALUES (?,?,?,?)");
            ps.setString(1, personaje.getNombre());
            ps.setInt(2, personaje.getPesoMaximo());
            ps.setInt(3, personaje.getXp());
            ps.setInt(4, personaje.getVida());
            //ojo, no estamos guardando el inventario!
            ps.execute();

            if (ps.getUpdateCount() != 1) {
                throw new RuntimeException("No se inserto el personaje " + personaje);
            }
            ps.close();

            return null;
        });
    }

    @Override
    public void crearSetDatosIniciales() {

    }

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
            //La url de conexion no deberia estar harcodeada aca, que se puede hacer para mejorar esto?
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/epers_ejemplo_jdbc?user=root&password=42547268&serverTimezone=UTC");
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
