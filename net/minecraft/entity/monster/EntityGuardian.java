/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityLookHelper;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.entity.projectile.EntityFishHook;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemFishFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateSwimmer;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandomFishable;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityGuardian
/*     */   extends EntityMob {
/*     */   private float field_175482_b;
/*     */   private float field_175484_c;
/*     */   private float field_175483_bk;
/*     */   private float field_175485_bl;
/*     */   private float field_175486_bm;
/*     */   private EntityLivingBase targetedEntity;
/*     */   private int field_175479_bo;
/*     */   private boolean field_175480_bp;
/*     */   private EntityAIWander wander;
/*     */   
/*     */   public EntityGuardian(World worldIn) {
/*  54 */     super(worldIn);
/*  55 */     this.experienceValue = 10;
/*  56 */     setSize(0.85F, 0.85F);
/*  57 */     this.tasks.addTask(4, new AIGuardianAttack(this));
/*     */     EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
/*  59 */     this.tasks.addTask(5, (EntityAIBase)(entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D)));
/*  60 */     this.tasks.addTask(7, (EntityAIBase)(this.wander = new EntityAIWander(this, 1.0D, 80)));
/*  61 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  62 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityGuardian.class, 12.0F, 0.01F));
/*  63 */     this.tasks.addTask(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  64 */     this.wander.setMutexBits(3);
/*  65 */     entityaimovetowardsrestriction.setMutexBits(3);
/*  66 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new GuardianTargetSelector(this)));
/*  67 */     this.moveHelper = new GuardianMoveHelper(this);
/*  68 */     this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  73 */     super.applyEntityAttributes();
/*  74 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
/*  75 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
/*  76 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
/*  77 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  82 */     super.readEntityFromNBT(tagCompund);
/*  83 */     setElder(tagCompund.getBoolean("Elder"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  88 */     super.writeEntityToNBT(tagCompound);
/*  89 */     tagCompound.setBoolean("Elder", isElder());
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigate getNewNavigator(World worldIn) {
/*  94 */     return (PathNavigate)new PathNavigateSwimmer((EntityLiving)this, worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  99 */     super.entityInit();
/* 100 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/* 101 */     this.dataWatcher.addObject(17, Integer.valueOf(0));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isSyncedFlagSet(int flagId) {
/* 106 */     return ((this.dataWatcher.getWatchableObjectInt(16) & flagId) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSyncedFlag(int flagId, boolean state) {
/* 111 */     int i = this.dataWatcher.getWatchableObjectInt(16);
/*     */     
/* 113 */     if (state) {
/*     */       
/* 115 */       this.dataWatcher.updateObject(16, Integer.valueOf(i | flagId));
/*     */     }
/*     */     else {
/*     */       
/* 119 */       this.dataWatcher.updateObject(16, Integer.valueOf(i & (flagId ^ 0xFFFFFFFF)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175472_n() {
/* 125 */     return isSyncedFlagSet(2);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175476_l(boolean p_175476_1_) {
/* 130 */     setSyncedFlag(2, p_175476_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public int func_175464_ck() {
/* 135 */     return isElder() ? 60 : 80;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isElder() {
/* 140 */     return isSyncedFlagSet(4);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElder(boolean elder) {
/* 145 */     setSyncedFlag(4, elder);
/*     */     
/* 147 */     if (elder) {
/*     */       
/* 149 */       setSize(1.9975F, 1.9975F);
/* 150 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/* 151 */       getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
/* 152 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
/* 153 */       enablePersistence();
/* 154 */       this.wander.setExecutionChance(400);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setElder() {
/* 160 */     setElder(true);
/* 161 */     this.field_175486_bm = this.field_175485_bl = 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void setTargetedEntity(int entityId) {
/* 166 */     this.dataWatcher.updateObject(17, Integer.valueOf(entityId));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasTargetedEntity() {
/* 171 */     return (this.dataWatcher.getWatchableObjectInt(17) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLivingBase getTargetedEntity() {
/* 176 */     if (!hasTargetedEntity())
/*     */     {
/* 178 */       return null;
/*     */     }
/* 180 */     if (this.worldObj.isRemote) {
/*     */       
/* 182 */       if (this.targetedEntity != null)
/*     */       {
/* 184 */         return this.targetedEntity;
/*     */       }
/*     */ 
/*     */       
/* 188 */       Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));
/*     */       
/* 190 */       if (entity instanceof EntityLivingBase) {
/*     */         
/* 192 */         this.targetedEntity = (EntityLivingBase)entity;
/* 193 */         return this.targetedEntity;
/*     */       } 
/*     */ 
/*     */       
/* 197 */       return null;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 203 */     return getAttackTarget();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDataWatcherUpdate(int dataID) {
/* 209 */     super.onDataWatcherUpdate(dataID);
/*     */     
/* 211 */     if (dataID == 16) {
/*     */       
/* 213 */       if (isElder() && this.width < 1.0F)
/*     */       {
/* 215 */         setSize(1.9975F, 1.9975F);
/*     */       }
/*     */     }
/* 218 */     else if (dataID == 17) {
/*     */       
/* 220 */       this.field_175479_bo = 0;
/* 221 */       this.targetedEntity = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTalkInterval() {
/* 227 */     return 160;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 232 */     return !isInWater() ? "mob.guardian.land.idle" : (isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 237 */     return !isInWater() ? "mob.guardian.land.hit" : (isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 242 */     return !isInWater() ? "mob.guardian.land.death" : (isElder() ? "mob.guardian.elder.death" : "mob.guardian.death");
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/* 247 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 252 */     return this.height * 0.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getBlockPathWeight(BlockPos pos) {
/* 257 */     return (this.worldObj.getBlockState(pos).getBlock().getMaterial() == Material.water) ? (10.0F + this.worldObj.getLightBrightness(pos) - 0.5F) : super.getBlockPathWeight(pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 262 */     if (this.worldObj.isRemote) {
/*     */       
/* 264 */       this.field_175484_c = this.field_175482_b;
/*     */       
/* 266 */       if (!isInWater()) {
/*     */         
/* 268 */         this.field_175483_bk = 2.0F;
/*     */         
/* 270 */         if (this.motionY > 0.0D && this.field_175480_bp && !isSilent())
/*     */         {
/* 272 */           this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);
/*     */         }
/*     */         
/* 275 */         this.field_175480_bp = (this.motionY < 0.0D && this.worldObj.isBlockNormalCube((new BlockPos((Entity)this)).down(), false));
/*     */       }
/* 277 */       else if (func_175472_n()) {
/*     */         
/* 279 */         if (this.field_175483_bk < 0.5F)
/*     */         {
/* 281 */           this.field_175483_bk = 4.0F;
/*     */         }
/*     */         else
/*     */         {
/* 285 */           this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 290 */         this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
/*     */       } 
/*     */       
/* 293 */       this.field_175482_b += this.field_175483_bk;
/* 294 */       this.field_175486_bm = this.field_175485_bl;
/*     */       
/* 296 */       if (!isInWater()) {
/*     */         
/* 298 */         this.field_175485_bl = this.rand.nextFloat();
/*     */       }
/* 300 */       else if (func_175472_n()) {
/*     */         
/* 302 */         this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
/*     */       }
/*     */       else {
/*     */         
/* 306 */         this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
/*     */       } 
/*     */       
/* 309 */       if (func_175472_n() && isInWater()) {
/*     */         
/* 311 */         Vec3 vec3 = getLook(0.0F);
/*     */         
/* 313 */         for (int i = 0; i < 2; i++)
/*     */         {
/* 315 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width - vec3.xCoord * 1.5D, this.posY + this.rand.nextDouble() * this.height - vec3.yCoord * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width - vec3.zCoord * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */         }
/*     */       } 
/*     */       
/* 319 */       if (hasTargetedEntity()) {
/*     */         
/* 321 */         if (this.field_175479_bo < func_175464_ck())
/*     */         {
/* 323 */           this.field_175479_bo++;
/*     */         }
/*     */         
/* 326 */         EntityLivingBase entitylivingbase = getTargetedEntity();
/*     */         
/* 328 */         if (entitylivingbase != null) {
/*     */           
/* 330 */           getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 90.0F, 90.0F);
/* 331 */           getLookHelper().onUpdateLook();
/* 332 */           double d5 = func_175477_p(0.0F);
/* 333 */           double d0 = entitylivingbase.posX - this.posX;
/* 334 */           double d1 = entitylivingbase.posY + (entitylivingbase.height * 0.5F) - this.posY + getEyeHeight();
/* 335 */           double d2 = entitylivingbase.posZ - this.posZ;
/* 336 */           double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
/* 337 */           d0 /= d3;
/* 338 */           d1 /= d3;
/* 339 */           d2 /= d3;
/* 340 */           double d4 = this.rand.nextDouble();
/*     */           
/* 342 */           while (d4 < d3) {
/*     */             
/* 344 */             d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
/* 345 */             this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 351 */     if (this.inWater) {
/*     */       
/* 353 */       setAir(300);
/*     */     }
/* 355 */     else if (this.onGround) {
/*     */       
/* 357 */       this.motionY += 0.5D;
/* 358 */       this.motionX += ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
/* 359 */       this.motionZ += ((this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F);
/* 360 */       this.rotationYaw = this.rand.nextFloat() * 360.0F;
/* 361 */       this.onGround = false;
/* 362 */       this.isAirBorne = true;
/*     */     } 
/*     */     
/* 365 */     if (hasTargetedEntity())
/*     */     {
/* 367 */       this.rotationYaw = this.rotationYawHead;
/*     */     }
/*     */     
/* 370 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175471_a(float p_175471_1_) {
/* 375 */     return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175469_o(float p_175469_1_) {
/* 380 */     return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175477_p(float p_175477_1_) {
/* 385 */     return (this.field_175479_bo + p_175477_1_) / func_175464_ck();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 390 */     super.updateAITasks();
/*     */     
/* 392 */     if (isElder()) {
/*     */       
/* 394 */       int i = 1200;
/* 395 */       int j = 1200;
/* 396 */       int k = 6000;
/* 397 */       int l = 2;
/*     */       
/* 399 */       if ((this.ticksExisted + getEntityId()) % 1200 == 0) {
/*     */         
/* 401 */         Potion potion = Potion.digSlowdown;
/*     */         
/* 403 */         for (EntityPlayerMP entityplayermp : this.worldObj.getPlayers(EntityPlayerMP.class, new Predicate<EntityPlayerMP>()
/*     */             {
/*     */               public boolean apply(EntityPlayerMP p_apply_1_)
/*     */               {
/* 407 */                 return (EntityGuardian.this.getDistanceSqToEntity((Entity)p_apply_1_) < 2500.0D && p_apply_1_.theItemInWorldManager.survivalOrAdventure());
/*     */               }
/*     */             })) {
/*     */           
/* 411 */           if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200) {
/*     */             
/* 413 */             entityplayermp.playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(10, 0.0F));
/* 414 */             entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 419 */       if (!hasHome())
/*     */       {
/* 421 */         setHomePosAndDistance(new BlockPos((Entity)this), 16);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 428 */     int i = this.rand.nextInt(3) + this.rand.nextInt(lootingModifier + 1);
/*     */     
/* 430 */     if (i > 0)
/*     */     {
/* 432 */       entityDropItem(new ItemStack(Items.prismarine_shard, i, 0), 1.0F);
/*     */     }
/*     */     
/* 435 */     if (this.rand.nextInt(3 + lootingModifier) > 1) {
/*     */       
/* 437 */       entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
/*     */     }
/* 439 */     else if (this.rand.nextInt(3 + lootingModifier) > 1) {
/*     */       
/* 441 */       entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
/*     */     } 
/*     */     
/* 444 */     if (wasRecentlyHit && isElder())
/*     */     {
/* 446 */       entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRandomDrop() {
/* 452 */     ItemStack itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
/* 453 */     entityDropItem(itemstack, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isValidLightLevel() {
/* 458 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotColliding() {
/* 463 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 468 */     return ((this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos((Entity)this))) && super.getCanSpawnHere());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 473 */     if (!func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase) {
/*     */       
/* 475 */       EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();
/*     */       
/* 477 */       if (!source.isExplosion()) {
/*     */         
/* 479 */         entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage((Entity)this), 2.0F);
/* 480 */         entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 484 */     this.wander.makeUpdate();
/* 485 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVerticalFaceSpeed() {
/* 490 */     return 180;
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveEntityWithHeading(float strafe, float forward) {
/* 495 */     if (isServerWorld()) {
/*     */       
/* 497 */       if (isInWater())
/*     */       {
/* 499 */         moveFlying(strafe, forward, 0.1F);
/* 500 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 501 */         this.motionX *= 0.8999999761581421D;
/* 502 */         this.motionY *= 0.8999999761581421D;
/* 503 */         this.motionZ *= 0.8999999761581421D;
/*     */         
/* 505 */         if (!func_175472_n() && getAttackTarget() == null)
/*     */         {
/* 507 */           this.motionY -= 0.005D;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 512 */         super.moveEntityWithHeading(strafe, forward);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 517 */       super.moveEntityWithHeading(strafe, forward);
/*     */     } 
/*     */   }
/*     */   
/*     */   static class AIGuardianAttack
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityGuardian theEntity;
/*     */     private int tickCounter;
/*     */     
/*     */     public AIGuardianAttack(EntityGuardian guardian) {
/* 528 */       this.theEntity = guardian;
/* 529 */       setMutexBits(3);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 534 */       EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/* 535 */       return (entitylivingbase != null && entitylivingbase.isEntityAlive());
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 540 */       return (super.continueExecuting() && (this.theEntity.isElder() || this.theEntity.getDistanceSqToEntity((Entity)this.theEntity.getAttackTarget()) > 9.0D));
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 545 */       this.tickCounter = -10;
/* 546 */       this.theEntity.getNavigator().clearPathEntity();
/* 547 */       this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)this.theEntity.getAttackTarget(), 90.0F, 90.0F);
/* 548 */       this.theEntity.isAirBorne = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetTask() {
/* 553 */       this.theEntity.setTargetedEntity(0);
/* 554 */       this.theEntity.setAttackTarget((EntityLivingBase)null);
/* 555 */       this.theEntity.wander.makeUpdate();
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 560 */       EntityLivingBase entitylivingbase = this.theEntity.getAttackTarget();
/* 561 */       this.theEntity.getNavigator().clearPathEntity();
/* 562 */       this.theEntity.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 90.0F, 90.0F);
/*     */       
/* 564 */       if (!this.theEntity.canEntityBeSeen((Entity)entitylivingbase)) {
/*     */         
/* 566 */         this.theEntity.setAttackTarget((EntityLivingBase)null);
/*     */       }
/*     */       else {
/*     */         
/* 570 */         this.tickCounter++;
/*     */         
/* 572 */         if (this.tickCounter == 0) {
/*     */           
/* 574 */           this.theEntity.setTargetedEntity(this.theEntity.getAttackTarget().getEntityId());
/* 575 */           this.theEntity.worldObj.setEntityState((Entity)this.theEntity, (byte)21);
/*     */         }
/* 577 */         else if (this.tickCounter >= this.theEntity.func_175464_ck()) {
/*     */           
/* 579 */           float f = 1.0F;
/*     */           
/* 581 */           if (this.theEntity.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*     */           {
/* 583 */             f += 2.0F;
/*     */           }
/*     */           
/* 586 */           if (this.theEntity.isElder())
/*     */           {
/* 588 */             f += 2.0F;
/*     */           }
/*     */           
/* 591 */           entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage((Entity)this.theEntity, (Entity)this.theEntity), f);
/* 592 */           entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this.theEntity), (float)this.theEntity.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
/* 593 */           this.theEntity.setAttackTarget((EntityLivingBase)null);
/*     */         }
/* 595 */         else if (this.tickCounter < 60 || this.tickCounter % 20 == 0) {
/*     */         
/*     */         } 
/*     */ 
/*     */         
/* 600 */         super.updateTask();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianMoveHelper
/*     */     extends EntityMoveHelper
/*     */   {
/*     */     private EntityGuardian entityGuardian;
/*     */     
/*     */     public GuardianMoveHelper(EntityGuardian guardian) {
/* 611 */       super((EntityLiving)guardian);
/* 612 */       this.entityGuardian = guardian;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdateMoveHelper() {
/* 617 */       if (this.update && !this.entityGuardian.getNavigator().noPath()) {
/*     */         
/* 619 */         double d0 = this.posX - this.entityGuardian.posX;
/* 620 */         double d1 = this.posY - this.entityGuardian.posY;
/* 621 */         double d2 = this.posZ - this.entityGuardian.posZ;
/* 622 */         double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/* 623 */         d3 = MathHelper.sqrt_double(d3);
/* 624 */         d1 /= d3;
/* 625 */         float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
/* 626 */         this.entityGuardian.rotationYaw = limitAngle(this.entityGuardian.rotationYaw, f, 30.0F);
/* 627 */         this.entityGuardian.renderYawOffset = this.entityGuardian.rotationYaw;
/* 628 */         float f1 = (float)(this.speed * this.entityGuardian.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
/* 629 */         this.entityGuardian.setAIMoveSpeed(this.entityGuardian.getAIMoveSpeed() + (f1 - this.entityGuardian.getAIMoveSpeed()) * 0.125F);
/* 630 */         double d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.5D) * 0.05D;
/* 631 */         double d5 = Math.cos((this.entityGuardian.rotationYaw * 3.1415927F / 180.0F));
/* 632 */         double d6 = Math.sin((this.entityGuardian.rotationYaw * 3.1415927F / 180.0F));
/* 633 */         this.entityGuardian.motionX += d4 * d5;
/* 634 */         this.entityGuardian.motionZ += d4 * d6;
/* 635 */         d4 = Math.sin((this.entityGuardian.ticksExisted + this.entityGuardian.getEntityId()) * 0.75D) * 0.05D;
/* 636 */         this.entityGuardian.motionY += d4 * (d6 + d5) * 0.25D;
/* 637 */         this.entityGuardian.motionY += this.entityGuardian.getAIMoveSpeed() * d1 * 0.1D;
/* 638 */         EntityLookHelper entitylookhelper = this.entityGuardian.getLookHelper();
/* 639 */         double d7 = this.entityGuardian.posX + d0 / d3 * 2.0D;
/* 640 */         double d8 = this.entityGuardian.getEyeHeight() + this.entityGuardian.posY + d1 / d3 * 1.0D;
/* 641 */         double d9 = this.entityGuardian.posZ + d2 / d3 * 2.0D;
/* 642 */         double d10 = entitylookhelper.getLookPosX();
/* 643 */         double d11 = entitylookhelper.getLookPosY();
/* 644 */         double d12 = entitylookhelper.getLookPosZ();
/*     */         
/* 646 */         if (!entitylookhelper.getIsLooking()) {
/*     */           
/* 648 */           d10 = d7;
/* 649 */           d11 = d8;
/* 650 */           d12 = d9;
/*     */         } 
/*     */         
/* 653 */         this.entityGuardian.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
/* 654 */         this.entityGuardian.func_175476_l(true);
/*     */       }
/*     */       else {
/*     */         
/* 658 */         this.entityGuardian.setAIMoveSpeed(0.0F);
/* 659 */         this.entityGuardian.func_175476_l(false);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class GuardianTargetSelector
/*     */     implements Predicate<EntityLivingBase>
/*     */   {
/*     */     private EntityGuardian parentEntity;
/*     */     
/*     */     public GuardianTargetSelector(EntityGuardian guardian) {
/* 670 */       this.parentEntity = guardian;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean apply(EntityLivingBase p_apply_1_) {
/* 675 */       return ((p_apply_1_ instanceof EntityPlayer || p_apply_1_ instanceof net.minecraft.entity.passive.EntitySquid) && p_apply_1_.getDistanceSqToEntity((Entity)this.parentEntity) > 9.0D);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityGuardian.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */