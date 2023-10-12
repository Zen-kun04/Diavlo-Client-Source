/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.boss.EntityWither;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWitherSkull
/*     */   extends EntityFireball
/*     */ {
/*     */   public EntityWitherSkull(World worldIn) {
/*  20 */     super(worldIn);
/*  21 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityWitherSkull(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
/*  26 */     super(worldIn, shooter, accelX, accelY, accelZ);
/*  27 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getMotionFactor() {
/*  32 */     return isInvulnerable() ? 0.73F : super.getMotionFactor();
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityWitherSkull(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
/*  37 */     super(worldIn, x, y, z, accelX, accelY, accelZ);
/*  38 */     setSize(0.3125F, 0.3125F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBurning() {
/*  43 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getExplosionResistance(Explosion explosionIn, World worldIn, BlockPos pos, IBlockState blockStateIn) {
/*  48 */     float f = super.getExplosionResistance(explosionIn, worldIn, pos, blockStateIn);
/*  49 */     Block block = blockStateIn.getBlock();
/*     */     
/*  51 */     if (isInvulnerable() && EntityWither.canDestroyBlock(block))
/*     */     {
/*  53 */       f = Math.min(0.8F, f);
/*     */     }
/*     */     
/*  56 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onImpact(MovingObjectPosition movingObject) {
/*  61 */     if (!this.worldObj.isRemote) {
/*     */       
/*  63 */       if (movingObject.entityHit != null) {
/*     */         
/*  65 */         if (this.shootingEntity != null) {
/*     */           
/*  67 */           if (movingObject.entityHit.attackEntityFrom(DamageSource.causeMobDamage(this.shootingEntity), 8.0F))
/*     */           {
/*  69 */             if (!movingObject.entityHit.isEntityAlive())
/*     */             {
/*  71 */               this.shootingEntity.heal(5.0F);
/*     */             }
/*     */             else
/*     */             {
/*  75 */               applyEnchantments(this.shootingEntity, movingObject.entityHit);
/*     */             }
/*     */           
/*     */           }
/*     */         } else {
/*     */           
/*  81 */           movingObject.entityHit.attackEntityFrom(DamageSource.magic, 5.0F);
/*     */         } 
/*     */         
/*  84 */         if (movingObject.entityHit instanceof EntityLivingBase) {
/*     */           
/*  86 */           int i = 0;
/*     */           
/*  88 */           if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL) {
/*     */             
/*  90 */             i = 10;
/*     */           }
/*  92 */           else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
/*     */             
/*  94 */             i = 40;
/*     */           } 
/*     */           
/*  97 */           if (i > 0)
/*     */           {
/*  99 */             ((EntityLivingBase)movingObject.entityHit).addPotionEffect(new PotionEffect(Potion.wither.id, 20 * i, 1));
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 104 */       this.worldObj.newExplosion(this, this.posX, this.posY, this.posZ, 1.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
/* 105 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 111 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 121 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInvulnerable() {
/* 126 */     return (this.dataWatcher.getWatchableObjectByte(10) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvulnerable(boolean invulnerable) {
/* 131 */     this.dataWatcher.updateObject(10, Byte.valueOf((byte)(invulnerable ? 1 : 0)));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityWitherSkull.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */