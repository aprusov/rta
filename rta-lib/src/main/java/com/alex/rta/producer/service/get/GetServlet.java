package com.alex.rta.producer.service.get;

import com.alex.rta.data.requests.RequestState;
import com.alex.rta.data.requests.get.GetRequest;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.data.storage.api.account.DbAccountService;
import com.alex.rta.log.LogUtils;
import com.alex.rta.producer.IRequestEndpoint;
import com.alex.rta.producer.service.ServletBase;
import com.alex.rta.producer.service.ServletUtils;
import com.alex.rta.producer.service.transfer.TransferServlet;
import com.alex.rta.producer.service.transfer.TransferServletData;
import com.alex.rta.statusUpdateRepository.RequestStatus;
import com.alex.rta.subscriber.ISubscriber;
import com.google.gson.Gson;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Future;

public class GetServlet extends ServletBase<GetRequest, Double> {

    @Override
    protected GetRequest parseRequest(HttpServletRequest req) {
        String body = ServletUtils.getRequestBody(req);
        if(body == null){
            return null;
        }
        LogUtils.log("New request " + body);
        GetServletData data = gson.fromJson(body, GetServletData.class);
        return new GetRequest(data.sourceSystemId, data.accountId);
    }

    @Override
    protected void requestStatusListener(long id, RequestStatus<Double> x, final HttpServletResponse resp, final AsyncContext asyncContext) {
        if (x.getState() == RequestState.SUCCEEDED) {
            LogUtils.log("Publishing state " + x.getState() + " value " + x.getValue() + " for " + id);
            try {
                PrintWriter writer = resp.getWriter();
                writer.println(x.getValue());
            } catch (IOException e) {
                e.printStackTrace();
            }
            resp.setStatus(HttpServletResponse.SC_OK);
            asyncContext.complete();
        }
    }
}
