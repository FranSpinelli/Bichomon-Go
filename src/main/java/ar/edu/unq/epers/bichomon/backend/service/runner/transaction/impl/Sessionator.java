package ar.edu.unq.epers.bichomon.backend.service.runner.transaction.impl;

import ar.edu.unq.epers.bichomon.backend.service.runner.transaction.TransactionType;

public abstract class Sessionator {

    public Object getCurrentSession(TransactionType transactionType) {
        if (this.getSession() == null) {
            throw new RuntimeException("No hay ninguna session en el contexto");
        }
        return this.getSession();
    }

    protected abstract Object getSession();

    public abstract TransactionType getTransactionType();

}
