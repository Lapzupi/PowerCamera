package nl.svenar.powercamera.storage.db;

import com.github.sarhatabaot.kraken.core.db.HikariConnectionFactory;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.storage.CameraStorage;


import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public class CameraSql implements CameraStorage {
    private HikariConnectionFactory connectionFactory;

    public CameraSql(final HikariConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public CompletableFuture<Camera> getCamera(final String cameraId) {
        try (Connection connection = connectionFactory.getConnection()){

        } catch (SQLException e) {

        }
        return null;
    }

    @Override
    public CompletableFuture<Boolean> hasCamera(final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<CameraPoint> getCameraPoint(final String cameraId, final int num) {
        return null;
    }

    @Override
    public CompletableFuture<Boolean> hasCameraPoint(final String cameraId, final int num) {
        return null;
    }

    @Override
    public CompletableFuture<Camera> createCamera(final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> deleteCamera(final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Set<String>> getCameraIds() {
        return null;
    }

    @Override
    public CompletableFuture<Integer> getTotalAmountCameras() {
        return null;
    }
}
