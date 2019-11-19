package com.alex.rta.producer.service.get;

import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.subscriber.ISubscriber;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetServlet extends HttpServlet implements IRequestEndpoint<GetRequest> {
    private ISubscriber subscriber;
    private final Gson gson;

    public GetServlet() {
        gson = new Gson();
    }

    public void setSubscriber(ISubscriber<GetRequest> subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final GetRequest transferRequest = parseRequest(req);
        this.subscriber.next(transferRequest);
    }

    private GetRequest parseRequest(HttpServletRequest req) {
        String queryString = req.getQueryString();
        GetServletData data = gson.fromJson(queryString, GetServletData.class);
        return new GetRequest(data.sourceSystemId, data.sourceAccountId);
    }

}
