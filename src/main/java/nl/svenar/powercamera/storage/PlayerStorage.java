package nl.svenar.powercamera.storage;

import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
@SuppressWarnings("SameReturnValue")
public interface PlayerStorage {
    CompletableFuture<Boolean> hasPlayed(final String uuid, final String cameraId);
    CompletableFuture<Void> addPlayer(final String uuid, final String cameraId);

    CompletableFuture<Void> removePlayer(final String uuid, final String cameraId);

    CompletableFuture<Void> removePlayer(final String uuid);
}
