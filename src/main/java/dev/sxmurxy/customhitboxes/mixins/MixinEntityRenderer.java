package dev.sxmurxy.customhitboxes.mixins;

import java.awt.Color;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import dev.sxmurxy.customhitboxes.Wrapper;
import dev.sxmurxy.customhitboxes.utils.Utils;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.entity.PartEntity;

@Mixin(EntityRendererManager.class)
public class MixinEntityRenderer implements Wrapper {

	@Inject(method = "renderHitbox", at = @At(value = "HEAD"), cancellable = true)
	private void renderCustomHitbox(MatrixStack matrices, IVertexBuilder bufferBuilder, Entity entity, float partialTicks, CallbackInfo ci) {
		if(!MOD.settings.enabled.getValue())
			return;
		float f = entity.getBbWidth() / 2.0F;
		this.renderBox(matrices, bufferBuilder, entity, Utils.getEntityColor(entity));
		if(entity.isMultipartEntity()) {
			double d0 = -MathHelper.lerp(partialTicks, entity.xOld, entity.getX());
			double d1 = -MathHelper.lerp(partialTicks, entity.yOld, entity.getY());
			double d2 = -MathHelper.lerp(partialTicks, entity.zOld, entity.getZ());

			for(PartEntity<?> enderdragonpartentity : entity.getParts()) {
				matrices.pushPose();
				double d3 = d0 + MathHelper.lerp(partialTicks, enderdragonpartentity.xOld, enderdragonpartentity.getX());
				double d4 = d1 + MathHelper.lerp(partialTicks, enderdragonpartentity.yOld, enderdragonpartentity.getY());
				double d5 = d2 + MathHelper.lerp(partialTicks, enderdragonpartentity.zOld, enderdragonpartentity.getZ());
				matrices.translate(d3, d4, d5);
				this.renderBox(matrices, bufferBuilder, enderdragonpartentity, new Color(0.25F, 1.0F, 0.0F));
				matrices.popPose();
			}
		}

		if(entity instanceof LivingEntity && MOD.settings.eyeHitbox.getValue()) {
			float[] eyeComps = Utils.getFloatComponents(MOD.settings.eyeColor.getValue());
			WorldRenderer.renderLineBox(matrices, bufferBuilder, (-f), entity.getEyeHeight() - 0.01F, (-f), f,
					entity.getEyeHeight() + 0.01F, f, eyeComps[0], eyeComps[1], eyeComps[2], eyeComps[3]);
		}
		
		if(MOD.settings.viewingLine.getValue()) {
			Color viewColor = MOD.settings.viewingLineColor.getValue();
			Vector3d vector3d = entity.getViewVector(partialTicks);
			Matrix4f matrix4f = matrices.last().pose();
			bufferBuilder.vertex(matrix4f, 0.0F, entity.getEyeHeight(), 0.0F)
				.color(viewColor.getRed(), viewColor.getGreen(), viewColor.getBlue(), viewColor.getAlpha()).endVertex();
			bufferBuilder.vertex(matrix4f, (float) (vector3d.x * 2.0D), (float) (entity.getEyeHeight() + vector3d.y * 2.0D), (float) (vector3d.z * 2.0D))
				.color(viewColor.getRed(), viewColor.getGreen(), viewColor.getBlue(), viewColor.getAlpha()).endVertex();
		}
		ci.cancel();
	}

	private void renderBox(MatrixStack matrices, IVertexBuilder bufferBuilder, Entity entity, Color color) {
		float[] bodyComps = Utils.getFloatComponents(color);
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().move(-entity.getX(), -entity.getY(), -entity.getZ());
		WorldRenderer.renderLineBox(matrices, bufferBuilder, axisalignedbb, bodyComps[0], bodyComps[1], bodyComps[2], bodyComps[3]);
	}

}
