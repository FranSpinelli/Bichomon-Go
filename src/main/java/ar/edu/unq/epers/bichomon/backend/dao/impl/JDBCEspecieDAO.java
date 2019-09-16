package ar.edu.unq.epers.bichomon.backend.dao.impl;


import java.sql.*;

import java.util.ArrayList;

import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import connection.ConnectionBlock;
import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import java.util.List;

public class JDBCEspecieDAO implements EspecieDAO {

    @Override
    public void guardar(Especie especie) {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO especie (nombre, altura, peso, tipoDeBicho, energiaInicial, urlFoto, cantidadDeBichos) VALUES (?,?,?,?,?,?,0)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, especie.getNombre());
            ps.setInt(2, especie.getAltura());
            ps.setInt(3, especie.getPeso());
            ps.setString(4, especie.getTipo().name());
            ps.setInt(5, especie.getEnergiaInicial());
            ps.setString(6, especie.getUrlFoto());
            ps.execute();

            ResultSet resultSet = ps.getGeneratedKeys();
            if (resultSet.next()){
                especie.setId(resultSet.getInt(1));
            }
            ps.close();
            return null;
        });
    }


    @Override
    public void actualizar(Especie especie) {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("UPDATE especie SET nombre=?, altura=?, peso=?, energiaInicial=?, tipoDeBicho=?, cantidadDeBichos=?, urlFoto=? WHERE id=?");
            ps.setString(1, especie.getNombre());
            ps.setInt(2, especie.getAltura());
            ps.setInt(3, especie.getPeso());
            ps.setInt(4, especie.getEnergiaInicial());
            ps.setString(5, especie.getTipo().toString());
            ps.setInt(6, especie.getCantidadBichos());
            ps.setString(7, especie.getUrlFoto());
            ps.setInt(8, especie.getId());
            ps.execute();

            if (ps.getUpdateCount() != 1) {
                throw new RuntimeException("No existe el personaje " + especie);
            }
            ps.close();

            return null;

        });

    }

    @Override
    public Especie recuperar(String nombreEspecie) {
        return this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM especie WHERE nombre = ?");
            ps.setString(1, nombreEspecie);
            ResultSet resultSet = ps.executeQuery();
            Especie especie = null;
            if(resultSet.next()){
                especie = this.crearEspecieConResultSet(resultSet);
            }
            ps.close();
            return especie;
        });
    }

    @Override
    public List<Especie> recuperarTodos(){
        return this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM especie ORDER BY nombre ASC");
            ResultSet resultSet = ps.executeQuery();
            List<Especie> especies = new ArrayList<Especie>();
            while(resultSet.next()){
                especies.add(this.crearEspecieConResultSet(resultSet));
            }

            ps.close();
            return especies;
        });
    }

    @Override
    public void eliminarTodos() {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("DELETE FROM especie");
            ps.execute();

            ps.close();

            return null;
        });
    }


//PRIVATE FUNCTIONS----------------------------------------------------------------------------------------------------------------------

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

            return DriverManager.getConnection("jdbc:mysql://localhost:3306/epers_persistiendoConEstilo_jdbc?user=root&password=42547268&serverTimezone=UTC");
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

    private Especie crearEspecieConResultSet(ResultSet resultSet) throws SQLException {

        Especie especie = null;
        String nombreEspecie = resultSet.getString("nombre");
        TipoBicho tipo = TipoBicho.valueOf(resultSet.getString("tipoDeBicho"));
        especie = new Especie(nombreEspecie, tipo);
        especie.setId(resultSet.getInt("id"));
        especie.setAltura(resultSet.getInt("altura"));
        especie.setPeso(resultSet.getInt("peso"));
        especie.setEnergiaInicial(resultSet.getInt("energiaInicial"));
        especie.setUrlFoto(resultSet.getString("urlFoto"));
        especie.setCantidadBichos(resultSet.getInt("cantidadDeBichos"));
        return especie;
    }
}
