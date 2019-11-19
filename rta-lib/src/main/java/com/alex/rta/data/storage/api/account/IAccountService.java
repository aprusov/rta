package com.alex.rta.data.storage.api.account;

public interface IAccountService {
    double getAmount(long accountId);
    double transferFunds(long sourceAccountId, long targetAccountId, double amount);
}
