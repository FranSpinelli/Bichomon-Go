package ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class Neo4jTransaction extends Sessionator implements Transaction {

    private Session session = null;
    private Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );
    private org.neo4j.driver.v1.Transaction tx = null;

    public Neo4jTransaction(){
        driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "root" ) );
    }

    @Override
    public void commit() {
        tx.success();
    }

    @Override
    public void begin() {
        this.session = this.driver.session();
        this.tx = session.beginTransaction();
    }

    @Override
    public void rollback() {
        tx.failure();
    }

    @Override
    public void close() {
        session.close();
        tx.close();
        session = null;
        tx = null;
    }

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.NEO4J;
    }

    @Override
    protected Object getSession() {
        return this.tx;
    }
}
