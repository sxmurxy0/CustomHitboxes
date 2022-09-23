package dev.sxmurxy.customhitboxes.settings.impl;

import java.awt.Color;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.settings.Setting;
import dev.sxmurxy.customhitboxes.settings.SettingDataHandlers;
import dev.sxmurxy.customhitboxes.ui.animations.DoubleAnimation;
import dev.sxmurxy.customhitboxes.utils.DrawUtils;
import dev.sxmurxy.customhitboxes.utils.HoverUtils;
import net.minecraft.item.Item;
import net.minecraft.util.SoundEvents;

public class BooleanSetting extends Setting<Boolean> {

	private DoubleAnimation toggleAnim = new DoubleAnimation(1);
	private Item item;
	private ColorSetting colorSetting;

	public BooleanSetting(String name, boolean defaultValue, ColorSetting colorSetting, Item item) {
		super(name, defaultValue, SettingDataHandlers.BOOLEAN);
		this.item = item;
		this.colorSetting = colorSetting;
		if(this.colorSetting != null)
			colorSetting.setSeparated(false);
	}
	
	public BooleanSetting(String name, boolean defaultValue, ColorSetting colorSetting) {
		this(name, defaultValue, colorSetting, null);
	}
	
	public BooleanSetting(String name, boolean defaultValue) {
		this(name, defaultValue, null, null);
	}

	@Override
	public void render(MatrixStack matrices, double mouseX, double mouseY) {
		DrawUtils.drawDefaultBackground(x, y, width, height);
		if(item != null) {
			MC.getItemRenderer().renderGuiItem(item.getDefaultInstance(), (int)x, (int)(y - height + 1));
			DrawUtils.drawTextWithShadow(matrices, name, x + 20, y - textCenterY, 1f, Color.WHITE);
		} else {
			DrawUtils.drawTextWithShadow(matrices, name, x + 4, y - textCenterY, 1f, Color.WHITE);
		}
		if(value) {
			if (toggleAnim.isStarting())
				DrawUtils.drawDisabledCheckbox(matrices, x + width - 15, y - textCenterY + 0.5f, toggleAnim.get());
			else
				DrawUtils.drawEnabledCheckbox(matrices, x + width - 15, y - textCenterY + 0.5f, toggleAnim.get());
		} else {
			if (toggleAnim.isStarting())
				DrawUtils.drawEnabledCheckbox(matrices, x + width - 15, y - textCenterY + 0.5f, toggleAnim.get());
			else
				DrawUtils.drawDisabledCheckbox(matrices, x + width - 15, y - textCenterY + 0.5f, toggleAnim.get());
		}
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if(HoverUtils.hovered(mouseX, mouseY, x, y, width, height)) {
			if(colorSetting == null || !HoverUtils.hovered(mouseX, mouseY, x + width - 32, y - 5, 11, 11)) {
				setValue(!value);
				toggleAnim.begin(1, 0.9, 0.9, true);
				MC.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 2f);
			}
		}
	}
	
	@Override
	public void setCoords(double x, double y) {
		super.setCoords(x, y);
		if(colorSetting != null)
			colorSetting.setCoords(x, y);
	}
	
}
