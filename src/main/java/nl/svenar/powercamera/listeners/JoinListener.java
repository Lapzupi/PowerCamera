package nl.svenar.powercamera.listeners;

import nl.svenar.powercamera.Permissions;
import nl.svenar.powercamera.model.Camera;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powercamera.CameraRunnableView;
import nl.svenar.powercamera.PowerCamera;

public class JoinListener implements Listener {
    private final PowerCamera plugin;

    public JoinListener(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if(!plugin.getConfigPlugin().getJoin().isShowOnce()) {
            return;
        }

        final Player player = event.getPlayer();
        if (player.hasPermission(Permissions.BYPASS_JOIN_CAMERA))
            return;

        final String cameraId = plugin.getConfigPlugin().getJoin().getCameraId();
        if(!plugin.getCameraManager().hasCamera(cameraId)) {
            return;
        }

        final Camera camera = plugin.getCameraManager().getCamera(cameraId);
        this.plugin.getPlayerManager().setRunningTask(player.getUniqueId(),new CameraRunnableView(plugin, player, camera).start());
    }
}
