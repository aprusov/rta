package com.alex.rta.producer.service.transfer;

import com.alex.rta.data.requests.RequestState;
import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.log.LogUtils;
import com.alex.rta.producer.service.ServletBase;
import com.alex.rta.producer.service.ServletUtils;
import com.alex.rta.statusUpdateRepository.RequestStatus;
import org.eclipse.jetty.util.StringUtil;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TransferServlet extends ServletBase<TransferRequest, Void> {

    @Override
    protected TransferRequest parseRequest(HttpServletRequest req) {
        String body = ServletUtils.getRequestBody(req);
        if (StringUtil.isBlank(body)) {
            return null;
        }
        LogUtils.log("New request " + body);
        TransferServletData data = gson.fromJson(body, TransferServletData.class);
        TransferRequest transferRequest = new TransferRequest(data.sourceSystemId, data.sourceAccountId, data.targetAccountId, data.amount);
        return transferRequest;
    }

    @Override
    protected void requestStatusListener(long id, RequestStatus<Void> x, final HttpServletResponse resp, final AsyncContext asyncContext) {
        if (x.getState() == RequestState.SUCCEEDED) {
            LogUtils.log("Publishing state " + x.getState() + " for " + id);
            resp.setStatus(HttpServletResponse.SC_OK);
            asyncContext.complete();
        }
    }
}
