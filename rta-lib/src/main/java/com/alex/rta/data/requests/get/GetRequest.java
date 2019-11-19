package com.alex.rta.data.requests.get;

public class GetRequest {
    private long sourceSystemId ;
    private long accountId;

    public GetRequest(long sourceSystemId, long accountId) {
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
