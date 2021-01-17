package xratedjunior.moresquids.entity.hostile;

import net.minecraft.entity.CreatureAttribute;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public abstract class HostileWaterMobEntity extends MonsterEntity {
	
	public HostileWaterMobEntity(EntityType<? extends MonsterEntity> type, World worldIn) {
		super(type, worldIn);
		this.setPathPriority(PathNodeType.WATER, 0.0F);
	}
	
	/*********************************************************** Attributes ********************************************************/
	
	@Override
	public CreatureAttribute getCreatureAttribute() {
		return CreatureAttribute.WATER;
	}
	
	@Override
	public boolean canBreatheUnderwater() {
		return true;
	}
	
	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
	public boolean canBeLeashedTo(PlayerEntity player) {
		return false;
	}
	
	/**
	 * Get the experience points the entity currently has.
	 */
	@Override
	protected int getExperiencePoints(PlayerEntity player) {
		return 1 + this.world.rand.nextInt(3);
	}
	
	@Override
	public boolean isNotColliding(IWorldReader worldIn) {
		return worldIn.checkNoEntityCollision(this);
	}
	
	/*********************************************************** Tick ********************************************************/
	
	/**
	 * Gets called every tick from main Entity class
	 */
	@Override
	public void baseTick() {
		int i = this.getAir();
		super.baseTick();
		this.updateAir(i);
	}
	
	protected void updateAir(int p_209207_1_) {
		if (this.isAlive() && !this.isInWaterOrBubbleColumn()) {
			this.setAir(p_209207_1_ - 1);
			if (this.getAir() == -20) {
				this.setAir(0);
	            this.attackEntityFrom(DamageSource.DROWN, 2.0F);
			}
		} else {
			this.setAir(300);
		}
	}
	
	/*********************************************************** Sounds ********************************************************/
	
	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	@Override
	public int getTalkInterval() {
		return 120;
	}
}
