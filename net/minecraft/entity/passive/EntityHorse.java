/*      */ package net.minecraft.entity.passive;
/*      */ import com.google.common.base.Predicate;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityAgeable;
/*      */ import net.minecraft.entity.EntityCreature;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.IEntityLivingData;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.EntityAIBase;
/*      */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*      */ import net.minecraft.entity.ai.EntityAILookIdle;
/*      */ import net.minecraft.entity.ai.EntityAIMate;
/*      */ import net.minecraft.entity.ai.EntityAIPanic;
/*      */ import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
/*      */ import net.minecraft.entity.ai.EntityAIWander;
/*      */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*      */ import net.minecraft.entity.ai.attributes.IAttribute;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.ai.attributes.RangedAttribute;
/*      */ import net.minecraft.entity.player.EntityPlayer;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.AnimalChest;
/*      */ import net.minecraft.inventory.IInvBasic;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryBasic;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.StatCollector;
/*      */ import net.minecraft.world.DifficultyInstance;
/*      */ import net.minecraft.world.World;
/*      */ 
/*      */ public class EntityHorse extends EntityAnimal implements IInvBasic {
/*   45 */   private static final Predicate<Entity> horseBreedingSelector = new Predicate<Entity>()
/*      */     {
/*      */       public boolean apply(Entity p_apply_1_)
/*      */       {
/*   49 */         return (p_apply_1_ instanceof EntityHorse && ((EntityHorse)p_apply_1_).isBreeding());
/*      */       }
/*      */     };
/*   52 */   private static final IAttribute horseJumpStrength = (IAttribute)(new RangedAttribute((IAttribute)null, "horse.jumpStrength", 0.7D, 0.0D, 2.0D)).setDescription("Jump Strength").setShouldWatch(true);
/*   53 */   private static final String[] horseArmorTextures = new String[] { null, "textures/entity/horse/armor/horse_armor_iron.png", "textures/entity/horse/armor/horse_armor_gold.png", "textures/entity/horse/armor/horse_armor_diamond.png" };
/*   54 */   private static final String[] HORSE_ARMOR_TEXTURES_ABBR = new String[] { "", "meo", "goo", "dio" };
/*   55 */   private static final int[] armorValues = new int[] { 0, 5, 7, 11 };
/*   56 */   private static final String[] horseTextures = new String[] { "textures/entity/horse/horse_white.png", "textures/entity/horse/horse_creamy.png", "textures/entity/horse/horse_chestnut.png", "textures/entity/horse/horse_brown.png", "textures/entity/horse/horse_black.png", "textures/entity/horse/horse_gray.png", "textures/entity/horse/horse_darkbrown.png" };
/*   57 */   private static final String[] HORSE_TEXTURES_ABBR = new String[] { "hwh", "hcr", "hch", "hbr", "hbl", "hgr", "hdb" };
/*   58 */   private static final String[] horseMarkingTextures = new String[] { null, "textures/entity/horse/horse_markings_white.png", "textures/entity/horse/horse_markings_whitefield.png", "textures/entity/horse/horse_markings_whitedots.png", "textures/entity/horse/horse_markings_blackdots.png" };
/*   59 */   private static final String[] HORSE_MARKING_TEXTURES_ABBR = new String[] { "", "wo_", "wmo", "wdo", "bdo" };
/*      */   private int eatingHaystackCounter;
/*      */   private int openMouthCounter;
/*      */   private int jumpRearingCounter;
/*      */   public int field_110278_bp;
/*      */   public int field_110279_bq;
/*      */   protected boolean horseJumping;
/*      */   private AnimalChest horseChest;
/*      */   private boolean hasReproduced;
/*      */   protected int temper;
/*      */   protected float jumpPower;
/*      */   private boolean field_110294_bI;
/*      */   private float headLean;
/*      */   private float prevHeadLean;
/*      */   private float rearingAmount;
/*      */   private float prevRearingAmount;
/*      */   private float mouthOpenness;
/*      */   private float prevMouthOpenness;
/*      */   private int gallopTime;
/*      */   private String texturePrefix;
/*   79 */   private String[] horseTexturesArray = new String[3];
/*      */   
/*      */   private boolean field_175508_bO = false;
/*      */   
/*      */   public EntityHorse(World worldIn) {
/*   84 */     super(worldIn);
/*   85 */     setSize(1.4F, 1.6F);
/*   86 */     this.isImmuneToFire = false;
/*   87 */     setChested(false);
/*   88 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*   89 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*   90 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.2D));
/*   91 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIRunAroundLikeCrazy(this, 1.2D));
/*   92 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMate(this, 1.0D));
/*   93 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIFollowParent(this, 1.0D));
/*   94 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.7D));
/*   95 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*   96 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*   97 */     initHorseChest();
/*      */   }
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  102 */     super.entityInit();
/*  103 */     this.dataWatcher.addObject(16, Integer.valueOf(0));
/*  104 */     this.dataWatcher.addObject(19, Byte.valueOf((byte)0));
/*  105 */     this.dataWatcher.addObject(20, Integer.valueOf(0));
/*  106 */     this.dataWatcher.addObject(21, String.valueOf(""));
/*  107 */     this.dataWatcher.addObject(22, Integer.valueOf(0));
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseType(int type) {
/*  112 */     this.dataWatcher.updateObject(19, Byte.valueOf((byte)type));
/*  113 */     resetTexturePrefix();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHorseType() {
/*  118 */     return this.dataWatcher.getWatchableObjectByte(19);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseVariant(int variant) {
/*  123 */     this.dataWatcher.updateObject(20, Integer.valueOf(variant));
/*  124 */     resetTexturePrefix();
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHorseVariant() {
/*  129 */     return this.dataWatcher.getWatchableObjectInt(20);
/*      */   }
/*      */ 
/*      */   
/*      */   public String getName() {
/*  134 */     if (hasCustomName())
/*      */     {
/*  136 */       return getCustomNameTag();
/*      */     }
/*      */ 
/*      */     
/*  140 */     int i = getHorseType();
/*      */     
/*  142 */     switch (i) {
/*      */ 
/*      */       
/*      */       default:
/*  146 */         return StatCollector.translateToLocal("entity.horse.name");
/*      */       
/*      */       case 1:
/*  149 */         return StatCollector.translateToLocal("entity.donkey.name");
/*      */       
/*      */       case 2:
/*  152 */         return StatCollector.translateToLocal("entity.mule.name");
/*      */       
/*      */       case 3:
/*  155 */         return StatCollector.translateToLocal("entity.zombiehorse.name");
/*      */       case 4:
/*      */         break;
/*  158 */     }  return StatCollector.translateToLocal("entity.skeletonhorse.name");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private boolean getHorseWatchableBoolean(int p_110233_1_) {
/*  165 */     return ((this.dataWatcher.getWatchableObjectInt(16) & p_110233_1_) != 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void setHorseWatchableBoolean(int p_110208_1_, boolean p_110208_2_) {
/*  170 */     int i = this.dataWatcher.getWatchableObjectInt(16);
/*      */     
/*  172 */     if (p_110208_2_) {
/*      */       
/*  174 */       this.dataWatcher.updateObject(16, Integer.valueOf(i | p_110208_1_));
/*      */     }
/*      */     else {
/*      */       
/*  178 */       this.dataWatcher.updateObject(16, Integer.valueOf(i & (p_110208_1_ ^ 0xFFFFFFFF)));
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAdultHorse() {
/*  184 */     return !isChild();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isTame() {
/*  189 */     return getHorseWatchableBoolean(2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean func_110253_bW() {
/*  194 */     return isAdultHorse();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getOwnerId() {
/*  199 */     return this.dataWatcher.getWatchableObjectString(21);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setOwnerId(String id) {
/*  204 */     this.dataWatcher.updateObject(21, id);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getHorseSize() {
/*  209 */     return 0.5F;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setScaleForAge(boolean p_98054_1_) {
/*  214 */     if (p_98054_1_) {
/*      */       
/*  216 */       setScale(getHorseSize());
/*      */     }
/*      */     else {
/*      */       
/*  220 */       setScale(1.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHorseJumping() {
/*  226 */     return this.horseJumping;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseTamed(boolean tamed) {
/*  231 */     setHorseWatchableBoolean(2, tamed);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseJumping(boolean jumping) {
/*  236 */     this.horseJumping = jumping;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean allowLeashing() {
/*  241 */     return (!isUndead() && super.allowLeashing());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void func_142017_o(float p_142017_1_) {
/*  246 */     if (p_142017_1_ > 6.0F && isEatingHaystack())
/*      */     {
/*  248 */       setEatingHaystack(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isChested() {
/*  254 */     return getHorseWatchableBoolean(8);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getHorseArmorIndexSynced() {
/*  259 */     return this.dataWatcher.getWatchableObjectInt(22);
/*      */   }
/*      */ 
/*      */   
/*      */   private int getHorseArmorIndex(ItemStack itemStackIn) {
/*  264 */     if (itemStackIn == null)
/*      */     {
/*  266 */       return 0;
/*      */     }
/*      */ 
/*      */     
/*  270 */     Item item = itemStackIn.getItem();
/*  271 */     return (item == Items.iron_horse_armor) ? 1 : ((item == Items.golden_horse_armor) ? 2 : ((item == Items.diamond_horse_armor) ? 3 : 0));
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isEatingHaystack() {
/*  277 */     return getHorseWatchableBoolean(32);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isRearing() {
/*  282 */     return getHorseWatchableBoolean(64);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBreeding() {
/*  287 */     return getHorseWatchableBoolean(16);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getHasReproduced() {
/*  292 */     return this.hasReproduced;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseArmorStack(ItemStack itemStackIn) {
/*  297 */     this.dataWatcher.updateObject(22, Integer.valueOf(getHorseArmorIndex(itemStackIn)));
/*  298 */     resetTexturePrefix();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setBreeding(boolean breeding) {
/*  303 */     setHorseWatchableBoolean(16, breeding);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setChested(boolean chested) {
/*  308 */     setHorseWatchableBoolean(8, chested);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHasReproduced(boolean hasReproducedIn) {
/*  313 */     this.hasReproduced = hasReproducedIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setHorseSaddled(boolean saddled) {
/*  318 */     setHorseWatchableBoolean(4, saddled);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTemper() {
/*  323 */     return this.temper;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setTemper(int temperIn) {
/*  328 */     this.temper = temperIn;
/*      */   }
/*      */ 
/*      */   
/*      */   public int increaseTemper(int p_110198_1_) {
/*  333 */     int i = MathHelper.clamp_int(getTemper() + p_110198_1_, 0, getMaxTemper());
/*  334 */     setTemper(i);
/*  335 */     return i;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  340 */     Entity entity = source.getEntity();
/*  341 */     return (this.riddenByEntity != null && this.riddenByEntity.equals(entity)) ? false : super.attackEntityFrom(source, amount);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTotalArmorValue() {
/*  346 */     return armorValues[getHorseArmorIndexSynced()];
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canBePushed() {
/*  351 */     return (this.riddenByEntity == null);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean prepareChunkForSpawn() {
/*  356 */     int i = MathHelper.floor_double(this.posX);
/*  357 */     int j = MathHelper.floor_double(this.posZ);
/*  358 */     this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, j));
/*  359 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void dropChests() {
/*  364 */     if (!this.worldObj.isRemote && isChested()) {
/*      */       
/*  366 */       dropItem(Item.getItemFromBlock((Block)Blocks.chest), 1);
/*  367 */       setChested(false);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void func_110266_cB() {
/*  373 */     openHorseMouth();
/*      */     
/*  375 */     if (!isSilent())
/*      */     {
/*  377 */       this.worldObj.playSoundAtEntity((Entity)this, "eating", 1.0F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void fall(float distance, float damageMultiplier) {
/*  383 */     if (distance > 1.0F)
/*      */     {
/*  385 */       playSound("mob.horse.land", 0.4F, 1.0F);
/*      */     }
/*      */     
/*  388 */     int i = MathHelper.ceiling_float_int((distance * 0.5F - 3.0F) * damageMultiplier);
/*      */     
/*  390 */     if (i > 0) {
/*      */       
/*  392 */       attackEntityFrom(DamageSource.fall, i);
/*      */       
/*  394 */       if (this.riddenByEntity != null)
/*      */       {
/*  396 */         this.riddenByEntity.attackEntityFrom(DamageSource.fall, i);
/*      */       }
/*      */       
/*  399 */       Block block = this.worldObj.getBlockState(new BlockPos(this.posX, this.posY - 0.2D - this.prevRotationYaw, this.posZ)).getBlock();
/*      */       
/*  401 */       if (block.getMaterial() != Material.air && !isSilent()) {
/*      */         
/*  403 */         Block.SoundType block$soundtype = block.stepSound;
/*  404 */         this.worldObj.playSoundAtEntity((Entity)this, block$soundtype.getStepSound(), block$soundtype.getVolume() * 0.5F, block$soundtype.getFrequency() * 0.75F);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getChestSize() {
/*  411 */     int i = getHorseType();
/*  412 */     return (!isChested() || (i != 1 && i != 2)) ? 2 : 17;
/*      */   }
/*      */ 
/*      */   
/*      */   private void initHorseChest() {
/*  417 */     AnimalChest animalchest = this.horseChest;
/*  418 */     this.horseChest = new AnimalChest("HorseChest", getChestSize());
/*  419 */     this.horseChest.setCustomName(getName());
/*      */     
/*  421 */     if (animalchest != null) {
/*      */       
/*  423 */       animalchest.removeInventoryChangeListener(this);
/*  424 */       int i = Math.min(animalchest.getSizeInventory(), this.horseChest.getSizeInventory());
/*      */       
/*  426 */       for (int j = 0; j < i; j++) {
/*      */         
/*  428 */         ItemStack itemstack = animalchest.getStackInSlot(j);
/*      */         
/*  430 */         if (itemstack != null)
/*      */         {
/*  432 */           this.horseChest.setInventorySlotContents(j, itemstack.copy());
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  437 */     this.horseChest.addInventoryChangeListener(this);
/*  438 */     updateHorseSlots();
/*      */   }
/*      */ 
/*      */   
/*      */   private void updateHorseSlots() {
/*  443 */     if (!this.worldObj.isRemote) {
/*      */       
/*  445 */       setHorseSaddled((this.horseChest.getStackInSlot(0) != null));
/*      */       
/*  447 */       if (canWearArmor())
/*      */       {
/*  449 */         setHorseArmorStack(this.horseChest.getStackInSlot(1));
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onInventoryChanged(InventoryBasic p_76316_1_) {
/*  456 */     int i = getHorseArmorIndexSynced();
/*  457 */     boolean flag = isHorseSaddled();
/*  458 */     updateHorseSlots();
/*      */     
/*  460 */     if (this.ticksExisted > 20) {
/*      */       
/*  462 */       if (i == 0 && i != getHorseArmorIndexSynced()) {
/*      */         
/*  464 */         playSound("mob.horse.armor", 0.5F, 1.0F);
/*      */       }
/*  466 */       else if (i != getHorseArmorIndexSynced()) {
/*      */         
/*  468 */         playSound("mob.horse.armor", 0.5F, 1.0F);
/*      */       } 
/*      */       
/*  471 */       if (!flag && isHorseSaddled())
/*      */       {
/*  473 */         playSound("mob.horse.leather", 0.5F, 1.0F);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getCanSpawnHere() {
/*  480 */     prepareChunkForSpawn();
/*  481 */     return super.getCanSpawnHere();
/*      */   }
/*      */ 
/*      */   
/*      */   protected EntityHorse getClosestHorse(Entity entityIn, double distance) {
/*  486 */     double d0 = Double.MAX_VALUE;
/*  487 */     Entity entity = null;
/*      */     
/*  489 */     for (Entity entity1 : this.worldObj.getEntitiesInAABBexcluding(entityIn, entityIn.getEntityBoundingBox().addCoord(distance, distance, distance), horseBreedingSelector)) {
/*      */       
/*  491 */       double d1 = entity1.getDistanceSq(entityIn.posX, entityIn.posY, entityIn.posZ);
/*      */       
/*  493 */       if (d1 < d0) {
/*      */         
/*  495 */         entity = entity1;
/*  496 */         d0 = d1;
/*      */       } 
/*      */     } 
/*      */     
/*  500 */     return (EntityHorse)entity;
/*      */   }
/*      */ 
/*      */   
/*      */   public double getHorseJumpStrength() {
/*  505 */     return getEntityAttribute(horseJumpStrength).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getDeathSound() {
/*  510 */     openHorseMouth();
/*  511 */     int i = getHorseType();
/*  512 */     return (i == 3) ? "mob.horse.zombie.death" : ((i == 4) ? "mob.horse.skeleton.death" : ((i != 1 && i != 2) ? "mob.horse.death" : "mob.horse.donkey.death"));
/*      */   }
/*      */ 
/*      */   
/*      */   protected Item getDropItem() {
/*  517 */     boolean flag = (this.rand.nextInt(4) == 0);
/*  518 */     int i = getHorseType();
/*  519 */     return (i == 4) ? Items.bone : ((i == 3) ? (flag ? null : Items.rotten_flesh) : Items.leather);
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getHurtSound() {
/*  524 */     openHorseMouth();
/*      */     
/*  526 */     if (this.rand.nextInt(3) == 0)
/*      */     {
/*  528 */       makeHorseRear();
/*      */     }
/*      */     
/*  531 */     int i = getHorseType();
/*  532 */     return (i == 3) ? "mob.horse.zombie.hit" : ((i == 4) ? "mob.horse.skeleton.hit" : ((i != 1 && i != 2) ? "mob.horse.hit" : "mob.horse.donkey.hit"));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isHorseSaddled() {
/*  537 */     return getHorseWatchableBoolean(4);
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getLivingSound() {
/*  542 */     openHorseMouth();
/*      */     
/*  544 */     if (this.rand.nextInt(10) == 0 && !isMovementBlocked())
/*      */     {
/*  546 */       makeHorseRear();
/*      */     }
/*      */     
/*  549 */     int i = getHorseType();
/*  550 */     return (i == 3) ? "mob.horse.zombie.idle" : ((i == 4) ? "mob.horse.skeleton.idle" : ((i != 1 && i != 2) ? "mob.horse.idle" : "mob.horse.donkey.idle"));
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getAngrySoundName() {
/*  555 */     openHorseMouth();
/*  556 */     makeHorseRear();
/*  557 */     int i = getHorseType();
/*  558 */     return (i != 3 && i != 4) ? ((i != 1 && i != 2) ? "mob.horse.angry" : "mob.horse.donkey.angry") : null;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  563 */     Block.SoundType block$soundtype = blockIn.stepSound;
/*      */     
/*  565 */     if (this.worldObj.getBlockState(pos.up()).getBlock() == Blocks.snow_layer)
/*      */     {
/*  567 */       block$soundtype = Blocks.snow_layer.stepSound;
/*      */     }
/*      */     
/*  570 */     if (!blockIn.getMaterial().isLiquid()) {
/*      */       
/*  572 */       int i = getHorseType();
/*      */       
/*  574 */       if (this.riddenByEntity != null && i != 1 && i != 2) {
/*      */         
/*  576 */         this.gallopTime++;
/*      */         
/*  578 */         if (this.gallopTime > 5 && this.gallopTime % 3 == 0)
/*      */         {
/*  580 */           playSound("mob.horse.gallop", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */           
/*  582 */           if (i == 0 && this.rand.nextInt(10) == 0)
/*      */           {
/*  584 */             playSound("mob.horse.breathe", block$soundtype.getVolume() * 0.6F, block$soundtype.getFrequency());
/*      */           }
/*      */         }
/*  587 */         else if (this.gallopTime <= 5)
/*      */         {
/*  589 */           playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */         }
/*      */       
/*  592 */       } else if (block$soundtype == Block.soundTypeWood) {
/*      */         
/*  594 */         playSound("mob.horse.wood", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */       }
/*      */       else {
/*      */         
/*  598 */         playSound("mob.horse.soft", block$soundtype.getVolume() * 0.15F, block$soundtype.getFrequency());
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*  605 */     super.applyEntityAttributes();
/*  606 */     getAttributeMap().registerAttribute(horseJumpStrength);
/*  607 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(53.0D);
/*  608 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.22499999403953552D);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxSpawnedInChunk() {
/*  613 */     return 6;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxTemper() {
/*  618 */     return 100;
/*      */   }
/*      */ 
/*      */   
/*      */   protected float getSoundVolume() {
/*  623 */     return 0.8F;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTalkInterval() {
/*  628 */     return 400;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean func_110239_cn() {
/*  633 */     return (getHorseType() == 0 || getHorseArmorIndexSynced() > 0);
/*      */   }
/*      */ 
/*      */   
/*      */   private void resetTexturePrefix() {
/*  638 */     this.texturePrefix = null;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean func_175507_cI() {
/*  643 */     return this.field_175508_bO;
/*      */   }
/*      */ 
/*      */   
/*      */   private void setHorseTexturePaths() {
/*  648 */     this.texturePrefix = "horse/";
/*  649 */     this.horseTexturesArray[0] = null;
/*  650 */     this.horseTexturesArray[1] = null;
/*  651 */     this.horseTexturesArray[2] = null;
/*  652 */     int i = getHorseType();
/*  653 */     int j = getHorseVariant();
/*      */     
/*  655 */     if (i == 0) {
/*      */       
/*  657 */       int k = j & 0xFF;
/*  658 */       int l = (j & 0xFF00) >> 8;
/*      */       
/*  660 */       if (k >= horseTextures.length) {
/*      */         
/*  662 */         this.field_175508_bO = false;
/*      */         
/*      */         return;
/*      */       } 
/*  666 */       this.horseTexturesArray[0] = horseTextures[k];
/*  667 */       this.texturePrefix += HORSE_TEXTURES_ABBR[k];
/*      */       
/*  669 */       if (l >= horseMarkingTextures.length) {
/*      */         
/*  671 */         this.field_175508_bO = false;
/*      */         
/*      */         return;
/*      */       } 
/*  675 */       this.horseTexturesArray[1] = horseMarkingTextures[l];
/*  676 */       this.texturePrefix += HORSE_MARKING_TEXTURES_ABBR[l];
/*      */     }
/*      */     else {
/*      */       
/*  680 */       this.horseTexturesArray[0] = "";
/*  681 */       this.texturePrefix += "_" + i + "_";
/*      */     } 
/*      */     
/*  684 */     int i1 = getHorseArmorIndexSynced();
/*      */     
/*  686 */     if (i1 >= horseArmorTextures.length) {
/*      */       
/*  688 */       this.field_175508_bO = false;
/*      */     }
/*      */     else {
/*      */       
/*  692 */       this.horseTexturesArray[2] = horseArmorTextures[i1];
/*  693 */       this.texturePrefix += HORSE_ARMOR_TEXTURES_ABBR[i1];
/*  694 */       this.field_175508_bO = true;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public String getHorseTexture() {
/*  700 */     if (this.texturePrefix == null)
/*      */     {
/*  702 */       setHorseTexturePaths();
/*      */     }
/*      */     
/*  705 */     return this.texturePrefix;
/*      */   }
/*      */ 
/*      */   
/*      */   public String[] getVariantTexturePaths() {
/*  710 */     if (this.texturePrefix == null)
/*      */     {
/*  712 */       setHorseTexturePaths();
/*      */     }
/*      */     
/*  715 */     return this.horseTexturesArray;
/*      */   }
/*      */ 
/*      */   
/*      */   public void openGUI(EntityPlayer playerEntity) {
/*  720 */     if (!this.worldObj.isRemote && (this.riddenByEntity == null || this.riddenByEntity == playerEntity) && isTame()) {
/*      */       
/*  722 */       this.horseChest.setCustomName(getName());
/*  723 */       playerEntity.displayGUIHorse(this, (IInventory)this.horseChest);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean interact(EntityPlayer player) {
/*  729 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*      */     
/*  731 */     if (itemstack != null && itemstack.getItem() == Items.spawn_egg)
/*      */     {
/*  733 */       return super.interact(player);
/*      */     }
/*  735 */     if (!isTame() && isUndead())
/*      */     {
/*  737 */       return false;
/*      */     }
/*  739 */     if (isTame() && isAdultHorse() && player.isSneaking()) {
/*      */       
/*  741 */       openGUI(player);
/*  742 */       return true;
/*      */     } 
/*  744 */     if (func_110253_bW() && this.riddenByEntity != null)
/*      */     {
/*  746 */       return super.interact(player);
/*      */     }
/*      */ 
/*      */     
/*  750 */     if (itemstack != null) {
/*      */       
/*  752 */       boolean flag = false;
/*      */       
/*  754 */       if (canWearArmor()) {
/*      */         
/*  756 */         int i = -1;
/*      */         
/*  758 */         if (itemstack.getItem() == Items.iron_horse_armor) {
/*      */           
/*  760 */           i = 1;
/*      */         }
/*  762 */         else if (itemstack.getItem() == Items.golden_horse_armor) {
/*      */           
/*  764 */           i = 2;
/*      */         }
/*  766 */         else if (itemstack.getItem() == Items.diamond_horse_armor) {
/*      */           
/*  768 */           i = 3;
/*      */         } 
/*      */         
/*  771 */         if (i >= 0) {
/*      */           
/*  773 */           if (!isTame()) {
/*      */             
/*  775 */             makeHorseRearWithSound();
/*  776 */             return true;
/*      */           } 
/*      */           
/*  779 */           openGUI(player);
/*  780 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/*  784 */       if (!flag && !isUndead()) {
/*      */         
/*  786 */         float f = 0.0F;
/*  787 */         int j = 0;
/*  788 */         int k = 0;
/*      */         
/*  790 */         if (itemstack.getItem() == Items.wheat) {
/*      */           
/*  792 */           f = 2.0F;
/*  793 */           j = 20;
/*  794 */           k = 3;
/*      */         }
/*  796 */         else if (itemstack.getItem() == Items.sugar) {
/*      */           
/*  798 */           f = 1.0F;
/*  799 */           j = 30;
/*  800 */           k = 3;
/*      */         }
/*  802 */         else if (Block.getBlockFromItem(itemstack.getItem()) == Blocks.hay_block) {
/*      */           
/*  804 */           f = 20.0F;
/*  805 */           j = 180;
/*      */         }
/*  807 */         else if (itemstack.getItem() == Items.apple) {
/*      */           
/*  809 */           f = 3.0F;
/*  810 */           j = 60;
/*  811 */           k = 3;
/*      */         }
/*  813 */         else if (itemstack.getItem() == Items.golden_carrot) {
/*      */           
/*  815 */           f = 4.0F;
/*  816 */           j = 60;
/*  817 */           k = 5;
/*      */           
/*  819 */           if (isTame() && getGrowingAge() == 0)
/*      */           {
/*  821 */             flag = true;
/*  822 */             setInLove(player);
/*      */           }
/*      */         
/*  825 */         } else if (itemstack.getItem() == Items.golden_apple) {
/*      */           
/*  827 */           f = 10.0F;
/*  828 */           j = 240;
/*  829 */           k = 10;
/*      */           
/*  831 */           if (isTame() && getGrowingAge() == 0) {
/*      */             
/*  833 */             flag = true;
/*  834 */             setInLove(player);
/*      */           } 
/*      */         } 
/*      */         
/*  838 */         if (getHealth() < getMaxHealth() && f > 0.0F) {
/*      */           
/*  840 */           heal(f);
/*  841 */           flag = true;
/*      */         } 
/*      */         
/*  844 */         if (!isAdultHorse() && j > 0) {
/*      */           
/*  846 */           addGrowth(j);
/*  847 */           flag = true;
/*      */         } 
/*      */         
/*  850 */         if (k > 0 && (flag || !isTame()) && k < getMaxTemper()) {
/*      */           
/*  852 */           flag = true;
/*  853 */           increaseTemper(k);
/*      */         } 
/*      */         
/*  856 */         if (flag)
/*      */         {
/*  858 */           func_110266_cB();
/*      */         }
/*      */       } 
/*      */       
/*  862 */       if (!isTame() && !flag) {
/*      */         
/*  864 */         if (itemstack != null && itemstack.interactWithEntity(player, (EntityLivingBase)this))
/*      */         {
/*  866 */           return true;
/*      */         }
/*      */         
/*  869 */         makeHorseRearWithSound();
/*  870 */         return true;
/*      */       } 
/*      */       
/*  873 */       if (!flag && canCarryChest() && !isChested() && itemstack.getItem() == Item.getItemFromBlock((Block)Blocks.chest)) {
/*      */         
/*  875 */         setChested(true);
/*  876 */         playSound("mob.chickenplop", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*  877 */         flag = true;
/*  878 */         initHorseChest();
/*      */       } 
/*      */       
/*  881 */       if (!flag && func_110253_bW() && !isHorseSaddled() && itemstack.getItem() == Items.saddle) {
/*      */         
/*  883 */         openGUI(player);
/*  884 */         return true;
/*      */       } 
/*      */       
/*  887 */       if (flag) {
/*      */         
/*  889 */         if (!player.capabilities.isCreativeMode && --itemstack.stackSize == 0)
/*      */         {
/*  891 */           player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*      */         }
/*      */         
/*  894 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/*  898 */     if (func_110253_bW() && this.riddenByEntity == null) {
/*      */       
/*  900 */       if (itemstack != null && itemstack.interactWithEntity(player, (EntityLivingBase)this))
/*      */       {
/*  902 */         return true;
/*      */       }
/*      */ 
/*      */       
/*  906 */       mountTo(player);
/*  907 */       return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/*  912 */     return super.interact(player);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void mountTo(EntityPlayer player) {
/*  919 */     player.rotationYaw = this.rotationYaw;
/*  920 */     player.rotationPitch = this.rotationPitch;
/*  921 */     setEatingHaystack(false);
/*  922 */     setRearing(false);
/*      */     
/*  924 */     if (!this.worldObj.isRemote)
/*      */     {
/*  926 */       player.mountEntity((Entity)this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canWearArmor() {
/*  932 */     return (getHorseType() == 0);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canCarryChest() {
/*  937 */     int i = getHorseType();
/*  938 */     return (i == 2 || i == 1);
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isMovementBlocked() {
/*  943 */     return (this.riddenByEntity != null && isHorseSaddled()) ? true : ((isEatingHaystack() || isRearing()));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUndead() {
/*  948 */     int i = getHorseType();
/*  949 */     return (i == 3 || i == 4);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSterile() {
/*  954 */     return (isUndead() || getHorseType() == 2);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBreedingItem(ItemStack stack) {
/*  959 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   private void func_110210_cH() {
/*  964 */     this.field_110278_bp = 1;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/*  969 */     super.onDeath(cause);
/*      */     
/*  971 */     if (!this.worldObj.isRemote)
/*      */     {
/*  973 */       dropChestItems();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLivingUpdate() {
/*  979 */     if (this.rand.nextInt(200) == 0)
/*      */     {
/*  981 */       func_110210_cH();
/*      */     }
/*      */     
/*  984 */     super.onLivingUpdate();
/*      */     
/*  986 */     if (!this.worldObj.isRemote) {
/*      */       
/*  988 */       if (this.rand.nextInt(900) == 0 && this.deathTime == 0)
/*      */       {
/*  990 */         heal(1.0F);
/*      */       }
/*      */       
/*  993 */       if (!isEatingHaystack() && this.riddenByEntity == null && this.rand.nextInt(300) == 0 && this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY) - 1, MathHelper.floor_double(this.posZ))).getBlock() == Blocks.grass)
/*      */       {
/*  995 */         setEatingHaystack(true);
/*      */       }
/*      */       
/*  998 */       if (isEatingHaystack() && ++this.eatingHaystackCounter > 50) {
/*      */         
/* 1000 */         this.eatingHaystackCounter = 0;
/* 1001 */         setEatingHaystack(false);
/*      */       } 
/*      */       
/* 1004 */       if (isBreeding() && !isAdultHorse() && !isEatingHaystack()) {
/*      */         
/* 1006 */         EntityHorse entityhorse = getClosestHorse((Entity)this, 16.0D);
/*      */         
/* 1008 */         if (entityhorse != null && getDistanceSqToEntity((Entity)entityhorse) > 4.0D)
/*      */         {
/* 1010 */           this.navigator.getPathToEntityLiving((Entity)entityhorse);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/* 1018 */     super.onUpdate();
/*      */     
/* 1020 */     if (this.worldObj.isRemote && this.dataWatcher.hasObjectChanged()) {
/*      */       
/* 1022 */       this.dataWatcher.func_111144_e();
/* 1023 */       resetTexturePrefix();
/*      */     } 
/*      */     
/* 1026 */     if (this.openMouthCounter > 0 && ++this.openMouthCounter > 30) {
/*      */       
/* 1028 */       this.openMouthCounter = 0;
/* 1029 */       setHorseWatchableBoolean(128, false);
/*      */     } 
/*      */     
/* 1032 */     if (!this.worldObj.isRemote && this.jumpRearingCounter > 0 && ++this.jumpRearingCounter > 20) {
/*      */       
/* 1034 */       this.jumpRearingCounter = 0;
/* 1035 */       setRearing(false);
/*      */     } 
/*      */     
/* 1038 */     if (this.field_110278_bp > 0 && ++this.field_110278_bp > 8)
/*      */     {
/* 1040 */       this.field_110278_bp = 0;
/*      */     }
/*      */     
/* 1043 */     if (this.field_110279_bq > 0) {
/*      */       
/* 1045 */       this.field_110279_bq++;
/*      */       
/* 1047 */       if (this.field_110279_bq > 300)
/*      */       {
/* 1049 */         this.field_110279_bq = 0;
/*      */       }
/*      */     } 
/*      */     
/* 1053 */     this.prevHeadLean = this.headLean;
/*      */     
/* 1055 */     if (isEatingHaystack()) {
/*      */       
/* 1057 */       this.headLean += (1.0F - this.headLean) * 0.4F + 0.05F;
/*      */       
/* 1059 */       if (this.headLean > 1.0F)
/*      */       {
/* 1061 */         this.headLean = 1.0F;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1066 */       this.headLean += (0.0F - this.headLean) * 0.4F - 0.05F;
/*      */       
/* 1068 */       if (this.headLean < 0.0F)
/*      */       {
/* 1070 */         this.headLean = 0.0F;
/*      */       }
/*      */     } 
/*      */     
/* 1074 */     this.prevRearingAmount = this.rearingAmount;
/*      */     
/* 1076 */     if (isRearing()) {
/*      */       
/* 1078 */       this.prevHeadLean = this.headLean = 0.0F;
/* 1079 */       this.rearingAmount += (1.0F - this.rearingAmount) * 0.4F + 0.05F;
/*      */       
/* 1081 */       if (this.rearingAmount > 1.0F)
/*      */       {
/* 1083 */         this.rearingAmount = 1.0F;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1088 */       this.field_110294_bI = false;
/* 1089 */       this.rearingAmount += (0.8F * this.rearingAmount * this.rearingAmount * this.rearingAmount - this.rearingAmount) * 0.6F - 0.05F;
/*      */       
/* 1091 */       if (this.rearingAmount < 0.0F)
/*      */       {
/* 1093 */         this.rearingAmount = 0.0F;
/*      */       }
/*      */     } 
/*      */     
/* 1097 */     this.prevMouthOpenness = this.mouthOpenness;
/*      */     
/* 1099 */     if (getHorseWatchableBoolean(128)) {
/*      */       
/* 1101 */       this.mouthOpenness += (1.0F - this.mouthOpenness) * 0.7F + 0.05F;
/*      */       
/* 1103 */       if (this.mouthOpenness > 1.0F)
/*      */       {
/* 1105 */         this.mouthOpenness = 1.0F;
/*      */       }
/*      */     }
/*      */     else {
/*      */       
/* 1110 */       this.mouthOpenness += (0.0F - this.mouthOpenness) * 0.7F - 0.05F;
/*      */       
/* 1112 */       if (this.mouthOpenness < 0.0F)
/*      */       {
/* 1114 */         this.mouthOpenness = 0.0F;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void openHorseMouth() {
/* 1121 */     if (!this.worldObj.isRemote) {
/*      */       
/* 1123 */       this.openMouthCounter = 1;
/* 1124 */       setHorseWatchableBoolean(128, true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean canMate() {
/* 1130 */     return (this.riddenByEntity == null && this.ridingEntity == null && isTame() && isAdultHorse() && !isSterile() && getHealth() >= getMaxHealth() && isInLove());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEating(boolean eating) {
/* 1135 */     setHorseWatchableBoolean(32, eating);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setEatingHaystack(boolean p_110227_1_) {
/* 1140 */     setEating(p_110227_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setRearing(boolean rearing) {
/* 1145 */     if (rearing)
/*      */     {
/* 1147 */       setEatingHaystack(false);
/*      */     }
/*      */     
/* 1150 */     setHorseWatchableBoolean(64, rearing);
/*      */   }
/*      */ 
/*      */   
/*      */   private void makeHorseRear() {
/* 1155 */     if (!this.worldObj.isRemote) {
/*      */       
/* 1157 */       this.jumpRearingCounter = 1;
/* 1158 */       setRearing(true);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void makeHorseRearWithSound() {
/* 1164 */     makeHorseRear();
/* 1165 */     String s = getAngrySoundName();
/*      */     
/* 1167 */     if (s != null)
/*      */     {
/* 1169 */       playSound(s, getSoundVolume(), getSoundPitch());
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void dropChestItems() {
/* 1175 */     dropItemsInChest((Entity)this, this.horseChest);
/* 1176 */     dropChests();
/*      */   }
/*      */ 
/*      */   
/*      */   private void dropItemsInChest(Entity entityIn, AnimalChest animalChestIn) {
/* 1181 */     if (animalChestIn != null && !this.worldObj.isRemote)
/*      */     {
/* 1183 */       for (int i = 0; i < animalChestIn.getSizeInventory(); i++) {
/*      */         
/* 1185 */         ItemStack itemstack = animalChestIn.getStackInSlot(i);
/*      */         
/* 1187 */         if (itemstack != null)
/*      */         {
/* 1189 */           entityDropItem(itemstack, 0.0F);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean setTamedBy(EntityPlayer player) {
/* 1197 */     setOwnerId(player.getUniqueID().toString());
/* 1198 */     setHorseTamed(true);
/* 1199 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveEntityWithHeading(float strafe, float forward) {
/* 1204 */     if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityLivingBase && isHorseSaddled()) {
/*      */       
/* 1206 */       this.prevRotationYaw = this.rotationYaw = this.riddenByEntity.rotationYaw;
/* 1207 */       this.rotationPitch = this.riddenByEntity.rotationPitch * 0.5F;
/* 1208 */       setRotation(this.rotationYaw, this.rotationPitch);
/* 1209 */       this.rotationYawHead = this.renderYawOffset = this.rotationYaw;
/* 1210 */       strafe = ((EntityLivingBase)this.riddenByEntity).moveStrafing * 0.5F;
/* 1211 */       forward = ((EntityLivingBase)this.riddenByEntity).moveForward;
/*      */       
/* 1213 */       if (forward <= 0.0F) {
/*      */         
/* 1215 */         forward *= 0.25F;
/* 1216 */         this.gallopTime = 0;
/*      */       } 
/*      */       
/* 1219 */       if (this.onGround && this.jumpPower == 0.0F && isRearing() && !this.field_110294_bI) {
/*      */         
/* 1221 */         strafe = 0.0F;
/* 1222 */         forward = 0.0F;
/*      */       } 
/*      */       
/* 1225 */       if (this.jumpPower > 0.0F && !isHorseJumping() && this.onGround) {
/*      */         
/* 1227 */         this.motionY = getHorseJumpStrength() * this.jumpPower;
/*      */         
/* 1229 */         if (isPotionActive(Potion.jump))
/*      */         {
/* 1231 */           this.motionY += ((getActivePotionEffect(Potion.jump).getAmplifier() + 1) * 0.1F);
/*      */         }
/*      */         
/* 1234 */         setHorseJumping(true);
/* 1235 */         this.isAirBorne = true;
/*      */         
/* 1237 */         if (forward > 0.0F) {
/*      */           
/* 1239 */           float f = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F);
/* 1240 */           float f1 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 1241 */           this.motionX += (-0.4F * f * this.jumpPower);
/* 1242 */           this.motionZ += (0.4F * f1 * this.jumpPower);
/* 1243 */           playSound("mob.horse.jump", 0.4F, 1.0F);
/*      */         } 
/*      */         
/* 1246 */         this.jumpPower = 0.0F;
/*      */       } 
/*      */       
/* 1249 */       this.stepHeight = 1.0F;
/* 1250 */       this.jumpMovementFactor = getAIMoveSpeed() * 0.1F;
/*      */       
/* 1252 */       if (!this.worldObj.isRemote) {
/*      */         
/* 1254 */         setAIMoveSpeed((float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
/* 1255 */         super.moveEntityWithHeading(strafe, forward);
/*      */       } 
/*      */       
/* 1258 */       if (this.onGround) {
/*      */         
/* 1260 */         this.jumpPower = 0.0F;
/* 1261 */         setHorseJumping(false);
/*      */       } 
/*      */       
/* 1264 */       this.prevLimbSwingAmount = this.limbSwingAmount;
/* 1265 */       double d1 = this.posX - this.prevPosX;
/* 1266 */       double d0 = this.posZ - this.prevPosZ;
/* 1267 */       float f2 = MathHelper.sqrt_double(d1 * d1 + d0 * d0) * 4.0F;
/*      */       
/* 1269 */       if (f2 > 1.0F)
/*      */       {
/* 1271 */         f2 = 1.0F;
/*      */       }
/*      */       
/* 1274 */       this.limbSwingAmount += (f2 - this.limbSwingAmount) * 0.4F;
/* 1275 */       this.limbSwing += this.limbSwingAmount;
/*      */     }
/*      */     else {
/*      */       
/* 1279 */       this.stepHeight = 0.5F;
/* 1280 */       this.jumpMovementFactor = 0.02F;
/* 1281 */       super.moveEntityWithHeading(strafe, forward);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 1287 */     super.writeEntityToNBT(tagCompound);
/* 1288 */     tagCompound.setBoolean("EatingHaystack", isEatingHaystack());
/* 1289 */     tagCompound.setBoolean("ChestedHorse", isChested());
/* 1290 */     tagCompound.setBoolean("HasReproduced", getHasReproduced());
/* 1291 */     tagCompound.setBoolean("Bred", isBreeding());
/* 1292 */     tagCompound.setInteger("Type", getHorseType());
/* 1293 */     tagCompound.setInteger("Variant", getHorseVariant());
/* 1294 */     tagCompound.setInteger("Temper", getTemper());
/* 1295 */     tagCompound.setBoolean("Tame", isTame());
/* 1296 */     tagCompound.setString("OwnerUUID", getOwnerId());
/*      */     
/* 1298 */     if (isChested()) {
/*      */       
/* 1300 */       NBTTagList nbttaglist = new NBTTagList();
/*      */       
/* 1302 */       for (int i = 2; i < this.horseChest.getSizeInventory(); i++) {
/*      */         
/* 1304 */         ItemStack itemstack = this.horseChest.getStackInSlot(i);
/*      */         
/* 1306 */         if (itemstack != null) {
/*      */           
/* 1308 */           NBTTagCompound nbttagcompound = new NBTTagCompound();
/* 1309 */           nbttagcompound.setByte("Slot", (byte)i);
/* 1310 */           itemstack.writeToNBT(nbttagcompound);
/* 1311 */           nbttaglist.appendTag((NBTBase)nbttagcompound);
/*      */         } 
/*      */       } 
/*      */       
/* 1315 */       tagCompound.setTag("Items", (NBTBase)nbttaglist);
/*      */     } 
/*      */     
/* 1318 */     if (this.horseChest.getStackInSlot(1) != null)
/*      */     {
/* 1320 */       tagCompound.setTag("ArmorItem", (NBTBase)this.horseChest.getStackInSlot(1).writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */     
/* 1323 */     if (this.horseChest.getStackInSlot(0) != null)
/*      */     {
/* 1325 */       tagCompound.setTag("SaddleItem", (NBTBase)this.horseChest.getStackInSlot(0).writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 1331 */     super.readEntityFromNBT(tagCompund);
/* 1332 */     setEatingHaystack(tagCompund.getBoolean("EatingHaystack"));
/* 1333 */     setBreeding(tagCompund.getBoolean("Bred"));
/* 1334 */     setChested(tagCompund.getBoolean("ChestedHorse"));
/* 1335 */     setHasReproduced(tagCompund.getBoolean("HasReproduced"));
/* 1336 */     setHorseType(tagCompund.getInteger("Type"));
/* 1337 */     setHorseVariant(tagCompund.getInteger("Variant"));
/* 1338 */     setTemper(tagCompund.getInteger("Temper"));
/* 1339 */     setHorseTamed(tagCompund.getBoolean("Tame"));
/* 1340 */     String s = "";
/*      */     
/* 1342 */     if (tagCompund.hasKey("OwnerUUID", 8)) {
/*      */       
/* 1344 */       s = tagCompund.getString("OwnerUUID");
/*      */     }
/*      */     else {
/*      */       
/* 1348 */       String s1 = tagCompund.getString("Owner");
/* 1349 */       s = PreYggdrasilConverter.getStringUUIDFromName(s1);
/*      */     } 
/*      */     
/* 1352 */     if (s.length() > 0)
/*      */     {
/* 1354 */       setOwnerId(s);
/*      */     }
/*      */     
/* 1357 */     IAttributeInstance iattributeinstance = getAttributeMap().getAttributeInstanceByName("Speed");
/*      */     
/* 1359 */     if (iattributeinstance != null)
/*      */     {
/* 1361 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(iattributeinstance.getBaseValue() * 0.25D);
/*      */     }
/*      */     
/* 1364 */     if (isChested()) {
/*      */       
/* 1366 */       NBTTagList nbttaglist = tagCompund.getTagList("Items", 10);
/* 1367 */       initHorseChest();
/*      */       
/* 1369 */       for (int i = 0; i < nbttaglist.tagCount(); i++) {
/*      */         
/* 1371 */         NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
/* 1372 */         int j = nbttagcompound.getByte("Slot") & 0xFF;
/*      */         
/* 1374 */         if (j >= 2 && j < this.horseChest.getSizeInventory())
/*      */         {
/* 1376 */           this.horseChest.setInventorySlotContents(j, ItemStack.loadItemStackFromNBT(nbttagcompound));
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/* 1381 */     if (tagCompund.hasKey("ArmorItem", 10)) {
/*      */       
/* 1383 */       ItemStack itemstack = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("ArmorItem"));
/*      */       
/* 1385 */       if (itemstack != null && isArmorItem(itemstack.getItem()))
/*      */       {
/* 1387 */         this.horseChest.setInventorySlotContents(1, itemstack);
/*      */       }
/*      */     } 
/*      */     
/* 1391 */     if (tagCompund.hasKey("SaddleItem", 10)) {
/*      */       
/* 1393 */       ItemStack itemstack1 = ItemStack.loadItemStackFromNBT(tagCompund.getCompoundTag("SaddleItem"));
/*      */       
/* 1395 */       if (itemstack1 != null && itemstack1.getItem() == Items.saddle)
/*      */       {
/* 1397 */         this.horseChest.setInventorySlotContents(0, itemstack1);
/*      */       }
/*      */     }
/* 1400 */     else if (tagCompund.getBoolean("Saddle")) {
/*      */       
/* 1402 */       this.horseChest.setInventorySlotContents(0, new ItemStack(Items.saddle));
/*      */     } 
/*      */     
/* 1405 */     updateHorseSlots();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canMateWith(EntityAnimal otherAnimal) {
/* 1410 */     if (otherAnimal == this)
/*      */     {
/* 1412 */       return false;
/*      */     }
/* 1414 */     if (otherAnimal.getClass() != getClass())
/*      */     {
/* 1416 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1420 */     EntityHorse entityhorse = (EntityHorse)otherAnimal;
/*      */     
/* 1422 */     if (canMate() && entityhorse.canMate()) {
/*      */       
/* 1424 */       int i = getHorseType();
/* 1425 */       int j = entityhorse.getHorseType();
/* 1426 */       return (i == j || (i == 0 && j == 1) || (i == 1 && j == 0));
/*      */     } 
/*      */ 
/*      */     
/* 1430 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public EntityAgeable createChild(EntityAgeable ageable) {
/* 1437 */     EntityHorse entityhorse = (EntityHorse)ageable;
/* 1438 */     EntityHorse entityhorse1 = new EntityHorse(this.worldObj);
/* 1439 */     int i = getHorseType();
/* 1440 */     int j = entityhorse.getHorseType();
/* 1441 */     int k = 0;
/*      */     
/* 1443 */     if (i == j) {
/*      */       
/* 1445 */       k = i;
/*      */     }
/* 1447 */     else if ((i == 0 && j == 1) || (i == 1 && j == 0)) {
/*      */       
/* 1449 */       k = 2;
/*      */     } 
/*      */     
/* 1452 */     if (k == 0) {
/*      */       
/* 1454 */       int l, i1 = this.rand.nextInt(9);
/*      */ 
/*      */       
/* 1457 */       if (i1 < 4) {
/*      */         
/* 1459 */         l = getHorseVariant() & 0xFF;
/*      */       }
/* 1461 */       else if (i1 < 8) {
/*      */         
/* 1463 */         l = entityhorse.getHorseVariant() & 0xFF;
/*      */       }
/*      */       else {
/*      */         
/* 1467 */         l = this.rand.nextInt(7);
/*      */       } 
/*      */       
/* 1470 */       int j1 = this.rand.nextInt(5);
/*      */       
/* 1472 */       if (j1 < 2) {
/*      */         
/* 1474 */         l |= getHorseVariant() & 0xFF00;
/*      */       }
/* 1476 */       else if (j1 < 4) {
/*      */         
/* 1478 */         l |= entityhorse.getHorseVariant() & 0xFF00;
/*      */       }
/*      */       else {
/*      */         
/* 1482 */         l |= this.rand.nextInt(5) << 8 & 0xFF00;
/*      */       } 
/*      */       
/* 1485 */       entityhorse1.setHorseVariant(l);
/*      */     } 
/*      */     
/* 1488 */     entityhorse1.setHorseType(k);
/* 1489 */     double d1 = getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.maxHealth).getBaseValue() + getModifiedMaxHealth();
/* 1490 */     entityhorse1.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(d1 / 3.0D);
/* 1491 */     double d2 = getEntityAttribute(horseJumpStrength).getBaseValue() + ageable.getEntityAttribute(horseJumpStrength).getBaseValue() + getModifiedJumpStrength();
/* 1492 */     entityhorse1.getEntityAttribute(horseJumpStrength).setBaseValue(d2 / 3.0D);
/* 1493 */     double d0 = getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + ageable.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getBaseValue() + getModifiedMovementSpeed();
/* 1494 */     entityhorse1.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(d0 / 3.0D);
/* 1495 */     return entityhorse1;
/*      */   }
/*      */ 
/*      */   
/*      */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 1500 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 1501 */     int i = 0;
/* 1502 */     int j = 0;
/*      */     
/* 1504 */     if (livingdata instanceof GroupData) {
/*      */       
/* 1506 */       i = ((GroupData)livingdata).horseType;
/* 1507 */       j = ((GroupData)livingdata).horseVariant & 0xFF | this.rand.nextInt(5) << 8;
/*      */     }
/*      */     else {
/*      */       
/* 1511 */       if (this.rand.nextInt(10) == 0) {
/*      */         
/* 1513 */         i = 1;
/*      */       }
/*      */       else {
/*      */         
/* 1517 */         int k = this.rand.nextInt(7);
/* 1518 */         int l = this.rand.nextInt(5);
/* 1519 */         i = 0;
/* 1520 */         j = k | l << 8;
/*      */       } 
/*      */       
/* 1523 */       livingdata = new GroupData(i, j);
/*      */     } 
/*      */     
/* 1526 */     setHorseType(i);
/* 1527 */     setHorseVariant(j);
/*      */     
/* 1529 */     if (this.rand.nextInt(5) == 0)
/*      */     {
/* 1531 */       setGrowingAge(-24000);
/*      */     }
/*      */     
/* 1534 */     if (i != 4 && i != 3) {
/*      */       
/* 1536 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(getModifiedMaxHealth());
/*      */       
/* 1538 */       if (i == 0)
/*      */       {
/* 1540 */         getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(getModifiedMovementSpeed());
/*      */       }
/*      */       else
/*      */       {
/* 1544 */         getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.17499999701976776D);
/*      */       }
/*      */     
/*      */     } else {
/*      */       
/* 1549 */       getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(15.0D);
/* 1550 */       getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*      */     } 
/*      */     
/* 1553 */     if (i != 2 && i != 1) {
/*      */       
/* 1555 */       getEntityAttribute(horseJumpStrength).setBaseValue(getModifiedJumpStrength());
/*      */     }
/*      */     else {
/*      */       
/* 1559 */       getEntityAttribute(horseJumpStrength).setBaseValue(0.5D);
/*      */     } 
/*      */     
/* 1562 */     setHealth(getMaxHealth());
/* 1563 */     return livingdata;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getGrassEatingAmount(float p_110258_1_) {
/* 1568 */     return this.prevHeadLean + (this.headLean - this.prevHeadLean) * p_110258_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getRearingAmount(float p_110223_1_) {
/* 1573 */     return this.prevRearingAmount + (this.rearingAmount - this.prevRearingAmount) * p_110223_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getMouthOpennessAngle(float p_110201_1_) {
/* 1578 */     return this.prevMouthOpenness + (this.mouthOpenness - this.prevMouthOpenness) * p_110201_1_;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setJumpPower(int jumpPowerIn) {
/* 1583 */     if (isHorseSaddled()) {
/*      */       
/* 1585 */       if (jumpPowerIn < 0) {
/*      */         
/* 1587 */         jumpPowerIn = 0;
/*      */       }
/*      */       else {
/*      */         
/* 1591 */         this.field_110294_bI = true;
/* 1592 */         makeHorseRear();
/*      */       } 
/*      */       
/* 1595 */       if (jumpPowerIn >= 90) {
/*      */         
/* 1597 */         this.jumpPower = 1.0F;
/*      */       }
/*      */       else {
/*      */         
/* 1601 */         this.jumpPower = 0.4F + 0.4F * jumpPowerIn / 90.0F;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void spawnHorseParticles(boolean p_110216_1_) {
/* 1608 */     EnumParticleTypes enumparticletypes = p_110216_1_ ? EnumParticleTypes.HEART : EnumParticleTypes.SMOKE_NORMAL;
/*      */     
/* 1610 */     for (int i = 0; i < 7; i++) {
/*      */       
/* 1612 */       double d0 = this.rand.nextGaussian() * 0.02D;
/* 1613 */       double d1 = this.rand.nextGaussian() * 0.02D;
/* 1614 */       double d2 = this.rand.nextGaussian() * 0.02D;
/* 1615 */       this.worldObj.spawnParticle(enumparticletypes, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, d0, d1, d2, new int[0]);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {
/* 1621 */     if (id == 7) {
/*      */       
/* 1623 */       spawnHorseParticles(true);
/*      */     }
/* 1625 */     else if (id == 6) {
/*      */       
/* 1627 */       spawnHorseParticles(false);
/*      */     }
/*      */     else {
/*      */       
/* 1631 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRiderPosition() {
/* 1637 */     super.updateRiderPosition();
/*      */     
/* 1639 */     if (this.prevRearingAmount > 0.0F) {
/*      */       
/* 1641 */       float f = MathHelper.sin(this.renderYawOffset * 3.1415927F / 180.0F);
/* 1642 */       float f1 = MathHelper.cos(this.renderYawOffset * 3.1415927F / 180.0F);
/* 1643 */       float f2 = 0.7F * this.prevRearingAmount;
/* 1644 */       float f3 = 0.15F * this.prevRearingAmount;
/* 1645 */       this.riddenByEntity.setPosition(this.posX + (f2 * f), this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset() + f3, this.posZ - (f2 * f1));
/*      */       
/* 1647 */       if (this.riddenByEntity instanceof EntityLivingBase)
/*      */       {
/* 1649 */         ((EntityLivingBase)this.riddenByEntity).renderYawOffset = this.renderYawOffset;
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private float getModifiedMaxHealth() {
/* 1656 */     return 15.0F + this.rand.nextInt(8) + this.rand.nextInt(9);
/*      */   }
/*      */ 
/*      */   
/*      */   private double getModifiedJumpStrength() {
/* 1661 */     return 0.4000000059604645D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D + this.rand.nextDouble() * 0.2D;
/*      */   }
/*      */ 
/*      */   
/*      */   private double getModifiedMovementSpeed() {
/* 1666 */     return (0.44999998807907104D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D + this.rand.nextDouble() * 0.3D) * 0.25D;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isArmorItem(Item p_146085_0_) {
/* 1671 */     return (p_146085_0_ == Items.iron_horse_armor || p_146085_0_ == Items.golden_horse_armor || p_146085_0_ == Items.diamond_horse_armor);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isOnLadder() {
/* 1676 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getEyeHeight() {
/* 1681 */     return this.height;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/* 1686 */     if (inventorySlot == 499 && canCarryChest()) {
/*      */       
/* 1688 */       if (itemStackIn == null && isChested()) {
/*      */         
/* 1690 */         setChested(false);
/* 1691 */         initHorseChest();
/* 1692 */         return true;
/*      */       } 
/*      */       
/* 1695 */       if (itemStackIn != null && itemStackIn.getItem() == Item.getItemFromBlock((Block)Blocks.chest) && !isChested()) {
/*      */         
/* 1697 */         setChested(true);
/* 1698 */         initHorseChest();
/* 1699 */         return true;
/*      */       } 
/*      */     } 
/*      */     
/* 1703 */     int i = inventorySlot - 400;
/*      */     
/* 1705 */     if (i >= 0 && i < 2 && i < this.horseChest.getSizeInventory()) {
/*      */       
/* 1707 */       if (i == 0 && itemStackIn != null && itemStackIn.getItem() != Items.saddle)
/*      */       {
/* 1709 */         return false;
/*      */       }
/* 1711 */       if (i != 1 || ((itemStackIn == null || isArmorItem(itemStackIn.getItem())) && canWearArmor())) {
/*      */         
/* 1713 */         this.horseChest.setInventorySlotContents(i, itemStackIn);
/* 1714 */         updateHorseSlots();
/* 1715 */         return true;
/*      */       } 
/*      */ 
/*      */       
/* 1719 */       return false;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1724 */     int j = inventorySlot - 500 + 2;
/*      */     
/* 1726 */     if (j >= 2 && j < this.horseChest.getSizeInventory()) {
/*      */       
/* 1728 */       this.horseChest.setInventorySlotContents(j, itemStackIn);
/* 1729 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 1733 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static class GroupData
/*      */     implements IEntityLivingData
/*      */   {
/*      */     public int horseType;
/*      */     
/*      */     public int horseVariant;
/*      */     
/*      */     public GroupData(int type, int variant) {
/* 1745 */       this.horseType = type;
/* 1746 */       this.horseVariant = variant;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityHorse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */