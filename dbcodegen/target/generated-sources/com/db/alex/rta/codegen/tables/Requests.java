/*
 * This file is generated by jOOQ.
 */
package com.db.alex.rta.codegen.tables;


import com.db.alex.rta.codegen.DefaultSchema;
import com.db.alex.rta.codegen.tables.records.RequestsRecord;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Requests extends TableImpl<RequestsRecord> {

    private static final long serialVersionUID = -2108901369;

    /**
     * The reference instance of <code>requests</code>
     */
    public static final Requests REQUESTS = new Requests();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<RequestsRecord> getRecordType() {
        return RequestsRecord.class;
    }

    /**
     * The column <code>requests.id</code>.
     */
    public final TableField<RequestsRecord, Long> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.BIGINT, this, "");

    /**
     * The column <code>requests.type</code>.
     */
    public final TableField<RequestsRecord, Byte> TYPE = createField(DSL.name("type"), org.jooq.impl.SQLDataType.TINYINT, this, "");

    /**
     * The column <code>requests.data</code>.
     */
    public final TableField<RequestsRecord, String> DATA = createField(DSL.name("data"), org.jooq.impl.SQLDataType.VARCHAR(4000), this, "");

    /**
     * Create a <code>requests</code> table reference
     */
    public Requests() {
        this(DSL.name("requests"), null);
    }

    /**
     * Create an aliased <code>requests</code> table reference
     */
    public Requests(String alias) {
        this(DSL.name(alias), REQUESTS);
    }

    /**
     * Create an aliased <code>requests</code> table reference
     */
    public Requests(Name alias) {
        this(alias, REQUESTS);
    }

    private Requests(Name alias, Table<RequestsRecord> aliased) {
        this(alias, aliased, null);
    }

    private Requests(Name alias, Table<RequestsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""));
    }

    public <O extends Record> Requests(Table<O> child, ForeignKey<O, RequestsRecord> key) {
        super(child, key, REQUESTS);
    }

    @Override
    public Schema getSchema() {
        return DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Requests as(String alias) {
        return new Requests(DSL.name(alias), this);
    }

    @Override
    public Requests as(Name alias) {
        return new Requests(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Requests rename(String name) {
        return new Requests(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Requests rename(Name name) {
        return new Requests(name, null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, Byte, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }
}