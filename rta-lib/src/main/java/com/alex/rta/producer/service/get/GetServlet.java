package com.alex.rta.producer.service.get;

import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.producer.service.ServletUtils;
import com.alex.rta.producer.service.transfer.TransferServlet;
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
        try {
            final GetRequest transferRequest = parseRequest(req);
            this.subscriber.next(transferRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private GetRequest parseRequest(HttpServletRequest req) {
        String body = ServletUtils.getRequestBody(req);
        if(body == null){
            return null;
        }
        GetServletData data = gson.fromJson(body, GetServletData.class);
        return new GetRequest(data.sourceSystemId, data.accountId);
    }

}
