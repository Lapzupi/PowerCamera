package nl.svenar.powercamera.objects;

import java.util.ArrayList;
import java.util.List;

public class Camera {
    private final String name;
    private final ArrayList<CameraPoint> points = new ArrayList<>();

    public Camera(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void addPoint(CameraPoint cameraPoint) {
        this.points.add(cameraPoint);
    }

    public List<CameraPoint> getPoints() {
        return this.points;
    }
    
}
