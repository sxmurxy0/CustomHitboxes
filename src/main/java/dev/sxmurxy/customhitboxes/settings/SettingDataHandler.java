package dev.sxmurxy.customhitboxes.settings;

import com.google.gson.JsonElement;

public interface SettingDataHandler<T> {
	
	default JsonElement write(T value) {
		return null;
	}
	
	T read(JsonElement je);
	
}
