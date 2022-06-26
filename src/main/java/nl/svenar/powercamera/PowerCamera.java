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
	private final String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + getDescription().getName() + ChatColor.BLACK + "] ";

	private CameraConfig camerasConfig;

	private PluginConfig pluginConfig;
	private CameraStorage cameraStorage;

	//These Should be in managers/caches
	private PlayerManager playerManager;

	@Deprecated public Map<UUID, String> player_selected_camera = new HashMap<>(); // Selected camera name
	@Deprecated public Map<UUID, ViewingMode> player_camera_mode = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)
	@Deprecated public Map<UUID, CameraHandlerRunnable> player_camera_handler = new HashMap<>(); // When the player is viewing the camera (/pc start & /pc preview)

	private final Instant startTime = Instant.now();

	@Override
	public void onEnable() {
		try {
			this.pluginConfig = new PluginConfig(this);
			this.cameraStorage = new CameraStorage(this);
		} catch (ConfigurateException e){
			getLogger().severe(() -> "Could not initialize camera.conf or config.conf");
			getLogger().warning(() -> "Please fix any errors and reload the plugin.");
			e.printStackTrace();
		}

		this.playerManager = new PlayerManager();

		Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);

		PaperCommandManager commandManager  = new PaperCommandManager(this);
		commandManager.enableUnstableAPI("help");
		commandManager.enableUnstableAPI("brigadier");
		commandManager.registerCommand(new PowerCameraCommand(this));

		setupConfig();

		getLogger().info(() -> "Enabled %s v%s".formatted(getDescription().getName(),getDescription().getVersion()));
		@SuppressWarnings("unused")
		Metrics metrics = new Metrics(this,9107);
	}

	@Override
	public void onDisable() {
		getLogger().info(() -> "Disabled %s v%s".formatted(getDescription().getName(),getDescription().getVersion()));
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

	public CameraStorage getCameraStorage() {
		return cameraStorage;
	}

	public PlayerManager getPlayerManager() {
		return playerManager;
	}
}
