package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class JDBCEspecieDAO implements EspecieDAO {
    @Override
    public void guardar(Especie especie) {
        this.executeWithConnection(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO especie (nombre, altura, peso, tipoDeBicho, energiaInicial, urlFoto, cantidadDeBichos) VALUES (?,?,?,?,?,?,?)");
            ps.setString(1, especie.getNombre());
            ps.setInt(2, especie.getAltura());
            ps.setInt(3, especie.getPeso());
            ps.setString(4, especie.getTipo().name());
            ps.setInt(5, especie.getEnergiaInicial());
            ps.setString(6, especie.getUrlFoto());
            ps.setInt(7,0);
            //ojo, no estamos guardando el inventario!
            ps.execute();

            if (ps.getUpdateCount() != 1) {
                throw new RuntimeException("No se inserto la especie " + especie);
            }
            ps.close();

            return null;
        });

    }

    @Override
    public void actualizar(Especie especie) {

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

                especie = new Especie(nombreEspecie, tipo);
                especie.setAltura(resultSet.getInt("altura"));
                especie.setPeso(resultSet.getInt("peso"));
                especie.setEnergiaInicial(resultSet.getInt("energiaInicial"));
                especie.setUrlFoto(resultSet.getString("urlFoto"));
                especie.setCantidadBichos(resultSet.getInt("cantidadDeBichos"));

            ps.close();
            return especie;
        };
    });
    }

    @Override
    public List<Especie> recuperarTodos() {
        return null;
    }
}
