package dev.sxmurxy.customhitboxes.utils;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.CustomHitboxes;
import dev.sxmurxy.customhitboxes.Wrapper;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;

public class DrawUtils implements Wrapper {

	public static final ResourceLocation SMOOTH_FONT = new ResourceLocation(CustomHitboxes.MOD_ID, "smooth");
	public static final ResourceLocation CHECK = new ResourceLocation(CustomHitboxes.MOD_ID, "textures/icons/check.png");
	public static final ResourceLocation CROSS = new ResourceLocation(CustomHitboxes.MOD_ID, "textures/icons/cross.png");
	public static final ResourceLocation CLOSE = new ResourceLocation(CustomHitboxes.MOD_ID, "textures/icons/close.png");
	public static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0f, 0.85f);
	public static final Color OFF_CHECKBOX_COLOR = new Color(207, 37, 19);
	public static final Color ON_CHECKBOX_COLOR = new Color(25, 230, 52);
	public static final int STEPS = 50;

	public enum CirclePart {
		FIRST_QUARTER(4, Math.PI / 2), 
		SECOND_QUARTER(4, Math.PI), 
		THIRD_QUARTER(4, 3 * Math.PI / 2),
		FOURTH_QUARTER(4, 0d);

		private int ratio;
		private double additionalAngle;

		private CirclePart(int ratio, double addAngle) {
			this.ratio = ratio;
			this.additionalAngle = addAngle;
		}
	}

	public enum GradientDirection {
		HORIZONTAL(new int[] { 0, 0, 1, 1 }), 
		VERTICAL(new int[] { 1, 0, 0, 1 });

		private int[] index;

		public Color[] getVertexColors(Color... colors) {
			return new Color[] { colors[index[0]], colors[index[1]], colors[index[2]], colors[index[3]] };
		}

		private GradientDirection(int[] index) {
			this.index = index;
		}
	}

	public static void drawPartOfCircle(double x, double y, double radius, CirclePart part, Color firstColor, Color secondColor) {
		double angle = Math.PI * 2 / STEPS / part.ratio;
		drawSetup();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_TRIANGLE_FAN);
		GL11.glColor4f(firstColor.getRed() / 255.0f, firstColor.getGreen() / 255.0f, firstColor.getBlue() / 255.0f,
				firstColor.getAlpha() / 255.0f);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(secondColor.getRed() / 255.0f, secondColor.getGreen() / 255.0f, secondColor.getBlue() / 255.0f,
				secondColor.getAlpha() / 255.0f);
		for (int i = 0; i <= STEPS; i++) {
			GL11.glVertex2d(x + radius * Math.sin(part.additionalAngle + angle * i),
					y + radius * Math.cos(part.additionalAngle + angle * i));
		}
		GL11.glEnd();
		drawFinish();
	}
	
	public static void drawRect(double x, double y, double width, double height, Color color) {
		GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
		drawSetup();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x, y);
		GL11.glVertex2d(x + width, y);
		GL11.glVertex2d(x + width, y - height);
		GL11.glVertex2d(x, y - height);
		GL11.glEnd();
		drawFinish();
	}
	
	public static void drawSmoothRect(double x, double y, double width, double height, Color color) {
		drawRect(x, y, width, height, color);
	    drawRect(x - 0.5f, y, 0.5f, height, color);
	    drawRect(x + width, y, 0.5f, height, color);
	    drawRect(x, y + 0.5f, width, 0.5f, color);
	    drawRect(x, y - height, width, 0.5f, color);
	}

	public static void drawBorderedRect(double x, double y, double width, double height, float borderWidth,
			Color fillColor, Color borderColor) {
		drawRect(x, y, width, height, borderColor);
		drawRect(x + borderWidth, y - borderWidth, width - borderWidth * 2, height - borderWidth * 2, fillColor);
	}

	public static void drawDefaultBackground(double x, double y, double width, double height) {
		Color transparent = Utils.injectAlpha(BACKGROUND_COLOR, 0);
		drawRect(x, y, width, height, BACKGROUND_COLOR);
		drawRectWithGradient(x, y + 2, width, 2, GradientDirection.VERTICAL, transparent, BACKGROUND_COLOR);
		drawRectWithGradient(x, y - height, width, 2, GradientDirection.VERTICAL, BACKGROUND_COLOR, transparent);
		drawRectWithGradient(x - 2, y, 2, height, GradientDirection.HORIZONTAL, transparent, BACKGROUND_COLOR);
		drawRectWithGradient(x + width, y, 2, height, GradientDirection.HORIZONTAL, BACKGROUND_COLOR, transparent);
		drawPartOfCircle(x + width, y - height, 2, CirclePart.FIRST_QUARTER, BACKGROUND_COLOR, transparent);
		drawPartOfCircle(x, y - height, 2, CirclePart.SECOND_QUARTER, BACKGROUND_COLOR, transparent);
		drawPartOfCircle(x, y, 2, CirclePart.THIRD_QUARTER, BACKGROUND_COLOR, transparent);
		drawPartOfCircle(x + width, y, 2, CirclePart.FOURTH_QUARTER, BACKGROUND_COLOR, transparent);
	}

	public static void drawRectWithGradient(double x, double y, double width, double height, GradientDirection direction, Color color1, Color color2) {
		drawSetup();
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glBegin(GL11.GL_QUADS);
		Color[] colors = direction.getVertexColors(color1, color2);
		GL11.glColor4f(colors[0].getRed() / 255.0f, colors[0].getGreen() / 255.0f, colors[0].getBlue() / 255.0f,
				colors[0].getAlpha() / 255.0f);
		GL11.glVertex2d(x, y - height);
		GL11.glColor4f(colors[1].getRed() / 255.0f, colors[1].getGreen() / 255.0f, colors[1].getBlue() / 255.0f,
				colors[1].getAlpha() / 255.0f);
		GL11.glVertex2d(x, y);
		GL11.glColor4f(colors[2].getRed() / 255.0f, colors[2].getGreen() / 255.0f, colors[2].getBlue() / 255.0f,
				colors[2].getAlpha() / 255.0f);
		GL11.glVertex2d(x + width, y);
		GL11.glColor4f(colors[3].getRed() / 255.0f, colors[3].getGreen() / 255.0f, colors[3].getBlue() / 255.0f,
				colors[3].getAlpha() / 255.0f);
		GL11.glVertex2d(x + width, y - height);
		GL11.glEnd();
		drawFinish();
	}

	public static void drawRainbowCircle(double x, double y, double radius, double blurRadius) {
		for (int n = 0; n < 5; n++) {
			
			double angle = Math.PI * 2 / 5 / STEPS;
			double addAngle = Math.PI * 2 / 5 * n;
			float hue = n * 0.2f;
			float f = 0.2f / STEPS;
			
			drawSetup();
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glBegin(GL11.GL_TRIANGLE_FAN);
			GL11.glColor3f(1f, 1f, 1f);
			GL11.glVertex2d(x, y);
			for (int i = 0; i <= STEPS; i++) {
				Color color = Color.getHSBColor(hue + f * i, 1f, 1f);
				GL11.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
				GL11.glVertex2d(x + radius * Math.sin(addAngle + angle * i), y + radius * Math.cos(addAngle + angle * i));
			}
			GL11.glEnd();
			
			boolean u = false;
			GL11.glBegin(GL11.GL_TRIANGLE_STRIP);
			for (int i = 0; i <= STEPS + 1; i++) {
				hue += f;
				if(u) {
					Color color = Utils.injectAlpha(Color.getHSBColor(hue, 1f, 1f), 1);
					GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f, color.getAlpha() / 255.0f);
					GL11.glVertex2d(x + (radius + blurRadius) * Math.sin(addAngle + angle * i), y + (radius + blurRadius) * Math.cos(addAngle + angle * i));
				} else {
					Color color = Color.getHSBColor(hue, 1f, 1f);
					GL11.glColor3f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f);
					GL11.glVertex2d(x + radius * Math.sin(addAngle + angle * i), y + radius * Math.cos(addAngle + angle * i));
				}
				u = !u;
			}
			GL11.glEnd();
			drawFinish();
		}
	}

	public static void drawText(MatrixStack matrices, String text, double x, double y, double scale, Color color) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScaled(scale, scale, 1);
		FONT.draw(matrices, new StringTextComponent(text).setStyle(Style.EMPTY.withFont(SMOOTH_FONT)), 0f,
				-FONT.lineHeight + 1, color.getRGB());
		GL11.glPopMatrix();
	}

	public static void drawTextWithShadow(MatrixStack matrices, String text, double x, double y, double scale,
			Color color) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, 0);
		GL11.glScaled(scale, scale, 1);
		FONT.drawShadow(matrices, new StringTextComponent(text).setStyle(Style.EMPTY.withFont(SMOOTH_FONT)), 0f,
				-FONT.lineHeight + 1, color.getRGB());
		GL11.glPopMatrix();
	}

	public static void drawTextWithCustomShadow(MatrixStack matrices, String text, double x, double y, double scale,
			Color textColor, Color shadowColor) {
		drawText(matrices, text, 1, -FONT.lineHeight + 2, scale, shadowColor);
		drawText(matrices, text, 0, -FONT.lineHeight + 1, scale, textColor);
	}

	public static void drawDisabledCheckbox(MatrixStack matrices, double x, double y, double scale) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 5, y - 5, 0);
		GL11.glScaled(scale, scale, 1);
		DrawUtils.drawSmoothRect(-6, 6, 12, 12, new Color(0f, 0f, 0f, 0.4f));
		drawSmoothRect(-5, 5, 10, 10, OFF_CHECKBOX_COLOR);
		TEX_MANAGER.bind(CROSS);
		AbstractGui.blit(matrices, -4, -4, 0, 0, 8, 8, 8, 8);
		GL11.glPopMatrix();
	}

	public static void drawEnabledCheckbox(MatrixStack matrices, double x, double y, double scale) {
		GL11.glPushMatrix();
		GL11.glTranslated(x + 5, y - 5, 0);
		GL11.glScaled(scale, scale, 1);
		DrawUtils.drawSmoothRect(-6, 6, 12, 12, new Color(0f, 0f, 0f, 0.4f));
		drawSmoothRect(-5, 5, 10, 10, ON_CHECKBOX_COLOR);
		TEX_MANAGER.bind(CHECK);
		AbstractGui.blit(matrices, -5, -5, 0, 0, 10, 10, 10, 10);
		GL11.glPopMatrix();
	}

	public static void drawClosingCross(MatrixStack matrices, double x, double y, Color color) {
		GL11.glColor4f(color.getRed() / 255.0f, color.getGreen() / 255.0f, color.getBlue() / 255.0f,
				color.getAlpha() / 255.0f);
		TEX_MANAGER.bind(CLOSE);
		AbstractGui.blit(matrices, (int) x, (int) y - 10, 0, 0, 10, 10, 10, 10);
	}

	private static void drawSetup() {
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	private static void drawFinish() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
	}

}
