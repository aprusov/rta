package com.alex.rta.data.requests.get;

import com.alex.rta.data.requests.RequestBase;

public class GetRequest extends RequestBase {
    private long sourceSystemId;
    private long accountId;

    public GetRequest(long sourceSystemId, long accountId) {
        super();
        this.sourceSystemId = sourceSystemId;
        this.accountId = accountId;
    }

    public long getSourceSystemId() {
        return sourceSystemId;
    }

    public void setSourceSystemId(long sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }
}
