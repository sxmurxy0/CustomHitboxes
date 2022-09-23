package dev.sxmurxy.customhitboxes.utils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import dev.sxmurxy.customhitboxes.Wrapper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class ConfigHelper {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	public static final JsonParser GSON_PARSER = new JsonParser();

	public static JsonObject readAsJson(File file) {
		StringBuilder content = new StringBuilder("");
		try {
			if(!file.exists())
				return new JsonObject();
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			bufferedReader.lines().forEach(l -> content.append(l));
			bufferedReader.close();
		} catch (IOException e) {
			configError();
			e.printStackTrace();
		}
		return parseString(content.toString());
	}

	public static void writeJsonObject(File file, JsonObject jo) {
		String content = GSON.toJson(jo);
		try {
			if(!file.exists())
				file.createNewFile();
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
			bufferedWriter.write(content);
			bufferedWriter.close();
		} catch (IOException e) {
			configError();
			e.printStackTrace();
		}
	}

	public static void appendOrOverwriteSetting(File file, String key, JsonElement je) {
		JsonObject jo = readAsJson(file);
		jo.add(key, je);
		writeJsonObject(file, jo);
	}

	public static JsonObject parseString(String str) {
		try {
			return GSON_PARSER.parse(str).getAsJsonObject();
		} catch (JsonParseException | IllegalStateException e) {
			return new JsonObject();
		}
	}
	
	public static File getCanonicalFile(File file) {
		try {
			return file.getCanonicalFile();
		} catch (IOException ignored) {
			return null;
		}
	}

	private static void configError() {
		Wrapper.CHAT.addMessage(new StringTextComponent("Config error! Something is wrong with file: " + 
				ConfigManager.CONFIG_FILE_PATH.getAbsolutePath()).withStyle(TextFormatting.RED));
	}

}
