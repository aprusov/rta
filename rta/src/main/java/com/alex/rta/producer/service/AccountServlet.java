package com.alex.rta.producer.service;

import com.alex.rta.data.TransferRequest;
import com.alex.rta.data.TransferRequestState;
import com.alex.rta.subscriber.ITransferSubscriber;
import com.google.gson.Gson;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

public class AccountServlet extends HttpServlet {
    private ITransferSubscriber subscriber;
    private final Gson gson;
    private final HashSet<ResponsePublisher> publishers = new HashSet<>();

    public AccountServlet() {
        gson = new Gson();
    }

    public void setSubscriber(ITransferSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final TransferRequest transferRequest = parseRequest(req);
        this.subscriber.next(transferRequest);
        ResponsePublisher responsePublisher = new ResponsePublisher(transferRequest, resp);
        this.publishers.add(responsePublisher);
    }

    private TransferRequest parseRequest(HttpServletRequest req){
        String queryString = req.getQueryString();
        AccountServletData accountServletData = gson.fromJson(queryString, AccountServletData.class);
        TransferRequest transferRequest = new TransferRequest(accountServletData.sourceSystemId, accountServletData.sourceAccountId, accountServletData.targetAccountId, accountServletData.amount);
        return transferRequest;
    }

    private class ResponsePublisher {
        private final Disposable subscribtion;

        ResponsePublisher(TransferRequest transferRequest, HttpServletResponse response) {
            PublishSubject<TransferRequestState> stateObservable = PublishSubject.create();
            subscribtion = stateObservable.subscribe(x -> {
                if (x.equals(TransferRequestState.SUCCEEDED)) {
                    publishers.remove(this);
                }
            });
        }
    }
}
