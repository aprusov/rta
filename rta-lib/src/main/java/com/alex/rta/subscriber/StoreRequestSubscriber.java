package com.alex.rta.subscriber;

import com.alex.rta.data.storage.db.DbConnectionTarget;
import com.alex.rta.log.LogUtils;
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
        LogUtils.log("StoreRequestSubscriber Processing " + json);
        db.execute(db -> {
            db
                    .insertInto(Requests.REQUESTS)
                    .columns(Requests.REQUESTS.TYPE, Requests.REQUESTS.DATA)
                    .values(requestType, json)
                    .execute();
        });
    }
}
