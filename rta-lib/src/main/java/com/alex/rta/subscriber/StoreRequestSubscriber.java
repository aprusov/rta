package com.alex.rta.subscriber;

import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.db.alex.rta.codegen.tables.Requests;
import com.google.gson.Gson;

public class StoreRequestSubscriber<T> implements ISubscriber<T> {

    private final DbConnectionTarget db;
    private final byte requestType;
    private final Gson gson;

    public StoreRequestSubscriber(DbConnectionTarget db, byte requestType) {
        this.db = db;
        this.requestType = requestType;
        gson = new Gson();
    }

    @Override
    public void next(T request) {
        String json = gson.toJson(request);
        db.executeTransaction(db -> {
            db
                    .insertInto(Requests.REQUESTS)
                    .columns(Requests.REQUESTS.ID, Requests.REQUESTS.TYPE, Requests.REQUESTS.DATA)
                    .values(-1L, requestType, json);
        });
    }
}
