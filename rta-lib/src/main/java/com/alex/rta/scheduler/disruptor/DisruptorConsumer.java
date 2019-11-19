package com.alex.rta.scheduler.disruptor;

import com.alex.rta.data.requests.transfer.TransferRequest;
import com.alex.rta.scheduler.IScheduler;
import com.alex.rta.subscriber.ISubscriber;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class DisruptorConsumer implements IScheduler<TransferRequest> {
    private final Disruptor<TransferRequestEvent> disruptor;
    private RingBuffer<TransferRequestEvent> ringBuffer;

    public DisruptorConsumer() {
        ThreadFactory threadFactory = DaemonThreadFactory.INSTANCE;

        WaitStrategy waitStrategy = new BusySpinWaitStrategy();
        disruptor = new Disruptor<>(
                TransferRequestEvent::new,
                16,
                threadFactory,
                ProducerType.SINGLE,
                waitStrategy);
    }

    public void start() {
        ringBuffer = disruptor.start();
    }

    @Override
    public void next(TransferRequest request) {
        long sequenceId = ringBuffer.next();
        TransferRequestEvent event = ringBuffer.get(sequenceId);
        event.setTransferRequest(request);
        ringBuffer.publish(sequenceId);
    }

    @Override
    public void subscribe(ISubscriber<TransferRequest>... subscribers) {
        EventHandler[] eventHandlers = Arrays.stream(subscribers)
                .map(DisruptorConsumer::getEventHandler)
                .toArray(EventHandler[]::new);
        disruptor.handleEventsWith(eventHandlers);
    }

    private static EventHandler<TransferRequestEvent> getEventHandler(ISubscriber<TransferRequest> x){
        return (TransferRequestEvent event, long sequence, boolean endOfBatch) ->
                x.next(event.getTransferRequest());
    }
}
