package com.alex.rta.subscriber;

import com.alex.rta.data.TransferRequest;
import com.alex.rta.data.storage.db.DbConnectionTarget;

public class StoreTransferRequestSubscriber implements ITransferSubscriber {

    private final DbConnectionTarget db;

    public StoreTransferRequestSubscriber(DbConnectionTarget db) {
        this.db = db;
    }

    @Override
    public void next(TransferRequest request) {
        db.execute(jooq -> {

        });
    }
}
