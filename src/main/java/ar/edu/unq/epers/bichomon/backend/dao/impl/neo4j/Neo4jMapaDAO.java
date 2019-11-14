package ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j;

import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import org.neo4j.driver.v1.*;

import java.util.List;

public class Neo4jMapaDAO {

    public void create(Ubicacion ubicacion) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J); //this.driver.session();

        //CREATE (n:Ubicacion {name: {elNombre}})
        String query = "MERGE (n:Ubicacion {name: {elNombre}}) ";
        session.run(query, Values.parameters("elNombre", ubicacion.getNombre()));
    }

    public void conectar(Ubicacion origen, Ubicacion destino, TipoCamino tipoCamino) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (origen:Ubicacion  {name: {nombreOrigen}}) " +
                "MATCH (destino:Ubicacion {name: {nombreDestino}}) " +
                "MERGE (origen)-[:CONECTADA_CON {tipoDeCamino: {tipoCamino}, precio: {precioDeLaRuta}}]->(destino)"; //CREATE
        session.run(query, Values.parameters("nombreOrigen", origen.getNombre(),
                "nombreDestino", destino.getNombre(),
                "tipoCamino", tipoCamino.getName(),
                "precioDeLaRuta", tipoCamino.getPrecio()
        ));
    }


    public Integer costoCaminoMasCorto(String origenNombre, String destinoNombre) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);
        String query = "MATCH p=shortestPath((origen:Ubicacion {nombre:{nombreOrigen}})-[*]->(destino:Ubicacion {nombre:{nombreDestino}})) " +
                "RETURN reduce(costo=0, r in relationships(p): costo + r.precio) as costo";
                /*"MATCH p=(a:Ubicacion {name: nombreOrigen})-[r:CONNECTS*..10]->(b:Ubicacion {name:nombreDestino}) " +
                "WITH p, reduce( " +
                "cost=0, x IN rels(p) | cost + (x.precio)) as cost " +
                "RETURN cost " +
                "ORDER BY cost ASC " +
                "LIMIT 1 ";*/
        StatementResult result = session.run(query, Values.parameters("nombreDestino", destinoNombre,
                "nombreOrigen", origenNombre));
        //Similar a list.stream().map(...)
        return result.next().get("costo").asInt();
    }

    public List<String> conectados(String nombreOrigen, String tipoCamino) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (origen:Ubicacion {name: {nombreOrigen}}) " +
                "MATCH (origen)-[:CONECTADA_CON {tipoDeCamino: {tipoCamino}]->(destino) " +
                "RETURN destino";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", nombreOrigen,
                "tipoCamino", tipoCamino));
        //Similar a list.stream().map(...)
        return result.list(ubication -> ubication.get(0).get("name").asString());
    }

    public int caminoDeUno(String nombreOrigen, String nombreDestino) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (origen:Ubicacion {name: {nombreOrigen}}) " +
                "MATCH (destino:Ubicacion {name: {nombreDestino}}) " +
                "MATCH (origen)-[r]->(destino) " +
                "RETURN r.precio";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", nombreOrigen,
                "nombreDestino", nombreDestino));
        return result.next().get(0).get("precio").asInt();
    }

    public Integer costoCaminoMasBarato(Ubicacion origen, Ubicacion destino) {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH p=(origen:Ubicacion {nombre:{nombreOrigen}})-[*]->(destino:Ubicacion {nombre:{nombreDestino}}) " +
                "RETURN reduce(costo=0, r in relationships(p): costo + r.precio) as costo" +
                "ORDER BY costo ASC " +
                "LIMIT 1";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", origen.getNombre(),
                "nombreDestino", destino.getNombre()));
        return result.next().get(0).get("costo").asInt();
    }

    public void deleteAll() {
        Session session = (Session) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (n) DETACH DELETE n";
        session.run(query);
    }
}
