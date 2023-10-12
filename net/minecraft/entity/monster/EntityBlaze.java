/*     */ package net.minecraft.entity.monster;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntitySmallFireball;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityBlaze extends EntityMob {
/*  24 */   private float heightOffset = 0.5F;
/*     */   
/*     */   private int heightOffsetUpdateTime;
/*     */   
/*     */   public EntityBlaze(World worldIn) {
/*  29 */     super(worldIn);
/*  30 */     this.isImmuneToFire = true;
/*  31 */     this.experienceValue = 10;
/*  32 */     this.tasks.addTask(4, new AIFireballAttack(this));
/*  33 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  34 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  35 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  36 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  37 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[0]));
/*  38 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  43 */     super.applyEntityAttributes();
/*  44 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
/*  45 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  46 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  51 */     super.entityInit();
/*  52 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  57 */     return "mob.blaze.breathe";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  62 */     return "mob.blaze.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  67 */     return "mob.blaze.death";
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/*  72 */     return 15728880;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/*  77 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  82 */     if (!this.onGround && this.motionY < 0.0D)
/*     */     {
/*  84 */       this.motionY *= 0.6D;
/*     */     }
/*     */     
/*  87 */     if (this.worldObj.isRemote) {
/*     */       
/*  89 */       if (this.rand.nextInt(24) == 0 && !isSilent())
/*     */       {
/*  91 */         this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */       
/*  94 */       for (int i = 0; i < 2; i++)
/*     */       {
/*  96 */         this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       }
/*     */     } 
/*     */     
/* 100 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 105 */     if (isWet())
/*     */     {
/* 107 */       attackEntityFrom(DamageSource.drown, 1.0F);
/*     */     }
/*     */     
/* 110 */     this.heightOffsetUpdateTime--;
/*     */     
/* 112 */     if (this.heightOffsetUpdateTime <= 0) {
/*     */       
/* 114 */       this.heightOffsetUpdateTime = 100;
/* 115 */       this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
/*     */     } 
/*     */     
/* 118 */     EntityLivingBase entitylivingbase = getAttackTarget();
/*     */     
/* 120 */     if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + getEyeHeight() + this.heightOffset) {
/*     */       
/* 122 */       this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
/* 123 */       this.isAirBorne = true;
/*     */     } 
/*     */     
/* 126 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 135 */     return Items.blaze_rod;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBurning() {
/* 140 */     return func_70845_n();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 145 */     if (wasRecentlyHit) {
/*     */       
/* 147 */       int i = this.rand.nextInt(2 + lootingModifier);
/*     */       
/* 149 */       for (int j = 0; j < i; j++)
/*     */       {
/* 151 */         dropItem(Items.blaze_rod, 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_70845_n() {
/* 158 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOnFire(boolean onFire) {
/* 163 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 165 */     if (onFire) {
/*     */       
/* 167 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else {
/*     */       
/* 171 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     } 
/*     */     
/* 174 */     this.dataWatcher.updateObject(16, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidLightLevel() {
/* 179 */     return true;
/*     */   }
/*     */   
/*     */   static class AIFireballAttack
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityBlaze blaze;
/*     */     private int field_179467_b;
/*     */     private int field_179468_c;
/*     */     
/*     */     public AIFireballAttack(EntityBlaze p_i45846_1_) {
/* 190 */       this.blaze = p_i45846_1_;
/* 191 */       setMutexBits(3);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 196 */       EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
/* 197 */       return (entitylivingbase != null && entitylivingbase.isEntityAlive());
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 202 */       this.field_179467_b = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetTask() {
/* 207 */       this.blaze.setOnFire(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 212 */       this.field_179468_c--;
/* 213 */       EntityLivingBase entitylivingbase = this.blaze.getAttackTarget();
/* 214 */       double d0 = this.blaze.getDistanceSqToEntity((Entity)entitylivingbase);
/*     */       
/* 216 */       if (d0 < 4.0D) {
/*     */         
/* 218 */         if (this.field_179468_c <= 0) {
/*     */           
/* 220 */           this.field_179468_c = 20;
/* 221 */           this.blaze.attackEntityAsMob((Entity)entitylivingbase);
/*     */         } 
/*     */         
/* 224 */         this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
/*     */       }
/* 226 */       else if (d0 < 256.0D) {
/*     */         
/* 228 */         double d1 = entitylivingbase.posX - this.blaze.posX;
/* 229 */         double d2 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - this.blaze.posY + (this.blaze.height / 2.0F);
/* 230 */         double d3 = entitylivingbase.posZ - this.blaze.posZ;
/*     */         
/* 232 */         if (this.field_179468_c <= 0) {
/*     */           
/* 234 */           this.field_179467_b++;
/*     */           
/* 236 */           if (this.field_179467_b == 1) {
/*     */             
/* 238 */             this.field_179468_c = 60;
/* 239 */             this.blaze.setOnFire(true);
/*     */           }
/* 241 */           else if (this.field_179467_b <= 4) {
/*     */             
/* 243 */             this.field_179468_c = 6;
/*     */           }
/*     */           else {
/*     */             
/* 247 */             this.field_179468_c = 100;
/* 248 */             this.field_179467_b = 0;
/* 249 */             this.blaze.setOnFire(false);
/*     */           } 
/*     */           
/* 252 */           if (this.field_179467_b > 1) {
/*     */             
/* 254 */             float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
/* 255 */             this.blaze.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.blaze.posX, (int)this.blaze.posY, (int)this.blaze.posZ), 0);
/*     */             
/* 257 */             for (int i = 0; i < 1; i++) {
/*     */               
/* 259 */               EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.blaze.worldObj, (EntityLivingBase)this.blaze, d1 + this.blaze.getRNG().nextGaussian() * f, d2, d3 + this.blaze.getRNG().nextGaussian() * f);
/* 260 */               entitysmallfireball.posY = this.blaze.posY + (this.blaze.height / 2.0F) + 0.5D;
/* 261 */               this.blaze.worldObj.spawnEntityInWorld((Entity)entitysmallfireball);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */         
/* 266 */         this.blaze.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 10.0F, 10.0F);
/*     */       }
/*     */       else {
/*     */         
/* 270 */         this.blaze.getNavigator().clearPathEntity();
/* 271 */         this.blaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
/*     */       } 
/*     */       
/* 274 */       super.updateTask();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityBlaze.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */