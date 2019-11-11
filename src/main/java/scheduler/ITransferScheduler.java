package scheduler;

import subscriber.ITransferSubscriber;
import data.TransferRequest;

public interface ITransferScheduler {
    void next(TransferRequest request);
    void subscribe(ITransferSubscriber subscriber);
}
