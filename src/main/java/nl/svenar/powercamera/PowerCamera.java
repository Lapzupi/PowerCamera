package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import co.aikar.commands.PaperCommandManager;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.config.CameraStorage;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.listeners.OnJoin;
import nl.svenar.powercamera.listeners.OnMove;
import org.spongepowered.configurate.ConfigurateException;

public class PowerCamera extends JavaPlugin {
    private final String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + getDescription().getName() + ChatColor.BLACK + "] ";

    private PluginConfig pluginConfig;
    private CameraStorage cameraStorage;

    //These Should be in managers/caches
    private PlayerManager playerManager;

    private final Instant startTime = Instant.now();

    @Override
    public void onEnable() {
        try {
            this.pluginConfig = new PluginConfig(this);
            this.cameraStorage = new CameraStorage(this);
        } catch (ConfigurateException e) {
            getLogger().severe(() -> "Could not initialize cameras.conf or config.conf");
            getLogger().warning(() -> "Please fix any errors and reload the plugin.");
            e.printStackTrace();
        }

        this.playerManager = new PlayerManager();

        Bukkit.getServer().getPluginManager().registerEvents(new OnMove(this), this);
        Bukkit.getServer().getPluginManager().registerEvents(new OnJoin(this), this);

        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new PowerCameraCommand(this));
        commandManager.getCommandCompletions().registerCompletion("points", context ->
                generateIntsInRange(getCameraStorage().getCamera(playerManager.getSelectedCameraId(context.getPlayer().getUniqueId())).getPoints().size()).stream().map(Object::toString).toList()
        );
        commandManager.getCommandCompletions().registerCompletion("cameras", context -> getCameraStorage().getCameraIds());

        getLogger().info(() -> "Enabled %s v%s".formatted(getDescription().getName(), getDescription().getVersion()));
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, 9107);
    }

    @Override
    public void onDisable() {
        getLogger().info(() -> "Disabled %s v%s".formatted(getDescription().getName(), getDescription().getVersion()));
    }

    public String getPluginChatPrefix() {
        return pluginChatPrefix;
    }


    public PluginConfig getConfigPlugin() {
        return pluginConfig;
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


    private List<Integer> generateIntsInRange(int max) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < max; i++){
			list.add(i);
		}
		return list;
    }
}
