package xratedjunior.moresquids.entity.client.renderer.hostile;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.SquidModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import xratedjunior.moresquids.MoreSquids;
import xratedjunior.moresquids.entity.hostile.CreeperSquidEntity;

public class CreeperSquidRenderer extends MobRenderer<CreeperSquidEntity, SquidModel<CreeperSquidEntity>> {
	private static final ResourceLocation CREEPER_SQUID_TEXTURES = MoreSquids.locate("textures/entity/hostile/creeper_squid.png");

	public CreeperSquidRenderer(EntityRendererManager renderManagerIn) {
		super(renderManagerIn, new SquidModel<>(), 0.7F);
	}
	
	@Override
	protected void applyRotations(CreeperSquidEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
		float f = MathHelper.lerp(partialTicks, entityLiving.prevSquidPitch, entityLiving.squidPitch);
		float f1 = MathHelper.lerp(partialTicks, entityLiving.prevSquidYaw, entityLiving.squidYaw);
		matrixStackIn.translate(0.0D, 0.5D, 0.0D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F - rotationYaw));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(f));
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(f1));
		matrixStackIn.translate(0.0D, (double)-1.2F, 0.0D);
	}

	/**
	 * Defines what float the third param in setRotationAngles of ModelBase is
	 */
	@Override
	protected float handleRotationFloat(CreeperSquidEntity livingBase, float partialTicks) {
		return MathHelper.lerp(partialTicks, livingBase.lastTentacleAngle, livingBase.tentacleAngle);
	}
	
	@Override
	protected void preRenderCallback(CreeperSquidEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
		float f = entitylivingbaseIn.getCreeperSquidFlashIntensity(partialTickTime);
		float f1 = 1.0F + MathHelper.sin(f * 100.0F) * f * 0.01F;
		f = MathHelper.clamp(f, 0.0F, 1.0F);
		f = f * f;
		f = f * f;
		float f2 = (1.0F + f * 0.4F) * f1;
		float f3 = (1.0F + f * 0.1F) / f1;
		matrixStackIn.scale(f2, f3, f2);
		super.preRenderCallback(entitylivingbaseIn, matrixStackIn, partialTickTime);
	}

	@Override
	protected float getOverlayProgress(CreeperSquidEntity livingEntityIn, float partialTicks) {
		float f = livingEntityIn.getCreeperSquidFlashIntensity(partialTicks);
		return (int)(f * 10.0F) % 2 == 0 ? 0.0F : MathHelper.clamp(f, 0.5F, 1.0F);
	}
	
	@Override
	public ResourceLocation getEntityTexture(CreeperSquidEntity entity) {
		return CREEPER_SQUID_TEXTURES;
	}
}
