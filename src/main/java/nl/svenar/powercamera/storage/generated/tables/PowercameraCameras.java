/*
 * This file is generated by jOOQ.
 */
package nl.svenar.powercamera.storage.generated.tables;


import java.util.function.Function;

import nl.svenar.powercamera.storage.generated.DefaultSchema;
import nl.svenar.powercamera.storage.generated.Keys;
import nl.svenar.powercamera.storage.generated.tables.records.PowercameraCamerasRecord;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Function4;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Records;
import org.jooq.Row4;
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
public class PowercameraCameras extends TableImpl<PowercameraCamerasRecord> {

    private static final long serialVersionUID = 1L;

    /**
     * The reference instance of <code>POWERCAMERA_CAMERAS</code>
     */
    public static final PowercameraCameras POWERCAMERA_CAMERAS = new PowercameraCameras();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<PowercameraCamerasRecord> getRecordType() {
        return PowercameraCamerasRecord.class;
    }

    /**
     * The column <code>POWERCAMERA_CAMERAS.ID</code>.
     */
    public final TableField<PowercameraCamerasRecord, String> ID = createField(DSL.name("ID"), SQLDataType.VARCHAR(15).nullable(false), this, "");

    /**
     * The column <code>POWERCAMERA_CAMERAS.ALIAS</code>.
     */
    public final TableField<PowercameraCamerasRecord, String> ALIAS = createField(DSL.name("ALIAS"), SQLDataType.VARCHAR(30).nullable(false), this, "");

    /**
     * The column <code>POWERCAMERA_CAMERAS.TOTAL_DURATION</code>.
     */
    public final TableField<PowercameraCamerasRecord, Double> TOTAL_DURATION = createField(DSL.name("TOTAL_DURATION"), SQLDataType.DOUBLE.nullable(false), this, "");

    /**
     * The column <code>POWERCAMERA_CAMERAS.RETURN_TO_ORIGIN</code>.
     */
    public final TableField<PowercameraCamerasRecord, Boolean> RETURN_TO_ORIGIN = createField(DSL.name("RETURN_TO_ORIGIN"), SQLDataType.BOOLEAN.nullable(false).defaultValue(DSL.field("1", SQLDataType.BOOLEAN)), this, "");

    private PowercameraCameras(Name alias, Table<PowercameraCamerasRecord> aliased) {
        this(alias, aliased, null);
    }

    private PowercameraCameras(Name alias, Table<PowercameraCamerasRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * Create an aliased <code>POWERCAMERA_CAMERAS</code> table reference
     */
    public PowercameraCameras(String alias) {
        this(DSL.name(alias), POWERCAMERA_CAMERAS);
    }

    /**
     * Create an aliased <code>POWERCAMERA_CAMERAS</code> table reference
     */
    public PowercameraCameras(Name alias) {
        this(alias, POWERCAMERA_CAMERAS);
    }

    /**
     * Create a <code>POWERCAMERA_CAMERAS</code> table reference
     */
    public PowercameraCameras() {
        this(DSL.name("POWERCAMERA_CAMERAS"), null);
    }

    public <O extends Record> PowercameraCameras(Table<O> child, ForeignKey<O, PowercameraCamerasRecord> key) {
        super(child, key, POWERCAMERA_CAMERAS);
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : DefaultSchema.DEFAULT_SCHEMA;
    }

    @Override
    public UniqueKey<PowercameraCamerasRecord> getPrimaryKey() {
        return Keys.CONSTRAINT_C;
    }

    @Override
    public PowercameraCameras as(String alias) {
        return new PowercameraCameras(DSL.name(alias), this);
    }

    @Override
    public PowercameraCameras as(Name alias) {
        return new PowercameraCameras(alias, this);
    }

    @Override
    public PowercameraCameras as(Table<?> alias) {
        return new PowercameraCameras(alias.getQualifiedName(), this);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraCameras rename(String name) {
        return new PowercameraCameras(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraCameras rename(Name name) {
        return new PowercameraCameras(name, null);
    }

    /**
     * Rename this table
     */
    @Override
    public PowercameraCameras rename(Table<?> name) {
        return new PowercameraCameras(name.getQualifiedName(), null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Double, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Function)}.
     */
    public <U> SelectField<U> mapping(Function4<? super String, ? super String, ? super Double, ? super Boolean, ? extends U> from) {
        return convertFrom(Records.mapping(from));
    }

    /**
     * Convenience mapping calling {@link SelectField#convertFrom(Class,
     * Function)}.
     */
    public <U> SelectField<U> mapping(Class<U> toType, Function4<? super String, ? super String, ? super Double, ? super Boolean, ? extends U> from) {
        return convertFrom(toType, Records.mapping(from));
    }
}
