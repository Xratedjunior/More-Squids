package xratedjunior.moresquids;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xratedjunior.moresquids.entity.client.MSEntityRenderInit;

@Mod(value = MoreSquids.MOD_ID)
public class MoreSquids 
{	
	public static final String MOD_ID = "moresquids";
	public static final ItemGroup MORESQUIDSITEMGROUP = new ItemGroupMoreSquids();
	
	public static final Logger logger = LogManager.getLogger(MOD_ID);
	
	public MoreSquids() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
	}
	
	//Client Setup
	private void clientSetup(final FMLClientSetupEvent event) {
		//Register Entity Renders
		MSEntityRenderInit.register();
    }
	
	public static ResourceLocation locate(String name) {
		return new ResourceLocation(MOD_ID, name);
	}

	public static String find(String key) {
		return new String(MOD_ID + ":" + key);
	}
}
