package com.alex.rta.data.storage.db;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.function.Consumer;

public class DbConnectionTarget {

    private String url;
    private final SQLDialect dialect;

    public DbConnectionTarget(String url, SQLDialect dialect) {
        this.url = url;
        this.dialect = dialect;
    }

    public void execute(Consumer<DSLContext> consumer) {
        try (Connection conn = DriverManager.getConnection(url)) {
            DSLContext create = DSL.using(conn, dialect);
            consumer.accept(create);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void executeTransaction(Consumer<DSLContext> consumer) {
        try (Connection conn = DriverManager.getConnection(url)) {
            DSLContext create = DSL.using(conn, dialect);
            create.transaction(() -> {
                consumer.accept(create);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
