package com.alex.rta.producer.service.transfer;

import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.requests.transfer.TransferRequestState;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.subscriber.ISubscriber;
import com.google.gson.Gson;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

public class TransferServlet extends HttpServlet implements IRequestEndpoint<TransferRequest> {
    private ISubscriber subscriber;
    private final Gson gson;
    private final HashSet<ResponsePublisher> publishers = new HashSet<>();

    public TransferServlet() {
        gson = new Gson();
    }

    public void setSubscriber(ISubscriber<TransferRequest> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final TransferRequest transferRequest = parseRequest(req);
        this.subscriber.next(transferRequest);
        ResponsePublisher responsePublisher = new ResponsePublisher(transferRequest, resp);
        this.publishers.add(responsePublisher);
    }

    private TransferRequest parseRequest(HttpServletRequest req) {
        String queryString = req.getQueryString();
        TransferServletData data = gson.fromJson(queryString, TransferServletData.class);
        TransferRequest transferRequest = new TransferRequest(data.sourceSystemId, data.sourceAccountId, data.targetAccountId, data.amount);
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