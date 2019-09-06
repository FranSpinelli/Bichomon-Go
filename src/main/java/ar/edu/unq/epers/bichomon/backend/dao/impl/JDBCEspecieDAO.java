package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;
import connection.ConnectionBlock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class JDBCEspecieDAO implements EspecieDAO {

    @Override
    public void guardar(Especie especie) {

    }

    @Override
    public void actualizar(Especie especie) {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("UPDATE especies SET nombre=?, altura=?, peso=?, energiaInicial=?, tipoDeBicho=?, cantidadDeBichos=?, urlFoto=? WHERE id=?");
            ps.setString(1, especie.getNombre());
            ps.setInt(2, especie.getAltura());
            ps.setInt(3, especie.getPeso());
            ps.setInt(4, especie.getEnergiaInicial());
            ps.setString(5, especie.getTipo().toString());//TODO Revisar
            ps.setInt(6, especie.getCantidadBichos());
            ps.setString(7, especie.getUrlFoto());
            ps.execute();

            if (ps.getUpdateCount() != 1) {
                throw new RuntimeException("No se inserto el personaje " + especie);
            }
            ps.close();

            return null;

        });
    }

    @Override
    public Especie recuperar(String nombreEspecie) {

    }

    @Override
    public List<Especie> recuperarTodos() {
        return this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM especies");

            ResultSet resultSet = ps.executeQuery();

            List<Especie> especies = new ArrayList<Especie>();
            while (resultSet.next()) {

                Especie especie = new Especie(resultSet.getInt("id"), resultSet.getString("nombre"), TipoBicho.valueOf(resultSet.getString("tipoDeBicho")));//TODO Revisar tipo de bicho
                especie.setAltura(resultSet.getInt("altura"));
                especie.setPeso(resultSet.getInt("peso"));
                especie.setEnergiaIncial(resultSet.getInt("energiaInicial"));
                especie.setUrlFoto(resultSet.getString("urlFoto"));
                especie.setCantidadBichos(resultSet.getInt("cantidadDeBichos"));
                especies.add(especie);
            }

            ps.close();
            return especies;
        });
    }

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
