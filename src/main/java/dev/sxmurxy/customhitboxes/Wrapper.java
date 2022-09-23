package dev.sxmurxy.customhitboxes;

import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.NewChatGui;
import net.minecraft.client.renderer.texture.TextureManager;

public interface Wrapper {
	
	CustomHitboxes MOD = CustomHitboxes.getInstance();
	Minecraft MC = Minecraft.getInstance();
	FontRenderer FONT = MC.gui.getFont();
	NewChatGui CHAT = MC.gui.getChat();
	MainWindow WINDOW = MC.getWindow();
	TextureManager TEX_MANAGER = MC.getTextureManager();
	
}
