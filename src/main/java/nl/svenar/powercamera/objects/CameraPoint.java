package nl.svenar.powercamera.objects;

import java.util.List;

import org.bukkit.Location;

public class CameraPoint {

    String type;
    String easing;
    Double duration;
    Location pointLocation;
    List<String> commandsStart;
    List<String> commandsEnd;

    public CameraPoint(String type, String easing, Double duration, Location pointLocation,
                       List<String> commandsStart, List<String> commandsEnd) {

        this.type = type;
        this.easing = easing;
        this.duration = duration;
        this.pointLocation = pointLocation;
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
        return this.pointLocation;
    }

    public List<String> getStartCommands() {
        return this.commandsStart;
    }

    public List<String> getEndCommands() {
        return this.commandsEnd;
    }

}
