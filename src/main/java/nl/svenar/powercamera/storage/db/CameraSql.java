package nl.svenar.powercamera.storage.db;

import com.github.sarhatabaot.kraken.core.db.ConnectionFactory;
import com.github.sarhatabaot.kraken.core.db.ExecuteQuery;
import com.github.sarhatabaot.kraken.core.db.ExecuteUpdate;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.storage.CameraStorage;
import nl.svenar.powercamera.storage.generated.tables.PowercameraCameras;
import nl.svenar.powercamera.storage.generated.tables.PowercameraPoints;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * @author sarhatabaot
 */
public class CameraSql implements CameraStorage {
    private final ConnectionFactory<PowerCamera> connectionFactory;

    public CameraSql(final ConnectionFactory<PowerCamera> connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public CompletableFuture<Camera> getCamera(final String cameraId) {
        ExecuteQuery<Camera, Record, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public Camera onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                Record recordResult = dslContext.select()
                        .from(PowercameraCameras.POWERCAMERA_CAMERAS)
                        .where(PowercameraCameras.POWERCAMERA_CAMERAS.ID.eq(cameraId))
                        .fetchOne();
                if(recordResult == null) {
                    return empty();
                }
                return getQuery(recordResult);
            }

            @Override
            public @NotNull Camera getQuery(@NotNull final Record result) throws Exception {
                final String cameraId = result.getValue(PowercameraCameras.POWERCAMERA_CAMERAS.ID);
                final String alias = result.getValue(PowercameraCameras.POWERCAMERA_CAMERAS.ALIAS);
                final double totalDuration = result.getValue(PowercameraCameras.POWERCAMERA_CAMERAS.TOTAL_DURATION);
                final boolean returnToOrigin = result.getValue(PowercameraCameras.POWERCAMERA_CAMERAS.RETURN_TO_ORIGIN);
                final List<CameraPoint> cameraPoints = getCameraPoints(cameraId).get();
                return new Camera(cameraId,alias,cameraPoints,totalDuration, returnToOrigin);
            }

            @Contract(pure = true)
            @Override
            public @Nullable Camera empty() {
                return null;
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<Boolean> hasCamera(final String cameraId) {
        ExecuteQuery<Boolean,Record,PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public @NotNull Boolean onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                return dslContext.fetchExists(PowercameraCameras.POWERCAMERA_CAMERAS.where(PowercameraCameras.POWERCAMERA_CAMERAS.ID.eq(cameraId)));
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
    public CompletableFuture<CameraPoint> getCameraPoint(final String cameraId, final int num) {
        ExecuteQuery<CameraPoint, Record, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Contract(pure = true)
            @Override
            public @Nullable CameraPoint onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                Record result = dslContext.select()
                        .from(PowercameraPoints.POWERCAMERA_POINTS)
                        .where(PowercameraPoints.POWERCAMERA_POINTS.CAMERA_ID.eq(cameraId))
                        .and(PowercameraPoints.POWERCAMERA_POINTS.NUM.eq(num))
                        .fetchOne();

                if(result == null)
                    return empty();

                return getQuery(result);
            }

            @Contract(pure = true)
            @Override
            public @NotNull CameraPoint getQuery(@NotNull final Record result) throws Exception {
                return getPointFromRecord(cameraId,result);
            }

            @Contract(pure = true)
            @Override
            public @Nullable CameraPoint empty() {
                return null;
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<Boolean> hasCameraPoint(final String cameraId, final int num) {
        ExecuteQuery<Boolean, Record, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public Boolean onRunQuery(final DSLContext dslContext) throws Exception {
                return dslContext.fetchExists(PowercameraPoints.POWERCAMERA_POINTS
                                .where(PowercameraPoints.POWERCAMERA_POINTS.CAMERA_ID.eq(cameraId)
                                        .and(PowercameraPoints.POWERCAMERA_POINTS.NUM.eq(num))));
            }

            @Override
            public Boolean getQuery(@NotNull final Record result) throws Exception {
                return false;
            }

            @Override
            public Boolean empty() {
                return false;
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<Void> createCamera(final String cameraId) {
        return CompletableFuture.supplyAsync(() -> {
            ExecuteUpdate<PowerCamera> executeUpdate = new ExecuteUpdate<>(connectionFactory) {
                @Override
                protected void onRunUpdate(final DSLContext dslContext) {
                    dslContext.insertInto(PowercameraCameras.POWERCAMERA_CAMERAS)
                            .set(PowercameraCameras.POWERCAMERA_CAMERAS.ID, cameraId)
                            .executeAsync(); //todo test
                }
            };
            executeUpdate.executeUpdate();
            return null;
        }
        );
    }

    @Override
    public CompletableFuture<Void> deleteCamera(final String cameraId) {
        return CompletableFuture.supplyAsync(() -> {
                    ExecuteUpdate<PowerCamera> executeUpdate = new ExecuteUpdate<>(connectionFactory) {
                        @Override
                        protected void onRunUpdate(final @NotNull DSLContext dslContext) {
                            dslContext.deleteFrom(PowercameraCameras.POWERCAMERA_CAMERAS)
                                    .where(PowercameraCameras.POWERCAMERA_CAMERAS.ID.eq(cameraId))
                                    .executeAsync();
                        }
                    };
                    executeUpdate.executeUpdate();
                    return null;
                }
        );
    }

    @Override
    public CompletableFuture<Set<String>> getCameraIds() {
        ExecuteQuery<Set<String>, Result<Record>, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public Set<String> onRunQuery(final DSLContext dslContext) throws Exception {
                Result<Record> recordResult = dslContext.select()
                        .from(PowercameraCameras.POWERCAMERA_CAMERAS)
                        .fetch();
                return getQuery(recordResult);
            }

            @Override
            public Set<String> getQuery(@NotNull final Result<Record> result) throws Exception {
                Set<String> idSet = new HashSet<>();
                for(Record recordResult: result) {
                    idSet.add(recordResult.getValue(PowercameraCameras.POWERCAMERA_CAMERAS.ID));
                }
                return idSet;
            }

            @Override
            public Set<String> empty() {
                return Collections.emptySet();
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<Integer> getTotalAmountCameras() {
        ExecuteQuery<Integer,Record, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Override
            public Integer onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                return dslContext.selectCount()
                        .from(PowercameraCameras.POWERCAMERA_CAMERAS)
                        .fetchOne(0, int.class);
            }

            @Contract(pure = true)
            @Override
            public @Nullable Integer getQuery(@NotNull final Record result) throws Exception {
                return null;
            }

            @Contract(pure = true)
            @Override
            public @Nullable Integer empty() {
                return null;
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    @Override
    public CompletableFuture<List<CameraPoint>> getCameraPoints(final String cameraId) {
        ExecuteQuery<List<CameraPoint>, Result<Record>, PowerCamera> executeQuery = new ExecuteQuery<>(connectionFactory) {
            @Contract(pure = true)
            @Override
            public @NotNull List<CameraPoint> onRunQuery(final @NotNull DSLContext dslContext) throws Exception {
                final Result<Record> result = dslContext.select()
                        .from(PowercameraPoints.POWERCAMERA_POINTS)
                        .where(PowercameraPoints.POWERCAMERA_POINTS.CAMERA_ID.eq(cameraId)).fetch();

                if(result.isEmpty())
                    return empty();

                return getQuery(result);
            }

            @Contract(pure = true)
            @Override
            public @NotNull List<CameraPoint> getQuery(@NotNull final Result<Record> result) throws Exception {
                List<CameraPoint> cameraPoints = new ArrayList<>();
                for(Record recordResult: result) {
                    cameraPoints.add(getPointFromRecord(cameraId,recordResult));
                }
                return cameraPoints;
            }

            @Contract(pure = true)
            @Override
            public @NotNull @Unmodifiable List<CameraPoint> empty() {
                return Collections.emptyList();
            }
        };
        return CompletableFuture.supplyAsync(executeQuery::prepareAndRunQuery);
    }

    private @NotNull CameraPoint getPointFromRecord(final String cameraId, final @NotNull Record recordResult) {
        int x = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.X);
        int y = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.Y);
        int z = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.Z);
        int yaw = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.YAW);
        int pitch = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.PITCH);
        double duration = recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.DURATION);
        CameraPoint.Easing easing = CameraPoint.Easing.valueOf(recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.EASING).getLiteral());
        CameraPoint.Type type = CameraPoint.Type.valueOf(recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.TYPE).getLiteral());
        World world = Bukkit.getWorld(recordResult.getValue(PowercameraPoints.POWERCAMERA_POINTS.WORLD_NAME));
        final Location location = new Location(world, x, y, z, yaw,pitch);
        //todo add commands
        return new CameraPoint(cameraId,type,easing,duration,location);
    }
}
