package data;

import java.util.concurrent.atomic.AtomicLong;

public class TransferRequest {
    private static AtomicLong ID = new AtomicLong();

    public TransferRequest(AccountInfo source, AccountInfo target, double amount) {
        this.id = ID.getAndIncrement();
        this.source = source;
        this.target = target;
        this.amount = amount;
    }

    private final long id;
    private final AccountInfo source;
    private final AccountInfo target;
    private final double amount;

    public long getId() {
        return id;
    }

    public AccountInfo getSource() {
        return source;
    }

    public AccountInfo getTarget() {
        return target;
    }

    public double getAmount() {
        return amount;
    }
}
