package nl.svenar.powercamera.model;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Camera {
    private final String id;
    private final String alias;

    private final List<CameraPoint> points;

    private final int duration;

    public Camera(final String id) {
        this.id = id;
        this.alias = id;
        this.duration = 10;
        this.points = new ArrayList<>();
    }

    public Camera(final String id, final String alias, final int duration, final List<CameraPoint> points) {
        this.id = id;
        this.alias = alias;
        this.duration = duration;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }

    public int getDuration() {
        return duration;
    }

    public List<CameraPoint> getPoints() {
        return points;
    }
}
