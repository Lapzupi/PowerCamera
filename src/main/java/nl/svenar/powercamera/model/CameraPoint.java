package nl.svenar.powercamera.model;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sarhatabaot
 */
public class CameraPoint {
    private final String cameraId;

    private Type type; //move, teleport,...?
    private Easing easing; //linear, none, ...?
    private Double duration;
    private Location location;
    private List<String> commandsStart;
    private List<String> commandsEnd;

    public CameraPoint(final String cameraId, final Type type, final Easing easing, final Double duration, final Location location, final List<String> commandsStart, final List<String> commandsEnd) {
        this.cameraId = cameraId;
        this.type = type;
        this.easing = easing;
        this.duration = duration;
        this.location = location;
        this.commandsStart = commandsStart;
        this.commandsEnd = commandsEnd;
    }

    public CameraPoint(final String cameraId, final Type type, final Easing easing, final Double duration, final Location location) {
        this.cameraId = cameraId;
        this.type = type;
        this.easing = easing;
        this.duration = duration;
        this.location = location;
        this.commandsStart = new ArrayList<>();
        this.commandsEnd = new ArrayList<>();
    }

    public Type getType() {
        return type;
    }

    public Easing getEasing() {
        return easing;
    }

    public Double getDuration() {
        return duration;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getCommandsStart() {
        return commandsStart;
    }

    public List<String> getCommandsEnd() {
        return commandsEnd;
    }

    public enum Easing {
        LINEAR, NONE;
    }
    public enum Type {
        MOVE, TELEPORT, NONE;
    }

    public void setType(final Type type) {
        this.type = type;
    }

    public void setEasing(final Easing easing) {
        this.easing = easing;
    }

    public void setDuration(final Double duration) {
        this.duration = duration;
    }

    public void setLocation(final Location location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "CameraPoint{" +
                "cameraId="+ cameraId +
                "type=" + type +
                ", easing=" + easing +
                ", duration=" + duration +
                ", location=" + location +
                ", commandsStart=" + commandsStart +
                ", commandsEnd=" + commandsEnd +
                '}';
    }
}
