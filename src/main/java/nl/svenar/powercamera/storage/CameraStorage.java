package nl.svenar.powercamera.storage;

import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;

import java.sql.Connection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public interface CameraStorage {

    CompletableFuture<Camera> getCamera(final String cameraId);
    CompletableFuture<Boolean> hasCamera(final String cameraId);

    CompletableFuture<CameraPoint> getCameraPoint(final String cameraId, final int num);

    CompletableFuture<Boolean> hasCameraPoint(final String cameraId, final int num);

    CompletableFuture<Void> createCamera(final String cameraId);

    CompletableFuture<Void> deleteCamera(final String cameraId);

    CompletableFuture<Void> addPoint(final CameraPoint cameraPoint);

    CompletableFuture<Void> removePoint(final String cameraId, final int pointNum);

    CompletableFuture<Set<String>> getCameraIds();

    CompletableFuture<Integer> getTotalAmountCameras();

    CompletableFuture<List<CameraPoint>> getCameraPoints(final String cameraId);

    CompletableFuture<List<String>> getCommandsStart(final String cameraId, final int pointNum);
    CompletableFuture<List<String>> getCommandsEnd(final String cameraId, final int pointNum);


}
