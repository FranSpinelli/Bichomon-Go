package DAOandServicesTests;

import static ar.edu.unq.epers.bichomon.backend.model.especie.TipoBicho.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import ar.edu.unq.epers.bichomon.backend.dao.EspecieDAO;
import ar.edu.unq.epers.bichomon.backend.dao.impl.JDBCEspecieDAO;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import org.junit.Before;
import org.junit.Test;

public class EspecieJDBCDAOTest {

    private EspecieDAO dao = new JDBCEspecieDAO();
    private Especie pacacho;

    @Before
    public void crearModelo() {
        this.pacacho = new Especie(1,"Pacachu", ELECTRICIDAD);
        this.pacacho.setAltura(400);//en cm
        this.pacacho.setPeso(5);
        this.pacacho.setUrlFoto("https://assets.pokemon.com/assets/cms2/img/pokedex/full/025.png");
        this.pacacho.setEnergiaIncial(10);
        this.pacacho.setCantidadBichos(0);
    }

    @Test
    public void al_guardar_y_luego_recuperar_se_obtiene_objetos_similares() {
        this.dao.guardar(this.pacacho);

        //Los personajes son iguales
        Especie otroPacacho = this.dao.recuperar("Maguito");
        assertEquals(this.pacacho.getNombre(), otroPacacho.getNombre());
        assertEquals(this.pacacho.getAltura(), otroPacacho.getAltura());
        assertEquals(this.pacacho.getPeso(), otroPacacho.getPeso());
        assertEquals(this.pacacho.getUrlFoto(), otroPacacho.getUrlFoto());
        assertEquals(this.pacacho.getEnergiaInicial(), otroPacacho.getEnergiaInicial());
        assertEquals(this.pacacho.getTipo(), otroPacacho.getTipo());
        assertEquals(this.pacacho.getCantidadBichos(), otroPacacho.getCantidadBichos());
        //assertEquals(this.pacacho.getId(), otroPacacho.getId());
        //Pero no son el mismo objeto =(
        //A esto nos referimos con "perdida de identidad"
        assertTrue(this.pacacho != otroPacacho);
    }

}
