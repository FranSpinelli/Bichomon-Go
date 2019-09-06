package ar.edu.unq.epers.bichomon.backend.dao.impl;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;

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
            ps.setString(5, especie.getTipo());//TODO Revisar
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

                Especie especie = new Especie(resultSet.getInt("id"), resultSet.getString("nombre"), resultSet.getString("tipoDeBicho"));//TODO Revisar tipo de bicho
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
}
