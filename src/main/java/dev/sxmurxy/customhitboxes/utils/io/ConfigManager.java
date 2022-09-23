package dev.sxmurxy.customhitboxes.utils.io;

import java.io.File;
import java.util.Map.Entry;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import dev.sxmurxy.customhitboxes.Wrapper;
import dev.sxmurxy.customhitboxes.settings.Setting;

public class ConfigManager implements Wrapper {
	
	public static final String CONFIG_FILE_NAME = "CustomHitboxes.cfg";
	public static final File CONFIG_DIRECTORY = new File(ConfigHelper.getCanonicalFile(MC.gameDirectory.getAbsoluteFile()), "config/");;
	public static final File CONFIG_FILE_PATH = new File(CONFIG_DIRECTORY, CONFIG_FILE_NAME);

	public void save(Setting<?> setting) {
		ConfigHelper.appendOrOverwriteSetting(CONFIG_FILE_PATH, setting.getName(), setting.write());
	}

	public void loadConfig() {
		if (CONFIG_FILE_PATH.exists()) {
			JsonObject config = ConfigHelper.readAsJson(CONFIG_FILE_PATH).getAsJsonObject();
			for(Entry<String, JsonElement> entry : config.entrySet()) {
				Setting<?> setting = MOD.settings.settingsList.stream().filter(s -> s.getName().equals(entry.getKey())).findFirst().orElseGet(null);
				if(setting != null)
					setting.read(entry.getValue());
			}
		}
	}
	
}
