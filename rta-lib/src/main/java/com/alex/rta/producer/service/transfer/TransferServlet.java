package com.alex.rta.producer.service.transfer;

import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.requests.transfer.TransferRequestState;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.producer.service.ServletUtils;
import com.alex.rta.statusUpdateRepository.StatusUpdateRepository;
import com.alex.rta.subscriber.ISubscriber;
import com.google.gson.Gson;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

public class TransferServlet extends HttpServlet implements IRequestEndpoint<TransferRequest> {
    private ISubscriber<TransferRequest> subscriber;
    private final Gson gson;

    public TransferServlet() {
        gson = new Gson();
    }

    public void setSubscriber(ISubscriber<TransferRequest> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final TransferRequest transferRequest = parseRequest(req);
            if (transferRequest == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            this.subscriber.next(transferRequest);

            StatusUpdateRepository.getInstance().setListener(
                    transferRequest.getId(),
                    x->requestStatusListener(x, resp),
                    TransferRequestState.SUCCEEDED, TransferRequestState.FAILED
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private TransferRequest parseRequest(HttpServletRequest req) {
        String body = ServletUtils.getRequestBody(req);
        if (body == null) {
            return null;
        }
        TransferServletData data = gson.fromJson(body, TransferServletData.class);
        TransferRequest transferRequest = new TransferRequest(data.sourceSystemId, data.sourceAccountId, data.targetAccountId, data.amount);
        return transferRequest;
    }

    private static void requestStatusListener(TransferRequestState x, final HttpServletResponse resp) {
        if (x == TransferRequestState.SUCCEEDED) {
            resp.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
