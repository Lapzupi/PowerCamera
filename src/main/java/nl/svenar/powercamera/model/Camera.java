package nl.svenar.powercamera.model;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {
    private final String id;
    private String alias;

    private List<Location> pathLocations;
    private Map<Integer, List<String>> commands;

    private int duration;

    public Camera(final String id) {
        this.id = id;
        this.alias = id;
        this.pathLocations = new ArrayList<>();
        this.commands = new HashMap<>();
        this.duration = 10;
    }

    public Camera(final String id, final String alias, final List<Location> pathLocations, final Map<Integer, List<String>> commands, final int duration) {
        this.id = id;
        this.alias = alias;
        this.pathLocations = pathLocations;
        this.commands = commands;
        this.duration = duration;
    }

    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public List<Location> getPathLocations() {
        return pathLocations;
    }

    public Map<Integer, List<String>> getCommands() {
        return commands;
    }

    public int getDuration() {
        return duration;
    }
}
