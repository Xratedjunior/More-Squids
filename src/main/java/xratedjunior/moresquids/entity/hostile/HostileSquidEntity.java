package xratedjunior.moresquids.entity.hostile;

import java.util.Random;

import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class HostileSquidEntity extends HostileWaterMobEntity {
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	public float squidRotation;
	public float prevSquidRotation;
	public float tentacleAngle;
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	private float rotationVelocity;
	private float rotateSpeed;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	
	public HostileSquidEntity(EntityType<? extends HostileWaterMobEntity> type, World worldIn) {
		super(type, worldIn);
		this.rand.setSeed((long)this.getEntityId());
		this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
	}
	
	/*********************************************************** Goals ********************************************************/
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new HostileSquidEntity.MoveRandomGoal(this));
	}
	
	/*********************************************************** Attributes ********************************************************/

	public static AttributeModifierMap.MutableAttribute hostileSquidAttributes() {
		return MobEntity.func_233666_p_().createMutableAttribute(Attributes.MAX_HEALTH, 10.0D);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean checkSquidSpawnRules(EntityType<? extends HostileSquidEntity> entityType, IWorld world, SpawnReason reason, BlockPos pos, Random random) {
		return pos.getY() > 45 && pos.getY() < world.getSeaLevel();
	}
	
	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntitySize sizeIn) {
		return sizeIn.height * 0.5F;
	}
	
	/*********************************************************** Tick ********************************************************/

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void livingTick() {
		super.livingTick();
		this.prevSquidPitch = this.squidPitch;
		this.prevSquidYaw = this.squidYaw;
		this.prevSquidRotation = this.squidRotation;
		this.lastTentacleAngle = this.tentacleAngle;
		this.squidRotation += this.rotationVelocity;
		if ((double)this.squidRotation > (Math.PI * 2D)) {
			if (this.world.isRemote) {
				this.squidRotation = ((float)Math.PI * 2F);
			} else {
				this.squidRotation = (float)((double)this.squidRotation - (Math.PI * 2D));
				if (this.rand.nextInt(10) == 0) {
					this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
				}

				this.world.setEntityState(this, (byte)19);
			}
		}

		if (this.isInWaterOrBubbleColumn()) {
			if (this.squidRotation < (float)Math.PI) {
				float f = this.squidRotation / (float)Math.PI;
				this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;
	            if ((double)f > 0.75D) {
	            	this.randomMotionSpeed = 1.0F;
	            	this.rotateSpeed = 1.0F;
	            } else {
	            	this.rotateSpeed *= 0.8F;
	            }
			} else {
				this.tentacleAngle = 0.0F;
	            this.randomMotionSpeed *= 0.9F;
	            this.rotateSpeed *= 0.99F;
			}

			if (!this.world.isRemote) {
				this.setMotion((double)(this.randomMotionVecX * this.randomMotionSpeed), (double)(this.randomMotionVecY * this.randomMotionSpeed), (double)(this.randomMotionVecZ * this.randomMotionSpeed));
			}

			Vector3d vector3d = this.getMotion();
			float f1 = MathHelper.sqrt(horizontalMag(vector3d));
			this.renderYawOffset += (-((float)MathHelper.atan2(vector3d.x, vector3d.z)) * (180F / (float)Math.PI) - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.squidYaw = (float)((double)this.squidYaw + Math.PI * (double)this.rotateSpeed * 1.5D);
			this.squidPitch += (-((float)MathHelper.atan2((double)f1, vector3d.y)) * (180F / (float)Math.PI) - this.squidPitch) * 0.1F;
		} else {
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;
			if (!this.world.isRemote) {
				double d0 = this.getMotion().y;
	            if (this.isPotionActive(Effects.LEVITATION)) {
	            	d0 = 0.05D * (double)(this.getActivePotionEffect(Effects.LEVITATION).getAmplifier() + 1);
	            } else if (!this.hasNoGravity()) {
	            	d0 -= 0.08D;
	            }

	            this.setMotion(0.0D, d0 * (double)0.98F, 0.0D);
			}

			this.squidPitch = (float)((double)this.squidPitch + (double)(-90.0F - this.squidPitch) * 0.02D);
		}
	}
	
	/*********************************************************** Attack ********************************************************/
	   
	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		if (super.attackEntityFrom(source, amount) && this.getRevengeTarget() != null) {
//			this.squirtInk();
			return true;
		} else {
			return false;
		}
	}
	
	/*********************************************************** Client ********************************************************/
	   
	/**
	 * Handler for {@link World#setEntityState}
	 */
	@OnlyIn(Dist.CLIENT)
	public void handleStatusUpdate(byte id) {
		if (id == 19) {
			this.squidRotation = 0.0F;
		} else {
			super.handleStatusUpdate(id);
		}
	}
	
	/*********************************************************** Sounds ********************************************************/

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_SQUID_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_SQUID_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_SQUID_DEATH;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume() {
		return 0.4F;
	}
	
	/*********************************************************** Move ********************************************************/
	   
	@Override
	public void travel(Vector3d travelVector) {
		this.move(MoverType.SELF, this.getMotion());
	}
	
	@Override
	protected boolean canTriggerWalking() {
		return false;
	}
	
	private void setMovementVector(float randomMotionVecXIn, float randomMotionVecYIn, float randomMotionVecZIn) {
		this.randomMotionVecX = randomMotionVecXIn;
		this.randomMotionVecY = randomMotionVecYIn;
		this.randomMotionVecZ = randomMotionVecZIn;
	}
	
	private boolean hasMovementVector() {
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}
	
	class MoveRandomGoal extends Goal {
		private final HostileSquidEntity hostileSquid;

		public MoveRandomGoal(HostileSquidEntity hostileSquid) {
			this.hostileSquid = hostileSquid;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
		 * method as well.
		 */
		public boolean shouldExecute() {
			return true;
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		public void tick() {
			int i = this.hostileSquid.getIdleTime();
			if (i > 100) {
				this.hostileSquid.setMovementVector(0.0F, 0.0F, 0.0F);
			} else if (this.hostileSquid.getRNG().nextInt(50) == 0 || !this.hostileSquid.inWater || !this.hostileSquid.hasMovementVector()) {
				float f = this.hostileSquid.getRNG().nextFloat() * ((float)Math.PI * 2F);
				float f1 = MathHelper.cos(f) * 0.2F;
	            float f2 = -0.1F + this.hostileSquid.getRNG().nextFloat() * 0.2F;
	            float f3 = MathHelper.sin(f) * 0.2F;
	            this.hostileSquid.setMovementVector(f1, f2, f3);
			}
		}
	}
}
