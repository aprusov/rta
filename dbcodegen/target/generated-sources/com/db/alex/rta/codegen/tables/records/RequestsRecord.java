/*
 * This file is generated by jOOQ.
 */
package com.db.alex.rta.codegen.tables.records;


import com.db.alex.rta.codegen.tables.Requests;

import javax.annotation.processing.Generated;

import org.jooq.Field;
import org.jooq.Record3;
import org.jooq.Row3;
import org.jooq.impl.TableRecordImpl;


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
public class RequestsRecord extends TableRecordImpl<RequestsRecord> implements Record3<Long, Byte, String> {

    private static final long serialVersionUID = -1578090594;

    /**
     * Setter for <code>requests.id</code>.
     */
    public void setId(Long value) {
        set(0, value);
    }

    /**
     * Getter for <code>requests.id</code>.
     */
    public Long getId() {
        return (Long) get(0);
    }

    /**
     * Setter for <code>requests.type</code>.
     */
    public void setType(Byte value) {
        set(1, value);
    }

    /**
     * Getter for <code>requests.type</code>.
     */
    public Byte getType() {
        return (Byte) get(1);
    }

    /**
     * Setter for <code>requests.data</code>.
     */
    public void setData(String value) {
        set(2, value);
    }

    /**
     * Getter for <code>requests.data</code>.
     */
    public String getData() {
        return (String) get(2);
    }

    // -------------------------------------------------------------------------
    // Record3 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row3<Long, Byte, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    @Override
    public Row3<Long, Byte, String> valuesRow() {
        return (Row3) super.valuesRow();
    }

    @Override
    public Field<Long> field1() {
        return Requests.REQUESTS.ID;
    }

    @Override
    public Field<Byte> field2() {
        return Requests.REQUESTS.TYPE;
    }

    @Override
    public Field<String> field3() {
        return Requests.REQUESTS.DATA;
    }

    @Override
    public Long component1() {
        return getId();
    }

    @Override
    public Byte component2() {
        return getType();
    }

    @Override
    public String component3() {
        return getData();
    }

    @Override
    public Long value1() {
        return getId();
    }

    @Override
    public Byte value2() {
        return getType();
    }

    @Override
    public String value3() {
        return getData();
    }

    @Override
    public RequestsRecord value1(Long value) {
        setId(value);
        return this;
    }

    @Override
    public RequestsRecord value2(Byte value) {
        setType(value);
        return this;
    }

    @Override
    public RequestsRecord value3(String value) {
        setData(value);
        return this;
    }

    @Override
    public RequestsRecord values(Long value1, Byte value2, String value3) {
        value1(value1);
        value2(value2);
        value3(value3);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached RequestsRecord
     */
    public RequestsRecord() {
        super(Requests.REQUESTS);
    }

    /**
     * Create a detached, initialised RequestsRecord
     */
    public RequestsRecord(Long id, Byte type, String data) {
        super(Requests.REQUESTS);

        set(0, id);
        set(1, type);
        set(2, data);
    }
}
