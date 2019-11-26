package com.alex.rta.statusUpdateRepository;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatusUpdateRepository {
    private StatusUpdateRepository() {
    }

    private static StatusUpdateRepository _instance = new StatusUpdateRepository();
    private Map<Long, IExternalListener> listeners = new HashMap<>();

    public static StatusUpdateRepository getInstance() {
        return _instance;
    }

    public <T> void setListener(Long requestId, Consumer<? super T> onNext, T... terminalStates) {
        listeners.put(requestId, new ExternalListener<T>(onNext, terminalStates));
    }

    public <T> void update(Long requestId, T state) {
        IExternalListener listener = listeners.getOrDefault(requestId, null);
        if (listener == null) {
            return;
        }
        boolean isTerminated = listener.update(state);
        if(isTerminated){
            listeners.remove(requestId);
        }
    }

    interface IExternalListener {
        boolean update(Object state);
    }

    class ExternalListener<T> implements IExternalListener{
        private final List<T> terminalStates;
        private final PublishSubject<T> subject = PublishSubject.create();
        private final Disposable subscription;

        public ExternalListener(Consumer<? super T> onNext, T... terminalStates) {
            this.terminalStates = Arrays.asList(terminalStates);
            subscription = subject.subscribe(onNext);
        }

        public boolean update(Object state) {
            subject.onNext((T)state);
            boolean isTerminal = terminalStates.contains(state);
            if (isTerminal) {
                subscription.dispose();
            }
            return isTerminal;
        }
    }
}
