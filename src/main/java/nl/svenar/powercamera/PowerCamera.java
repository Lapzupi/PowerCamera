package nl.svenar.powercamera;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import co.aikar.commands.PaperCommandManager;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.config.CameraStorage;
import nl.svenar.powercamera.model.ViewingMode;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.config.CameraConfig;
import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.listeners.OnJoin;
import nl.svenar.powercamera.listeners.OnMove;
import org.spongepowered.configurate.ConfigurateException;

public class PowerCamera extends JavaPlugin {
	private String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + "%plugin_name%" + ChatColor.BLACK + "] ";

	private CameraConfig camerasConfig;

	private PluginConfig pluginConfig;
	private CameraStorage cameraStorage;

	//These Should be in managers/caches
	public Map<UUID, String> player_selected_camera = new HashMap<>(); // Selected camera name
	public Map<UUID, ViewingMode> player_camera_mode = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
	public Map<UUID, CameraHandlerRunnable> player_camera_handler = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
	private final Instant startTime = Instant.now();

	@Override
	public void onEnable() {
		try {
			this.pluginConfig = new PluginConfig(this);
			this.cameraStorage = new CameraStorage(this);
		} catch (ConfigurateException e){
			getLogger().severe("Could not initialize camera.conf or config.conf");
			getLogger().warning("Please fix any errors and reload the plugin.");
			e.printStackTrace();
		}

		pluginChatPrefix = pluginChatPrefix.replace("%plugin_name%", getDescription().getName());

		Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);

		PaperCommandManager commandManager  = new PaperCommandManager(this);
		commandManager.enableUnstableAPI("help");
		commandManager.enableUnstableAPI("brigadier");
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
		camerasConfig = new CameraConfig(this);

	}

	public PluginConfig getConfigPlugin() {
		return pluginConfig;
	}

	public CameraConfig getConfigCameras() {
		return camerasConfig;
	}

	public Instant getStartTime() {
		return startTime;
	}
}
