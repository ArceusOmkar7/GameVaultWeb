package gamevaultbase.management;

import gamevaultbase.entities.Transaction;
import gamevaultbase.storage.TransactionStorage;

import java.util.List;

public class TransactionManagement {

    private final TransactionStorage transactionStorage;

    public TransactionManagement(TransactionStorage transactionStorage) {
        this.transactionStorage = transactionStorage;
    }

    public Transaction getTransaction(int transactionId) {
        return transactionStorage.findById(transactionId);
    }

    public List<Transaction> getAllTransactions() {
        return transactionStorage.findAll();
    }

    public void addTransaction(Transaction transaction) {
        transactionStorage.save(transaction);
    }
}