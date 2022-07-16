package nl.svenar.powercamera.managers;

import com.github.sarhatabaot.kraken.core.logging.LoggerUtil;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import nl.svenar.powercamera.PowerCamera;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.model.CameraPoint;
import nl.svenar.powercamera.storage.CameraStorage;
import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CameraManager {
    private final CameraStorage cameraStorage;
    private final PowerCamera plugin;
    private final LoadingCache<String, Camera> cameraCache;

    private final LoadingCache<String, List<Location>> pathCache;

    public CameraManager(final @NotNull PowerCamera plugin) {
        this.plugin = plugin;
        this.cameraStorage = plugin.getCameraStorage();
        this.cameraCache = buildCameraCache();
        this.pathCache = buildPathCache();

        try {
            this.cameraCache.getAll(this.cameraStorage.getCameraIds().get());
            this.pathCache.getAll(this.cameraStorage.getCameraIds().get());
        } catch (InterruptedException | ExecutionException e) {
            LoggerUtil.logSevereException(e);
        }
    }

    @Contract(" -> new")
    private @NotNull LoadingCache<String, Camera> buildCameraCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull Camera load(final @NotNull String key) throws Exception {
                        plugin.getLogger().info("%s Loaded %s into cache".formatted(CameraManager.class, key));
                        return cameraStorage.getCamera(key).get(30, TimeUnit.SECONDS);
                    }
                });
    }

    @Contract(" -> new")
    private @NotNull LoadingCache<String, List<Location>> buildPathCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<>() {
                    @Override
                    public List<Location> load(final @NotNull String key) throws Exception {
                        plugin.getLogger().info("%s Loaded %s into cache".formatted(CameraManager.class, key));
                        return generatePath(cameraStorage.getCamera(key).get(30, TimeUnit.SECONDS).getPoints());
                    }
                });
    }

    public void createCamera(final String cameraId) {
        this.cameraStorage.createCamera(cameraId);
    }

    public Camera getCamera(final String id) {
        try {
            return cameraCache.get(id);
        } catch (ExecutionException e){
            plugin.getLogger().severe(e.getMessage());
            return null;
        }
    }

    public boolean hasCamera(final String id) {
        return cameraCache.asMap().containsKey(id);
    }

    public Set<String> getCameraIds() {
        return cameraCache.asMap().keySet();
    }

    public void addPoint(final CameraPoint cameraPoint) {
        this.cameraStorage.addPoint(cameraPoint);
    }

    public void removePoint(final String cameraId, final int pointNum) {
        this.cameraStorage.removePoint(cameraId,pointNum);
    }

    public @NotNull List<Location> generatePath(@NotNull List<CameraPoint> points) {
        final ArrayList<Location> list = new ArrayList<>();
        final int singleFrameDuration = plugin.getConfigPlugin().getSingleFrameDuration();
        if (points.size() == 1)
            list.add(points.get(0).getLocation());

        for (int i = 0; i < points.size() - 1; i++) {
            CameraPoint currentPoint = points.get(i);
            CameraPoint nextPoint = points.get(i + 1);

            int maxSubPoints = calcMaxPoints(nextPoint.getDuration().intValue(), singleFrameDuration);
            for (int cursor = 0; cursor <= maxSubPoints; cursor++) {
                if (nextPoint.getEasing() == CameraPoint.Easing.LINEAR) {
                    list.add(translateLinearNext(currentPoint.getLocation(), nextPoint.getLocation(), cursor, maxSubPoints));
                } else {
                    list.add(nextPoint.getLocation()); //maybe not needed
                }
            }
        }

        return list;
    }

    /**
     * Calculates the where a point would be between 2 points.
     * @param point First Point
     * @param pointNext 2nd Point
     * @param progress Number of the sub point between 2 points
     * @param progressMax Max amount of sub points
     * @return The sub location
     */
    private @NotNull Location translateLinearNext(@NotNull Location point, @NotNull Location pointNext, int progress, int progressMax) {
        if (!point.getWorld().getUID().toString().equals(pointNext.getWorld().getUID().toString())) {
            return pointNext;
        }

        Location newPoint = new Location(pointNext.getWorld(), point.getX(), point.getY(), point.getZ());

        newPoint.setX(calculateProgress(point.getX(), pointNext.getX(), progress, progressMax));
        newPoint.setY(calculateProgress(point.getY(), pointNext.getY(), progress, progressMax));
        newPoint.setZ(calculateProgress(point.getZ(), pointNext.getZ(), progress, progressMax));
        newPoint.setYaw((float) calculateProgress(point.getYaw(), pointNext.getYaw(), progress, progressMax));
        newPoint.setPitch((float) calculateProgress(point.getPitch(), pointNext.getPitch(), progress, progressMax));

        return newPoint;
    }

    private double calculateProgress(double firstPoint, double lastPoint, int subPointPosition, int maxSubPoints) {
        return firstPoint + ((double) subPointPosition / (double) maxSubPoints) * (lastPoint - firstPoint);
    }

    private int calcMaxPoints(int duration, int singleFrameDurationMs) {
        return (duration * 1000) / singleFrameDurationMs;
    }

}
