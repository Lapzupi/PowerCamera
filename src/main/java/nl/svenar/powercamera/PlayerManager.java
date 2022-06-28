package nl.svenar.powercamera;

import nl.svenar.powercamera.model.ViewingMode;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * @author sarhatabaot
 */
public class PlayerManager {
    private final Map<UUID, String> selectedCamera;
    private final Map<UUID, ViewingMode> currentViewingMode;

    private final Map<UUID, CameraRunnable> runningTasks;

    public PlayerManager() {
        this.selectedCamera = new WeakHashMap<>();
        this.currentViewingMode = new WeakHashMap<>();
        this.runningTasks = new HashMap<>();
    }

    @Nullable
    //make the check before running this, perhaps we can make this optional, and check if present
    public CameraRunnable getCurrentRunningCameraTask(final UUID uuid) {
        return runningTasks.getOrDefault(uuid,null);
    }

    public boolean hasRunningTask(final UUID uuid) {
        return runningTasks.containsKey(uuid);
    }

    public ViewingMode getViewingMode(final UUID uuid) {
        return currentViewingMode.getOrDefault(uuid,ViewingMode.NONE);
    }

    public void setViewingMode(final UUID uuid, final ViewingMode mode) {
        currentViewingMode.put(uuid,mode);
    }

    public void setRunningTask(final UUID uuid,final CameraRunnable runnable) {
        this.runningTasks.put(uuid,runnable);
    }
    @Nullable
    public String getSelectedCameraId(final UUID uuid) {
        return selectedCamera.getOrDefault(uuid,null);
    }

    public boolean hasSelectedCamera(final UUID uuid) {
        return selectedCamera.containsKey(uuid);
    }

    public void setSelectedCameraId(@NotNull final UUID uuid,@NotNull final String cameraId) {
        selectedCamera.put(uuid,cameraId);
    }

    public Map<UUID, CameraRunnable> getRunningTasks() {
        return runningTasks;
    }
}
