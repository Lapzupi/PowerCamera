package nl.svenar.powercamera.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Camera {
    private final String id;
    private final String alias;

    private final List<CameraPoint> points;

    private Double totalDuration;

    private boolean returnToOrigin;

    public Camera(final String id) {
        this.id = id;
        this.alias = id;
        this.points = new ArrayList<>();
    }

    public Camera(final String id, final String alias, final List<CameraPoint> points, final Double totalDuration, final boolean returnToOrigin) {
        this.id = id;
        this.alias = alias;
        this.points = points;
        this.totalDuration = totalDuration;
        this.returnToOrigin = returnToOrigin;
    }


    public String getId() {
        return id;
    }

    public String getAlias() {
        return alias;
    }


    public List<CameraPoint> getPoints() {
        return points;
    }

    public void addPoint(final CameraPoint cameraPoint) {
        points.add(cameraPoint);
    }

    public void deletePoint(final int num) {
        points.remove(num);
    }

    public boolean isReturnToOrigin() {
        return returnToOrigin;
    }

    public Double calculateTotalDuration() {
        return getPoints().stream().map(CameraPoint::getDuration).mapToDouble(Double::doubleValue).sum();
    }

    public Double getTotalDuration() {
        if(totalDuration == null) {
            this.totalDuration = calculateTotalDuration();
        }
        return totalDuration;
    }

    public void updateTotalDuration() {
        this.totalDuration = calculateTotalDuration();
    }

    @Override
    public String toString() {
        return "Camera{" +
                "id='" + id + '\'' +
                ", alias='" + alias + '\'' +
                ", points=" + points +
                ", returnToOrigin=" + returnToOrigin +
                '}';
    }
}
