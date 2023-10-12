/*     */ package net.minecraft.entity.monster;
/*     */ import java.util.Random;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityLargeFireball;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityGhast extends EntityFlying implements IMob {
/*  26 */   private int explosionStrength = 1;
/*     */ 
/*     */   
/*     */   public EntityGhast(World worldIn) {
/*  30 */     super(worldIn);
/*  31 */     setSize(4.0F, 4.0F);
/*  32 */     this.isImmuneToFire = true;
/*  33 */     this.experienceValue = 5;
/*  34 */     this.moveHelper = new GhastMoveHelper(this);
/*  35 */     this.tasks.addTask(5, new AIRandomFly(this));
/*  36 */     this.tasks.addTask(7, new AILookAround(this));
/*  37 */     this.tasks.addTask(7, new AIFireballAttack(this));
/*  38 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer((EntityLiving)this));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAttacking() {
/*  43 */     return (this.dataWatcher.getWatchableObjectByte(16) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttacking(boolean attacking) {
/*  48 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)(attacking ? 1 : 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFireballStrength() {
/*  53 */     return this.explosionStrength;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  58 */     super.onUpdate();
/*     */     
/*  60 */     if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
/*     */     {
/*  62 */       setDead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  68 */     if (isEntityInvulnerable(source))
/*     */     {
/*  70 */       return false;
/*     */     }
/*  72 */     if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer) {
/*     */       
/*  74 */       super.attackEntityFrom(source, 1000.0F);
/*  75 */       ((EntityPlayer)source.getEntity()).triggerAchievement((StatBase)AchievementList.ghast);
/*  76 */       return true;
/*     */     } 
/*     */ 
/*     */     
/*  80 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  86 */     super.entityInit();
/*  87 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  92 */     super.applyEntityAttributes();
/*  93 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/*  94 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  99 */     return "mob.ghast.moan";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 104 */     return "mob.ghast.scream";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 109 */     return "mob.ghast.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 114 */     return Items.gunpowder;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 119 */     int i = this.rand.nextInt(2) + this.rand.nextInt(1 + lootingModifier);
/*     */     
/* 121 */     for (int j = 0; j < i; j++)
/*     */     {
/* 123 */       dropItem(Items.ghast_tear, 1);
/*     */     }
/*     */     
/* 126 */     i = this.rand.nextInt(3) + this.rand.nextInt(1 + lootingModifier);
/*     */     
/* 128 */     for (int k = 0; k < i; k++)
/*     */     {
/* 130 */       dropItem(Items.gunpowder, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 136 */     return 10.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 141 */     return (this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnedInChunk() {
/* 146 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 151 */     super.writeEntityToNBT(tagCompound);
/* 152 */     tagCompound.setInteger("ExplosionPower", this.explosionStrength);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 157 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 159 */     if (tagCompund.hasKey("ExplosionPower", 99))
/*     */     {
/* 161 */       this.explosionStrength = tagCompund.getInteger("ExplosionPower");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 167 */     return 2.6F;
/*     */   }
/*     */   
/*     */   static class AIFireballAttack
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     public int attackTimer;
/*     */     
/*     */     public AIFireballAttack(EntityGhast ghast) {
/* 177 */       this.parentEntity = ghast;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 182 */       return (this.parentEntity.getAttackTarget() != null);
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 187 */       this.attackTimer = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetTask() {
/* 192 */       this.parentEntity.setAttacking(false);
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 197 */       EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
/* 198 */       double d0 = 64.0D;
/*     */       
/* 200 */       if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen((Entity)entitylivingbase)) {
/*     */         
/* 202 */         World world = this.parentEntity.worldObj;
/* 203 */         this.attackTimer++;
/*     */         
/* 205 */         if (this.attackTimer == 10)
/*     */         {
/* 207 */           world.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos((Entity)this.parentEntity), 0);
/*     */         }
/*     */         
/* 210 */         if (this.attackTimer == 20)
/*     */         {
/* 212 */           double d1 = 4.0D;
/* 213 */           Vec3 vec3 = this.parentEntity.getLook(1.0F);
/* 214 */           double d2 = entitylivingbase.posX - this.parentEntity.posX + vec3.xCoord * d1;
/* 215 */           double d3 = (entitylivingbase.getEntityBoundingBox()).minY + (entitylivingbase.height / 2.0F) - 0.5D + this.parentEntity.posY + (this.parentEntity.height / 2.0F);
/* 216 */           double d4 = entitylivingbase.posZ - this.parentEntity.posZ + vec3.zCoord * d1;
/* 217 */           world.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos((Entity)this.parentEntity), 0);
/* 218 */           EntityLargeFireball entitylargefireball = new EntityLargeFireball(world, (EntityLivingBase)this.parentEntity, d2, d3, d4);
/* 219 */           entitylargefireball.explosionPower = this.parentEntity.getFireballStrength();
/* 220 */           entitylargefireball.posX = this.parentEntity.posX + vec3.xCoord * d1;
/* 221 */           entitylargefireball.posY = this.parentEntity.posY + (this.parentEntity.height / 2.0F) + 0.5D;
/* 222 */           entitylargefireball.posZ = this.parentEntity.posZ + vec3.zCoord * d1;
/* 223 */           world.spawnEntityInWorld((Entity)entitylargefireball);
/* 224 */           this.attackTimer = -40;
/*     */         }
/*     */       
/* 227 */       } else if (this.attackTimer > 0) {
/*     */         
/* 229 */         this.attackTimer--;
/*     */       } 
/*     */       
/* 232 */       this.parentEntity.setAttacking((this.attackTimer > 10));
/*     */     }
/*     */   }
/*     */   
/*     */   static class AILookAround
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     
/*     */     public AILookAround(EntityGhast ghast) {
/* 242 */       this.parentEntity = ghast;
/* 243 */       setMutexBits(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 248 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 253 */       if (this.parentEntity.getAttackTarget() == null) {
/*     */         
/* 255 */         this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * 180.0F / 3.1415927F;
/*     */       }
/*     */       else {
/*     */         
/* 259 */         EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
/* 260 */         double d0 = 64.0D;
/*     */         
/* 262 */         if (entitylivingbase.getDistanceSqToEntity((Entity)this.parentEntity) < d0 * d0) {
/*     */           
/* 264 */           double d1 = entitylivingbase.posX - this.parentEntity.posX;
/* 265 */           double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
/* 266 */           this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * 180.0F / 3.1415927F;
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIRandomFly
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     
/*     */     public AIRandomFly(EntityGhast ghast) {
/* 278 */       this.parentEntity = ghast;
/* 279 */       setMutexBits(1);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 284 */       EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();
/*     */       
/* 286 */       if (!entitymovehelper.isUpdating())
/*     */       {
/* 288 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 292 */       double d0 = entitymovehelper.getX() - this.parentEntity.posX;
/* 293 */       double d1 = entitymovehelper.getY() - this.parentEntity.posY;
/* 294 */       double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
/* 295 */       double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 296 */       return (d3 < 1.0D || d3 > 3600.0D);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 302 */       return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 307 */       Random random = this.parentEntity.getRNG();
/* 308 */       double d0 = this.parentEntity.posX + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 309 */       double d1 = this.parentEntity.posY + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 310 */       double d2 = this.parentEntity.posZ + ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
/* 311 */       this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class GhastMoveHelper
/*     */     extends EntityMoveHelper
/*     */   {
/*     */     private EntityGhast parentEntity;
/*     */     private int courseChangeCooldown;
/*     */     
/*     */     public GhastMoveHelper(EntityGhast ghast) {
/* 322 */       super((EntityLiving)ghast);
/* 323 */       this.parentEntity = ghast;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdateMoveHelper() {
/* 328 */       if (this.update) {
/*     */         
/* 330 */         double d0 = this.posX - this.parentEntity.posX;
/* 331 */         double d1 = this.posY - this.parentEntity.posY;
/* 332 */         double d2 = this.posZ - this.parentEntity.posZ;
/* 333 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */         
/* 335 */         if (this.courseChangeCooldown-- <= 0) {
/*     */           
/* 337 */           this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
/* 338 */           d3 = MathHelper.sqrt_double(d3);
/*     */           
/* 340 */           if (isNotColliding(this.posX, this.posY, this.posZ, d3)) {
/*     */             
/* 342 */             this.parentEntity.motionX += d0 / d3 * 0.1D;
/* 343 */             this.parentEntity.motionY += d1 / d3 * 0.1D;
/* 344 */             this.parentEntity.motionZ += d2 / d3 * 0.1D;
/*     */           }
/*     */           else {
/*     */             
/* 348 */             this.update = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean isNotColliding(double x, double y, double z, double p_179926_7_) {
/* 356 */       double d0 = (x - this.parentEntity.posX) / p_179926_7_;
/* 357 */       double d1 = (y - this.parentEntity.posY) / p_179926_7_;
/* 358 */       double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
/* 359 */       AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();
/*     */       
/* 361 */       for (int i = 1; i < p_179926_7_; i++) {
/*     */         
/* 363 */         axisalignedbb = axisalignedbb.offset(d0, d1, d2);
/*     */         
/* 365 */         if (!this.parentEntity.worldObj.getCollidingBoundingBoxes((Entity)this.parentEntity, axisalignedbb).isEmpty())
/*     */         {
/* 367 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 371 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityGhast.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */