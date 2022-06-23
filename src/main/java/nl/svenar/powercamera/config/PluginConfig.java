package nl.svenar.powercamera.config;


import com.github.sarhatabaot.kraken.core.config.ConfigFile;

import nl.svenar.powercamera.PowerCamera;
import org.jetbrains.annotations.NotNull;

public class PluginConfig extends ConfigFile<PowerCamera> {
	public PluginConfig(@NotNull final PowerCamera plugin) {
		super(plugin, "", "config.yml", "");
	}

}
