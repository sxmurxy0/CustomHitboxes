package dev.sxmurxy.customhitboxes.settings.impl;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.settings.Setting;
import dev.sxmurxy.customhitboxes.settings.SettingDataHandlers;
import dev.sxmurxy.customhitboxes.ui.ColorPicker;
import dev.sxmurxy.customhitboxes.utils.DrawUtils;
import dev.sxmurxy.customhitboxes.utils.HoverUtils;
import net.minecraft.util.SoundEvents;

public class ColorSetting extends Setting<Color> {

	private static final ColorPicker cp = ColorPicker.getInstance();
	private boolean separated = true;
	
	public ColorSetting(String name, Color defaultColor) {
		super(name, defaultColor, SettingDataHandlers.COLOR);
	}

	@Override
	public void render(MatrixStack matrices, double mouseX, double mouseY) {
		if (separated) {
			DrawUtils.drawDefaultBackground(x, y, width, height);
			DrawUtils.drawTextWithShadow(matrices, name, x + 4, y - textCenterY, 1f, Color.WHITE);
		}
		DrawUtils.drawSmoothRect(x + width - 31, y - textCenterY + 0.5f, 10, 10, DrawUtils.BACKGROUND_COLOR);
		DrawUtils.drawSmoothRect(x + width - 30, y - textCenterY - 0.5f, 8, 8, value);
	}

	@Override
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (HoverUtils.hovered(mouseX, mouseY, x + width - 32, y - 5, 11, 11)) {
			MC.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 2f);
			cp.toggle(this);
		}
	}
	
	public void setSeparated(boolean separated) {
		this.separated = separated;
	}
	
	public boolean isSeparated() {
		return separated;
	}
	
}
