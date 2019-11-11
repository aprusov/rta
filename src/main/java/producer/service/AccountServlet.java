package producer.service;

import consumer.ITransferConsumer;
import data.TransferRequest;
import data.TransferRequestState;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;

public class AccountServlet extends HttpServlet {
    private final ITransferConsumer consumer;
    private final HashSet<ResponsePublisher> publishers = new HashSet<>();

    public AccountServlet(ITransferConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        final TransferRequest transferRequest = new TransferRequest();
        Observable<TransferRequestState> stateObservable = this.consumer.next(transferRequest);
        ResponsePublisher responsePublisher = new ResponsePublisher(transferRequest, stateObservable, resp);
        this.publishers.add(responsePublisher);
    }

    private class ResponsePublisher {
        private final Disposable subscribtion;

        public ResponsePublisher(TransferRequest transferRequest, Observable<TransferRequestState> stateObservable, HttpServletResponse response) {
            subscribtion = stateObservable.subscribe(x -> {
                if (x.equals(TransferRequestState.SUCCEEDED)) {
                    publishers.remove(this);
                }
            });
        }
    }
}
