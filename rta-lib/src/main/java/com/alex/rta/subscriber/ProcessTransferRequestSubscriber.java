package com.alex.rta.subscriber;

import com.alex.rta.data.requests.RequestState;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.api.account.IAccountService;
import com.alex.rta.log.LogUtils;
import com.alex.rta.statusUpdateRepository.StatusUpdateRepository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;

public class ProcessTransferRequestSubscriber implements ISubscriber<TransferRequest> {
    private final IAccountService accountService;
    private final Executor reactor;

    public ProcessTransferRequestSubscriber(IAccountService accountService, Executor reactor) {
        this.accountService = accountService;
        this.reactor = reactor;
    }

    @Override
    public void next(TransferRequest request) {
        reactor.execute(() -> nextImpl(request));
    }

    private void nextImpl(TransferRequest request) {
        LogUtils.log("ProcessTransferRequestSubscriber Processing " + request.getId());

        try {
            Future<Void> future = accountService.transferFunds(
                    request.getSourceAccountId(),
                    request.getTargetAccountId(),
                    request.getAmount()
            );
            future.get();
            StatusUpdateRepository.getInstance().update(
                    request.getId(),
                    RequestState.SUCCEEDED,
                    null
            );
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            StatusUpdateRepository.getInstance().update(
                    request.getId(),
                    RequestState.FAILED,
                    null
            );
        }
    }
}
