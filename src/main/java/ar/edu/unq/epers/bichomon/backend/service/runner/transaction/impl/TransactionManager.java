package ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;
import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionManager implements Transaction {
    private Set<ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction> txsActuales = new HashSet<>();
    private List<Transaction> posiblesTransacciones = new ArrayList<>();

    @Override
    public void commit() {
        this.txsActuales.forEach(tx -> tx.commit());
    }

    @Override
    public void begin() {
        this.txsActuales.forEach(tx -> tx.begin());
    }

    @Override
    public void rollback() {
        this.txsActuales.forEach(tx -> tx.rollback());
    }

    @Override
    public void close() {
        this.txsActuales.forEach(tx -> tx.close());
        this.txsActuales = new HashSet<>();
    }

    @Override
    public TransactionType getTransactionType() {
        throw new RuntimeException();
    }

    public TransactionManager addTransaction(ar.edu.unq.epers.bichomon.backend.service.runner.transaction.Transaction tx){
        this.txsActuales.add(tx);
        return this;
    }

    public TransactionManager addTransaction(TransactionType txType){

        this.txsActuales.add(this.posiblesTransacciones.stream().filter(tx -> tx.getTransactionType() == txType).findFirst().get());
        return this;
    }

    public TransactionManager addPossibleTransaction(Transaction transaction){
        this.posiblesTransacciones.add(transaction);
        return this;
    }

    public Object getCurrentSession(TransactionType transactionType){
        Object x = txsActuales.stream().filter(tx -> tx.getTransactionType() == transactionType).findFirst().get().getCurrentSession(transactionType);
        return  x;
    }
}
