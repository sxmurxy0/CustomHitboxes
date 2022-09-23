package dev.sxmurxy.customhitboxes.settings;

import java.awt.Color;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

import dev.sxmurxy.customhitboxes.utils.Utils;

public class SettingDataHandlers {

	public static final SettingDataHandler<Boolean> BOOLEAN = new SettingDataHandler<Boolean>() {
		@Override
		public Boolean read(JsonElement je) {
			return je.getAsBoolean();
		}

		@Override
		public JsonElement write(Boolean v) {
			return new JsonPrimitive(v);
		}
	};

	public static final SettingDataHandler<Color> COLOR = new SettingDataHandler<Color>() {
		@Override
		public Color read(JsonElement je) {
			return Utils.getColor(je.getAsInt());
		}

		@Override
		public JsonElement write(Color v) {
			return new JsonPrimitive(v.getRGB());
		}
	};

}
