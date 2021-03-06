package website.skylorbeck.minecraft.iconicwands.entity;

import com.google.common.collect.Sets;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import website.skylorbeck.minecraft.iconicwands.Declarar;

import java.util.Optional;
import java.util.Set;

public class MagicProjectileEntity extends PersistentProjectileEntity {
    private static final TrackedData<Integer> COLOR = DataTracker.registerData(MagicProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private Potion potion = Potions.EMPTY;
    private final Set<StatusEffectInstance> effects = Sets.newHashSet();
    private boolean colorSet;
    private BlockPos startingPos = BlockPos.ORIGIN;
    private int maxDist = 5;
    private boolean doesLight = false;
    private boolean doesBurn = false;
    private boolean doesWarp = false;
    private boolean doesExplode = false;
    private boolean doesLightning = false;
    public MagicProjectileEntity(EntityType<? extends MagicProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public MagicProjectileEntity(World world, double x, double y, double z) {
        super(Declarar.MAGIC_PROJECTILE, x, y, z, world);
    }

    public MagicProjectileEntity(World world, LivingEntity owner) {
        super(Declarar.MAGIC_PROJECTILE, owner, world);
        this.startingPos = this.getBlockPos();
    }

    public int getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    @Override
    protected SoundEvent getHitSound() {
        return SoundEvents.BLOCK_AMETHYST_BLOCK_HIT;
    }

    private void initColor() {
        this.colorSet = false;
        if (this.potion == Potions.EMPTY && this.effects.isEmpty()) {
            this.dataTracker.set(COLOR, -1);
        } else {
            this.dataTracker.set(COLOR, PotionUtil.getColor(PotionUtil.getPotionEffects(this.potion, this.effects)));
        }
    }

    public void addEffect(StatusEffectInstance effect) {
        this.effects.add(effect);
        this.getDataTracker().set(COLOR, PotionUtil.getColor(PotionUtil.getPotionEffects(this.potion, this.effects)));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(COLOR, -1);
    }

    @Override
    public void tick() {
        super.tick();
         spawnParticles(world.random.nextInt(4)+1);
        if (!world.isClient && !this.startingPos.isWithinDistance(this.getPos(),maxDist) || this.inGround || this.getVelocity().length()<0.1f){
            this.discard();
        }
    }

    private void spawnParticles(int amount) {
        if (amount <= 0) {
            return;
        }
        double x = -0.25f + (world.random.nextFloat()*0.5f);
        double y = world.random.nextFloat()*0.25f;
        double z = -0.25f +(world.random.nextFloat()*0.5f);
        for (int j = 0; j < amount; ++j) {
            this.world.addParticle(ParticleTypes.ENCHANT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), x, y, z);
        }
    }

    public int getColor() {
        return this.dataTracker.get(COLOR);
    }

    public void setColor(int color) {
        this.colorSet = true;
        this.dataTracker.set(COLOR, color);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        if (this.potion != Potions.EMPTY) {
            nbt.putString("Potion", Registry.POTION.getId(this.potion).toString());
        }
        if (this.colorSet) {
            nbt.putInt("Color", this.getColor());
        }
        if (!this.effects.isEmpty()) {
            NbtList nbtList = new NbtList();
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                nbtList.add(statusEffectInstance.writeNbt(new NbtCompound()));
            }
            nbt.put("CustomPotionEffects", nbtList);
        }
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("Potion", 8)) {
            this.potion = PotionUtil.getPotion(nbt);
        }
        for (StatusEffectInstance statusEffectInstance : PotionUtil.getCustomPotionEffects(nbt)) {
            this.addEffect(statusEffectInstance);
        }
        if (nbt.contains("Color", 99)) {
            this.setColor(nbt.getInt("Color"));
        } else {
            this.initColor();
        }
    }

    @Override
    protected void onHit(LivingEntity target) {
        super.onHit(target);
        Entity entity = this.getEffectCause();
        for (StatusEffectInstance statusEffectInstance : this.potion.getEffects()) {
            target.addStatusEffect(new StatusEffectInstance(statusEffectInstance.getEffectType(), Math.max(statusEffectInstance.getDuration() / 8, 1), statusEffectInstance.getAmplifier(), statusEffectInstance.isAmbient(), statusEffectInstance.shouldShowParticles()), entity);
        }
        if (!this.effects.isEmpty()) {
            for (StatusEffectInstance statusEffectInstance : this.effects) {
                target.addStatusEffect(statusEffectInstance, entity);
            }
        }
        if (this.doesExplode) {
            world.createExplosion(target, target.getX(), target.getY()+1, target.getZ(), 1.0f, Explosion.DestructionType.NONE);
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        BlockPos blockPos = blockHitResult.getBlockPos();
        if (this.doesLight){
            Optional<BlockPos> optional = BlockPos.findClosest(blockHitResult.getBlockPos(),2,2,(block -> world.getBlockState(block).isAir()));
            optional.ifPresent(block -> world.setBlockState(block, Declarar.TIMED_LIGHT.getDefaultState()));
        } else if (this.doesBurn && AbstractFireBlock.canPlaceAt(world, blockPos = blockPos.offset(blockHitResult.getSide()), blockHitResult.getSide().getOpposite())){
            world.setBlockState(blockPos, AbstractFireBlock.getState(world,blockPos));
        } else if (this.doesWarp){
            if(this.getOwner()!=null)
            this.getOwner().teleport(blockPos.getX()+0.5,blockPos.getY()+1.5,blockPos.getZ()+0.5);
        } else if (this.doesExplode) {
            world.createExplosion(this, blockPos.getX()+0.5, blockPos.getY()+1, blockPos.getZ()+0.5, 1.0f, Explosion.DestructionType.NONE);
        } else if (this.doesLightning) {
            LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(this.world);
            lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(blockPos));
            if (!world.isClient)
            lightningEntity.setChanneler((ServerPlayerEntity) this.getOwner());
            this.world.spawnEntity(lightningEntity);
        }
        super.onBlockHit(blockHitResult);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == 0) {
            int i = this.getColor();
            if (i != -1) {
                double d = (double)(i >> 16 & 0xFF) / 255.0;
                double e = (double)(i >> 8 & 0xFF) / 255.0;
                double f = (double)(i >> 0 & 0xFF) / 255.0;
                for (int j = 0; j < 20; ++j) {
                    this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getParticleX(0.5), this.getRandomBodyY(), this.getParticleZ(0.5), d, e, f);
                }
            }
        } else {
            super.handleStatus(status);
        }
    }

    public boolean isDoesWarp() {
        return doesWarp;
    }

    public void setDoesWarp(boolean doesWarp) {
        this.doesWarp = doesWarp;
    }

    public boolean isDoesBurn() {
        return doesBurn;
    }

    public void setDoesBurn(boolean doesBurn) {
        this.doesBurn = doesBurn;
    }

    public boolean isDoesLight() {
        return doesLight;
    }

    public void setDoesLight(boolean doesLight) {
        this.doesLight = doesLight;
    }

    public boolean isDoesExplode() {
        return doesExplode;
    }

    public void setDoesExplode(boolean doesExplode) {
        this.doesExplode = doesExplode;
    }

    public boolean isDoesLightning() {
        return doesLightning;
    }

    public void setDoesLightning(boolean doesLightning) {
        this.doesLightning = doesLightning;
    }
}