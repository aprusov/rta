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

import java.util.concurrent.ThreadFactory;

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
    public void subscribe(ISubscriber subscriber) {
        EventHandler<TransferRequest> eventHandler =
                (event, sequence, endOfBatch) -> subscriber.next(event);
        disruptor.handleEventsWith(new EventHandler[]{eventHandler});
    }
}
