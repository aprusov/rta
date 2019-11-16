package com.alex.rta.data.storage.db;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Consumer;

public class DbConnectionTarget {

    private String url;
    public DbConnectionTarget(String url) {
        this.url = url;
    }

    public void execute(Consumer<DSLContext> consumer) {
        try (Connection conn = DriverManager.getConnection(url)) {
            DSLContext create = DSL.using(conn, SQLDialect.MYSQL);
            consumer.accept(create);
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
