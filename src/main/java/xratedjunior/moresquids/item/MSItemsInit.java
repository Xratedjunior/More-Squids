package xratedjunior.moresquids.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import xratedjunior.moresquids.MoreSquids;
import xratedjunior.moresquids.entity.MSEntityinit;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSItemsInit {

	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
		MSEntityinit.registerSpawnEggs(event);
    	MoreSquids.logger.info("Spawn Egss registered");
	}
}
