/*
 * This file is generated by jOOQ.
 */
package nl.svenar.powercamera.storage.generated.tables;


import java.util.function.Function;

import nl.svenar.powercamera.storage.generated.DefaultSchema;
import nl.svenar.powercamera.storage.generated.Keys;
import nl.svenar.powercamera.storage.generated.tables.records.PowercameraPlayersRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function3;
import org.jooq.Identity;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row3;
import org.jooq.Schema;
import org.jooq.SelectField;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PowercameraPlayers extends TableImpl<PowercameraPlayersRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>powercamera_players</code>
     */
    public static final PowercameraPlayers POWERCAMERA_PLAYERS = new PowercameraPlayers();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PowercameraPlayersRecord> getRecordType() {
        return PowercameraPlayersRecord.class;
    }

    /**
     * The column <code>powercamera_players.ID</code>.
     */
    public final TableField<PowercameraPlayersRecord, Integer> ID = createField(DSL.name("ID"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");

    /**
     * The column <code>powercamera_players.UUID</code>.
     */
    public final TableField<PowercameraPlayersRecord, String> UUID = createField(DSL.name("UUID"), SQLDataType.VARCHAR(36).nullable(false), this, "");

    /**
     * The column <code>powercamera_players.CAMERA_ID</code>.
     */
    public final TableField<PowercameraPlayersRecord, String> CAMERA_ID = createField(DSL.name("CAMERA_ID"), SQLDataType.VARCHAR(15).nullable(false), this, "");

    private PowercameraPlayers(Name alias, Table<PowercameraPlayersRecord> aliased) {
        this(alias, aliased, null);
    }

    private PowercameraPlayers(Name alias, Table<PowercameraPlayersRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>powercamera_players</code> table reference
     */
    public PowercameraPlayers(String alias) {
        this(DSL.name(alias), POWERCAMERA_PLAYERS);
    }

    /**
     * Create an aliased <code>powercamera_players</code> table reference
     */
    public PowercameraPlayers(Name alias) {
        this(alias, POWERCAMERA_PLAYERS);
    }

    /**
     * Create a <code>powercamera_players</code> table reference
     */
    public PowercameraPlayers() {
        this(DSL.name("powercamera_players"), null);
    }

    public <O extends Record> PowercameraPlayers(Table<O> child, ForeignKey<O, PowercameraPlayersRecord> key) {
        super(child, key, POWERCAMERA_PLAYERS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public Identity<PowercameraPlayersRecord, Integer> getIdentity() {
        return (Identity<PowercameraPlayersRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<PowercameraPlayersRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_D;
    }

    @Override
    public PowercameraPlayers as(String alias) {
        return new PowercameraPlayers(DSL.name(alias), this);
    }

    @Override
    public PowercameraPlayers as(Name alias) {
        return new PowercameraPlayers(alias, this);
    }

    @Override
    public PowercameraPlayers as(Table<?> alias) {
        return new PowercameraPlayers(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraPlayers rename(String name) {
        return new PowercameraPlayers(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraPlayers rename(Name name) {
        return new PowercameraPlayers(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraPlayers rename(Table<?> name) {
        return new PowercameraPlayers(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row3 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row3<Integer, String, String> fieldsRow() {
        return (Row3) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function3<? super Integer, ? super String, ? super String, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function3<? super Integer, ? super String, ? super String, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
