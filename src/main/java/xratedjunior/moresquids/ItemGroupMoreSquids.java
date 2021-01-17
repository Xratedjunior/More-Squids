package xratedjunior.moresquids;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class ItemGroupMoreSquids extends ItemGroup 
{
	public ItemGroupMoreSquids() {
		super("moresquids");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(Items.SQUID_SPAWN_EGG);
	}
}
