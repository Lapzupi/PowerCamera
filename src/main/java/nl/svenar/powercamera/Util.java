package nl.svenar.powercamera;

import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.lang.NoSuchMethodError;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class Util {
	private static Random random = new Random();

	@Deprecated
	public static String serializeLocation(Location loc) {
		return loc.getWorld().getUID() + ";" + loc.getX() + ";" + loc.getY() + ";" + loc.getZ() + ";" + loc.getYaw() + ";" + loc.getPitch();
	}

	@Deprecated
	public static Location deserializeLocation(String input) {
		String[] input_split = input.split(";");

		UUID world_uid = UUID.fromString(input_split[0]);

		double x = Double.parseDouble(input_split[1]);
		double y = Double.parseDouble(input_split[2]);
		double z = Double.parseDouble(input_split[3]);

		float yaw = Float.parseFloat(input_split[4]);
		float pitch = Float.parseFloat(input_split[5]);

		World world = Bukkit.getServer().getWorld(world_uid);
		if (world == null) {
			world = Bukkit.getServer().getWorlds().get(0);
		}

		return new Location(world, x, y, z, yaw, pitch);
	}

	//todo pretty sure this is in Time
	public static int timeStringToSecondsConverter(String timeInput) {
		Matcher regex_int = Pattern.compile("^\\d+[^a-zA-Z]?$").matcher(timeInput);

		Matcher regex_seconds = Pattern.compile("\\d+[sS]").matcher(timeInput);
		Matcher regex_minutes = Pattern.compile("\\d+[mM]").matcher(timeInput);
		Matcher regex_hours = Pattern.compile("\\d+[hH]").matcher(timeInput);

		int seconds = 0;

		if (regex_int.find()) {
			seconds = Integer.parseInt(timeInput);
		} else {
			if (regex_seconds.find()) {
				seconds += Integer.parseInt(timeInput.substring(regex_seconds.start(), regex_seconds.end() - 1));
			}

			if (regex_minutes.find()) {
				seconds += Integer.parseInt(timeInput.substring(regex_minutes.start(), regex_minutes.end() - 1)) * 60;
			}

			if (regex_hours.find()) {
				seconds += Integer.parseInt(timeInput.substring(regex_hours.start(), regex_hours.end() - 1)) * 3600;
			}
		}

		return seconds;
	}

	public static boolean isPlayerInvisible(Player player) {
		try {
			return player.isInvisible();
		} catch (NoSuchMethodError e) {
			return player.hasPotionEffect(PotionEffectType.INVISIBILITY);
		}
	}

	public static Random getRandom() {
		return random;
	}
}
