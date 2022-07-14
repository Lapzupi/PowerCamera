package nl.svenar.powercamera.storage.db;

import com.github.sarhatabaot.kraken.core.db.ConnectionFactory;
import com.github.sarhatabaot.kraken.core.db.ExecuteQuery;
import com.github.sarhatabaot.kraken.core.db.ExecuteUpdate;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.storage.PlayerStorage;
import nl.svenar.powercamera.storage.generated.tables.PowercameraPlayers;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;

import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public class PlayerSql implements PlayerStorage {
    private final ConnectionFactory<PowerCamera> connectionFactory;

    public PlayerSql(final ConnectionFactory<PowerCamera> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public CompletableFuture<Boolean> hasPlayed(final String uuid, final String cameraId) {
        ExecuteQuery<Boolean,Record,PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public @NotNull Boolean onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                return dslContext.fetchExists(PowercameraPlayers.POWERCAMERA_PLAYERS
                                .where(PowercameraPlayers.POWERCAMERA_PLAYERS.UUID.eq(uuid)
                                        .and(PowercameraPlayers.POWERCAMERA_PLAYERS.CAMERA_ID.eq(cameraId)))
                        );
            }

            @Contract(pure = true)
            @Override
            public @NotNull Boolean getQuery(@NotNull final Record result) throws Exception {
                return false;
            }

            @Contract(pure = true)
            @Override
            public @NotNull Boolean empty() {
                return false;
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<Void> addPlayer(final String uuid, final String cameraId) {
        return CompletableFuture.supplyAsync(() -> {
            ExecuteUpdate<PowerCamera> executeUpdate = new ExecuteUpdate<>(connectionFactory) {
                @Override
                protected void onRunUpdate(final DSLContext dslContext) {
                    dslContext.insertInto(PowercameraPlayers.POWERCAMERA_PLAYERS)
                            .set(PowercameraPlayers.POWERCAMERA_PLAYERS.UUID,uuid)
                            .set(PowercameraPlayers.POWERCAMERA_PLAYERS.CAMERA_ID, cameraId)
                            .execute();
                }
            };
            executeUpdate.executeUpdate();
            return null;
        });
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid, final String cameraId) {
        return CompletableFuture.supplyAsync(() -> {
            ExecuteUpdate<PowerCamera> executeUpdate = new ExecuteUpdate<>(connectionFactory) {
                @Override
                protected void onRunUpdate(final @NotNull DSLContext dslContext) {
                    dslContext.deleteFrom(PowercameraPlayers.POWERCAMERA_PLAYERS)
                            .where(PowercameraPlayers.POWERCAMERA_PLAYERS.UUID.eq(uuid))
                            .and(PowercameraPlayers.POWERCAMERA_PLAYERS.CAMERA_ID.eq(cameraId))
                            .execute();
                }
            };
            executeUpdate.executeUpdate();

            return null;
        });
    }

    @Override
    public CompletableFuture<Void> removePlayer(final String uuid) {
        return CompletableFuture.supplyAsync(() -> {
            ExecuteUpdate<PowerCamera> executeUpdate = new ExecuteUpdate<>(connectionFactory) {
                @Override
                protected void onRunUpdate(final @NotNull DSLContext dslContext) {
                    dslContext.deleteFrom(PowercameraPlayers.POWERCAMERA_PLAYERS)
                            .where(PowercameraPlayers.POWERCAMERA_PLAYERS.UUID.eq(uuid))
                            .execute();
                }
            };
            executeUpdate.executeUpdate();
            return null;
        });
    }
}
