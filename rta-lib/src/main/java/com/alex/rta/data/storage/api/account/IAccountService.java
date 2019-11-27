package com.alex.rta.data.storage.api.account;

import java.util.concurrent.Future;

public interface IAccountService {
    Future<Double> getAmount(long accountId);
    Future<Void> transferFunds(long sourceAccountId, long targetAccountId, double amount);
}
