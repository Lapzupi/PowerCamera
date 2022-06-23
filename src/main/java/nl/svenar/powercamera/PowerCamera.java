package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import co.aikar.commands.PaperCommandManager;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.model.CameraMode;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.events.OnJoin;
import nl.svenar.powercamera.events.OnMove;

public class PowerCamera extends JavaPlugin {
	private String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%plugin_name%" + ChatColor.BLACK + "] ";
	private PluginConfig pluginConfig;
	private CameraStorage camerasConfig;

	//These Should be in managers/caches
	public Map<UUID, String> player_selected_camera = new HashMap<>(); // Selected camera name
	public Map<UUID, CameraMode> player_camera_mode = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Map<UUID, CameraHandler> player_camera_handler = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Instant powercamera_start_time = Instant.now();

	@Override
	public void onEnable() {

		pluginChatPrefix = pluginChatPrefix.replace("%plugin_name%", getDescription().getName());

		Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);

		PaperCommandManager commandManager  = new PaperCommandManager(this);
		commandManager.registerCommand(new PowerCameraCommand(this));

		setupConfig();

		getLogger().info("Enabled " + getDescription().getName() + " v" + getDescription().getVersion());

		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this,9107);
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled " + getDescription().getName() + " v" + getDescription().getVersion());
	}

	public String getPluginChatPrefix() {
		return pluginChatPrefix;
	}

	private void setupConfig() {
		pluginConfig = new PluginConfig(this);
		camerasConfig = new CameraStorage(this);

		pluginConfig.getConfig().set("version", null);
		camerasConfig.getConfig().set("version", null);

		if (!pluginConfig.getConfig().isSet("camera-effects.spectator-mode"))
			pluginConfig.getConfig().set("camera-effects.spectator-mode", true);

		if (!pluginConfig.getConfig().isSet("camera-effects.invisible"))
			pluginConfig.getConfig().set("camera-effects.invisible", false);

		if (pluginConfig.getConfig().isSet("on-new-player-join-camera-path")) {
			ArrayList<String> list = new ArrayList<>();
			list.add(pluginConfig.getConfig().getString("on-new-player-join-camera-path"));
			pluginConfig.getConfig().set("on-join.random-player-camera-path", list);
			pluginConfig.getConfig().set("on-join.show-once", true);
			pluginConfig.getConfig().set("on-new-player-join-camera-path", null);
		}

		for (String camera_name : camerasConfig.getCameras()) {
			List<String> points = camerasConfig.getPoints(camera_name);
			List<String> newPoints = new ArrayList<>();
			for (String point : points) {
				if (!point.startsWith("location:") && !point.startsWith("command:")) {
					point = "location:" + point;
				}
				
				if (point.startsWith("location:") && !(point.startsWith("location:linear:") || point.startsWith("location:teleport:"))) {
					point = point.replaceFirst("location:", "location:linear:");
				}
				
				newPoints.add(point);
//				if (point.contains(":")) {
//					new_points.add(point);
//				} else {
//					new_points.add("location:" + point);
//				}
			}
			camerasConfig.getConfig().set("cameras." + camera_name + ".points", newPoints);
		}

		pluginConfig.getConfig().set("version", getDescription().getVersion());
		pluginConfig.saveConfig();

		camerasConfig.getConfig().set("version", getDescription().getVersion());
		camerasConfig.saveConfig();
	}

	public PluginConfig getConfigPlugin() {
		return pluginConfig;
	}

	public CameraStorage getConfigCameras() {
		return camerasConfig;
	}
}
