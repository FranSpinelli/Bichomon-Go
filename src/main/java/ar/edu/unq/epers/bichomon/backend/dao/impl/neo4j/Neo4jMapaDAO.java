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


    public int caminoMasCorto(String origenNombre, String destinoNombre) {
        Session session = this.driver.session();

        try {
            String query = "MATCH p=(a:Ubicacion {name: nombreOrigen})-[r:CONNECTS*..10]->(b:Ubicacion {name:nombreDestino}) " +
                    "WITH p, reduce( " +
                    "cost=0, x IN rels(p) | cost + (x.precio)) as cost " +
                    "RETURN cost " +
                    "ORDER BY cost ASC " +
                    "LIMIT 1 ";
            StatementResult result = session.run(query, Values.parameters("nombreDestino", destinoNombre,
                    "nombreOrigen", origenNombre));
            //Similar a list.stream().map(...)
            return result.next().get("cost").asInt();
        } finally {
            session.close();
        }
    }
    public List<String> conectados(String origenNombre, String tipoCamino) {
        Session session = this.driver.session();
        try {
            String query = "MATCH (origen:Ubicacion {name: nombreOrigen}) " +
                    "MATCH (origen)-[:CONECTADA_CON {tipoDeConeccion: tipoConeccion]->(destino) " +
                    "RETURN destino";
            StatementResult result = session.run(query, Values.parameters("nombreDestino", origenNombre,
                    "nombreOrigen", tipoCamino));
            //Similar a list.stream().map(...)
            return result.list(ubication -> ubication.get(0).get("name").asString());
        } finally {
            session.close();
        }
    }
    public int caminoDeUno(String origenNombre, String destinoNombre) {
        Session session = this.driver.session();
        try {
            String query = "MATCH (origen:Ubicacion {name: nombreOrigen}) " +
                    "MATCH (destino:Ubicacion {name: nombreDestino}) " +
                    "MATCH (origen)-[r]->(destino) " +
                    "RETURN r.precio";
            StatementResult result = session.run(query, Values.parameters("nombreOrigen", origenNombre,
                    "nombreDestino", destinoNombre));
            return result.next().get(0).get("precio").asInt();
        } finally {
            session.close();
        }
    }
}
