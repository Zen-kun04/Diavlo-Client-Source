/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.EnumSkyBlock;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityMob
/*     */   extends EntityCreature
/*     */   implements IMob {
/*     */   public EntityMob(World worldIn) {
/*  19 */     super(worldIn);
/*  20 */     this.experienceValue = 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  25 */     updateArmSwingProgress();
/*  26 */     float f = getBrightness(1.0F);
/*     */     
/*  28 */     if (f > 0.5F)
/*     */     {
/*  30 */       this.entityAge += 2;
/*     */     }
/*     */     
/*  33 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  38 */     super.onUpdate();
/*     */     
/*  40 */     if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
/*     */     {
/*  42 */       setDead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSwimSound() {
/*  48 */     return "game.hostile.swim";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getSplashSound() {
/*  53 */     return "game.hostile.swim.splash";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  58 */     if (isEntityInvulnerable(source))
/*     */     {
/*  60 */       return false;
/*     */     }
/*  62 */     if (super.attackEntityFrom(source, amount)) {
/*     */       
/*  64 */       Entity entity = source.getEntity();
/*  65 */       return (this.riddenByEntity != entity && this.ridingEntity != entity) ? true : true;
/*     */     } 
/*     */ 
/*     */     
/*  69 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  75 */     return "game.hostile.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  80 */     return "game.hostile.die";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getFallSoundString(int damageValue) {
/*  85 */     return (damageValue > 4) ? "game.hostile.hurt.fall.big" : "game.hostile.hurt.fall.small";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/*  90 */     float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
/*  91 */     int i = 0;
/*     */     
/*  93 */     if (entityIn instanceof EntityLivingBase) {
/*     */       
/*  95 */       f += EnchantmentHelper.getModifierForCreature(getHeldItem(), ((EntityLivingBase)entityIn).getCreatureAttribute());
/*  96 */       i += EnchantmentHelper.getKnockbackModifier((EntityLivingBase)this);
/*     */     } 
/*     */     
/*  99 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), f);
/*     */     
/* 101 */     if (flag) {
/*     */       
/* 103 */       if (i > 0) {
/*     */         
/* 105 */         entityIn.addVelocity((-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F), 0.1D, (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F));
/* 106 */         this.motionX *= 0.6D;
/* 107 */         this.motionZ *= 0.6D;
/*     */       } 
/*     */       
/* 110 */       int j = EnchantmentHelper.getFireAspectModifier((EntityLivingBase)this);
/*     */       
/* 112 */       if (j > 0)
/*     */       {
/* 114 */         entityIn.setFire(j * 4);
/*     */       }
/*     */       
/* 117 */       applyEnchantments((EntityLivingBase)this, entityIn);
/*     */     } 
/*     */     
/* 120 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos) {
/* 125 */     return 0.5F - this.worldObj.getLightBrightness(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidLightLevel() {
/* 130 */     BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
/*     */     
/* 132 */     if (this.worldObj.getLightFor(EnumSkyBlock.SKY, blockpos) > this.rand.nextInt(32))
/*     */     {
/* 134 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 138 */     int i = this.worldObj.getLightFromNeighbors(blockpos);
/*     */     
/* 140 */     if (this.worldObj.isThundering()) {
/*     */       
/* 142 */       int j = this.worldObj.getSkylightSubtracted();
/* 143 */       this.worldObj.setSkylightSubtracted(10);
/* 144 */       i = this.worldObj.getLightFromNeighbors(blockpos);
/* 145 */       this.worldObj.setSkylightSubtracted(j);
/*     */     } 
/*     */     
/* 148 */     return (i <= this.rand.nextInt(8));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 154 */     return (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL && isValidLightLevel() && super.getCanSpawnHere());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/* 159 */     super.applyEntityAttributes();
/* 160 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDropLoot() {
/* 165 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityMob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */