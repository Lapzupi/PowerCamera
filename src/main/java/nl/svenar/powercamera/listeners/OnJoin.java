package nl.svenar.powercamera.listeners;

import nl.svenar.powercamera.model.Camera;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powercamera.CameraRunnable;
import nl.svenar.powercamera.PowerCamera;

public class OnJoin implements Listener {

    private final PowerCamera plugin;

    public OnJoin(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        if(!plugin.getConfigPlugin().getJoin().isShowOnce()) {
            return;
        }

        final Player player = event.getPlayer();
        if (player.hasPermission("powercamera.bypass.joincamera"))
            return;

        final String cameraId = plugin.getConfigPlugin().getJoin().getCameraId();
        if(!plugin.getCameraManager().hasCamera(cameraId)) {
            return;
        }

        final Camera camera = plugin.getCameraManager().getCamera(cameraId);
        this.plugin.getPlayerManager().setRunningTask(player.getUniqueId(),new CameraRunnable(plugin, player, camera).start());
    }
}
