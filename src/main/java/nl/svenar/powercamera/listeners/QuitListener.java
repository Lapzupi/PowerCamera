package nl.svenar.powercamera.listeners;

import nl.svenar.powercamera.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author sarhatabaot
 */
public class QuitListener implements Listener {
    private final PlayerManager playerManager;

    public QuitListener(final PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void onPlayerLeave(final @NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        if (playerManager.hasRunningTask(player.getUniqueId())) {
            playerManager.getCurrentRunningCameraTask(player.getUniqueId()).stop();
        }
        if (playerManager.hasSelectedCamera(player.getUniqueId())) {
            playerManager.removeSelectedCamera(player.getUniqueId());
        }
        playerManager.removeViewingMode(player.getUniqueId());
    }

}
