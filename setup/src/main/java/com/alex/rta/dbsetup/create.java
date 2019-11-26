package com.alex.rta.dbsetup;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class create {
    private final Connection connection;

    public create(Connection connection) {
        this.connection = connection;
    }

    public void execute() throws SQLException {

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.execute("drop table if exists accounts");
        statement.execute("create table accounts (id bigint, name varchar(200), balance double)");

        statement.execute("drop table if exists requests");
        statement.execute("create table requests (id bigint, type tinyint, data varchar(4000))");
    }

}
