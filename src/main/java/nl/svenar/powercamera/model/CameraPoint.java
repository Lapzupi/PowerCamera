package nl.svenar.powercamera.model;

import org.bukkit.Location;

import java.util.List;

/**
 * @author sarhatabaot
 */
public class CameraPoint {
    private String type;
    private String easing;
    private Double duration;
    private Location location;
    private List<String> commandsStart;
    private List<String> commandsEnd;


    public CameraPoint(final String type, final String easing, final Double duration, final Location location, final List<String> commandsStart, final List<String> commandsEnd) {
        this.type = type;
        this.easing = easing;
        this.duration = duration;
        this.location = location;
        this.commandsStart = commandsStart;
        this.commandsEnd = commandsEnd;
    }

    public String getType() {
        return type;
    }

    public String getEasing() {
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
}
