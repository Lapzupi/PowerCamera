package nl.svenar.powercamera.storage.db;

import nl.svenar.powercamera.storage.PlayerStorage;

import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public class PlayerSql implements PlayerStorage {
    @Override
    public CompletableFuture<Boolean> hasPlayed(final String uuid, final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> addPlayer(final String uuid, final String cameraId) {
        return null;
    }
}
