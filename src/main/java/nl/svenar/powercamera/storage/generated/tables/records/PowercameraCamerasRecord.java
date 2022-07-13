/*
 * This file is generated by jOOQ.
 */
package nl.svenar.powercamera.storage.generated.tables.records;


import nl.svenar.powercamera.storage.generated.tables.PowercameraCameras;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class PowercameraCamerasRecord extends UpdatableRecordImpl<PowercameraCamerasRecord> implements Record4<String, String, Double, Boolean> {

    private static final long serialVersionUID = 1L;

    /**
     * Setter for <code>POWERCAMERA_CAMERAS.ID</code>.
     */
    public void setId(String value) {
        set(0, value);
    }

    /**
     * Getter for <code>POWERCAMERA_CAMERAS.ID</code>.
     */
    public String getId() {
        return (String) get(0);
    }

    /**
     * Setter for <code>POWERCAMERA_CAMERAS.ALIAS</code>.
     */
    public void setAlias(String value) {
        set(1, value);
    }

    /**
     * Getter for <code>POWERCAMERA_CAMERAS.ALIAS</code>.
     */
    public String getAlias() {
        return (String) get(1);
    }

    /**
     * Setter for <code>POWERCAMERA_CAMERAS.TOTAL_DURATION</code>.
     */
    public void setTotalDuration(Double value) {
        set(2, value);
    }

    /**
     * Getter for <code>POWERCAMERA_CAMERAS.TOTAL_DURATION</code>.
     */
    public Double getTotalDuration() {
        return (Double) get(2);
    }

    /**
     * Setter for <code>POWERCAMERA_CAMERAS.RETURN_TO_ORIGIN</code>.
     */
    public void setReturnToOrigin(Boolean value) {
        set(3, value);
    }

    /**
     * Getter for <code>POWERCAMERA_CAMERAS.RETURN_TO_ORIGIN</code>.
     */
    public Boolean getReturnToOrigin() {
        return (Boolean) get(3);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    @Override
    public Record1<String> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    @Override
    public Row4<String, String, Double, Boolean> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<String, String, Double, Boolean> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<String> field1() {
        return PowercameraCameras.POWERCAMERA_CAMERAS.ID;
    }

    @Override
    public Field<String> field2() {
        return PowercameraCameras.POWERCAMERA_CAMERAS.ALIAS;
    }

    @Override
    public Field<Double> field3() {
        return PowercameraCameras.POWERCAMERA_CAMERAS.TOTAL_DURATION;
    }

    @Override
    public Field<Boolean> field4() {
        return PowercameraCameras.POWERCAMERA_CAMERAS.RETURN_TO_ORIGIN;
    }

    @Override
    public String component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getAlias();
    }

    @Override
    public Double component3() {
        return getTotalDuration();
    }

    @Override
    public Boolean component4() {
        return getReturnToOrigin();
    }

    @Override
    public String value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getAlias();
    }

    @Override
    public Double value3() {
        return getTotalDuration();
    }

    @Override
    public Boolean value4() {
        return getReturnToOrigin();
    }

    @Override
    public PowercameraCamerasRecord value1(String value) {
        setId(value);
        return this;
    }

    @Override
    public PowercameraCamerasRecord value2(String value) {
        setAlias(value);
        return this;
    }

    @Override
    public PowercameraCamerasRecord value3(Double value) {
        setTotalDuration(value);
        return this;
    }

    @Override
    public PowercameraCamerasRecord value4(Boolean value) {
        setReturnToOrigin(value);
        return this;
    }

    @Override
    public PowercameraCamerasRecord values(String value1, String value2, Double value3, Boolean value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached PowercameraCamerasRecord
     */
    public PowercameraCamerasRecord() {
        super(PowercameraCameras.POWERCAMERA_CAMERAS);
    }

    /**
     * Create a detached, initialised PowercameraCamerasRecord
     */
    public PowercameraCamerasRecord(String id, String alias, Double totalDuration, Boolean returnToOrigin) {
        super(PowercameraCameras.POWERCAMERA_CAMERAS);

        setId(id);
        setAlias(alias);
        setTotalDuration(totalDuration);
        setReturnToOrigin(returnToOrigin);
    }
}
