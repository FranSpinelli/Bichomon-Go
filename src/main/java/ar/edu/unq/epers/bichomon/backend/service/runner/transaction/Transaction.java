package ar.edu.unq.epers.bichomon.backend.service.runner.transaction;

public interface Transaction {

    void commit();

    void begin();

    void rollback();

    void close();

    Object getCurrentSession(TransactionType transactionType);

    TransactionType getTransactionType();
}
