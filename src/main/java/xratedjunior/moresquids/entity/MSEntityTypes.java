package xratedjunior.moresquids.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.event.RegistryEvent.Register;
import xratedjunior.moresquids.MoreSquids;
import xratedjunior.moresquids.entity.hostile.CreeperSquidEntity;

public class MSEntityTypes {
	
	/*********************************************************** Neutral ********************************************************/

	public static final EntityType<CreeperSquidEntity> CREEPER_SQUID = buildEntity("creeper_squid", EntityType.Builder.<CreeperSquidEntity>create(CreeperSquidEntity::new, EntityClassification.MONSTER).size(0.8F, 0.8F));
	
	private static <T extends Entity> EntityType<T> buildEntity(String key, EntityType.Builder<T> builder) {
		return builder.build(MoreSquids.find(key));
	}
	
	public static void registerEntityTypes(Register<EntityType<?>> event) {
		registerEntity(event, "creeper_squid", CREEPER_SQUID, CreeperSquidEntity.hostileSquidAttributes());
	}
	
	private static void registerEntity(Register<EntityType<?>> event, String entityName, EntityType<? extends LivingEntity> entityType, AttributeModifierMap.MutableAttribute attributes) {
		MSEntityinit.register(event.getRegistry(), entityName, entityType);
		GlobalEntityTypeAttributes.put(entityType, attributes.create());
	}
}
