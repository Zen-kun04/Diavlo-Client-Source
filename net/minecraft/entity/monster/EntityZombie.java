/*     */ package net.minecraft.entity.monster;
/*     */ import java.util.Calendar;
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIBreakDoor;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttribute;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*     */ import net.minecraft.entity.passive.EntityChicken;
/*     */ import net.minecraft.entity.passive.EntityVillager;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityZombie extends EntityMob {
/*  48 */   protected static final IAttribute reinforcementChance = (IAttribute)(new RangedAttribute((IAttribute)null, "zombie.spawnReinforcements", 0.0D, 0.0D, 1.0D)).setDescription("Spawn Reinforcements Chance");
/*  49 */   private static final UUID babySpeedBoostUUID = UUID.fromString("B9766B59-9566-4402-BC1F-2EE2A276D836");
/*  50 */   private static final AttributeModifier babySpeedBoostModifier = new AttributeModifier(babySpeedBoostUUID, "Baby speed boost", 0.5D, 1);
/*  51 */   private final EntityAIBreakDoor breakDoor = new EntityAIBreakDoor((EntityLiving)this);
/*     */   private int conversionTime;
/*     */   private boolean isBreakDoorsTaskSet = false;
/*  54 */   private float zombieWidth = -1.0F;
/*     */   
/*     */   private float zombieHeight;
/*     */   
/*     */   public EntityZombie(World worldIn) {
/*  59 */     super(worldIn);
/*  60 */     ((PathNavigateGround)getNavigator()).setBreakDoors(true);
/*  61 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  62 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
/*  63 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  64 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  65 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  66 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  67 */     applyEntityAI();
/*  68 */     setSize(0.6F, 1.95F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAI() {
/*  73 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityVillager.class, 1.0D, true));
/*  74 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIAttackOnCollide(this, EntityIronGolem.class, 1.0D, true));
/*  75 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIMoveThroughVillage(this, 1.0D, false));
/*  76 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, true, new Class[] { EntityPigZombie.class }));
/*  77 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  78 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityVillager.class, false));
/*  79 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  84 */     super.applyEntityAttributes();
/*  85 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
/*  86 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  87 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
/*  88 */     getAttributeMap().registerAttribute(reinforcementChance).setBaseValue(this.rand.nextDouble() * 0.10000000149011612D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  93 */     super.entityInit();
/*  94 */     getDataWatcher().addObject(12, Byte.valueOf((byte)0));
/*  95 */     getDataWatcher().addObject(13, Byte.valueOf((byte)0));
/*  96 */     getDataWatcher().addObject(14, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTotalArmorValue() {
/* 101 */     int i = super.getTotalArmorValue() + 2;
/*     */     
/* 103 */     if (i > 20)
/*     */     {
/* 105 */       i = 20;
/*     */     }
/*     */     
/* 108 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreakDoorsTaskSet() {
/* 113 */     return this.isBreakDoorsTaskSet;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBreakDoorsAItask(boolean par1) {
/* 118 */     if (this.isBreakDoorsTaskSet != par1) {
/*     */       
/* 120 */       this.isBreakDoorsTaskSet = par1;
/*     */       
/* 122 */       if (par1) {
/*     */         
/* 124 */         this.tasks.addTask(1, (EntityAIBase)this.breakDoor);
/*     */       }
/*     */       else {
/*     */         
/* 128 */         this.tasks.removeTask((EntityAIBase)this.breakDoor);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChild() {
/* 135 */     return (getDataWatcher().getWatchableObjectByte(12) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getExperiencePoints(EntityPlayer player) {
/* 140 */     if (isChild())
/*     */     {
/* 142 */       this.experienceValue = (int)(this.experienceValue * 2.5F);
/*     */     }
/*     */     
/* 145 */     return super.getExperiencePoints(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChild(boolean childZombie) {
/* 150 */     getDataWatcher().updateObject(12, Byte.valueOf((byte)(childZombie ? 1 : 0)));
/*     */     
/* 152 */     if (this.worldObj != null && !this.worldObj.isRemote) {
/*     */       
/* 154 */       IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 155 */       iattributeinstance.removeModifier(babySpeedBoostModifier);
/*     */       
/* 157 */       if (childZombie)
/*     */       {
/* 159 */         iattributeinstance.applyModifier(babySpeedBoostModifier);
/*     */       }
/*     */     } 
/*     */     
/* 163 */     setChildSize(childZombie);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVillager() {
/* 168 */     return (getDataWatcher().getWatchableObjectByte(13) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVillager(boolean villager) {
/* 173 */     getDataWatcher().updateObject(13, Byte.valueOf((byte)(villager ? 1 : 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 178 */     if (this.worldObj.isDaytime() && !this.worldObj.isRemote && !isChild()) {
/*     */       
/* 180 */       float f = getBrightness(1.0F);
/* 181 */       BlockPos blockpos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
/*     */       
/* 183 */       if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos)) {
/*     */         
/* 185 */         boolean flag = true;
/* 186 */         ItemStack itemstack = getEquipmentInSlot(4);
/*     */         
/* 188 */         if (itemstack != null) {
/*     */           
/* 190 */           if (itemstack.isItemStackDamageable()) {
/*     */             
/* 192 */             itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
/*     */             
/* 194 */             if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
/*     */               
/* 196 */               renderBrokenItemStack(itemstack);
/* 197 */               setCurrentItemOrArmor(4, (ItemStack)null);
/*     */             } 
/*     */           } 
/*     */           
/* 201 */           flag = false;
/*     */         } 
/*     */         
/* 204 */         if (flag)
/*     */         {
/* 206 */           setFire(8);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 211 */     if (isRiding() && getAttackTarget() != null && this.ridingEntity instanceof EntityChicken)
/*     */     {
/* 213 */       ((EntityLiving)this.ridingEntity).getNavigator().setPath(getNavigator().getPath(), 1.5D);
/*     */     }
/*     */     
/* 216 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 221 */     if (super.attackEntityFrom(source, amount)) {
/*     */       
/* 223 */       EntityLivingBase entitylivingbase = getAttackTarget();
/*     */       
/* 225 */       if (entitylivingbase == null && source.getEntity() instanceof EntityLivingBase)
/*     */       {
/* 227 */         entitylivingbase = (EntityLivingBase)source.getEntity();
/*     */       }
/*     */       
/* 230 */       if (entitylivingbase != null && this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.rand.nextFloat() < getEntityAttribute(reinforcementChance).getAttributeValue()) {
/*     */         
/* 232 */         int i = MathHelper.floor_double(this.posX);
/* 233 */         int j = MathHelper.floor_double(this.posY);
/* 234 */         int k = MathHelper.floor_double(this.posZ);
/* 235 */         EntityZombie entityzombie = new EntityZombie(this.worldObj);
/*     */         
/* 237 */         for (int l = 0; l < 50; l++) {
/*     */           
/* 239 */           int i1 = i + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/* 240 */           int j1 = j + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/* 241 */           int k1 = k + MathHelper.getRandomIntegerInRange(this.rand, 7, 40) * MathHelper.getRandomIntegerInRange(this.rand, -1, 1);
/*     */           
/* 243 */           if (World.doesBlockHaveSolidTopSurface((IBlockAccess)this.worldObj, new BlockPos(i1, j1 - 1, k1)) && this.worldObj.getLightFromNeighbors(new BlockPos(i1, j1, k1)) < 10) {
/*     */             
/* 245 */             entityzombie.setPosition(i1, j1, k1);
/*     */             
/* 247 */             if (!this.worldObj.isAnyPlayerWithinRangeAt(i1, j1, k1, 7.0D) && this.worldObj.checkNoEntityCollision(entityzombie.getEntityBoundingBox(), (Entity)entityzombie) && this.worldObj.getCollidingBoundingBoxes((Entity)entityzombie, entityzombie.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(entityzombie.getEntityBoundingBox())) {
/*     */               
/* 249 */               this.worldObj.spawnEntityInWorld((Entity)entityzombie);
/* 250 */               entityzombie.setAttackTarget(entitylivingbase);
/* 251 */               entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
/* 252 */               getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement caller charge", -0.05000000074505806D, 0));
/* 253 */               entityzombie.getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Zombie reinforcement callee charge", -0.05000000074505806D, 0));
/*     */               
/*     */               break;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/* 260 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 264 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 270 */     if (!this.worldObj.isRemote && isConverting()) {
/*     */       
/* 272 */       int i = getConversionTimeBoost();
/* 273 */       this.conversionTime -= i;
/*     */       
/* 275 */       if (this.conversionTime <= 0)
/*     */       {
/* 277 */         convertToVillager();
/*     */       }
/*     */     } 
/*     */     
/* 281 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 286 */     boolean flag = super.attackEntityAsMob(entityIn);
/*     */     
/* 288 */     if (flag) {
/*     */       
/* 290 */       int i = this.worldObj.getDifficulty().getDifficultyId();
/*     */       
/* 292 */       if (getHeldItem() == null && isBurning() && this.rand.nextFloat() < i * 0.3F)
/*     */       {
/* 294 */         entityIn.setFire(2 * i);
/*     */       }
/*     */     } 
/*     */     
/* 298 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 303 */     return "mob.zombie.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 308 */     return "mob.zombie.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 313 */     return "mob.zombie.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/* 318 */     playSound("mob.zombie.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 323 */     return Items.rotten_flesh;
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 328 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRandomDrop() {
/* 333 */     switch (this.rand.nextInt(3)) {
/*     */       
/*     */       case 0:
/* 336 */         dropItem(Items.iron_ingot, 1);
/*     */         break;
/*     */       
/*     */       case 1:
/* 340 */         dropItem(Items.carrot, 1);
/*     */         break;
/*     */       
/*     */       case 2:
/* 344 */         dropItem(Items.potato, 1);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
/* 350 */     super.setEquipmentBasedOnDifficulty(difficulty);
/*     */     
/* 352 */     if (this.rand.nextFloat() < ((this.worldObj.getDifficulty() == EnumDifficulty.HARD) ? 0.05F : 0.01F)) {
/*     */       
/* 354 */       int i = this.rand.nextInt(3);
/*     */       
/* 356 */       if (i == 0) {
/*     */         
/* 358 */         setCurrentItemOrArmor(0, new ItemStack(Items.iron_sword));
/*     */       }
/*     */       else {
/*     */         
/* 362 */         setCurrentItemOrArmor(0, new ItemStack(Items.iron_shovel));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 369 */     super.writeEntityToNBT(tagCompound);
/*     */     
/* 371 */     if (isChild())
/*     */     {
/* 373 */       tagCompound.setBoolean("IsBaby", true);
/*     */     }
/*     */     
/* 376 */     if (isVillager())
/*     */     {
/* 378 */       tagCompound.setBoolean("IsVillager", true);
/*     */     }
/*     */     
/* 381 */     tagCompound.setInteger("ConversionTime", isConverting() ? this.conversionTime : -1);
/* 382 */     tagCompound.setBoolean("CanBreakDoors", isBreakDoorsTaskSet());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 387 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 389 */     if (tagCompund.getBoolean("IsBaby"))
/*     */     {
/* 391 */       setChild(true);
/*     */     }
/*     */     
/* 394 */     if (tagCompund.getBoolean("IsVillager"))
/*     */     {
/* 396 */       setVillager(true);
/*     */     }
/*     */     
/* 399 */     if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1)
/*     */     {
/* 401 */       startConversion(tagCompund.getInteger("ConversionTime"));
/*     */     }
/*     */     
/* 404 */     setBreakDoorsAItask(tagCompund.getBoolean("CanBreakDoors"));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKillEntity(EntityLivingBase entityLivingIn) {
/* 409 */     super.onKillEntity(entityLivingIn);
/*     */     
/* 411 */     if ((this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) && entityLivingIn instanceof EntityVillager) {
/*     */       
/* 413 */       if (this.worldObj.getDifficulty() != EnumDifficulty.HARD && this.rand.nextBoolean()) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 418 */       EntityLiving entityliving = (EntityLiving)entityLivingIn;
/* 419 */       EntityZombie entityzombie = new EntityZombie(this.worldObj);
/* 420 */       entityzombie.copyLocationAndAnglesFrom((Entity)entityLivingIn);
/* 421 */       this.worldObj.removeEntity((Entity)entityLivingIn);
/* 422 */       entityzombie.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityzombie)), (IEntityLivingData)null);
/* 423 */       entityzombie.setVillager(true);
/*     */       
/* 425 */       if (entityLivingIn.isChild())
/*     */       {
/* 427 */         entityzombie.setChild(true);
/*     */       }
/*     */       
/* 430 */       entityzombie.setNoAI(entityliving.isAIDisabled());
/*     */       
/* 432 */       if (entityliving.hasCustomName()) {
/*     */         
/* 434 */         entityzombie.setCustomNameTag(entityliving.getCustomNameTag());
/* 435 */         entityzombie.setAlwaysRenderNameTag(entityliving.getAlwaysRenderNameTag());
/*     */       } 
/*     */       
/* 438 */       this.worldObj.spawnEntityInWorld((Entity)entityzombie);
/* 439 */       this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1016, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 445 */     float f = 1.74F;
/*     */     
/* 447 */     if (isChild())
/*     */     {
/* 449 */       f = (float)(f - 0.81D);
/*     */     }
/*     */     
/* 452 */     return f;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean func_175448_a(ItemStack stack) {
/* 457 */     return (stack.getItem() == Items.egg && isChild() && isRiding()) ? false : super.func_175448_a(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 462 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 463 */     float f = difficulty.getClampedAdditionalDifficulty();
/* 464 */     setCanPickUpLoot((this.rand.nextFloat() < 0.55F * f));
/*     */     
/* 466 */     if (livingdata == null)
/*     */     {
/* 468 */       livingdata = new GroupData((this.worldObj.rand.nextFloat() < 0.05F), (this.worldObj.rand.nextFloat() < 0.05F));
/*     */     }
/*     */     
/* 471 */     if (livingdata instanceof GroupData) {
/*     */       
/* 473 */       GroupData entityzombie$groupdata = (GroupData)livingdata;
/*     */       
/* 475 */       if (entityzombie$groupdata.isVillager)
/*     */       {
/* 477 */         setVillager(true);
/*     */       }
/*     */       
/* 480 */       if (entityzombie$groupdata.isChild) {
/*     */         
/* 482 */         setChild(true);
/*     */         
/* 484 */         if (this.worldObj.rand.nextFloat() < 0.05D) {
/*     */           
/* 486 */           List<EntityChicken> list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, getEntityBoundingBox().expand(5.0D, 3.0D, 5.0D), EntitySelectors.IS_STANDALONE);
/*     */           
/* 488 */           if (!list.isEmpty())
/*     */           {
/* 490 */             EntityChicken entitychicken = list.get(0);
/* 491 */             entitychicken.setChickenJockey(true);
/* 492 */             mountEntity((Entity)entitychicken);
/*     */           }
/*     */         
/* 495 */         } else if (this.worldObj.rand.nextFloat() < 0.05D) {
/*     */           
/* 497 */           EntityChicken entitychicken1 = new EntityChicken(this.worldObj);
/* 498 */           entitychicken1.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 499 */           entitychicken1.onInitialSpawn(difficulty, (IEntityLivingData)null);
/* 500 */           entitychicken1.setChickenJockey(true);
/* 501 */           this.worldObj.spawnEntityInWorld((Entity)entitychicken1);
/* 502 */           mountEntity((Entity)entitychicken1);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 507 */     setBreakDoorsAItask((this.rand.nextFloat() < f * 0.1F));
/* 508 */     setEquipmentBasedOnDifficulty(difficulty);
/* 509 */     setEnchantmentBasedOnDifficulty(difficulty);
/*     */     
/* 511 */     if (getEquipmentInSlot(4) == null) {
/*     */       
/* 513 */       Calendar calendar = this.worldObj.getCurrentDate();
/*     */       
/* 515 */       if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
/*     */         
/* 517 */         setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.lit_pumpkin : Blocks.pumpkin));
/* 518 */         this.equipmentDropChances[4] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 522 */     getEntityAttribute(SharedMonsterAttributes.knockbackResistance).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextDouble() * 0.05000000074505806D, 0));
/* 523 */     double d0 = this.rand.nextDouble() * 1.5D * f;
/*     */     
/* 525 */     if (d0 > 1.0D)
/*     */     {
/* 527 */       getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random zombie-spawn bonus", d0, 2));
/*     */     }
/*     */     
/* 530 */     if (this.rand.nextFloat() < f * 0.05F) {
/*     */       
/* 532 */       getEntityAttribute(reinforcementChance).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 0.25D + 0.5D, 0));
/* 533 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).applyModifier(new AttributeModifier("Leader zombie bonus", this.rand.nextDouble() * 3.0D + 1.0D, 2));
/* 534 */       setBreakDoorsAItask(true);
/*     */     } 
/*     */     
/* 537 */     return livingdata;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 542 */     ItemStack itemstack = player.getCurrentEquippedItem();
/*     */     
/* 544 */     if (itemstack != null && itemstack.getItem() == Items.golden_apple && itemstack.getMetadata() == 0 && isVillager() && isPotionActive(Potion.weakness)) {
/*     */       
/* 546 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 548 */         itemstack.stackSize--;
/*     */       }
/*     */       
/* 551 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 553 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */       
/* 556 */       if (!this.worldObj.isRemote)
/*     */       {
/* 558 */         startConversion(this.rand.nextInt(2401) + 3600);
/*     */       }
/*     */       
/* 561 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 565 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void startConversion(int ticks) {
/* 571 */     this.conversionTime = ticks;
/* 572 */     getDataWatcher().updateObject(14, Byte.valueOf((byte)1));
/* 573 */     removePotionEffect(Potion.weakness.id);
/* 574 */     addPotionEffect(new PotionEffect(Potion.damageBoost.id, ticks, Math.min(this.worldObj.getDifficulty().getDifficultyId() - 1, 0)));
/* 575 */     this.worldObj.setEntityState((Entity)this, (byte)16);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 580 */     if (id == 16) {
/*     */       
/* 582 */       if (!isSilent())
/*     */       {
/* 584 */         this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.zombie.remedy", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 589 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDespawn() {
/* 595 */     return !isConverting();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isConverting() {
/* 600 */     return (getDataWatcher().getWatchableObjectByte(14) == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void convertToVillager() {
/* 605 */     EntityVillager entityvillager = new EntityVillager(this.worldObj);
/* 606 */     entityvillager.copyLocationAndAnglesFrom((Entity)this);
/* 607 */     entityvillager.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos((Entity)entityvillager)), (IEntityLivingData)null);
/* 608 */     entityvillager.setLookingForHome();
/*     */     
/* 610 */     if (isChild())
/*     */     {
/* 612 */       entityvillager.setGrowingAge(-24000);
/*     */     }
/*     */     
/* 615 */     this.worldObj.removeEntity((Entity)this);
/* 616 */     entityvillager.setNoAI(isAIDisabled());
/*     */     
/* 618 */     if (hasCustomName()) {
/*     */       
/* 620 */       entityvillager.setCustomNameTag(getCustomNameTag());
/* 621 */       entityvillager.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
/*     */     } 
/*     */     
/* 624 */     this.worldObj.spawnEntityInWorld((Entity)entityvillager);
/* 625 */     entityvillager.addPotionEffect(new PotionEffect(Potion.confusion.id, 200, 0));
/* 626 */     this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1017, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getConversionTimeBoost() {
/* 631 */     int i = 1;
/*     */     
/* 633 */     if (this.rand.nextFloat() < 0.01F) {
/*     */       
/* 635 */       int j = 0;
/* 636 */       BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
/*     */       
/* 638 */       for (int k = (int)this.posX - 4; k < (int)this.posX + 4 && j < 14; k++) {
/*     */         
/* 640 */         for (int l = (int)this.posY - 4; l < (int)this.posY + 4 && j < 14; l++) {
/*     */           
/* 642 */           for (int i1 = (int)this.posZ - 4; i1 < (int)this.posZ + 4 && j < 14; i1++) {
/*     */             
/* 644 */             Block block = this.worldObj.getBlockState((BlockPos)blockpos$mutableblockpos.set(k, l, i1)).getBlock();
/*     */             
/* 646 */             if (block == Blocks.iron_bars || block == Blocks.bed) {
/*     */               
/* 648 */               if (this.rand.nextFloat() < 0.3F)
/*     */               {
/* 650 */                 i++;
/*     */               }
/*     */               
/* 653 */               j++;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 660 */     return i;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setChildSize(boolean isChild) {
/* 665 */     multiplySize(isChild ? 0.5F : 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void setSize(float width, float height) {
/* 670 */     boolean flag = (this.zombieWidth > 0.0F && this.zombieHeight > 0.0F);
/* 671 */     this.zombieWidth = width;
/* 672 */     this.zombieHeight = height;
/*     */     
/* 674 */     if (!flag)
/*     */     {
/* 676 */       multiplySize(1.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected final void multiplySize(float size) {
/* 682 */     super.setSize(this.zombieWidth * size, this.zombieHeight * size);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYOffset() {
/* 687 */     return isChild() ? 0.0D : -0.35D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 692 */     super.onDeath(cause);
/*     */     
/* 694 */     if (cause.getEntity() instanceof EntityCreeper && !(this instanceof EntityPigZombie) && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
/*     */       
/* 696 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 697 */       entityDropItem(new ItemStack(Items.skull, 1, 2), 0.0F);
/*     */     } 
/*     */   }
/*     */   
/*     */   class GroupData
/*     */     implements IEntityLivingData
/*     */   {
/*     */     public boolean isChild;
/*     */     public boolean isVillager;
/*     */     
/*     */     private GroupData(boolean isBaby, boolean isVillagerZombie) {
/* 708 */       this.isChild = false;
/* 709 */       this.isVillager = false;
/* 710 */       this.isChild = isBaby;
/* 711 */       this.isVillager = isVillagerZombie;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */