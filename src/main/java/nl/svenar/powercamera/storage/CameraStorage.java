package nl.svenar.powercamera.storage;

import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;

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

    CompletableFuture<Camera> createCamera(final String cameraId);

    CompletableFuture<Void> deleteCamera(final String cameraId);

    CompletableFuture<Set<String>> getCameraIds();

    CompletableFuture<Integer> getTotalAmountCameras();


}
