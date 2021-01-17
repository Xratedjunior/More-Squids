package xratedjunior.moresquids.entity.client;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import xratedjunior.moresquids.MoreSquids;
import xratedjunior.moresquids.entity.MSEntityTypes;
import xratedjunior.moresquids.entity.client.renderer.hostile.CreeperSquidRenderer;

public class MSEntityRenderInit {
	
	public static void register() {
		MoreSquids.logger.info("Registering Entity Renders");
		
		register(MSEntityTypes.CREEPER_SQUID, CreeperSquidRenderer::new);
		
		MoreSquids.logger.info("Registered Entity Renders");
	}
	
	private static <T extends Entity> void register(EntityType<T> entityType, IRenderFactory<? super T> renderFactory) {
		RenderingRegistry.registerEntityRenderingHandler(entityType, renderFactory);
	}
}
