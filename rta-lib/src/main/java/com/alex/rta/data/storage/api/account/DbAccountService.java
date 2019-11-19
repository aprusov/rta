package com.alex.rta.data.storage.api.account;

import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.db.alex.rta.codegen.tables.Accounts;
import com.db.alex.rta.codegen.tables.records.AccountsRecord;

public class DbAccountService implements IAccountService {
    private final DbConnectionTarget dbConnectionTarget;

    public DbAccountService(DbConnectionTarget dbConnectionTarget) {
        this.dbConnectionTarget = dbConnectionTarget;
    }

    @Override
    public double getAmount(long accountId) {
        final Double[] result = new Double[1];
        this.dbConnectionTarget.execute(db->{
                AccountsRecord record = db
                        .selectFrom(Accounts.ACCOUNTS)
                        .where(Accounts.ACCOUNTS.ID.eq(accountId))
                        .fetchOne();
                result[0] = record.get(Accounts.ACCOUNTS.BALANCE);
            });
        return 0;
    }

    @Override
    public double transferFunds(long sourceAccountId, long targetAccountId, double amount) {

        return 0;
    }
}
