package com.alex.rta.statusUpdateRepository;

import com.alex.rta.data.requests.RequestState;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import java.util.*;

public class StatusUpdateRepository {
    private StatusUpdateRepository() {
    }

    private static StatusUpdateRepository _instance = new StatusUpdateRepository();
    private Map<Long, IExternalListener> listeners = new HashMap<>();

    public static StatusUpdateRepository getInstance() {
        return _instance;
    }

    public <T> void setListener(Long requestId, Consumer<RequestStatus<T>> onNext) {
        listeners.put(requestId, new ExternalListener<>(onNext));
    }

    public <T> void update(Long requestId, RequestState state, T value) {
        IExternalListener listener = listeners.getOrDefault(requestId, null);
        if (listener == null) {
            return;
        }
        boolean isTerminated = listener.update(state, value);
        if (isTerminated) {
            listeners.remove(requestId);
        }
    }

    interface IExternalListener {
        boolean update(RequestState state, Object value) ;
    }

    class ExternalListener<T> implements IExternalListener {
        private final PublishSubject<RequestStatus<T>> subject = PublishSubject.create();
        private final Disposable subscription;

        public ExternalListener(Consumer<RequestStatus<T>> onNext) {
            subscription = subject.subscribe(onNext);
        }

        public boolean update(RequestState state, Object value) {
            subject.onNext(new RequestStatus<T>(state, (T)value));
            boolean isTerminal = state == RequestState.SUCCEEDED || state == RequestState.FAILED;
            if (isTerminal) {
                subscription.dispose();
            }
            return isTerminal;
        }
    }
}
