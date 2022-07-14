package nl.svenar.powercamera;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import co.aikar.commands.PaperCommandManager;
import com.github.sarhatabaot.kraken.core.db.ConnectionFactory;
import com.github.sarhatabaot.kraken.core.db.MySqlConnectionFactory;
import com.github.sarhatabaot.kraken.core.db.SqlLiteConnectionFactory;
import com.github.sarhatabaot.kraken.core.logging.LoggerUtil;
import nl.svenar.powercamera.commands.PowerCameraCommand;
import nl.svenar.powercamera.listeners.QuitListener;
import nl.svenar.powercamera.managers.CameraManager;
import nl.svenar.powercamera.managers.PlayerManager;
import nl.svenar.powercamera.storage.CameraStorage;
import nl.svenar.powercamera.storage.PlayerStorage;
import nl.svenar.powercamera.storage.configurate.CameraConfigurate;
import nl.svenar.powercamera.storage.configurate.PlayersConfigurate;
import nl.svenar.powercamera.storage.db.CameraSql;
import nl.svenar.powercamera.storage.db.PlayerSql;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import nl.svenar.powercamera.config.PluginConfig;
import nl.svenar.powercamera.listeners.JoinListener;
import nl.svenar.powercamera.listeners.MoveListener;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public class PowerCamera extends JavaPlugin {
    private final String pluginChatPrefix = ChatColor.BLACK + "[" + ChatColor.AQUA + getDescription().getName() + ChatColor.BLACK + "] ";

    private PluginConfig pluginConfig;

    private CameraStorage cameraStorage;
    private PlayerStorage playerStorage;

    //These Should be in managers/caches
    private PlayerManager playerManager;
    private CameraManager cameraManager;


    private final Instant startTime = Instant.now();

    @Override
    public void onEnable() {
        initConfig();
        initStorage();
        initManagers();

        registerListeners();

        initCommands();
        getLogger().info(() -> "Enabled %s v%s".formatted(getDescription().getName(), getDescription().getVersion()));
        @SuppressWarnings("unused")
        Metrics metrics = new Metrics(this, 9107);
    }

    private void initConfig() {
        try {
            this.pluginConfig = new PluginConfig(this);
        } catch (ConfigurateException e) {
            getLogger().severe(() -> "Could not initialize config.conf");
            getLogger().warning(() -> "Please fix any errors and reload the plugin.");
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();
        pluginManager.registerEvents(new MoveListener(this), this);
        pluginManager.registerEvents(new JoinListener(this), this);
        pluginManager.registerEvents(new QuitListener(getPlayerManager()),this);
    }

    private void initCommands() {
        PaperCommandManager commandManager = new PaperCommandManager(this);
        commandManager.enableUnstableAPI("help");
        commandManager.enableUnstableAPI("brigadier");
        commandManager.registerCommand(new PowerCameraCommand(this));
        commandManager.getCommandCompletions().registerCompletion("points", context ->
                generateIntsInRange(cameraManager.getCamera(playerManager.getSelectedCameraId(context.getPlayer().getUniqueId())).getPoints().size()).stream().map(Object::toString).toList()
        );
        commandManager.getCommandCompletions().registerCompletion("cameras", context -> cameraManager.getCameraIds());
    }

    private void initManagers() {
        this.playerManager = new PlayerManager();
        this.cameraManager = new CameraManager(this);
    }

    private void initStorage() {
        String databaseType = pluginConfig.getDatabase().getType();
        switch (databaseType) {
            case "sql" -> {
                ConnectionFactory<PowerCamera> connectionFactory = new MySqlConnectionFactory<>("powercamera-hikari");
                this.cameraStorage = new CameraSql(connectionFactory);
                this.playerStorage = new PlayerSql(connectionFactory);
            }
            case "sqlite" -> {
                ConnectionFactory<PowerCamera> connectionFactory = new SqlLiteConnectionFactory<>("powercamera-hikari");
                this.cameraStorage = new CameraSql(connectionFactory);
                this.playerStorage = new PlayerSql(connectionFactory);
            }
            //hocon
            default -> {
                try {
                    this.cameraStorage = new CameraConfigurate(this);
                    this.playerStorage = new PlayersConfigurate(this);
                } catch (ConfigurateException e) {
                    LoggerUtil.logSevereException(e);
                }
            }

        }

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


    private @NotNull List<Integer> generateIntsInRange(int max) {
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < max; i++){
			list.add(i);
		}
		return list;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }
}
