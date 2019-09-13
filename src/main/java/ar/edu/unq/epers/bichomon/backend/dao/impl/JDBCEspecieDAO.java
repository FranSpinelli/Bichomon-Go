package ar.edu.unq.epers.bichomon.backend.dao.impl;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
            PreparedStatement ps = conn.prepareStatement("INSERT INTO especie (id,nombre, altura, peso, tipoDeBicho, energiaInicial, urlFoto, cantidadDeBichos) VALUES (?,?,?,?,?,?,?,0)");
            ps.setInt(1,especie.getId());
            ps.setString(2, especie.getNombre());
            ps.setInt(3, especie.getAltura());
            ps.setInt(4, especie.getPeso());
            ps.setString(5, especie.getTipo().name());
            ps.setInt(6, especie.getEnergiaInicial());
            ps.setString(7, especie.getUrlFoto());
            ps.execute();

            if (ps.getUpdateCount() != 1) {
                throw new RuntimeException("No se inserto la especie " + especie);
            }
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
            ps.setString(5, especie.getTipo().toString());//TODO Revisar
            ps.setInt(6, especie.getCantidadBichos());
            ps.setString(7, especie.getUrlFoto());
            ps.setString(8, especie.getId());
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
            PreparedStatement ps = conn.prepareStatement("SELECT altura, peso, tipoDeBicho, energiaInicial, urlFoto, cantidadDeBichos FROM especie WHERE nombre = ?");
            ps.setString(1, nombreEspecie);

            ResultSet resultSet = ps.executeQuery();

            Especie especie = null;
            while (resultSet.next()) {
                //si personaje no es null aca significa que el while dio mas de una vuelta, eso
                //suele pasar cuando el resultado (resultset) tiene mas de un elemento.
                if (especie != null) {
                    throw new RuntimeException("Existe mas de un personaje con el nombre " + nombreEspecie);
                }

                TipoBicho tipo = TipoBicho.valueOf(resultSet.getString("tipoDeBicho"));
                int idDeEspecie = resultSet.getInt("id");

                especie = new Especie(idDeEspecie,nombreEspecie, tipo);
                especie.setAltura(resultSet.getInt("altura"));
                especie.setPeso(resultSet.getInt("peso"));
                especie.setEnergiaInicial(resultSet.getInt("energiaInicial"));
                especie.setUrlFoto(resultSet.getString("urlFoto"));
                especie.setCantidadBichos(resultSet.getInt("cantidadDeBichos"));

            ps.close();
            return especie;
        }
            return especie;
        });
    }

    @Override
    public List<Especie> recuperarTodos(){
        return this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM especie");

            ResultSet resultSet = ps.executeQuery();

            List<Especie> especies = new ArrayList<Especie>();
            while (resultSet.next()) {

                Especie especie = new Especie(
                        resultSet.getInt("id"),
                        resultSet.getString("nombre"),
                        TipoBicho.valueOf(resultSet.getString("tipoDeBicho"))
                );//TODO Revisar tipo de bicho

                especie.setAltura(resultSet.getInt("altura"));
                especie.setPeso(resultSet.getInt("peso"));
                especie.setEnergiaInicial(resultSet.getInt("energiaInicial"));
                especie.setUrlFoto(resultSet.getString("urlFoto"));
                especie.setCantidadBichos(resultSet.getInt("cantidadDeBichos"));
                especies.add(especie);
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
            /*todo: esperar respuesta del mail para saber como chequear esto, y si hacerlo o no */
            /*if (ps.getUpdateCount() != 0 ) {
                throw new RuntimeException("No se pudo eliminar los datos de la tabla");
            }*/
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
