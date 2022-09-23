package dev.sxmurxy.customhitboxes.ui;

import com.mojang.blaze3d.matrix.MatrixStack;

import dev.sxmurxy.customhitboxes.Wrapper;

public class Element implements Wrapper {

	protected double x, y, width, height, textCenterY;
	protected String name;

	protected Element(double x, double y, double width, double height, String name) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.name = name;
		this.textCenterY = (height - FONT.lineHeight) / 2;
	}

	protected Element(double width, double height, String name) {
		this(0, 0, width, height, name);
	}
	
	public void render(MatrixStack matrices, double mouseX, double mouseY) {

	}
	
	public void setCoords(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getHeight() {
		return height;
	}
	
}
