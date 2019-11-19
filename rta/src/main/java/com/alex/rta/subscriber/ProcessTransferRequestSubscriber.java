package com.alex.rta.subscriber;

import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.api.account.IAccountService;

public class ProcessTransferRequestSubscriber implements ISubscriber<TransferRequest> {
    private final IAccountService accountService;

    public ProcessTransferRequestSubscriber(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void next(TransferRequest request) {
        accountService.transferFunds(
                request.getSourceAccountId(),
                request.getTargetAccountId(),
                request.getAmount()
        );
    }
}
