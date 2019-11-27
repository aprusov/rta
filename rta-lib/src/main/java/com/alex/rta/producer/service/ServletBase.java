package com.alex.rta.producer.service;

import com.alex.rta.data.requests.RequestBase;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.statusUpdateRepository.RequestStatus;
import com.alex.rta.statusUpdateRepository.StatusUpdateRepository;
import com.alex.rta.subscriber.ISubscriber;
import com.google.gson.Gson;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ServletBase<TRequest extends RequestBase, TValue> extends HttpServlet implements IRequestEndpoint<TRequest> {
    private ISubscriber<TRequest> subscriber;
    protected final Gson gson = new Gson();

    public void setSubscriber(ISubscriber<TRequest> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            final TRequest request = parseRequest(req);
            if (request == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            AsyncContext asyncContext = req.startAsync(req, resp);
            StatusUpdateRepository.getInstance().<TValue>setListener(
                    request.getId(),
                    x -> requestStatusListener(request.getId(), x, resp, asyncContext)
            );

            this.subscriber.next(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract TRequest parseRequest(HttpServletRequest req);

    protected abstract void requestStatusListener(long id, RequestStatus<TValue> x, final HttpServletResponse resp, final AsyncContext asyncContext);
}