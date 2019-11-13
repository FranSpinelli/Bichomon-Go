package ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.SessionatorType;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;

import java.util.Set;

public class TransactionManager implements Transaction {
    private Set<ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction> txs;

    @Override
    public void commit() {
        this.txs.forEach(tx -> tx.commit());
    }

    @Override
    public void begin() {
        this.txs.forEach(tx -> tx.begin());
    }

    @Override
    public void rollback() {
        this.txs.forEach(tx -> tx.rollback());
    }

    @Override
    public void close() {
        this.txs.forEach(tx -> tx.close());
    }

    @Override
    public SessionatorType getSessionatorType() {
        throw new RuntimeException();
    }

    public Transaction addTransaction(ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction tx){
        this.txs.add(tx);
        return this;
    }

    public Object getCurrentSession(SessionatorType sessionatorType){
        return txs.stream().filter(tx -> tx.getSessionatorType() == sessionatorType).findFirst().get().getCurrentSession(sessionatorType);
    }
}
