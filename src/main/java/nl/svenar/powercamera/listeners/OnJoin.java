package nl.svenar.powercamera.listeners;

import java.util.List;
import java.util.Random;

import nl.svenar.powercamera.Util;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import nl.svenar.powercamera.CameraHandlerRunnable;
import nl.svenar.powercamera.PowerCamera;

public class OnJoin implements Listener {

    private final PowerCamera plugin;

    public OnJoin(PowerCamera plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        if (player.hasPermission("powercamera.bypass.joincamera"))
            return;

        if (this.plugin.getConfigCameras().addPlayer(event.getPlayer().getUniqueId()) || !this.plugin.getConfigPlugin().getConfig().getBoolean("on-join.show-once")) {
            List<String> joinCameras = this.plugin.getConfigPlugin().getConfig().getStringList("on-join.random-player-camera-path");
            Random rand = Util.getRandom();
            String camera_name = joinCameras.get(rand.nextInt(joinCameras.size()));
            if (camera_name.length() > 0) {
                if (this.plugin.getConfigCameras().cameraExists(camera_name)) {
                    this.plugin.player_camera_handler.put(event.getPlayer().getUniqueId(), new CameraHandlerRunnable(plugin, event.getPlayer(), camera_name).generatePath().start());
                }
            }
        }

        if (event.getPlayer().isInvisible()) {
            event.getPlayer().setInvisible(false);
        }
    }
}
