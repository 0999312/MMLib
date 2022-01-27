package cn.mcmod_mmf.mmlib.entity;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;

public abstract class EntityBullet extends AbstractHurtingProjectile {
    protected double damage = 1;
    protected boolean ignoreInvulnerability = false;
    protected int maxTick = 100;
    protected double knockbackStrength = 0;
    protected int ticksSinceFired;

    public EntityBullet(EntityType<? extends EntityBullet> p_i50160_1_, Level p_i50160_2_) {
        super(p_i50160_1_, p_i50160_2_);
    }

    public EntityBullet(EntityType<? extends EntityBullet> p_i50160_1_, Level worldIn, LivingEntity shooter) {
        this(p_i50160_1_, worldIn, shooter, 0, 0, 0);
        setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
    }

    public EntityBullet(EntityType<? extends EntityBullet> p_i50160_1_, Level worldIn, LivingEntity shooter,
            double accelX, double accelY, double accelZ) {
        super(p_i50160_1_, shooter, accelX, accelY, accelZ, worldIn);
    }

    public EntityBullet(EntityType<? extends EntityBullet> p_i50160_1_, Level worldIn, double x, double y, double z,
            double accelX, double accelY, double accelZ) {
        super(p_i50160_1_, x, y, z, accelX, accelY, accelZ, worldIn);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("tsf", ticksSinceFired);
        compound.putDouble("damage", damage);
        if (ignoreInvulnerability)
            compound.putBoolean("ignoreinv", ignoreInvulnerability);
        if (knockbackStrength != 0)
            compound.putDouble("knockback", knockbackStrength);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        ticksSinceFired = compound.getInt("tsf");
        damage = compound.getDouble("damage");
        // The docs says if it's not here it's gonna be false/0 so it should be good
        ignoreInvulnerability = compound.getBoolean("ignoreinv");
        knockbackStrength = compound.getDouble("knockback");
    }

    @Override
    public void tick() {
        // Using a thing I save so that bullets don't get clogged up on chunk borders
        ticksSinceFired++;
        if (ticksSinceFired > getMaxTick() || getDeltaMovement().lengthSqr() < 0.01) {
            remove(RemovalReason.KILLED);
        }
        super.tick();
    }

    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        // Don't disappear on blocks if we're set to noclipping
        if (!level.isClientSide && (!this.noPhysics || result.getType() != HitResult.Type.BLOCK))
            remove(RemovalReason.KILLED);
    }

    @Override
    protected void onHitEntity(EntityHitResult raytrace) {
        super.onHitEntity(raytrace);
        if (!level.isClientSide) {
            Entity target = raytrace.getEntity();
            Entity shooter = this.getOwner();
            int lastHurtResistant = target.invulnerableTime;
            if (ignoreInvulnerability)
                target.invulnerableTime = 0;
            boolean damaged = target.hurt((new IndirectEntityDamageSource("arrow", this, shooter)).setProjectile(),
                    (float) damage);

            if (damaged && target instanceof LivingEntity) {
                LivingEntity livingTarget = (LivingEntity) target;
                if (knockbackStrength > 0) {
                    double actualKnockback = knockbackStrength;

                    Vec3 vec = getDeltaMovement().multiply(1, 0, 1).normalize().scale(actualKnockback);
                    if (vec.lengthSqr() > 0)
                        livingTarget.moveTo(vec.x, 0.1, vec.z);
                }

                if (shooter instanceof LivingEntity)
                    doEnchantDamageEffects((LivingEntity) shooter, target);

                onLivingEntityHit(this, livingTarget, shooter, level);
            } else if (!damaged && ignoreInvulnerability)
                target.invulnerableTime = lastHurtResistant;
        }
    }

    public abstract void onLivingEntityHit(EntityBullet entityBullet, LivingEntity livingTarget, Entity shooter,
            Level level);

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public ParticleOptions getTrailParticle() {
        return ParticleTypes.SMOKE;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDamage() {
        return damage;
    }

    public void setIgnoreInvulnerability(boolean ignoreInvulnerability) {
        this.ignoreInvulnerability = ignoreInvulnerability;
    }

    public boolean isIgnoreInvulnerability() {
        return ignoreInvulnerability;
    }

    public int getMaxTick() {
        return maxTick;
    }

    public void setMaxTick(int maxTick) {
        this.maxTick = maxTick;
    }

    @Override
    public boolean shouldBurn() {
        return false;
    }

    @Override
    protected float getInertia() {
        return 1F;
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    /**
     * Knockback on impact, 0.6 is equivalent to Punch I.
     */
    public void setKnockbackStrength(double knockbackStrength) {
        this.knockbackStrength = knockbackStrength;
    }
}
