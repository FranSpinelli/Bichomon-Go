package ar.edu.unq.epers.bichomon.backend.dao.impl.neo4j;

import ar.edu.unq.epers.bichomon.backend.model.camino.TipoCamino;
import ar.edu.unq.epers.bichomon.backend.model.ubicacion.Ubicacion;
import ar.edu.unq.epers.bichomon.backend.service.runner.TransactionRunner;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.exceptions.NoSuchRecordException;

import java.util.List;

public class Neo4jMapaDAO {

    public void create(Ubicacion ubicacion) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J); //this.driver.session();

        //CREATE (n:Ubicacion {name: {elNombre}})
        String query = "CREATE (n:Ubicacion {name: {elNombre}}) ";
        session.run(query, Values.parameters("elNombre", ubicacion.getNombre()));
    }

    public void conectar(Ubicacion origen, Ubicacion destino, TipoCamino tipoCamino) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

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
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);
        String query = "MATCH p=shortestPath((origen:Ubicacion {name:{nombreOrigen}})-[*]->(destino:Ubicacion {name:{nombreDestino}})) " +
                "RETURN reduce(costo=0, r in relationships(p) | costo + r.precio) as costo";
        StatementResult result = session.run(query, Values.parameters("nombreDestino", destinoNombre,
                "nombreOrigen", origenNombre));
        //Similar a list.stream().map(...)
        try{
            return result.next().get("costo").asInt();
        }
        catch(NoSuchRecordException e){
            return null;
        }

    }

    public List<String> conectados(String nombreOrigen, String tipoCamino) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (origen:Ubicacion {name: {nombreOrigen}}) " +
                "MATCH (origen)-[:CONECTADA_CON {tipoDeCamino: {tipoCamino}}]->(destino) " +
                "RETURN destino";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", nombreOrigen,
                "tipoCamino", tipoCamino));
        //Similar a list.stream().map(...)
        return result.list(ubication -> ubication.get(0).get("name").asString());
    }
/*
    public int caminoDeUno(String nombreOrigen, String nombreDestino) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH (origen:Ubicacion {name: {nombreOrigen}}) " +
                "MATCH (destino:Ubicacion {name: {nombreDestino}}) " +
                "MATCH (origen)-[r]->(destino) " +
                "RETURN r.precio";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", nombreOrigen,
                "nombreDestino", nombreDestino));
        return result.next().get(0).get("precio").asInt();
    }*/

    public Integer costoCaminoMasBarato(Ubicacion origen, Ubicacion destino) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);

        String query = "MATCH p=(origen:Ubicacion {name:{nombreOrigen}})-[*]->(destino:Ubicacion {name:{nombreDestino}}) " +
                "RETURN reduce(costo=0, r in relationships(p) | costo + r.precio) as costo " +
                "ORDER BY costo ASC " +
                "LIMIT 1";
        StatementResult result = session.run(query, Values.parameters("nombreOrigen", origen.getNombre(),
                "nombreDestino", destino.getNombre()));
        try{
            return result.next().get("costo").asInt();
        }
        catch(NoSuchRecordException e){
            return null;
        }
    }

    public void deleteAll() {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);
        String query = "MATCH (n) DETACH DELETE n";
        session.run(query);
    }

    public String recuperar(String ubicacion) {
        Transaction session = (Transaction) TransactionRunner.getCurrentSession(TransactionType.NEO4J);
        String query = "MATCH (n:Ubicacion {name: {nombre}}) RETURN n.name";
        StatementResult result = session.run(query, Values.parameters("nombre", ubicacion));
        try{
            return result.next().get("n.name").asString();
        }
        catch(NoSuchRecordException e){
            return null;
        }
    }
}
