package ar.edu.unq.epers.bichomon.frontend.api;

import ar.edu.unq.epers.bichomon.backend.model.bicho.Bicho;
import ar.edu.unq.epers.bichomon.backend.model.especie.Especie;
import ar.edu.unq.epers.bichomon.backend.service.ServiceFactory;
import ar.edu.unq.epers.bichomon.backend.service.especie.EspecieService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Esta implementacion decora el servicio {@link EspecieService} devuelto
 * por {@link ServiceFactory} y expone sus metodos a traves de una API
 * REST.
 * 
 * Esto es codigo de frontend, no deberian tocar nada de aca.
 * 
 * @author Steve Frontend
 */
@Path("especie")
public class EspecieServiceREST implements EspecieService {
	
	private final EspecieService decorado;

	public EspecieServiceREST() {
		this.decorado = new ServiceFactory().getEspecieService();
	}

	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public void crearEspecie(Especie especie) {
		this.decorado.crearEspecie(especie);
	}

	@Override
	@GET
	@Path("{nombreEspecie}")
	@Produces(MediaType.APPLICATION_JSON)
	public Especie getEspecie(@PathParam("nombreEspecie") String nombreEspecie) {
		return this.decorado.getEspecie(nombreEspecie);
	}

	@Override
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Especie> getAllEspecies() {
		return this.decorado.getAllEspecies();
	}

	@Override
	@POST
	@Path("{nombreEspecie}/{nombreBicho}")
	@Produces(MediaType.APPLICATION_JSON)
	public Bicho crearBicho(@PathParam("nombreEspecie") String nombreEspecie) {
		return this.decorado.crearBicho(nombreEspecie);
	}

	@Override
	public List<Especie> populares() {
		return this.decorado.populares();
	}

	@Override
	public List<Especie> impopulares() {
		return this.decorado.impopulares();
	}

}
