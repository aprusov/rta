package com.alex.rta.subscriber;

import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.api.account.IAccountService;

public class ProcessGetRequestSubscriber implements ISubscriber<GetRequest> {
    private final IAccountService accountService;

    public ProcessGetRequestSubscriber(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void next(GetRequest request) {
        double amount = accountService.getAmount(request.getAccountId());
    }
}
