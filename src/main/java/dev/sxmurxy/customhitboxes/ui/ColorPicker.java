package dev.sxmurxy.customhitboxes.ui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.CustomHitboxes;
import dev.sxmurxy.customhitboxes.settings.impl.ColorSetting;
import dev.sxmurxy.customhitboxes.utils.DrawUtils;
import dev.sxmurxy.customhitboxes.utils.DrawUtils.GradientDirection;
import dev.sxmurxy.customhitboxes.utils.HoverUtils;
import dev.sxmurxy.customhitboxes.utils.Utils;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class ColorPicker extends Element {

	private static final ColorPicker instance = new ColorPicker();
	public static final ResourceLocation TRANSPARENT = new ResourceLocation(CustomHitboxes.MOD_ID, "textures/transparent.png");
	private final double sliderHeight = 18, radius = 50, sliderWidth = radius * 2, centerX = x + 70, centerY = y - 70, 
			xp = x + 20, xs = x + 135, sy = y - 20, brightnessY = sy - 71, saturationY = sy - 35.5, alphaY = sy;
	private double pointerX, pointerY;
	private boolean opened, pointerDragging, brightnessDragging, saturationDragging, alphaDragging;
	private float hue, saturation, brightness, alpha;
	private TextFieldWidget hexInput;
	private ColorSetting setting;
	private String title;

	private ColorPicker() {
		super(460, 355, 250, 170, "");
		hexInput = new TextFieldWidget(FONT, (int)xs + 39, (int)(y - height) + 22, 60, 18, new TranslationTextComponent("colorPicker.hexInput"));
		hexInput.setMaxLength(7);
		hexInput.setMessage(new StringTextComponent("#Hex"));
	}

	@Override
	public void render(MatrixStack matrices, double mouseX, double mouseY) {
		if (pointerDragging)
			setPointerDraggingPos(mouseX, mouseY);
		else if (brightnessDragging)
			setBrightnessDraggingPos(mouseX);
		else if (saturationDragging)
			setSaturationDraggingPos(mouseX);
		else if (alphaDragging)
			setAlphaDraggingPos(mouseX);
		DrawUtils.drawDefaultBackground(x, y, width, height);
		DrawUtils.drawTextWithShadow(matrices, title, x + (width - MC.font.width(title)) / 2, y - height + 12.5, 1, Color.WHITE);
		if (HoverUtils.hovered(mouseX, mouseY, x + width - 13, y - height + 12, 9, 9))
			DrawUtils.drawClosingCross(matrices, x + width - 13, y - height + 13, Color.RED);
		else
			DrawUtils.drawClosingCross(matrices, x + width - 13, y - height + 13, Color.WHITE);
		DrawUtils.drawBorderedRect(x + width / 2 - 40, y - height + 40, 80, 18, 1, getColor(), Color.WHITE);
		hexInput.render(matrices, (int)mouseX, (int)mouseY, 0f);
		DrawUtils.drawRainbowCircle(centerX, centerY, radius - 1, 3f);
		
		// pointer
		DrawUtils.drawRect(xp + pointerX - 0.5, sy - pointerY + 4.5, 1, 10, Color.BLACK);
		DrawUtils.drawRect(xp + pointerX - 5, sy - pointerY, 10, 1, Color.BLACK);
		
		// brightness slider
		renderSlider(matrices, brightnessY, Color.BLACK, Color.getHSBColor(hue, saturation, 1f), brightness, "Brightness");
		
		// saturation slider
		renderSlider(matrices, saturationY, Color.WHITE, Color.getHSBColor(hue, 1f, brightness), saturation, "Saturation");
		
		// alpha slider
		TEX_MANAGER.bind(TRANSPARENT);
		AbstractGui.blit(matrices, (int)xs, (int)(sy - sliderHeight), 0, 0, (int)sliderWidth, (int)sliderHeight, (int)sliderWidth, (int)sliderHeight);
		GL11.glDisable(GL11.GL_ALPHA_TEST);
		Color color = Color.getHSBColor(hue, saturation, brightness);
		renderSlider(matrices, alphaY, Utils.injectAlpha(color, 1), color, alpha, "Alpha");
		GL11.glEnable(GL11.GL_ALPHA_TEST);
	}
	
	private void renderSlider(MatrixStack matrices, double y, Color left, Color right, float sv,  String sliderTitle) {
		DrawUtils.drawRectWithGradient(xs, y, sliderWidth, sliderHeight, GradientDirection.HORIZONTAL, left, right);
		DrawUtils.drawRect(xs + (sv * sliderWidth) - 0.5, y, 2, sliderHeight, Color.BLACK);
		DrawUtils.drawTextWithShadow(matrices, sliderTitle, xs + 1, y - 20, 1, Color.WHITE);
		DrawUtils.drawTextWithShadow(matrices, String.valueOf(Math.ceil(sv * 100)), xs + FONT.width(sliderTitle) + 7,
				y - 20, 1f, Color.WHITE);
	}

	public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (HoverUtils.hovered(mouseX, mouseY, x + width - 12, y - height + 12, 8, 8)) {
			opened = false;
			MC.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1f, 2f);
		} else if (HoverUtils.hovered(mouseX, mouseY, centerX, centerY, radius))
			pointerDragging = true;
		else if (HoverUtils.hovered(mouseX, mouseY, xs, brightnessY, sliderWidth, sliderHeight))
			brightnessDragging = true;
		else if (HoverUtils.hovered(mouseX, mouseY, xs, saturationY, sliderWidth, sliderHeight))
			saturationDragging = true;
		else if (HoverUtils.hovered(mouseX, mouseY, xs, alphaY, sliderWidth, sliderHeight))
			alphaDragging = true;
		else 
			hexInput.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void mouseReleased(double mouseX, double mouseY, int mouseButton) {
		pointerDragging = false;
		saturationDragging = false;
		brightnessDragging = false;
		alphaDragging = false;
	}
	
	public void keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		hexInput.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
		inputUpdate();
	}
	
	public void chatTyped(char p_231042_1_, int p_231042_2_) {
		hexInput.charTyped(p_231042_1_, p_231042_2_);
		inputUpdate();
	}
	
	public void tick() {
		hexInput.tick();
	}
	
	private void calculatePointerPosition() {
		double angle = Math.PI * 2 * hue;
		double h = saturation * radius;
		double x1, y1;
		if (angle >= 3 * Math.PI / 2) {
			x1 = Math.cos(angle - 3 * Math.PI / 2) * h;
			y1 = cathet(h, x1);
		} else if (angle >= Math.PI) {
			x1 = Math.sin(angle - Math.PI) * h;
			y1 = -cathet(h, x1);
		} else if (angle >= Math.PI / 2) {
			x1 = -Math.cos(angle - Math.PI / 2) * h;
			y1 = -cathet(h, x1);
		} else {
			x1 = -Math.sin(angle) * h;
			y1 = cathet(h, x1);
		}
		pointerX = radius - x1;
		pointerY = radius - y1;
	}
	
	private void calculateColor() {
		double h = Math.sqrt(square(pointerX - radius) + square(radius - pointerY));
		double c = pointerX - radius;
		double angle;
		if (pointerX >= radius && pointerY < radius)
			angle = Math.asin(c / h);
		else if (pointerX >= radius && pointerY > radius)
			angle = Math.PI / 2 + Math.acos(c / h);
		else if (pointerX < radius && pointerY >= radius)
			angle = Math.PI + Math.asin(-c / h);
		else
			angle = 3 * Math.PI / 2 + Math.acos(-c / h);
		hue = (float) (angle / (Math.PI * 2f));
		saturation = (float) (h / radius);
		saveValue(true);
	}

	private void inputUpdate() {
		if(hexInput.getValue().startsWith("#") && hexInput.getValue().length() == 7) {
			try {
				Color color = Color.decode(hexInput.getValue());
				applyColor(color, false);
				calculatePointerPosition();
				saveValue(false);
			} catch(NumberFormatException ignored) {
				
			}
		} else if(!hexInput.getValue().isEmpty() && !hexInput.getValue().startsWith("#")) {
			hexInput.setTextColor(new Color(194, 35, 35).getRGB());
			hexInput.setSuggestion("");
		} else {
			hexInput.setTextColor(14737632);
			hexInput.setSuggestion("#FFFFFF".substring(hexInput.getValue().length(), 7));
		}
	}
	
	private void updateInputValue(Color color) {
		hexInput.setValue(String.format("#%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue()));
	}

	private void setPointerDraggingPos(double mouseX, double mouseY) {
		if (!HoverUtils.hovered(mouseX, mouseY, centerX, centerY, radius)) {
			double dX = mouseX - centerX;
			double dY = mouseY - centerY;
			double angle;
			double x1, y1;
			if (mouseX >= centerX && mouseY >= centerY) {
				angle = -Math.atan(dX / dY);
				x1 = Math.sin(angle) * radius;
				y1 = cathet(radius, x1);
			} else if (mouseX >= centerX && mouseY < centerY) {
				angle = Math.atan(dY / dX);
				x1 = -Math.cos(angle) * radius;
				y1 = -cathet(radius, x1);
			} else if (mouseX < centerX && mouseY < centerY) {
				angle = Math.atan(dX / dY);
				x1 = Math.sin(angle) * radius;
				y1 = -cathet(radius, x1);
			} else {
				angle = -Math.atan(dY / dX);
				x1 = Math.cos(angle) * radius;
				y1 = cathet(radius, x1);
			}
			pointerX = radius - x1;
			pointerY = radius - y1;
		} else {
			pointerX = mouseX - xp;
			pointerY = sy - mouseY;
		}
		calculateColor();
	}

	private void setBrightnessDraggingPos(double mouseX) {
		brightness = (float) (calculateSliderX(mouseX) / sliderWidth);
		saveValue(true);
	}

	private void setSaturationDraggingPos(double mouseX) {
		saturation = (float) (calculateSliderX(mouseX) / sliderWidth);
		calculatePointerPosition();
		saveValue(true);
	}

	private void setAlphaDraggingPos(double mouseX) {
		alpha = (float) (calculateSliderX(mouseX) / sliderWidth);
		saveValue(true);
	}
	
	private Color getColor() {
		return Utils.injectAlpha(Color.getHSBColor(hue, saturation, brightness), alpha);
	}
	
	private double calculateSliderX(double mouseX) {
		return mouseX - xs > 0 ? Math.min(sliderWidth, mouseX - xs) : 0;
	}

	private double cathet(double h, double a) {
		return Math.sqrt(square(h) - square(a));
	}

	private double square(double a) {
		return a * a;
	}

	private void saveValue(boolean updateInput) {
		Color color = getColor();
		setting.setValue(color);
		if(updateInput)
			updateInputValue(color);
	}

	public boolean isOpened() {
		return opened;
	}

	public void toggle(ColorSetting setting) {
		if(opened) {
			if(this.setting == setting)
				opened = false;
			else
				create(setting);
		} else {
			if(this.setting == setting)
				opened = true;
			else
				create(setting);
		}
	}

	private void create(ColorSetting setting) {
		this.setting = setting;
		this.title = setting.getName();
		applyColor(setting.getValue(), true);
		updateInputValue(setting.getValue());
		this.opened = true;
	}
	
	private void applyColor(Color color, boolean updateAlpha) {
		float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
		this.hue = hsb[0];
		this.saturation = hsb[1];
		this.brightness = hsb[2];
		if(updateAlpha)
			this.alpha = color.getAlpha() / 255.0f;
		calculatePointerPosition();
	}

	public void onClose() {
		mouseReleased(-1, -1, -1);
	}

	public static ColorPicker getInstance() {
		return instance;
	}

}
