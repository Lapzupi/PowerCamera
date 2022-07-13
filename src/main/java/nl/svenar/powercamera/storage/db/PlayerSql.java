package nl.svenar.powercamera.storage.db;

import com.github.sarhatabaot.kraken.core.db.ConnectionFactory;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.storage.PlayerStorage;

import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public class PlayerSql implements PlayerStorage {
    private ConnectionFactory<PowerCamera> connectionFactory;

    public PlayerSql(final ConnectionFactory<PowerCamera> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public CompletableFuture<Boolean> hasPlayed(final String uuid, final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> addPlayer(final String uuid, final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid, final String cameraId) {
        return null;
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid) {
        return null;
    }
}
