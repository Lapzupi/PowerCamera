package nl.svenar.powercamera.config;


import com.github.sarhatabaot.kraken.core.config.ConfigFile;

import com.github.sarhatabaot.kraken.core.config.HoconConfigurateFile;
import com.github.sarhatabaot.kraken.core.config.Transformation;
import nl.svenar.powercamera.PowerCamera;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.ConfigurateException;

public class PluginConfig extends HoconConfigurateFile<PowerCamera> {
	public PluginConfig(@NotNull final PowerCamera plugin) throws ConfigurateException{
		super(plugin, "", "config.conf", "");
	}

	@Override
	protected void initValues() throws ConfigurateException {

	}

	@Override
	protected void builderOptions() {

	}

	@Override
	protected Transformation getTransformation() {
		return null;
	}
}
