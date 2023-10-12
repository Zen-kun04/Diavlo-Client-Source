/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIFindEntityNearest;
/*     */ import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldType;
/*     */ import net.minecraft.world.biome.BiomeGenBase;
/*     */ import net.minecraft.world.chunk.Chunk;
/*     */ 
/*     */ public class EntitySlime
/*     */   extends EntityLiving
/*     */   implements IMob {
/*     */   public float squishAmount;
/*     */   public float squishFactor;
/*     */   public float prevSquishFactor;
/*     */   private boolean wasOnGround;
/*     */   
/*     */   public EntitySlime(World worldIn) {
/*  37 */     super(worldIn);
/*  38 */     this.moveHelper = new SlimeMoveHelper(this);
/*  39 */     this.tasks.addTask(1, new AISlimeFloat(this));
/*  40 */     this.tasks.addTask(2, new AISlimeAttack(this));
/*  41 */     this.tasks.addTask(3, new AISlimeFaceRandom(this));
/*  42 */     this.tasks.addTask(5, new AISlimeHop(this));
/*  43 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIFindEntityNearestPlayer(this));
/*  44 */     this.targetTasks.addTask(3, (EntityAIBase)new EntityAIFindEntityNearest(this, EntityIronGolem.class));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  49 */     super.entityInit();
/*  50 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)1));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setSlimeSize(int size) {
/*  55 */     this.dataWatcher.updateObject(16, Byte.valueOf((byte)size));
/*  56 */     setSize(0.51000005F * size, 0.51000005F * size);
/*  57 */     setPosition(this.posX, this.posY, this.posZ);
/*  58 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue((size * size));
/*  59 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue((0.2F + 0.1F * size));
/*  60 */     setHealth(getMaxHealth());
/*  61 */     this.experienceValue = size;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSlimeSize() {
/*  66 */     return this.dataWatcher.getWatchableObjectByte(16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  71 */     super.writeEntityToNBT(tagCompound);
/*  72 */     tagCompound.setInteger("Size", getSlimeSize() - 1);
/*  73 */     tagCompound.setBoolean("wasOnGround", this.wasOnGround);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  78 */     super.readEntityFromNBT(tagCompund);
/*  79 */     int i = tagCompund.getInteger("Size");
/*     */     
/*  81 */     if (i < 0)
/*     */     {
/*  83 */       i = 0;
/*     */     }
/*     */     
/*  86 */     setSlimeSize(i + 1);
/*  87 */     this.wasOnGround = tagCompund.getBoolean("wasOnGround");
/*     */   }
/*     */ 
/*     */   
/*     */   protected EnumParticleTypes getParticleType() {
/*  92 */     return EnumParticleTypes.SLIME;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getJumpSound() {
/*  97 */     return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 102 */     if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && getSlimeSize() > 0)
/*     */     {
/* 104 */       this.isDead = true;
/*     */     }
/*     */     
/* 107 */     this.squishFactor += (this.squishAmount - this.squishFactor) * 0.5F;
/* 108 */     this.prevSquishFactor = this.squishFactor;
/* 109 */     super.onUpdate();
/*     */     
/* 111 */     if (this.onGround && !this.wasOnGround) {
/*     */       
/* 113 */       int i = getSlimeSize();
/*     */       
/* 115 */       for (int j = 0; j < i * 8; j++) {
/*     */         
/* 117 */         float f = this.rand.nextFloat() * 3.1415927F * 2.0F;
/* 118 */         float f1 = this.rand.nextFloat() * 0.5F + 0.5F;
/* 119 */         float f2 = MathHelper.sin(f) * i * 0.5F * f1;
/* 120 */         float f3 = MathHelper.cos(f) * i * 0.5F * f1;
/* 121 */         World world = this.worldObj;
/* 122 */         EnumParticleTypes enumparticletypes = getParticleType();
/* 123 */         double d0 = this.posX + f2;
/* 124 */         double d1 = this.posZ + f3;
/* 125 */         world.spawnParticle(enumparticletypes, d0, (getEntityBoundingBox()).minY, d1, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       } 
/*     */       
/* 128 */       if (makesSoundOnLand())
/*     */       {
/* 130 */         playSound(getJumpSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) / 0.8F);
/*     */       }
/*     */       
/* 133 */       this.squishAmount = -0.5F;
/*     */     }
/* 135 */     else if (!this.onGround && this.wasOnGround) {
/*     */       
/* 137 */       this.squishAmount = 1.0F;
/*     */     } 
/*     */     
/* 140 */     this.wasOnGround = this.onGround;
/* 141 */     alterSquishAmount();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void alterSquishAmount() {
/* 146 */     this.squishAmount *= 0.6F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getJumpDelay() {
/* 151 */     return this.rand.nextInt(20) + 10;
/*     */   }
/*     */ 
/*     */   
/*     */   protected EntitySlime createInstance() {
/* 156 */     return new EntitySlime(this.worldObj);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDataWatcherUpdate(int dataID) {
/* 161 */     if (dataID == 16) {
/*     */       
/* 163 */       int i = getSlimeSize();
/* 164 */       setSize(0.51000005F * i, 0.51000005F * i);
/* 165 */       this.rotationYaw = this.rotationYawHead;
/* 166 */       this.renderYawOffset = this.rotationYawHead;
/*     */       
/* 168 */       if (isInWater() && this.rand.nextInt(20) == 0)
/*     */       {
/* 170 */         resetHeight();
/*     */       }
/*     */     } 
/*     */     
/* 174 */     super.onDataWatcherUpdate(dataID);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDead() {
/* 179 */     int i = getSlimeSize();
/*     */     
/* 181 */     if (!this.worldObj.isRemote && i > 1 && getHealth() <= 0.0F) {
/*     */       
/* 183 */       int j = 2 + this.rand.nextInt(3);
/*     */       
/* 185 */       for (int k = 0; k < j; k++) {
/*     */         
/* 187 */         float f = ((k % 2) - 0.5F) * i / 4.0F;
/* 188 */         float f1 = ((k / 2) - 0.5F) * i / 4.0F;
/* 189 */         EntitySlime entityslime = createInstance();
/*     */         
/* 191 */         if (hasCustomName())
/*     */         {
/* 193 */           entityslime.setCustomNameTag(getCustomNameTag());
/*     */         }
/*     */         
/* 196 */         if (isNoDespawnRequired())
/*     */         {
/* 198 */           entityslime.enablePersistence();
/*     */         }
/*     */         
/* 201 */         entityslime.setSlimeSize(i / 2);
/* 202 */         entityslime.setLocationAndAngles(this.posX + f, this.posY + 0.5D, this.posZ + f1, this.rand.nextFloat() * 360.0F, 0.0F);
/* 203 */         this.worldObj.spawnEntityInWorld((Entity)entityslime);
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     super.setDead();
/*     */   }
/*     */ 
/*     */   
/*     */   public void applyEntityCollision(Entity entityIn) {
/* 212 */     super.applyEntityCollision(entityIn);
/*     */     
/* 214 */     if (entityIn instanceof EntityIronGolem && canDamagePlayer())
/*     */     {
/* 216 */       func_175451_e((EntityLivingBase)entityIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn) {
/* 222 */     if (canDamagePlayer())
/*     */     {
/* 224 */       func_175451_e((EntityLivingBase)entityIn);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void func_175451_e(EntityLivingBase p_175451_1_) {
/* 230 */     int i = getSlimeSize();
/*     */     
/* 232 */     if (canEntityBeSeen((Entity)p_175451_1_) && getDistanceSqToEntity((Entity)p_175451_1_) < 0.6D * i * 0.6D * i && p_175451_1_.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), getAttackStrength())) {
/*     */       
/* 234 */       playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 235 */       applyEnchantments((EntityLivingBase)this, (Entity)p_175451_1_);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 241 */     return 0.625F * this.height;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDamagePlayer() {
/* 246 */     return (getSlimeSize() > 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAttackStrength() {
/* 251 */     return getSlimeSize();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 256 */     return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 261 */     return "mob.slime." + ((getSlimeSize() > 1) ? "big" : "small");
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 266 */     return (getSlimeSize() == 1) ? Items.slime_ball : null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 271 */     BlockPos blockpos = new BlockPos(MathHelper.floor_double(this.posX), 0, MathHelper.floor_double(this.posZ));
/* 272 */     Chunk chunk = this.worldObj.getChunkFromBlockCoords(blockpos);
/*     */     
/* 274 */     if (this.worldObj.getWorldInfo().getTerrainType() == WorldType.FLAT && this.rand.nextInt(4) != 1)
/*     */     {
/* 276 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 280 */     if (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL) {
/*     */       
/* 282 */       BiomeGenBase biomegenbase = this.worldObj.getBiomeGenForCoords(blockpos);
/*     */       
/* 284 */       if (biomegenbase == BiomeGenBase.swampland && this.posY > 50.0D && this.posY < 70.0D && this.rand.nextFloat() < 0.5F && this.rand.nextFloat() < this.worldObj.getCurrentMoonPhaseFactor() && this.worldObj.getLightFromNeighbors(new BlockPos((Entity)this)) <= this.rand.nextInt(8))
/*     */       {
/* 286 */         return super.getCanSpawnHere();
/*     */       }
/*     */       
/* 289 */       if (this.rand.nextInt(10) == 0 && chunk.getRandomWithSeed(987234911L).nextInt(10) == 0 && this.posY < 40.0D)
/*     */       {
/* 291 */         return super.getCanSpawnHere();
/*     */       }
/*     */     } 
/*     */     
/* 295 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 301 */     return 0.4F * getSlimeSize();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVerticalFaceSpeed() {
/* 306 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean makesSoundOnJump() {
/* 311 */     return (getSlimeSize() > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean makesSoundOnLand() {
/* 316 */     return (getSlimeSize() > 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void jump() {
/* 321 */     this.motionY = 0.41999998688697815D;
/* 322 */     this.isAirBorne = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 327 */     int i = this.rand.nextInt(3);
/*     */     
/* 329 */     if (i < 2 && this.rand.nextFloat() < 0.5F * difficulty.getClampedAdditionalDifficulty())
/*     */     {
/* 331 */       i++;
/*     */     }
/*     */     
/* 334 */     int j = 1 << i;
/* 335 */     setSlimeSize(j);
/* 336 */     return super.onInitialSpawn(difficulty, livingdata);
/*     */   }
/*     */   
/*     */   static class AISlimeAttack
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     private int field_179465_b;
/*     */     
/*     */     public AISlimeAttack(EntitySlime slimeIn) {
/* 346 */       this.slime = slimeIn;
/* 347 */       setMutexBits(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 352 */       EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
/* 353 */       return (entitylivingbase == null) ? false : (!entitylivingbase.isEntityAlive() ? false : ((!(entitylivingbase instanceof EntityPlayer) || !((EntityPlayer)entitylivingbase).capabilities.disableDamage)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 358 */       this.field_179465_b = 300;
/* 359 */       super.startExecuting();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 364 */       EntityLivingBase entitylivingbase = this.slime.getAttackTarget();
/* 365 */       return (entitylivingbase == null) ? false : (!entitylivingbase.isEntityAlive() ? false : ((entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage) ? false : ((--this.field_179465_b > 0))));
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 370 */       this.slime.faceEntity((Entity)this.slime.getAttackTarget(), 10.0F, 10.0F);
/* 371 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.slime.rotationYaw, this.slime.canDamagePlayer());
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeFaceRandom
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     private float field_179459_b;
/*     */     private int field_179460_c;
/*     */     
/*     */     public AISlimeFaceRandom(EntitySlime slimeIn) {
/* 383 */       this.slime = slimeIn;
/* 384 */       setMutexBits(2);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 389 */       return (this.slime.getAttackTarget() == null && (this.slime.onGround || this.slime.isInWater() || this.slime.isInLava()));
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 394 */       if (--this.field_179460_c <= 0) {
/*     */         
/* 396 */         this.field_179460_c = 40 + this.slime.getRNG().nextInt(60);
/* 397 */         this.field_179459_b = this.slime.getRNG().nextInt(360);
/*     */       } 
/*     */       
/* 400 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).func_179920_a(this.field_179459_b, false);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeFloat
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     
/*     */     public AISlimeFloat(EntitySlime slimeIn) {
/* 410 */       this.slime = slimeIn;
/* 411 */       setMutexBits(5);
/* 412 */       ((PathNavigateGround)slimeIn.getNavigator()).setCanSwim(true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 417 */       return (this.slime.isInWater() || this.slime.isInLava());
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 422 */       if (this.slime.getRNG().nextFloat() < 0.8F)
/*     */       {
/* 424 */         this.slime.getJumpHelper().setJumping();
/*     */       }
/*     */       
/* 427 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.2D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISlimeHop
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntitySlime slime;
/*     */     
/*     */     public AISlimeHop(EntitySlime slimeIn) {
/* 437 */       this.slime = slimeIn;
/* 438 */       setMutexBits(5);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 443 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 448 */       ((EntitySlime.SlimeMoveHelper)this.slime.getMoveHelper()).setSpeed(1.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   static class SlimeMoveHelper
/*     */     extends EntityMoveHelper
/*     */   {
/*     */     private float field_179922_g;
/*     */     private int field_179924_h;
/*     */     private EntitySlime slime;
/*     */     private boolean field_179923_j;
/*     */     
/*     */     public SlimeMoveHelper(EntitySlime slimeIn) {
/* 461 */       super(slimeIn);
/* 462 */       this.slime = slimeIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_179920_a(float p_179920_1_, boolean p_179920_2_) {
/* 467 */       this.field_179922_g = p_179920_1_;
/* 468 */       this.field_179923_j = p_179920_2_;
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSpeed(double speedIn) {
/* 473 */       this.speed = speedIn;
/* 474 */       this.update = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdateMoveHelper() {
/* 479 */       this.entity.rotationYaw = limitAngle(this.entity.rotationYaw, this.field_179922_g, 30.0F);
/* 480 */       this.entity.rotationYawHead = this.entity.rotationYaw;
/* 481 */       this.entity.renderYawOffset = this.entity.rotationYaw;
/*     */       
/* 483 */       if (!this.update) {
/*     */         
/* 485 */         this.entity.setMoveForward(0.0F);
/*     */       }
/*     */       else {
/*     */         
/* 489 */         this.update = false;
/*     */         
/* 491 */         if (this.entity.onGround) {
/*     */           
/* 493 */           this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */           
/* 495 */           if (this.field_179924_h-- <= 0)
/*     */           {
/* 497 */             this.field_179924_h = this.slime.getJumpDelay();
/*     */             
/* 499 */             if (this.field_179923_j)
/*     */             {
/* 501 */               this.field_179924_h /= 3;
/*     */             }
/*     */             
/* 504 */             this.slime.getJumpHelper().setJumping();
/*     */             
/* 506 */             if (this.slime.makesSoundOnJump())
/*     */             {
/* 508 */               this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), ((this.slime.getRNG().nextFloat() - this.slime.getRNG().nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 513 */             this.slime.moveStrafing = this.slime.moveForward = 0.0F;
/* 514 */             this.entity.setAIMoveSpeed(0.0F);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 519 */           this.entity.setAIMoveSpeed((float)(this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue()));
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntitySlime.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */