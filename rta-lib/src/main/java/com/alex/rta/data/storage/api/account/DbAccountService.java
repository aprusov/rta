package com.alex.rta.data.storage.api.account;

import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.db.alex.rta.codegen.tables.Accounts;
import com.db.alex.rta.codegen.tables.records.AccountsRecord;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DbAccountService implements IAccountService {
    private final DbConnectionTarget dbConnectionTarget;

    private static Executor executor = Executors.newSingleThreadExecutor();

    public DbAccountService(DbConnectionTarget dbConnectionTarget) {
        this.dbConnectionTarget = dbConnectionTarget;
    }

    @Override
    public Future<Double> getAmount(long accountId) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        executor.execute(() -> {
            final Double[] result = new Double[1];
            this.dbConnectionTarget.execute(db -> {
                AccountsRecord account = db.fetchOne(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ACCOUNTID.eq(accountId));
                result[0] = account.get(Accounts.ACCOUNTS.BALANCE);
            });
            if(result[0] != null)
            {
                future.complete(result[0]);
            } else {
                future.completeExceptionally(new RuntimeException(""));
            }
        });
        return future;
    }

    @Override
    public Future<Void> transferFunds(long sourceAccountId, long targetAccountId, double amount) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        executor.execute(() -> {

            // Assume This is slow

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final Boolean[] result = new Boolean[1];
            // TODO: should be executeTransaction
            this.dbConnectionTarget.execute(db -> {
                AccountsRecord source = db.fetchOne(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ACCOUNTID.eq(sourceAccountId));
                AccountsRecord target = db.fetchOne(Accounts.ACCOUNTS, Accounts.ACCOUNTS.ACCOUNTID.eq(targetAccountId));

                if(source == null || target==null)
                {
                    result[0] = false;
                    return;
                }
                Double sourceBalance = source.getBalance();
                source.setBalance(sourceBalance - amount);
                source.store();

                Double targetBalance = target.getBalance();
                target.setBalance(targetBalance + amount);
                target.store();

                result[0] = false;
            });
            if(result[0])
            {
                future.complete(null);
            } else {
                future.completeExceptionally(new RuntimeException(""));
            }
        });
        return future;
    }
}
