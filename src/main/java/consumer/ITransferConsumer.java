package consumer;

import data.TransferRequest;
import data.TransferRequestState;
import io.reactivex.Observable;

public interface ITransferConsumer {
    Observable<TransferRequestState> next(TransferRequest request);
}
