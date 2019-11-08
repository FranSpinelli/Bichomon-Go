package ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j;

import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import org.neo4j.driver.v1.*;

import java.lang.reflect.Array;
import java.util.List;

public class Neo4jMapaDAO {

    private Driver driver;

    public Neo4jMapaDAO(){
        this.driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );
    }

    public void create(Ubicacion ubicacion) {
        Session session = this.driver.session();

        try {
            //CREATE (n:Ubicacion {name: {elNombre}})
            String query = "MERGE (n:Ubicacion {name: {elNombre}}) ";
            session.run(query, Values.parameters("elNombre", ubicacion.getNombre()));
        } finally {
            session.close();
        }
    }

    public void crearRelacionEstaConectadaPor(Ubicacion origen, Ubicacion destino, TipoCamino ruta) {
        Session session = this.driver.session();

        try {
            String query = "MATCH (origen:Ubicacion  {name: {nombreOrigen}}) " +
                    "MATCH (destino:Ubicacion {name: {nombreDestino}}) " +
                    "CREATE (origen)-[:CONECTADA_CON {tipoDeConeccion: tipoConeccion, precio: precioDeLaRuta}]->(destino)"; //CREATE
            session.run(query, Values.parameters("nombreOrigen", origen.getNombre(),
                    "nombreDestino", destino.getNombre(),
                    "tipoConeccion", ruta.getName(),
                    "precioDeLaRuta", ruta.getPrecio()
            ));

        } finally {
            session.close();
        }
    }

    public List<int> conectados(String ubicacionNombre, String origenNombre) {
        Session session = this.driver.session();

        try {
            String query = "MATCH p=shortestPath((origen:Ubicacion {name:nombreOrigen})-[*]->(destino:Ubicacion {name:nombreDestino}) " +
            "RETURN p";
            StatementResult result = session.run(query, Values.parameters("nombreOrigen", ubicacionNombre,
                    "nombreOrigen", origenNombre
            ));
            //Similar a list.stream().map(...)
            return result.list(ubication -> {
                Value algo = ubication.get(0);
                int costo = algo.get("precio").asInt();
                return costo;
            });
        } finally {
            session.close();
        }
    /*MATCH p=shortestPath(
(bacon:Ubicacion {name:"Johto"})-[*]->(meg:Ubicacion {name:"Pokecenter"})
)
RETURN p*/


}
