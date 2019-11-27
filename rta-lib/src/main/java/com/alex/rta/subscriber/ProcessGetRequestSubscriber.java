package com.alex.rta.subscriber;

import com.alex.rta.data.requests.RequestState;
import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.storage.api.account.IAccountService;
import com.alex.rta.log.LogUtils;
import com.alex.rta.statusUpdateRepository.StatusUpdateRepository;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class ProcessGetRequestSubscriber implements ISubscriber<GetRequest> {
    private final IAccountService accountService;

    public ProcessGetRequestSubscriber(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void next(GetRequest request) {
        LogUtils.log("ProcessGetRequestSubscriber Processing " + request.getId());
        Future<Double> future = accountService.getAmount(request.getAccountId());
        Double amount = null;
        try {
            amount = future.get();
            StatusUpdateRepository.getInstance().update(
                    request.getId(),
                    RequestState.SUCCEEDED,
                    amount
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
