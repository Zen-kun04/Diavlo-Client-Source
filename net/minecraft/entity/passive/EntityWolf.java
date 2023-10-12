/*     */ package net.minecraft.entity.passive;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIBeg;
/*     */ import net.minecraft.entity.ai.EntityAIFollowOwner;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITargetNonTamed;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.monster.EntitySkeleton;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWolf
/*     */   extends EntityTameable
/*     */ {
/*     */   private float headRotationCourse;
/*     */   private float headRotationCourseOld;
/*     */   private boolean isWet;
/*     */   private boolean isShaking;
/*     */   private float timeWolfIsShaking;
/*     */   private float prevTimeWolfIsShaking;
/*     */   
/*     */   public EntityWolf(World worldIn) {
/*  52 */     super(worldIn);
/*  53 */     setSize(0.6F, 0.8F);
/*  54 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  55 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  56 */     this.tasks.addTask(2, (EntityAIBase)this.aiSit);
/*  57 */     this.tasks.addTask(3, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
/*  58 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide((EntityCreature)this, 1.0D, true));
/*  59 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 2.0F));
/*  60 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIMate(this, 1.0D));
/*  61 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  62 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIBeg(this, 8.0F));
/*  63 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  64 */     this.tasks.addTask(9, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  65 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIOwnerHurtByTarget(this));
/*  66 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAIOwnerHurtTarget(this));
/*  67 */     this.targetTasks.addTask(3, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, true, new Class[0]));
/*  68 */     this.targetTasks.addTask(4, (EntityAIBase)new EntityAITargetNonTamed(this, EntityAnimal.class, false, new Predicate<Entity>()
/*     */           {
/*     */             public boolean apply(Entity p_apply_1_)
/*     */             {
/*  72 */               return (p_apply_1_ instanceof EntitySheep || p_apply_1_ instanceof EntityRabbit);
/*     */             }
/*     */           }));
/*  75 */     this.targetTasks.addTask(5, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntitySkeleton.class, false));
/*  76 */     setTamed(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  81 */     super.applyEntityAttributes();
/*  82 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */     
/*  84 */     if (isTamed()) {
/*     */       
/*  86 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
/*     */     }
/*     */     else {
/*     */       
/*  90 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*     */     } 
/*     */     
/*  93 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage);
/*  94 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(2.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAttackTarget(EntityLivingBase entitylivingbaseIn) {
/*  99 */     super.setAttackTarget(entitylivingbaseIn);
/*     */     
/* 101 */     if (entitylivingbaseIn == null) {
/*     */       
/* 103 */       setAngry(false);
/*     */     }
/* 105 */     else if (!isTamed()) {
/*     */       
/* 107 */       setAngry(true);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 113 */     this.dataWatcher.updateObject(18, Float.valueOf(getHealth()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 118 */     super.entityInit();
/* 119 */     this.dataWatcher.addObject(18, new Float(getHealth()));
/* 120 */     this.dataWatcher.addObject(19, new Byte((byte)0));
/* 121 */     this.dataWatcher.addObject(20, new Byte((byte)EnumDyeColor.RED.getMetadata()));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/* 126 */     playSound("mob.wolf.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 131 */     super.writeEntityToNBT(tagCompound);
/* 132 */     tagCompound.setBoolean("Angry", isAngry());
/* 133 */     tagCompound.setByte("CollarColor", (byte)getCollarColor().getDyeDamage());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 138 */     super.readEntityFromNBT(tagCompund);
/* 139 */     setAngry(tagCompund.getBoolean("Angry"));
/*     */     
/* 141 */     if (tagCompund.hasKey("CollarColor", 99))
/*     */     {
/* 143 */       setCollarColor(EnumDyeColor.byDyeDamage(tagCompund.getByte("CollarColor")));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 149 */     return isAngry() ? "mob.wolf.growl" : ((this.rand.nextInt(3) == 0) ? ((isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F) ? "mob.wolf.whine" : "mob.wolf.panting") : "mob.wolf.bark");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 154 */     return "mob.wolf.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 159 */     return "mob.wolf.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 164 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 169 */     return Item.getItemById(-1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 174 */     super.onLivingUpdate();
/*     */     
/* 176 */     if (!this.worldObj.isRemote && this.isWet && !this.isShaking && !hasPath() && this.onGround) {
/*     */       
/* 178 */       this.isShaking = true;
/* 179 */       this.timeWolfIsShaking = 0.0F;
/* 180 */       this.prevTimeWolfIsShaking = 0.0F;
/* 181 */       this.worldObj.setEntityState((Entity)this, (byte)8);
/*     */     } 
/*     */     
/* 184 */     if (!this.worldObj.isRemote && getAttackTarget() == null && isAngry())
/*     */     {
/* 186 */       setAngry(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 192 */     super.onUpdate();
/* 193 */     this.headRotationCourseOld = this.headRotationCourse;
/*     */     
/* 195 */     if (isBegging()) {
/*     */       
/* 197 */       this.headRotationCourse += (1.0F - this.headRotationCourse) * 0.4F;
/*     */     }
/*     */     else {
/*     */       
/* 201 */       this.headRotationCourse += (0.0F - this.headRotationCourse) * 0.4F;
/*     */     } 
/*     */     
/* 204 */     if (isWet()) {
/*     */       
/* 206 */       this.isWet = true;
/* 207 */       this.isShaking = false;
/* 208 */       this.timeWolfIsShaking = 0.0F;
/* 209 */       this.prevTimeWolfIsShaking = 0.0F;
/*     */     }
/* 211 */     else if ((this.isWet || this.isShaking) && this.isShaking) {
/*     */       
/* 213 */       if (this.timeWolfIsShaking == 0.0F)
/*     */       {
/* 215 */         playSound("mob.wolf.shake", getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*     */       }
/*     */       
/* 218 */       this.prevTimeWolfIsShaking = this.timeWolfIsShaking;
/* 219 */       this.timeWolfIsShaking += 0.05F;
/*     */       
/* 221 */       if (this.prevTimeWolfIsShaking >= 2.0F) {
/*     */         
/* 223 */         this.isWet = false;
/* 224 */         this.isShaking = false;
/* 225 */         this.prevTimeWolfIsShaking = 0.0F;
/* 226 */         this.timeWolfIsShaking = 0.0F;
/*     */       } 
/*     */       
/* 229 */       if (this.timeWolfIsShaking > 0.4F) {
/*     */         
/* 231 */         float f = (float)(getEntityBoundingBox()).minY;
/* 232 */         int i = (int)(MathHelper.sin((this.timeWolfIsShaking - 0.4F) * 3.1415927F) * 7.0F);
/*     */         
/* 234 */         for (int j = 0; j < i; j++) {
/*     */           
/* 236 */           float f1 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
/* 237 */           float f2 = (this.rand.nextFloat() * 2.0F - 1.0F) * this.width * 0.5F;
/* 238 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, this.posX + f1, (f + 0.8F), this.posZ + f2, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWolfWet() {
/* 246 */     return this.isWet;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getShadingWhileWet(float p_70915_1_) {
/* 251 */     return 0.75F + (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70915_1_) / 2.0F * 0.25F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getShakeAngle(float p_70923_1_, float p_70923_2_) {
/* 256 */     float f = (this.prevTimeWolfIsShaking + (this.timeWolfIsShaking - this.prevTimeWolfIsShaking) * p_70923_1_ + p_70923_2_) / 1.8F;
/*     */     
/* 258 */     if (f < 0.0F) {
/*     */       
/* 260 */       f = 0.0F;
/*     */     }
/* 262 */     else if (f > 1.0F) {
/*     */       
/* 264 */       f = 1.0F;
/*     */     } 
/*     */     
/* 267 */     return MathHelper.sin(f * 3.1415927F) * MathHelper.sin(f * 3.1415927F * 11.0F) * 0.15F * 3.1415927F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getInterestedAngle(float p_70917_1_) {
/* 272 */     return (this.headRotationCourseOld + (this.headRotationCourse - this.headRotationCourseOld) * p_70917_1_) * 0.15F * 3.1415927F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 277 */     return this.height * 0.8F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getVerticalFaceSpeed() {
/* 282 */     return isSitting() ? 20 : super.getVerticalFaceSpeed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 287 */     if (isEntityInvulnerable(source))
/*     */     {
/* 289 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 293 */     Entity entity = source.getEntity();
/* 294 */     this.aiSit.setSitting(false);
/*     */     
/* 296 */     if (entity != null && !(entity instanceof EntityPlayer) && !(entity instanceof net.minecraft.entity.projectile.EntityArrow))
/*     */     {
/* 298 */       amount = (amount + 1.0F) / 2.0F;
/*     */     }
/*     */     
/* 301 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 307 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (int)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
/*     */     
/* 309 */     if (flag)
/*     */     {
/* 311 */       applyEnchantments((EntityLivingBase)this, entityIn);
/*     */     }
/*     */     
/* 314 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTamed(boolean tamed) {
/* 319 */     super.setTamed(tamed);
/*     */     
/* 321 */     if (tamed) {
/*     */       
/* 323 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(20.0D);
/*     */     }
/*     */     else {
/*     */       
/* 327 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(8.0D);
/*     */     } 
/*     */     
/* 330 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 335 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 337 */     if (isTamed()) {
/*     */       
/* 339 */       if (itemstack != null)
/*     */       {
/* 341 */         if (itemstack.getItem() instanceof ItemFood) {
/*     */           
/* 343 */           ItemFood itemfood = (ItemFood)itemstack.getItem();
/*     */           
/* 345 */           if (itemfood.isWolfsFavoriteMeat() && this.dataWatcher.getWatchableObjectFloat(18) < 20.0F)
/*     */           {
/* 347 */             if (!player.capabilities.isCreativeMode)
/*     */             {
/* 349 */               itemstack.stackSize--;
/*     */             }
/*     */             
/* 352 */             heal(itemfood.getHealAmount(itemstack));
/*     */             
/* 354 */             if (itemstack.stackSize <= 0)
/*     */             {
/* 356 */               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */             }
/*     */             
/* 359 */             return true;
/*     */           }
/*     */         
/* 362 */         } else if (itemstack.getItem() == Items.dye) {
/*     */           
/* 364 */           EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(itemstack.getMetadata());
/*     */           
/* 366 */           if (enumdyecolor != getCollarColor()) {
/*     */             
/* 368 */             setCollarColor(enumdyecolor);
/*     */             
/* 370 */             if (!player.capabilities.isCreativeMode && --itemstack.stackSize <= 0)
/*     */             {
/* 372 */               player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */             }
/*     */             
/* 375 */             return true;
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 380 */       if (isOwner((EntityLivingBase)player) && !this.worldObj.isRemote && !isBreedingItem(itemstack))
/*     */       {
/* 382 */         this.aiSit.setSitting(!isSitting());
/* 383 */         this.isJumping = false;
/* 384 */         this.navigator.clearPathEntity();
/* 385 */         setAttackTarget((EntityLivingBase)null);
/*     */       }
/*     */     
/* 388 */     } else if (itemstack != null && itemstack.getItem() == Items.bone && !isAngry()) {
/*     */       
/* 390 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 392 */         itemstack.stackSize--;
/*     */       }
/*     */       
/* 395 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 397 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */       
/* 400 */       if (!this.worldObj.isRemote)
/*     */       {
/* 402 */         if (this.rand.nextInt(3) == 0) {
/*     */           
/* 404 */           setTamed(true);
/* 405 */           this.navigator.clearPathEntity();
/* 406 */           setAttackTarget((EntityLivingBase)null);
/* 407 */           this.aiSit.setSitting(true);
/* 408 */           setHealth(20.0F);
/* 409 */           setOwnerId(player.getUniqueID().toString());
/* 410 */           playTameEffect(true);
/* 411 */           this.worldObj.setEntityState((Entity)this, (byte)7);
/*     */         }
/*     */         else {
/*     */           
/* 415 */           playTameEffect(false);
/* 416 */           this.worldObj.setEntityState((Entity)this, (byte)6);
/*     */         } 
/*     */       }
/*     */       
/* 420 */       return true;
/*     */     } 
/*     */     
/* 423 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 428 */     if (id == 8) {
/*     */       
/* 430 */       this.isShaking = true;
/* 431 */       this.timeWolfIsShaking = 0.0F;
/* 432 */       this.prevTimeWolfIsShaking = 0.0F;
/*     */     }
/*     */     else {
/*     */       
/* 436 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getTailRotation() {
/* 442 */     return isAngry() ? 1.5393804F : (isTamed() ? ((0.55F - (20.0F - this.dataWatcher.getWatchableObjectFloat(18)) * 0.02F) * 3.1415927F) : 0.62831855F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreedingItem(ItemStack stack) {
/* 447 */     return (stack == null) ? false : (!(stack.getItem() instanceof ItemFood) ? false : ((ItemFood)stack.getItem()).isWolfsFavoriteMeat());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSpawnedInChunk() {
/* 452 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAngry() {
/* 457 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setAngry(boolean angry) {
/* 462 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 464 */     if (angry) {
/*     */       
/* 466 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x2)));
/*     */     }
/*     */     else {
/*     */       
/* 470 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFD)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumDyeColor getCollarColor() {
/* 476 */     return EnumDyeColor.byDyeDamage(this.dataWatcher.getWatchableObjectByte(20) & 0xF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCollarColor(EnumDyeColor collarcolor) {
/* 481 */     this.dataWatcher.updateObject(20, Byte.valueOf((byte)(collarcolor.getDyeDamage() & 0xF)));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityWolf createChild(EntityAgeable ageable) {
/* 486 */     EntityWolf entitywolf = new EntityWolf(this.worldObj);
/* 487 */     String s = getOwnerId();
/*     */     
/* 489 */     if (s != null && s.trim().length() > 0) {
/*     */       
/* 491 */       entitywolf.setOwnerId(s);
/* 492 */       entitywolf.setTamed(true);
/*     */     } 
/*     */     
/* 495 */     return entitywolf;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBegging(boolean beg) {
/* 500 */     if (beg) {
/*     */       
/* 502 */       this.dataWatcher.updateObject(19, Byte.valueOf((byte)1));
/*     */     }
/*     */     else {
/*     */       
/* 506 */       this.dataWatcher.updateObject(19, Byte.valueOf((byte)0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMateWith(EntityAnimal otherAnimal) {
/* 512 */     if (otherAnimal == this)
/*     */     {
/* 514 */       return false;
/*     */     }
/* 516 */     if (!isTamed())
/*     */     {
/* 518 */       return false;
/*     */     }
/* 520 */     if (!(otherAnimal instanceof EntityWolf))
/*     */     {
/* 522 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 526 */     EntityWolf entitywolf = (EntityWolf)otherAnimal;
/* 527 */     return !entitywolf.isTamed() ? false : (entitywolf.isSitting() ? false : ((isInLove() && entitywolf.isInLove())));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isBegging() {
/* 533 */     return (this.dataWatcher.getWatchableObjectByte(19) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDespawn() {
/* 538 */     return (!isTamed() && this.ticksExisted > 2400);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldAttackEntity(EntityLivingBase p_142018_1_, EntityLivingBase p_142018_2_) {
/* 543 */     if (!(p_142018_1_ instanceof net.minecraft.entity.monster.EntityCreeper) && !(p_142018_1_ instanceof net.minecraft.entity.monster.EntityGhast)) {
/*     */       
/* 545 */       if (p_142018_1_ instanceof EntityWolf) {
/*     */         
/* 547 */         EntityWolf entitywolf = (EntityWolf)p_142018_1_;
/*     */         
/* 549 */         if (entitywolf.isTamed() && entitywolf.getOwner() == p_142018_2_)
/*     */         {
/* 551 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 555 */       return (p_142018_1_ instanceof EntityPlayer && p_142018_2_ instanceof EntityPlayer && !((EntityPlayer)p_142018_2_).canAttackPlayer((EntityPlayer)p_142018_1_)) ? false : ((!(p_142018_1_ instanceof EntityHorse) || !((EntityHorse)p_142018_1_).isTame()));
/*     */     } 
/*     */ 
/*     */     
/* 559 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean allowLeashing() {
/* 565 */     return (!isAngry() && super.allowLeashing());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityWolf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */