package dev.sxmurxy.customhitboxes.settings;

import com.google.gson.JsonElement;

import dev.sxmurxy.customhitboxes.ui.Element;

public class Setting<T> extends Element {

	private SettingDataHandler<T> handler;
	protected T value;

	protected Setting(String name, T defaultValue, SettingDataHandler<T> handler) {
		super(155, 20, name);
		this.value = defaultValue;
		this.handler = handler;
	}
	
	public String getName() {
		return name;
	}
	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
		MOD.configManager.save(this);
	}
	
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		
	}
	
	public void read(JsonElement el) {
		this.value = handler.read(el);
	}

	public JsonElement write() {
		return handler.write(value);
	}

}
