package xratedjunior.moresquids.entity;

import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import xratedjunior.moresquids.MoreSquids;
import xratedjunior.moresquids.entity.hostile.HostileSquidEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MSEntityinit
{
	private static final ItemGroup MORESQUIDS = MoreSquids.MORESQUIDSITEMGROUP;
	
	public static void registerSpawnEggs(Register<Item> event) {
		createSpawnEgg(event, MSEntityTypes.CREEPER_SQUID, 0x286926, 0x1a1a1a);
	}
	
	private static void createSpawnEgg(Register<Item> event, EntityType<?> entityType, int baseColor, int dotColor) {
		register(event.getRegistry(), "creeper_squid_spawn_egg", new SpawnEggItem(entityType, baseColor, dotColor, new Item.Properties().group(MORESQUIDS)));
	}
	
	@SubscribeEvent
    public static void onEntityTypeRegistry(Register<EntityType<?>> event) {
		MSEntityTypes.registerEntityTypes(event);
		MoreSquids.logger.info("EntityTypes registered");
		
		/*********************************************************** Hostile ********************************************************/
		
		EntitySpawnPlacementRegistry.register(MSEntityTypes.CREEPER_SQUID, EntitySpawnPlacementRegistry.PlacementType.IN_WATER, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, HostileSquidEntity::checkSquidSpawnRules);
		
		MoreSquids.logger.info("EntitySpawnPlacements registered");
    }

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, String name, T object) {
		object.setRegistryName(MoreSquids.locate(name));
		registry.register(object);
	}
}
