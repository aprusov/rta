package subscriber;

import data.TransferRequest;

public interface ITransferSubscriber {
    void onRequest(TransferRequest request);
}
