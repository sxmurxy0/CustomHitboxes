package dev.sxmurxy.customhitboxes;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import dev.sxmurxy.customhitboxes.settings.Setting;
import dev.sxmurxy.customhitboxes.settings.impl.BooleanSetting;
import dev.sxmurxy.customhitboxes.settings.impl.ColorSetting;
import dev.sxmurxy.customhitboxes.ui.UserInterface;
import dev.sxmurxy.customhitboxes.utils.EventHandler;
import dev.sxmurxy.customhitboxes.utils.io.ConfigManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CustomHitboxes.MOD_ID)
public class CustomHitboxes {
	
	private static CustomHitboxes instance;
	public static final String MOD_ID = "customhitboxes";
	public Settings settings;
	public ConfigManager configManager;
	public UserInterface userInterface;

	public CustomHitboxes() {
		instance = this;
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::init);
	}

	private void init(final FMLCommonSetupEvent event) {
		this.settings = new Settings();
		this.configManager = new ConfigManager();
		this.userInterface = new UserInterface();
		this.settings.init();
		this.configManager.loadConfig();
		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	public static CustomHitboxes getInstance() {
		return instance;
	}
	
	public class Settings {
		public final List<Setting<?>> settingsList;
		public final BooleanSetting enabled;
		public final ColorSetting defaultColor;
		public final ColorSetting eyeColor;
		public final ColorSetting viewingLineColor;
		public final ColorSetting playerColor;
		public final ColorSetting animalColor;
		public final ColorSetting monsterColor;
		public final ColorSetting throwableColor;
		public final ColorSetting dropColor;
		public final ColorSetting miscColor;
		public final BooleanSetting eyeHitbox;
		public final BooleanSetting viewingLine;
		public final BooleanSetting players;
		public final BooleanSetting animals;
		public final BooleanSetting monsters;
		public final BooleanSetting throwables;
		public final BooleanSetting drop;
		public final BooleanSetting misc;
		public final KeyBinding toggleInterfaceKey;
		
		private Settings() {
			this.settingsList = Lists.newArrayList();
			this.enabled = new BooleanSetting("Enabled", true);
			this.defaultColor = new ColorSetting("Default color", Color.WHITE);
			this.eyeColor = new ColorSetting("Eye color", Color.RED);
			this.viewingLineColor = new ColorSetting("Viewing line color", Color.BLUE);
			this.playerColor = new ColorSetting("Player color", Color.WHITE);
			this.animalColor = new ColorSetting("Animal color", Color.WHITE);
			this.monsterColor = new ColorSetting("Monster color", Color.WHITE);
			this.throwableColor = new ColorSetting("Throwable color", Color.WHITE);
			this.dropColor = new ColorSetting("Drop color", Color.WHITE);
			this.miscColor = new ColorSetting("Other color", Color.WHITE);
			this.eyeHitbox = new BooleanSetting("Eye hitbox", false, eyeColor);
			this.viewingLine = new BooleanSetting("Viewing line", false, viewingLineColor);
			this.players = new BooleanSetting("Custom players", true, playerColor, Items.PLAYER_HEAD);
			this.animals = new BooleanSetting("Custom animals", true, animalColor, Items.PIG_SPAWN_EGG);
			this.monsters = new BooleanSetting("Custom monsters", true, monsterColor, Items.ZOMBIE_SPAWN_EGG);
			this.throwables = new BooleanSetting("Custom throwables", false, throwableColor, Items.SPLASH_POTION);
			this.drop = new BooleanSetting("Custom drop", false, dropColor, Items.GOLDEN_APPLE);
			this.misc = new BooleanSetting("Other", false, miscColor, Items.ARROW);
			this.toggleInterfaceKey = new KeyBinding("key.customhitboxes.openInterface", 345,
					"key.category." + CustomHitboxes.MOD_ID);
		}
		
		private void init() {
			ClientRegistry.registerKeyBinding(toggleInterfaceKey);
			double y = 145;
			for(Field field : this.getClass().getDeclaredFields()) {
				Class<?> superClass = field.getType().getSuperclass();
				if(superClass == null)
					continue;
				if(superClass.isAssignableFrom(Setting.class)) {
					try {
						Setting<?> setting = (Setting<?>)field.get(this);
						if(setting instanceof ColorSetting && !((ColorSetting)setting).isSeparated());
						else setting.setCoords(155, y += setting.getHeight() + 4);
						this.settingsList.add(setting);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			Collections.reverse(settingsList);
		}
		
	}
	
}
