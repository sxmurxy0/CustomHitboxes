package dev.sxmurxy.customhitboxes.ui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.Wrapper;
import dev.sxmurxy.customhitboxes.settings.Setting;
import dev.sxmurxy.customhitboxes.utils.DrawUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

public class UserInterface extends Screen implements Wrapper {

	private static final ColorPicker cp = ColorPicker.getInstance();
	private double scale;
	private boolean opened;

	public UserInterface() {
		super(new StringTextComponent("UserInterface"));
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
		mouseX /= scale;
		mouseY /= scale;
		DrawUtils.drawRect(0, WINDOW.getGuiScaledHeight(), WINDOW.getGuiScaledWidth(), WINDOW.getGuiScaledHeight(), new Color(0f, 0f, 0f, 0.35f));
		GL11.glPushMatrix();
		GL11.glScaled(scale, scale, 1);
		DrawUtils.drawTextWithShadow(matrices, "CustomHitboxes v1", 225 - FONT.width("CustomHitboxes v1") / 1.35f, 140, 1.5f, Color.WHITE);
		for(Setting<?> setting : MOD.settings.settingsList) {
			setting.render(matrices, mouseX, mouseY);
		}
		if(cp.isOpened())
			cp.render(matrices, mouseX, mouseY);
		GL11.glPopMatrix();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if(mouseButton == 0) {
			mouseX /= scale;
			mouseY /= scale;
			for(Setting<?> setting : MOD.settings.settingsList) {
				setting.mouseClicked(mouseX, mouseY, mouseButton);
			}
			if(cp.isOpened())
				cp.mouseClicked(mouseX, mouseY, mouseButton);
		}
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
		if(mouseButton == 0 && cp.isOpened()) 
			cp.mouseReleased(mouseX /= scale, mouseY /= scale, mouseButton);
		return super.mouseReleased(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public boolean keyPressed(int p_231046_1_, int p_231046_2_, int p_231046_3_) {
		if(cp.isOpened())
			cp.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
		return super.keyPressed(p_231046_1_, p_231046_2_, p_231046_3_);
	}
	
	@Override
	public boolean charTyped(char p_231042_1_, int p_231042_2_) {
		if(cp.isOpened())
			cp.chatTyped(p_231042_1_, p_231042_2_);
		return super.charTyped(p_231042_1_, p_231042_2_);
	}
	
	@Override
	public void tick() {
		if(cp.isOpened())
			cp.tick();
	}
	
	public void toggle() {
		opened = !opened;
		if (opened)
			MC.setScreen(this);
		else
			onClose();
	}

	private void calculateScale() {
		switch ((int)WINDOW.getGuiScale()) {
		case 1:
			scale = 2;
			break;
		case 2:
			scale = 1;
			break;
		case 3:
			scale = 2 / 3d;
			break;
		case 4:
			scale = 0.5;
			break;
		}
	}
	
	@Override
	public void init() {
		calculateScale();
	}
	
	@Override
	public void onClose() {
		if (cp.isOpened())
			cp.onClose();
		opened = false;
		MC.setScreen((Screen)null);
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return true;
	}

}
