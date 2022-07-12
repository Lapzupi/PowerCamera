package nl.svenar.powercamera;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import nl.svenar.powercamera.model.Camera;
import nl.svenar.powercamera.storage.CameraStorage;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CameraManager {
    private CameraStorage cameraStorage;
    private final PowerCamera plugin;
    private final LoadingCache<String, Camera> loadingCache;

    public CameraManager(final PowerCamera plugin) {
        this.plugin = plugin;
        this.cameraStorage = plugin.getCameraStorage();
        this.loadingCache = buildCache();
    }

    @Contract(" -> new")
    private @NotNull LoadingCache<String, Camera> buildCache() {
        return CacheBuilder.newBuilder()
                .build(new CacheLoader<>() {
                    @Override
                    public @NotNull Camera load(final @NotNull String key) throws Exception {
                        plugin.getLogger().info("%s Loaded %s into cache".formatted(CameraManager.class, key));
                        return plugin.getCameraStorage().getCamera(key).get(30, TimeUnit.SECONDS);
                    }
                });
    }

    public Camera getCamera(final String id) {
        try {
            return loadingCache.get(id);
        } catch (ExecutionException e){
            plugin.getLogger().severe(e.getMessage());
            return null;
        }
    }

    public Set<String> getCameraIds() {
        return loadingCache.asMap().keySet();
    }

}
