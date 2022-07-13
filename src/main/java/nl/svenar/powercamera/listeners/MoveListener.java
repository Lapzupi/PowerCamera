package nl.svenar.powercamera.listeners;

import nl.svenar.powercamera.model.ViewingMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import nl.svenar.powercamera.PowerCamera;
import org.jetbrains.annotations.NotNull;

public class MoveListener implements Listener {

	private final PowerCamera plugin;

	public MoveListener(PowerCamera plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerMove(@NotNull PlayerMoveEvent e) {
		ViewingMode viewingMode = plugin.getPlayerManager().getViewingMode(e.getPlayer().getUniqueId());
		if(viewingMode == ViewingMode.PREVIEW) {
			e.setCancelled(true);
		}
	}
}
