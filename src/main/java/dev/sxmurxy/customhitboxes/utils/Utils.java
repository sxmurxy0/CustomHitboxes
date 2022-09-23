package dev.sxmurxy.customhitboxes.utils;

import java.awt.Color;

import dev.sxmurxy.customhitboxes.Wrapper;
import dev.sxmurxy.customhitboxes.settings.impl.BooleanSetting;
import dev.sxmurxy.customhitboxes.settings.impl.ColorSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.FlyingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.passive.AmbientEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WaterMobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ThrowableEntity;

public class Utils implements Wrapper {
	
	public enum EntityCast {
		PLAYER(MOD.settings.players, MOD.settings.playerColor),
		ANIMAL(MOD.settings.animals, MOD.settings.animalColor),
		MONSTER(MOD.settings.monsters, MOD.settings.monsterColor),
		THROWABLE(MOD.settings.throwables, MOD.settings.throwableColor),
		DROP(MOD.settings.drop, MOD.settings.dropColor),
		MISC(MOD.settings.misc, MOD.settings.miscColor);
		
		private BooleanSetting enabledSetting;
		private ColorSetting colorSetting;
		
		private EntityCast(BooleanSetting enabledSetting, ColorSetting colorSetting) {
			this.enabledSetting = enabledSetting;
			this.colorSetting = colorSetting;
		}
		
		public Color getColor() {
			if(enabledSetting.getValue())
				return colorSetting.getValue();
			else
				return MOD.settings.defaultColor.getValue();
		}
		
	}

	public static EntityCast getEntityCast(Entity entity) {
		if (entity instanceof PlayerEntity)
			return EntityCast.PLAYER;
		else if	(entity instanceof AnimalEntity || entity instanceof AmbientEntity || entity instanceof WaterMobEntity)
			return EntityCast.ANIMAL;
		else if (entity instanceof MonsterEntity || entity instanceof SlimeEntity || entity instanceof FlyingEntity
				|| entity instanceof ShulkerEntity)
			return EntityCast.MONSTER;
		else if(entity instanceof ThrowableEntity) 
			return EntityCast.THROWABLE;
		else if(entity instanceof ItemEntity)
			return EntityCast.DROP;
		else
			return EntityCast.MISC;
	}
	
	public static Color getEntityColor(Entity entity) {
		return getEntityCast(entity).getColor();
	}
	
	public static Color injectAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}
	
	public static Color injectAlpha(Color color, float alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)(alpha * 255.0f));
	}
	
	public static float[] getFloatComponents(Color color) {
		float[] components = new float[4];
		components[0] = color.getRed() / 255.0f;
		components[1] = color.getGreen() / 255.0f;
		components[2] = color.getBlue() / 255.0f;
		components[3] = color.getAlpha() / 255.0f;
		return components;
	}

	public static Color getColor(int color) {
		int r = color >> 16 & 0xFF;
		int g = color >> 8 & 0xFF;
		int b = color & 0xFF;
		int a = color >> 24 & 0xFF;
		return new Color(r, g, b, a);
	}

}
