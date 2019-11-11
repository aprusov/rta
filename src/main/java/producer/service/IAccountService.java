package producer.service;

import data.AccountInfo;

import java.util.concurrent.Future;

public interface IAccountService {
    Future<Boolean> transfer (AccountInfo source, AccountInfo target, double amount);
}
