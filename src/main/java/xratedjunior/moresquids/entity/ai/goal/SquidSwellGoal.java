package xratedjunior.moresquids.entity.ai.goal;

import java.util.EnumSet;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import xratedjunior.moresquids.entity.hostile.CreeperSquidEntity;

public class SquidSwellGoal extends Goal {
	private final CreeperSquidEntity swellingCreeperSquid;
	private LivingEntity creeperSquidAttackTarget;

	public SquidSwellGoal(CreeperSquidEntity entityCreeperSquidIn) {
		this.swellingCreeperSquid = entityCreeperSquidIn;
		this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
	 * method as well.
	 */
	@Override
	public boolean shouldExecute() {
		LivingEntity livingentity = this.swellingCreeperSquid.getAttackTarget();
		return this.swellingCreeperSquid.getCreeperState() > 0 || livingentity != null && this.swellingCreeperSquid.getDistanceSq(livingentity) < 9.0D;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting() {
		this.swellingCreeperSquid.getNavigator().clearPath();
		this.creeperSquidAttackTarget = this.swellingCreeperSquid.getAttackTarget();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by another one
	 */
	@Override
	public void resetTask() {
		this.creeperSquidAttackTarget = null;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		if (this.creeperSquidAttackTarget == null) {
			this.swellingCreeperSquid.setCreeperState(-1);
		} else if (this.swellingCreeperSquid.getDistanceSq(this.creeperSquidAttackTarget) > 49.0D) {
			this.swellingCreeperSquid.setCreeperState(-1);
		} else if (!this.swellingCreeperSquid.getEntitySenses().canSee(this.creeperSquidAttackTarget)) {
			this.swellingCreeperSquid.setCreeperState(-1);
		} else {
			this.swellingCreeperSquid.setCreeperState(1);
		}
	}
}