/*      */ package net.minecraft.entity.player;
/*      */ 
/*      */ import com.google.common.base.Charsets;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.mojang.authlib.GameProfile;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ import java.util.UUID;
/*      */ import net.minecraft.block.Block;
/*      */ import net.minecraft.block.BlockBed;
/*      */ import net.minecraft.block.BlockDirectional;
/*      */ import net.minecraft.block.material.Material;
/*      */ import net.minecraft.block.properties.IProperty;
/*      */ import net.minecraft.block.state.IBlockState;
/*      */ import net.minecraft.command.server.CommandBlockLogic;
/*      */ import net.minecraft.enchantment.EnchantmentHelper;
/*      */ import net.minecraft.entity.Entity;
/*      */ import net.minecraft.entity.EntityList;
/*      */ import net.minecraft.entity.EntityLiving;
/*      */ import net.minecraft.entity.EntityLivingBase;
/*      */ import net.minecraft.entity.EnumCreatureAttribute;
/*      */ import net.minecraft.entity.IEntityMultiPart;
/*      */ import net.minecraft.entity.IMerchant;
/*      */ import net.minecraft.entity.SharedMonsterAttributes;
/*      */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*      */ import net.minecraft.entity.boss.EntityDragonPart;
/*      */ import net.minecraft.entity.item.EntityItem;
/*      */ import net.minecraft.entity.monster.EntityMob;
/*      */ import net.minecraft.entity.passive.EntityHorse;
/*      */ import net.minecraft.entity.passive.EntityPig;
/*      */ import net.minecraft.entity.projectile.EntityArrow;
/*      */ import net.minecraft.entity.projectile.EntityFishHook;
/*      */ import net.minecraft.event.ClickEvent;
/*      */ import net.minecraft.init.Blocks;
/*      */ import net.minecraft.init.Items;
/*      */ import net.minecraft.inventory.Container;
/*      */ import net.minecraft.inventory.ContainerPlayer;
/*      */ import net.minecraft.inventory.IInventory;
/*      */ import net.minecraft.inventory.InventoryEnderChest;
/*      */ import net.minecraft.item.EnumAction;
/*      */ import net.minecraft.item.Item;
/*      */ import net.minecraft.item.ItemStack;
/*      */ import net.minecraft.nbt.NBTBase;
/*      */ import net.minecraft.nbt.NBTTagCompound;
/*      */ import net.minecraft.nbt.NBTTagList;
/*      */ import net.minecraft.network.Packet;
/*      */ import net.minecraft.network.play.server.S12PacketEntityVelocity;
/*      */ import net.minecraft.potion.Potion;
/*      */ import net.minecraft.scoreboard.IScoreObjectiveCriteria;
/*      */ import net.minecraft.scoreboard.Score;
/*      */ import net.minecraft.scoreboard.ScoreObjective;
/*      */ import net.minecraft.scoreboard.ScorePlayerTeam;
/*      */ import net.minecraft.scoreboard.Scoreboard;
/*      */ import net.minecraft.scoreboard.Team;
/*      */ import net.minecraft.server.MinecraftServer;
/*      */ import net.minecraft.stats.AchievementList;
/*      */ import net.minecraft.stats.StatBase;
/*      */ import net.minecraft.stats.StatList;
/*      */ import net.minecraft.tileentity.TileEntitySign;
/*      */ import net.minecraft.util.AxisAlignedBB;
/*      */ import net.minecraft.util.BlockPos;
/*      */ import net.minecraft.util.ChatComponentText;
/*      */ import net.minecraft.util.DamageSource;
/*      */ import net.minecraft.util.EnumFacing;
/*      */ import net.minecraft.util.EnumParticleTypes;
/*      */ import net.minecraft.util.FoodStats;
/*      */ import net.minecraft.util.IChatComponent;
/*      */ import net.minecraft.util.MathHelper;
/*      */ import net.minecraft.util.Vec3;
/*      */ import net.minecraft.world.EnumDifficulty;
/*      */ import net.minecraft.world.IInteractionObject;
/*      */ import net.minecraft.world.LockCode;
/*      */ import net.minecraft.world.World;
/*      */ import net.minecraft.world.WorldSettings;
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class EntityPlayer
/*      */   extends EntityLivingBase
/*      */ {
/*   81 */   public InventoryPlayer inventory = new InventoryPlayer(this);
/*   82 */   private InventoryEnderChest theInventoryEnderChest = new InventoryEnderChest();
/*      */   public Container inventoryContainer;
/*      */   public Container openContainer;
/*   85 */   protected FoodStats foodStats = new FoodStats();
/*      */   protected int flyToggleTimer;
/*      */   public float prevCameraYaw;
/*      */   public float cameraYaw;
/*      */   public int xpCooldown;
/*      */   public double prevChasingPosX;
/*      */   public double prevChasingPosY;
/*      */   public double prevChasingPosZ;
/*      */   public double chasingPosX;
/*      */   public double chasingPosY;
/*      */   public double chasingPosZ;
/*      */   protected boolean sleeping;
/*      */   public BlockPos playerLocation;
/*      */   private int sleepTimer;
/*      */   public float renderOffsetX;
/*      */   public float renderOffsetY;
/*      */   public float renderOffsetZ;
/*      */   private BlockPos spawnChunk;
/*      */   private boolean spawnForced;
/*      */   private BlockPos startMinecartRidingCoordinate;
/*  105 */   public PlayerCapabilities capabilities = new PlayerCapabilities();
/*      */   public int experienceLevel;
/*      */   public int experienceTotal;
/*      */   public float experience;
/*      */   private int xpSeed;
/*      */   private ItemStack itemInUse;
/*      */   private int itemInUseCount;
/*  112 */   protected float speedOnGround = 0.1F;
/*  113 */   protected float speedInAir = 0.02F;
/*      */   
/*      */   private int lastXPSound;
/*      */   private final GameProfile gameProfile;
/*      */   private boolean hasReducedDebug = false;
/*      */   public EntityFishHook fishEntity;
/*      */   
/*      */   public EntityPlayer(World worldIn, GameProfile gameProfileIn) {
/*  121 */     super(worldIn);
/*  122 */     this.entityUniqueID = getUUID(gameProfileIn);
/*  123 */     this.gameProfile = gameProfileIn;
/*  124 */     this.inventoryContainer = (Container)new ContainerPlayer(this.inventory, !worldIn.isRemote, this);
/*  125 */     this.openContainer = this.inventoryContainer;
/*  126 */     BlockPos blockpos = worldIn.getSpawnPoint();
/*  127 */     setLocationAndAngles(blockpos.getX() + 0.5D, (blockpos.getY() + 1), blockpos.getZ() + 0.5D, 0.0F, 0.0F);
/*  128 */     this.unused180 = 180.0F;
/*  129 */     this.fireResistance = 20;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void applyEntityAttributes() {
/*  134 */     super.applyEntityAttributes();
/*  135 */     getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(1.0D);
/*  136 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.10000000149011612D);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void entityInit() {
/*  141 */     super.entityInit();
/*  142 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*  143 */     this.dataWatcher.addObject(17, Float.valueOf(0.0F));
/*  144 */     this.dataWatcher.addObject(18, Integer.valueOf(0));
/*  145 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getItemInUse() {
/*  150 */     return this.itemInUse;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getItemInUseCount() {
/*  155 */     return this.itemInUseCount;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUsingItem() {
/*  160 */     return (this.itemInUse != null);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getItemInUseDuration() {
/*  165 */     return isUsingItem() ? (this.itemInUse.getMaxItemUseDuration() - this.itemInUseCount) : 0;
/*      */   }
/*      */ 
/*      */   
/*      */   public void stopUsingItem() {
/*  170 */     if (this.itemInUse != null)
/*      */     {
/*  172 */       this.itemInUse.onPlayerStoppedUsing(this.worldObj, this, this.itemInUseCount);
/*      */     }
/*      */     
/*  175 */     clearItemInUse();
/*      */   }
/*      */ 
/*      */   
/*      */   public void clearItemInUse() {
/*  180 */     this.itemInUse = null;
/*  181 */     this.itemInUseCount = 0;
/*      */     
/*  183 */     if (!this.worldObj.isRemote)
/*      */     {
/*  185 */       setEating(false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isBlocking() {
/*  191 */     return (isUsingItem() && this.itemInUse.getItem().getItemUseAction(this.itemInUse) == EnumAction.BLOCK);
/*      */   }
/*      */ 
/*      */   
/*      */   public void onUpdate() {
/*  196 */     this.noClip = isSpectator();
/*      */     
/*  198 */     if (isSpectator())
/*      */     {
/*  200 */       this.onGround = false;
/*      */     }
/*      */     
/*  203 */     if (this.itemInUse != null) {
/*      */       
/*  205 */       ItemStack itemstack = this.inventory.getCurrentItem();
/*      */       
/*  207 */       if (itemstack == this.itemInUse) {
/*      */         
/*  209 */         if (this.itemInUseCount <= 25 && this.itemInUseCount % 4 == 0)
/*      */         {
/*  211 */           updateItemUse(itemstack, 5);
/*      */         }
/*      */         
/*  214 */         if (--this.itemInUseCount == 0 && !this.worldObj.isRemote)
/*      */         {
/*  216 */           onItemUseFinish();
/*      */         }
/*      */       }
/*      */       else {
/*      */         
/*  221 */         clearItemInUse();
/*      */       } 
/*      */     } 
/*      */     
/*  225 */     if (this.xpCooldown > 0)
/*      */     {
/*  227 */       this.xpCooldown--;
/*      */     }
/*      */     
/*  230 */     if (isPlayerSleeping()) {
/*      */       
/*  232 */       this.sleepTimer++;
/*      */       
/*  234 */       if (this.sleepTimer > 100)
/*      */       {
/*  236 */         this.sleepTimer = 100;
/*      */       }
/*      */       
/*  239 */       if (!this.worldObj.isRemote)
/*      */       {
/*  241 */         if (!isInBed())
/*      */         {
/*  243 */           wakeUpPlayer(true, true, false);
/*      */         }
/*  245 */         else if (this.worldObj.isDaytime())
/*      */         {
/*  247 */           wakeUpPlayer(false, true, true);
/*      */         }
/*      */       
/*      */       }
/*  251 */     } else if (this.sleepTimer > 0) {
/*      */       
/*  253 */       this.sleepTimer++;
/*      */       
/*  255 */       if (this.sleepTimer >= 110)
/*      */       {
/*  257 */         this.sleepTimer = 0;
/*      */       }
/*      */     } 
/*      */     
/*  261 */     super.onUpdate();
/*      */     
/*  263 */     if (!this.worldObj.isRemote && this.openContainer != null && !this.openContainer.canInteractWith(this)) {
/*      */       
/*  265 */       closeScreen();
/*  266 */       this.openContainer = this.inventoryContainer;
/*      */     } 
/*      */     
/*  269 */     if (isBurning() && this.capabilities.disableDamage)
/*      */     {
/*  271 */       extinguish();
/*      */     }
/*      */     
/*  274 */     this.prevChasingPosX = this.chasingPosX;
/*  275 */     this.prevChasingPosY = this.chasingPosY;
/*  276 */     this.prevChasingPosZ = this.chasingPosZ;
/*  277 */     double d5 = this.posX - this.chasingPosX;
/*  278 */     double d0 = this.posY - this.chasingPosY;
/*  279 */     double d1 = this.posZ - this.chasingPosZ;
/*  280 */     double d2 = 10.0D;
/*      */     
/*  282 */     if (d5 > d2)
/*      */     {
/*  284 */       this.prevChasingPosX = this.chasingPosX = this.posX;
/*      */     }
/*      */     
/*  287 */     if (d1 > d2)
/*      */     {
/*  289 */       this.prevChasingPosZ = this.chasingPosZ = this.posZ;
/*      */     }
/*      */     
/*  292 */     if (d0 > d2)
/*      */     {
/*  294 */       this.prevChasingPosY = this.chasingPosY = this.posY;
/*      */     }
/*      */     
/*  297 */     if (d5 < -d2)
/*      */     {
/*  299 */       this.prevChasingPosX = this.chasingPosX = this.posX;
/*      */     }
/*      */     
/*  302 */     if (d1 < -d2)
/*      */     {
/*  304 */       this.prevChasingPosZ = this.chasingPosZ = this.posZ;
/*      */     }
/*      */     
/*  307 */     if (d0 < -d2)
/*      */     {
/*  309 */       this.prevChasingPosY = this.chasingPosY = this.posY;
/*      */     }
/*      */     
/*  312 */     this.chasingPosX += d5 * 0.25D;
/*  313 */     this.chasingPosZ += d1 * 0.25D;
/*  314 */     this.chasingPosY += d0 * 0.25D;
/*      */     
/*  316 */     if (this.ridingEntity == null)
/*      */     {
/*  318 */       this.startMinecartRidingCoordinate = null;
/*      */     }
/*      */     
/*  321 */     if (!this.worldObj.isRemote) {
/*      */       
/*  323 */       this.foodStats.onUpdate(this);
/*  324 */       triggerAchievement(StatList.minutesPlayedStat);
/*      */       
/*  326 */       if (isEntityAlive())
/*      */       {
/*  328 */         triggerAchievement(StatList.timeSinceDeathStat);
/*      */       }
/*      */     } 
/*      */     
/*  332 */     int i = 29999999;
/*  333 */     double d3 = MathHelper.clamp_double(this.posX, -2.9999999E7D, 2.9999999E7D);
/*  334 */     double d4 = MathHelper.clamp_double(this.posZ, -2.9999999E7D, 2.9999999E7D);
/*      */     
/*  336 */     if (d3 != this.posX || d4 != this.posZ)
/*      */     {
/*  338 */       setPosition(d3, this.posY, d4);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMaxInPortalTime() {
/*  344 */     return this.capabilities.disableDamage ? 0 : 80;
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getSwimSound() {
/*  349 */     return "game.player.swim";
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getSplashSound() {
/*  354 */     return "game.player.swim.splash";
/*      */   }
/*      */ 
/*      */   
/*      */   public int getPortalCooldown() {
/*  359 */     return 10;
/*      */   }
/*      */ 
/*      */   
/*      */   public void playSound(String name, float volume, float pitch) {
/*  364 */     this.worldObj.playSoundToNearExcept(this, name, volume, pitch);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateItemUse(ItemStack itemStackIn, int p_71010_2_) {
/*  369 */     if (itemStackIn.getItemUseAction() == EnumAction.DRINK)
/*      */     {
/*  371 */       playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
/*      */     }
/*      */     
/*  374 */     if (itemStackIn.getItemUseAction() == EnumAction.EAT) {
/*      */       
/*  376 */       for (int i = 0; i < p_71010_2_; i++) {
/*      */         
/*  378 */         Vec3 vec3 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
/*  379 */         vec3 = vec3.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  380 */         vec3 = vec3.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  381 */         double d0 = -this.rand.nextFloat() * 0.6D - 0.3D;
/*  382 */         Vec3 vec31 = new Vec3((this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
/*  383 */         vec31 = vec31.rotatePitch(-this.rotationPitch * 3.1415927F / 180.0F);
/*  384 */         vec31 = vec31.rotateYaw(-this.rotationYaw * 3.1415927F / 180.0F);
/*  385 */         vec31 = vec31.addVector(this.posX, this.posY + getEyeHeight(), this.posZ);
/*      */         
/*  387 */         if (itemStackIn.getHasSubtypes()) {
/*      */           
/*  389 */           this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()), itemStackIn.getMetadata() });
/*      */         }
/*      */         else {
/*      */           
/*  393 */           this.worldObj.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec31.xCoord, vec31.yCoord, vec31.zCoord, vec3.xCoord, vec3.yCoord + 0.05D, vec3.zCoord, new int[] { Item.getIdFromItem(itemStackIn.getItem()) });
/*      */         } 
/*      */       } 
/*      */       
/*  397 */       playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void onItemUseFinish() {
/*  403 */     if (this.itemInUse != null) {
/*      */       
/*  405 */       updateItemUse(this.itemInUse, 16);
/*  406 */       int i = this.itemInUse.stackSize;
/*  407 */       ItemStack itemstack = this.itemInUse.onItemUseFinish(this.worldObj, this);
/*      */       
/*  409 */       if (itemstack != this.itemInUse || (itemstack != null && itemstack.stackSize != i)) {
/*      */         
/*  411 */         this.inventory.mainInventory[this.inventory.currentItem] = itemstack;
/*      */         
/*  413 */         if (itemstack.stackSize == 0)
/*      */         {
/*  415 */           this.inventory.mainInventory[this.inventory.currentItem] = null;
/*      */         }
/*      */       } 
/*      */       
/*  419 */       clearItemInUse();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void handleStatusUpdate(byte id) {
/*  425 */     if (id == 9) {
/*      */       
/*  427 */       onItemUseFinish();
/*      */     }
/*  429 */     else if (id == 23) {
/*      */       
/*  431 */       this.hasReducedDebug = false;
/*      */     }
/*  433 */     else if (id == 22) {
/*      */       
/*  435 */       this.hasReducedDebug = true;
/*      */     }
/*      */     else {
/*      */       
/*  439 */       super.handleStatusUpdate(id);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean isMovementBlocked() {
/*  445 */     return (getHealth() <= 0.0F || isPlayerSleeping());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void closeScreen() {
/*  450 */     this.openContainer = this.inventoryContainer;
/*      */   }
/*      */ 
/*      */   
/*      */   public void updateRidden() {
/*  455 */     if (!this.worldObj.isRemote && isSneaking()) {
/*      */       
/*  457 */       mountEntity((Entity)null);
/*  458 */       setSneaking(false);
/*      */     }
/*      */     else {
/*      */       
/*  462 */       double d0 = this.posX;
/*  463 */       double d1 = this.posY;
/*  464 */       double d2 = this.posZ;
/*  465 */       float f = this.rotationYaw;
/*  466 */       float f1 = this.rotationPitch;
/*  467 */       super.updateRidden();
/*  468 */       this.prevCameraYaw = this.cameraYaw;
/*  469 */       this.cameraYaw = 0.0F;
/*  470 */       addMountedMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
/*      */       
/*  472 */       if (this.ridingEntity instanceof EntityPig) {
/*      */         
/*  474 */         this.rotationPitch = f1;
/*  475 */         this.rotationYaw = f;
/*  476 */         this.renderYawOffset = ((EntityPig)this.ridingEntity).renderYawOffset;
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void preparePlayerToSpawn() {
/*  483 */     setSize(0.6F, 1.8F);
/*  484 */     super.preparePlayerToSpawn();
/*  485 */     setHealth(getMaxHealth());
/*  486 */     this.deathTime = 0;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void updateEntityActionState() {
/*  491 */     super.updateEntityActionState();
/*  492 */     updateArmSwingProgress();
/*  493 */     this.rotationYawHead = this.rotationYaw;
/*      */   }
/*      */ 
/*      */   
/*      */   public void onLivingUpdate() {
/*  498 */     if (this.flyToggleTimer > 0)
/*      */     {
/*  500 */       this.flyToggleTimer--;
/*      */     }
/*      */     
/*  503 */     if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL && this.worldObj.getGameRules().getBoolean("naturalRegeneration")) {
/*      */       
/*  505 */       if (getHealth() < getMaxHealth() && this.ticksExisted % 20 == 0)
/*      */       {
/*  507 */         heal(1.0F);
/*      */       }
/*      */       
/*  510 */       if (this.foodStats.needFood() && this.ticksExisted % 10 == 0)
/*      */       {
/*  512 */         this.foodStats.setFoodLevel(this.foodStats.getFoodLevel() + 1);
/*      */       }
/*      */     } 
/*      */     
/*  516 */     this.inventory.decrementAnimations();
/*  517 */     this.prevCameraYaw = this.cameraYaw;
/*  518 */     super.onLivingUpdate();
/*  519 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*      */     
/*  521 */     if (!this.worldObj.isRemote)
/*      */     {
/*  523 */       iattributeinstance.setBaseValue(this.capabilities.getWalkSpeed());
/*      */     }
/*      */     
/*  526 */     this.jumpMovementFactor = this.speedInAir;
/*      */     
/*  528 */     if (isSprinting())
/*      */     {
/*  530 */       this.jumpMovementFactor = (float)(this.jumpMovementFactor + this.speedInAir * 0.3D);
/*      */     }
/*      */     
/*  533 */     setAIMoveSpeed((float)iattributeinstance.getAttributeValue());
/*  534 */     float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*  535 */     float f1 = (float)(Math.atan(-this.motionY * 0.20000000298023224D) * 15.0D);
/*      */     
/*  537 */     if (f > 0.1F)
/*      */     {
/*  539 */       f = 0.1F;
/*      */     }
/*      */     
/*  542 */     if (!this.onGround || getHealth() <= 0.0F)
/*      */     {
/*  544 */       f = 0.0F;
/*      */     }
/*      */     
/*  547 */     if (this.onGround || getHealth() <= 0.0F)
/*      */     {
/*  549 */       f1 = 0.0F;
/*      */     }
/*      */     
/*  552 */     this.cameraYaw += (f - this.cameraYaw) * 0.4F;
/*  553 */     this.cameraPitch += (f1 - this.cameraPitch) * 0.8F;
/*      */     
/*  555 */     if (getHealth() > 0.0F && !isSpectator()) {
/*      */       
/*  557 */       AxisAlignedBB axisalignedbb = null;
/*      */       
/*  559 */       if (this.ridingEntity != null && !this.ridingEntity.isDead) {
/*      */         
/*  561 */         axisalignedbb = getEntityBoundingBox().union(this.ridingEntity.getEntityBoundingBox()).expand(1.0D, 0.0D, 1.0D);
/*      */       }
/*      */       else {
/*      */         
/*  565 */         axisalignedbb = getEntityBoundingBox().expand(1.0D, 0.5D, 1.0D);
/*      */       } 
/*      */       
/*  568 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, axisalignedbb);
/*      */       
/*  570 */       for (int i = 0; i < list.size(); i++) {
/*      */         
/*  572 */         Entity entity = list.get(i);
/*      */         
/*  574 */         if (!entity.isDead)
/*      */         {
/*  576 */           collideWithPlayer(entity);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private void collideWithPlayer(Entity p_71044_1_) {
/*  584 */     p_71044_1_.onCollideWithPlayer(this);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getScore() {
/*  589 */     return this.dataWatcher.getWatchableObjectInt(18);
/*      */   }
/*      */ 
/*      */   
/*      */   public void setScore(int p_85040_1_) {
/*  594 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_85040_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addScore(int p_85039_1_) {
/*  599 */     int i = getScore();
/*  600 */     this.dataWatcher.updateObject(18, Integer.valueOf(i + p_85039_1_));
/*      */   }
/*      */ 
/*      */   
/*      */   public void onDeath(DamageSource cause) {
/*  605 */     super.onDeath(cause);
/*  606 */     setSize(0.2F, 0.2F);
/*  607 */     setPosition(this.posX, this.posY, this.posZ);
/*  608 */     this.motionY = 0.10000000149011612D;
/*      */     
/*  610 */     if (getName().equals("Notch"))
/*      */     {
/*  612 */       dropItem(new ItemStack(Items.apple, 1), true, false);
/*      */     }
/*      */     
/*  615 */     if (!this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/*  617 */       this.inventory.dropAllItems();
/*      */     }
/*      */     
/*  620 */     if (cause != null) {
/*      */       
/*  622 */       this.motionX = (-MathHelper.cos((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
/*  623 */       this.motionZ = (-MathHelper.sin((this.attackedAtYaw + this.rotationYaw) * 3.1415927F / 180.0F) * 0.1F);
/*      */     }
/*      */     else {
/*      */       
/*  627 */       this.motionX = this.motionZ = 0.0D;
/*      */     } 
/*      */     
/*  630 */     triggerAchievement(StatList.deathsStat);
/*  631 */     func_175145_a(StatList.timeSinceDeathStat);
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getHurtSound() {
/*  636 */     return "game.player.hurt";
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getDeathSound() {
/*  641 */     return "game.player.die";
/*      */   }
/*      */ 
/*      */   
/*      */   public void addToPlayerScore(Entity entityIn, int amount) {
/*  646 */     addScore(amount);
/*  647 */     Collection<ScoreObjective> collection = getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.totalKillCount);
/*      */     
/*  649 */     if (entityIn instanceof EntityPlayer) {
/*      */       
/*  651 */       triggerAchievement(StatList.playerKillsStat);
/*  652 */       collection.addAll(getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.playerKillCount));
/*  653 */       collection.addAll(func_175137_e(entityIn));
/*      */     }
/*      */     else {
/*      */       
/*  657 */       triggerAchievement(StatList.mobKillsStat);
/*      */     } 
/*      */     
/*  660 */     for (ScoreObjective scoreobjective : collection) {
/*      */       
/*  662 */       Score score = getWorldScoreboard().getValueFromObjective(getName(), scoreobjective);
/*  663 */       score.func_96648_a();
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private Collection<ScoreObjective> func_175137_e(Entity p_175137_1_) {
/*  669 */     ScorePlayerTeam scoreplayerteam = getWorldScoreboard().getPlayersTeam(getName());
/*      */     
/*  671 */     if (scoreplayerteam != null) {
/*      */       
/*  673 */       int i = scoreplayerteam.getChatFormat().getColorIndex();
/*      */       
/*  675 */       if (i >= 0 && i < IScoreObjectiveCriteria.field_178793_i.length)
/*      */       {
/*  677 */         for (ScoreObjective scoreobjective : getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178793_i[i])) {
/*      */           
/*  679 */           Score score = getWorldScoreboard().getValueFromObjective(p_175137_1_.getName(), scoreobjective);
/*  680 */           score.func_96648_a();
/*      */         } 
/*      */       }
/*      */     } 
/*      */     
/*  685 */     ScorePlayerTeam scoreplayerteam1 = getWorldScoreboard().getPlayersTeam(p_175137_1_.getName());
/*      */     
/*  687 */     if (scoreplayerteam1 != null) {
/*      */       
/*  689 */       int j = scoreplayerteam1.getChatFormat().getColorIndex();
/*      */       
/*  691 */       if (j >= 0 && j < IScoreObjectiveCriteria.field_178792_h.length)
/*      */       {
/*  693 */         return getWorldScoreboard().getObjectivesFromCriteria(IScoreObjectiveCriteria.field_178792_h[j]);
/*      */       }
/*      */     } 
/*      */     
/*  697 */     return Lists.newArrayList();
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem dropOneItem(boolean dropAll) {
/*  702 */     return dropItem(this.inventory.decrStackSize(this.inventory.currentItem, (dropAll && this.inventory.getCurrentItem() != null) ? (this.inventory.getCurrentItem()).stackSize : 1), false, true);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemStackIn, boolean unused) {
/*  707 */     return dropItem(itemStackIn, false, false);
/*      */   }
/*      */ 
/*      */   
/*      */   public EntityItem dropItem(ItemStack droppedItem, boolean dropAround, boolean traceItem) {
/*  712 */     if (droppedItem == null)
/*      */     {
/*  714 */       return null;
/*      */     }
/*  716 */     if (droppedItem.stackSize == 0)
/*      */     {
/*  718 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  722 */     double d0 = this.posY - 0.30000001192092896D + getEyeHeight();
/*  723 */     EntityItem entityitem = new EntityItem(this.worldObj, this.posX, d0, this.posZ, droppedItem);
/*  724 */     entityitem.setPickupDelay(40);
/*      */     
/*  726 */     if (traceItem)
/*      */     {
/*  728 */       entityitem.setThrower(getName());
/*      */     }
/*      */     
/*  731 */     if (dropAround) {
/*      */       
/*  733 */       float f = this.rand.nextFloat() * 0.5F;
/*  734 */       float f1 = this.rand.nextFloat() * 3.1415927F * 2.0F;
/*  735 */       entityitem.motionX = (-MathHelper.sin(f1) * f);
/*  736 */       entityitem.motionZ = (MathHelper.cos(f1) * f);
/*  737 */       entityitem.motionY = 0.20000000298023224D;
/*      */     }
/*      */     else {
/*      */       
/*  741 */       float f2 = 0.3F;
/*  742 */       entityitem.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
/*  743 */       entityitem.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f2);
/*  744 */       entityitem.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f2 + 0.1F);
/*  745 */       float f3 = this.rand.nextFloat() * 3.1415927F * 2.0F;
/*  746 */       f2 = 0.02F * this.rand.nextFloat();
/*  747 */       entityitem.motionX += Math.cos(f3) * f2;
/*  748 */       entityitem.motionY += ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.1F);
/*  749 */       entityitem.motionZ += Math.sin(f3) * f2;
/*      */     } 
/*      */     
/*  752 */     joinEntityItemWithWorld(entityitem);
/*      */     
/*  754 */     if (traceItem)
/*      */     {
/*  756 */       triggerAchievement(StatList.dropStat);
/*      */     }
/*      */     
/*  759 */     return entityitem;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void joinEntityItemWithWorld(EntityItem itemIn) {
/*  765 */     this.worldObj.spawnEntityInWorld((Entity)itemIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getToolDigEfficiency(Block p_180471_1_) {
/*  770 */     float f = this.inventory.getStrVsBlock(p_180471_1_);
/*      */     
/*  772 */     if (f > 1.0F) {
/*      */       
/*  774 */       int i = EnchantmentHelper.getEfficiencyModifier(this);
/*  775 */       ItemStack itemstack = this.inventory.getCurrentItem();
/*      */       
/*  777 */       if (i > 0 && itemstack != null)
/*      */       {
/*  779 */         f += (i * i + 1);
/*      */       }
/*      */     } 
/*      */     
/*  783 */     if (isPotionActive(Potion.digSpeed))
/*      */     {
/*  785 */       f *= 1.0F + (getActivePotionEffect(Potion.digSpeed).getAmplifier() + 1) * 0.2F;
/*      */     }
/*      */     
/*  788 */     if (isPotionActive(Potion.digSlowdown)) {
/*      */       
/*  790 */       float f1 = 1.0F;
/*      */       
/*  792 */       switch (getActivePotionEffect(Potion.digSlowdown).getAmplifier()) {
/*      */         
/*      */         case 0:
/*  795 */           f1 = 0.3F;
/*      */           break;
/*      */         
/*      */         case 1:
/*  799 */           f1 = 0.09F;
/*      */           break;
/*      */         
/*      */         case 2:
/*  803 */           f1 = 0.0027F;
/*      */           break;
/*      */ 
/*      */         
/*      */         default:
/*  808 */           f1 = 8.1E-4F;
/*      */           break;
/*      */       } 
/*  811 */       f *= f1;
/*      */     } 
/*      */     
/*  814 */     if (isInsideOfMaterial(Material.water) && !EnchantmentHelper.getAquaAffinityModifier(this))
/*      */     {
/*  816 */       f /= 5.0F;
/*      */     }
/*      */     
/*  819 */     if (!this.onGround)
/*      */     {
/*  821 */       f /= 5.0F;
/*      */     }
/*      */     
/*  824 */     return f;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canHarvestBlock(Block blockToHarvest) {
/*  829 */     return this.inventory.canHeldItemHarvest(blockToHarvest);
/*      */   }
/*      */ 
/*      */   
/*      */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  834 */     super.readEntityFromNBT(tagCompund);
/*  835 */     this.entityUniqueID = getUUID(this.gameProfile);
/*  836 */     NBTTagList nbttaglist = tagCompund.getTagList("Inventory", 10);
/*  837 */     this.inventory.readFromNBT(nbttaglist);
/*  838 */     this.inventory.currentItem = tagCompund.getInteger("SelectedItemSlot");
/*  839 */     this.sleeping = tagCompund.getBoolean("Sleeping");
/*  840 */     this.sleepTimer = tagCompund.getShort("SleepTimer");
/*  841 */     this.experience = tagCompund.getFloat("XpP");
/*  842 */     this.experienceLevel = tagCompund.getInteger("XpLevel");
/*  843 */     this.experienceTotal = tagCompund.getInteger("XpTotal");
/*  844 */     this.xpSeed = tagCompund.getInteger("XpSeed");
/*      */     
/*  846 */     if (this.xpSeed == 0)
/*      */     {
/*  848 */       this.xpSeed = this.rand.nextInt();
/*      */     }
/*      */     
/*  851 */     setScore(tagCompund.getInteger("Score"));
/*      */     
/*  853 */     if (this.sleeping) {
/*      */       
/*  855 */       this.playerLocation = new BlockPos((Entity)this);
/*  856 */       wakeUpPlayer(true, true, false);
/*      */     } 
/*      */     
/*  859 */     if (tagCompund.hasKey("SpawnX", 99) && tagCompund.hasKey("SpawnY", 99) && tagCompund.hasKey("SpawnZ", 99)) {
/*      */       
/*  861 */       this.spawnChunk = new BlockPos(tagCompund.getInteger("SpawnX"), tagCompund.getInteger("SpawnY"), tagCompund.getInteger("SpawnZ"));
/*  862 */       this.spawnForced = tagCompund.getBoolean("SpawnForced");
/*      */     } 
/*      */     
/*  865 */     this.foodStats.readNBT(tagCompund);
/*  866 */     this.capabilities.readCapabilitiesFromNBT(tagCompund);
/*      */     
/*  868 */     if (tagCompund.hasKey("EnderItems", 9)) {
/*      */       
/*  870 */       NBTTagList nbttaglist1 = tagCompund.getTagList("EnderItems", 10);
/*  871 */       this.theInventoryEnderChest.loadInventoryFromNBT(nbttaglist1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  877 */     super.writeEntityToNBT(tagCompound);
/*  878 */     tagCompound.setTag("Inventory", (NBTBase)this.inventory.writeToNBT(new NBTTagList()));
/*  879 */     tagCompound.setInteger("SelectedItemSlot", this.inventory.currentItem);
/*  880 */     tagCompound.setBoolean("Sleeping", this.sleeping);
/*  881 */     tagCompound.setShort("SleepTimer", (short)this.sleepTimer);
/*  882 */     tagCompound.setFloat("XpP", this.experience);
/*  883 */     tagCompound.setInteger("XpLevel", this.experienceLevel);
/*  884 */     tagCompound.setInteger("XpTotal", this.experienceTotal);
/*  885 */     tagCompound.setInteger("XpSeed", this.xpSeed);
/*  886 */     tagCompound.setInteger("Score", getScore());
/*      */     
/*  888 */     if (this.spawnChunk != null) {
/*      */       
/*  890 */       tagCompound.setInteger("SpawnX", this.spawnChunk.getX());
/*  891 */       tagCompound.setInteger("SpawnY", this.spawnChunk.getY());
/*  892 */       tagCompound.setInteger("SpawnZ", this.spawnChunk.getZ());
/*  893 */       tagCompound.setBoolean("SpawnForced", this.spawnForced);
/*      */     } 
/*      */     
/*  896 */     this.foodStats.writeNBT(tagCompound);
/*  897 */     this.capabilities.writeCapabilitiesToNBT(tagCompound);
/*  898 */     tagCompound.setTag("EnderItems", (NBTBase)this.theInventoryEnderChest.saveInventoryToNBT());
/*  899 */     ItemStack itemstack = this.inventory.getCurrentItem();
/*      */     
/*  901 */     if (itemstack != null && itemstack.getItem() != null)
/*      */     {
/*  903 */       tagCompound.setTag("SelectedItem", (NBTBase)itemstack.writeToNBT(new NBTTagCompound()));
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  909 */     if (isEntityInvulnerable(source))
/*      */     {
/*  911 */       return false;
/*      */     }
/*  913 */     if (this.capabilities.disableDamage && !source.canHarmInCreative())
/*      */     {
/*  915 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  919 */     this.entityAge = 0;
/*      */     
/*  921 */     if (getHealth() <= 0.0F)
/*      */     {
/*  923 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  927 */     if (isPlayerSleeping() && !this.worldObj.isRemote)
/*      */     {
/*  929 */       wakeUpPlayer(true, true, false);
/*      */     }
/*      */     
/*  932 */     if (source.isDifficultyScaled()) {
/*      */       
/*  934 */       if (this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
/*      */       {
/*  936 */         amount = 0.0F;
/*      */       }
/*      */       
/*  939 */       if (this.worldObj.getDifficulty() == EnumDifficulty.EASY)
/*      */       {
/*  941 */         amount = amount / 2.0F + 1.0F;
/*      */       }
/*      */       
/*  944 */       if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
/*      */       {
/*  946 */         amount = amount * 3.0F / 2.0F;
/*      */       }
/*      */     } 
/*      */     
/*  950 */     if (amount == 0.0F)
/*      */     {
/*  952 */       return false;
/*      */     }
/*      */ 
/*      */     
/*  956 */     Entity entity = source.getEntity();
/*      */     
/*  958 */     if (entity instanceof EntityArrow && ((EntityArrow)entity).shootingEntity != null)
/*      */     {
/*  960 */       entity = ((EntityArrow)entity).shootingEntity;
/*      */     }
/*      */     
/*  963 */     return super.attackEntityFrom(source, amount);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean canAttackPlayer(EntityPlayer other) {
/*  971 */     Team team = getTeam();
/*  972 */     Team team1 = other.getTeam();
/*  973 */     return (team == null) ? true : (!team.isSameTeam(team1) ? true : team.getAllowFriendlyFire());
/*      */   }
/*      */ 
/*      */   
/*      */   protected void damageArmor(float p_70675_1_) {
/*  978 */     this.inventory.damageArmor(p_70675_1_);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getTotalArmorValue() {
/*  983 */     return this.inventory.getTotalArmorValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public float getArmorVisibility() {
/*  988 */     int i = 0;
/*      */     
/*  990 */     for (ItemStack itemstack : this.inventory.armorInventory) {
/*      */       
/*  992 */       if (itemstack != null)
/*      */       {
/*  994 */         i++;
/*      */       }
/*      */     } 
/*      */     
/*  998 */     return i / this.inventory.armorInventory.length;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void damageEntity(DamageSource damageSrc, float damageAmount) {
/* 1003 */     if (!isEntityInvulnerable(damageSrc)) {
/*      */       
/* 1005 */       if (!damageSrc.isUnblockable() && isBlocking() && damageAmount > 0.0F)
/*      */       {
/* 1007 */         damageAmount = (1.0F + damageAmount) * 0.5F;
/*      */       }
/*      */       
/* 1010 */       damageAmount = applyArmorCalculations(damageSrc, damageAmount);
/* 1011 */       damageAmount = applyPotionDamageCalculations(damageSrc, damageAmount);
/* 1012 */       float f = damageAmount;
/* 1013 */       damageAmount = Math.max(damageAmount - getAbsorptionAmount(), 0.0F);
/* 1014 */       setAbsorptionAmount(getAbsorptionAmount() - f - damageAmount);
/*      */       
/* 1016 */       if (damageAmount != 0.0F) {
/*      */         
/* 1018 */         addExhaustion(damageSrc.getHungerDamage());
/* 1019 */         float f1 = getHealth();
/* 1020 */         setHealth(getHealth() - damageAmount);
/* 1021 */         getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
/*      */         
/* 1023 */         if (damageAmount < 3.4028235E37F)
/*      */         {
/* 1025 */           addStat(StatList.damageTakenStat, Math.round(damageAmount * 10.0F));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void openEditSign(TileEntitySign signTile) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void openEditCommandBlock(CommandBlockLogic cmdBlockLogic) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayVillagerTradeGui(IMerchant villager) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayGUIChest(IInventory chestInventory) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayGUIHorse(EntityHorse horse, IInventory horseInventory) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayGui(IInteractionObject guiOwner) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void displayGUIBook(ItemStack bookStack) {}
/*      */ 
/*      */   
/*      */   public boolean interactWith(Entity targetEntity) {
/* 1061 */     if (isSpectator()) {
/*      */       
/* 1063 */       if (targetEntity instanceof IInventory)
/*      */       {
/* 1065 */         displayGUIChest((IInventory)targetEntity);
/*      */       }
/*      */       
/* 1068 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1072 */     ItemStack itemstack = getCurrentEquippedItem();
/* 1073 */     ItemStack itemstack1 = (itemstack != null) ? itemstack.copy() : null;
/*      */     
/* 1075 */     if (!targetEntity.interactFirst(this)) {
/*      */       
/* 1077 */       if (itemstack != null && targetEntity instanceof EntityLivingBase) {
/*      */         
/* 1079 */         if (this.capabilities.isCreativeMode)
/*      */         {
/* 1081 */           itemstack = itemstack1;
/*      */         }
/*      */         
/* 1084 */         if (itemstack.interactWithEntity(this, (EntityLivingBase)targetEntity)) {
/*      */           
/* 1086 */           if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode)
/*      */           {
/* 1088 */             destroyCurrentEquippedItem();
/*      */           }
/*      */           
/* 1091 */           return true;
/*      */         } 
/*      */       } 
/*      */       
/* 1095 */       return false;
/*      */     } 
/*      */ 
/*      */     
/* 1099 */     if (itemstack != null && itemstack == getCurrentEquippedItem())
/*      */     {
/* 1101 */       if (itemstack.stackSize <= 0 && !this.capabilities.isCreativeMode) {
/*      */         
/* 1103 */         destroyCurrentEquippedItem();
/*      */       }
/* 1105 */       else if (itemstack.stackSize < itemstack1.stackSize && this.capabilities.isCreativeMode) {
/*      */         
/* 1107 */         itemstack.stackSize = itemstack1.stackSize;
/*      */       } 
/*      */     }
/*      */     
/* 1111 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemStack getCurrentEquippedItem() {
/* 1118 */     return this.inventory.getCurrentItem();
/*      */   }
/*      */ 
/*      */   
/*      */   public void destroyCurrentEquippedItem() {
/* 1123 */     this.inventory.setInventorySlotContents(this.inventory.currentItem, (ItemStack)null);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getYOffset() {
/* 1128 */     return -0.35D;
/*      */   }
/*      */ 
/*      */   
/*      */   public void attackTargetEntityWithCurrentItem(Entity targetEntity) {
/* 1133 */     if (targetEntity.canAttackWithItem())
/*      */     {
/* 1135 */       if (!targetEntity.hitByEntity((Entity)this)) {
/*      */         
/* 1137 */         float f = (float)getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
/* 1138 */         int i = 0;
/* 1139 */         float f1 = 0.0F;
/*      */         
/* 1141 */         if (targetEntity instanceof EntityLivingBase) {
/*      */           
/* 1143 */           f1 = EnchantmentHelper.getModifierForCreature(getHeldItem(), ((EntityLivingBase)targetEntity).getCreatureAttribute());
/*      */         }
/*      */         else {
/*      */           
/* 1147 */           f1 = EnchantmentHelper.getModifierForCreature(getHeldItem(), EnumCreatureAttribute.UNDEFINED);
/*      */         } 
/*      */         
/* 1150 */         i += EnchantmentHelper.getKnockbackModifier(this);
/*      */         
/* 1152 */         if (isSprinting())
/*      */         {
/* 1154 */           i++;
/*      */         }
/*      */         
/* 1157 */         if (f > 0.0F || f1 > 0.0F) {
/*      */           
/* 1159 */           boolean flag = (this.fallDistance > 0.0F && !this.onGround && !isOnLadder() && !isInWater() && !isPotionActive(Potion.blindness) && this.ridingEntity == null && targetEntity instanceof EntityLivingBase);
/*      */           
/* 1161 */           if (flag && f > 0.0F)
/*      */           {
/* 1163 */             f *= 1.5F;
/*      */           }
/*      */           
/* 1166 */           f += f1;
/* 1167 */           boolean flag1 = false;
/* 1168 */           int j = EnchantmentHelper.getFireAspectModifier(this);
/*      */           
/* 1170 */           if (targetEntity instanceof EntityLivingBase && j > 0 && !targetEntity.isBurning()) {
/*      */             
/* 1172 */             flag1 = true;
/* 1173 */             targetEntity.setFire(1);
/*      */           } 
/*      */           
/* 1176 */           double d0 = targetEntity.motionX;
/* 1177 */           double d1 = targetEntity.motionY;
/* 1178 */           double d2 = targetEntity.motionZ;
/* 1179 */           boolean flag2 = targetEntity.attackEntityFrom(DamageSource.causePlayerDamage(this), f);
/*      */           
/* 1181 */           if (flag2) {
/*      */             EntityLivingBase entityLivingBase;
/* 1183 */             if (i > 0) {
/*      */               
/* 1185 */               targetEntity.addVelocity((-MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F), 0.1D, (MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F) * i * 0.5F));
/* 1186 */               this.motionX *= 0.6D;
/* 1187 */               this.motionZ *= 0.6D;
/* 1188 */               setSprinting(false);
/*      */             } 
/*      */             
/* 1191 */             if (targetEntity instanceof EntityPlayerMP && targetEntity.velocityChanged) {
/*      */               
/* 1193 */               ((EntityPlayerMP)targetEntity).playerNetServerHandler.sendPacket((Packet)new S12PacketEntityVelocity(targetEntity));
/* 1194 */               targetEntity.velocityChanged = false;
/* 1195 */               targetEntity.motionX = d0;
/* 1196 */               targetEntity.motionY = d1;
/* 1197 */               targetEntity.motionZ = d2;
/*      */             } 
/*      */             
/* 1200 */             if (flag)
/*      */             {
/* 1202 */               onCriticalHit(targetEntity);
/*      */             }
/*      */             
/* 1205 */             if (f1 > 0.0F)
/*      */             {
/* 1207 */               onEnchantmentCritical(targetEntity);
/*      */             }
/*      */             
/* 1210 */             if (f >= 18.0F)
/*      */             {
/* 1212 */               triggerAchievement((StatBase)AchievementList.overkill);
/*      */             }
/*      */             
/* 1215 */             setLastAttacker(targetEntity);
/*      */             
/* 1217 */             if (targetEntity instanceof EntityLivingBase)
/*      */             {
/* 1219 */               EnchantmentHelper.applyThornEnchantments((EntityLivingBase)targetEntity, (Entity)this);
/*      */             }
/*      */             
/* 1222 */             EnchantmentHelper.applyArthropodEnchantments(this, targetEntity);
/* 1223 */             ItemStack itemstack = getCurrentEquippedItem();
/* 1224 */             Entity entity = targetEntity;
/*      */             
/* 1226 */             if (targetEntity instanceof EntityDragonPart) {
/*      */               
/* 1228 */               IEntityMultiPart ientitymultipart = ((EntityDragonPart)targetEntity).entityDragonObj;
/*      */               
/* 1230 */               if (ientitymultipart instanceof EntityLivingBase)
/*      */               {
/* 1232 */                 entityLivingBase = (EntityLivingBase)ientitymultipart;
/*      */               }
/*      */             } 
/*      */             
/* 1236 */             if (itemstack != null && entityLivingBase instanceof EntityLivingBase) {
/*      */               
/* 1238 */               itemstack.hitEntity(entityLivingBase, this);
/*      */               
/* 1240 */               if (itemstack.stackSize <= 0)
/*      */               {
/* 1242 */                 destroyCurrentEquippedItem();
/*      */               }
/*      */             } 
/*      */             
/* 1246 */             if (targetEntity instanceof EntityLivingBase) {
/*      */               
/* 1248 */               addStat(StatList.damageDealtStat, Math.round(f * 10.0F));
/*      */               
/* 1250 */               if (j > 0)
/*      */               {
/* 1252 */                 targetEntity.setFire(j * 4);
/*      */               }
/*      */             } 
/*      */             
/* 1256 */             addExhaustion(0.3F);
/*      */           }
/* 1258 */           else if (flag1) {
/*      */             
/* 1260 */             targetEntity.extinguish();
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void onCriticalHit(Entity entityHit) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void onEnchantmentCritical(Entity entityHit) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void respawnPlayer() {}
/*      */ 
/*      */   
/*      */   public void setDead() {
/* 1281 */     super.setDead();
/* 1282 */     this.inventoryContainer.onContainerClosed(this);
/*      */     
/* 1284 */     if (this.openContainer != null)
/*      */     {
/* 1286 */       this.openContainer.onContainerClosed(this);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isEntityInsideOpaqueBlock() {
/* 1292 */     return (!this.sleeping && super.isEntityInsideOpaqueBlock());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isUser() {
/* 1297 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public GameProfile getGameProfile() {
/* 1302 */     return this.gameProfile;
/*      */   }
/*      */ 
/*      */   
/*      */   public EnumStatus trySleep(BlockPos bedLocation) {
/* 1307 */     if (!this.worldObj.isRemote) {
/*      */       
/* 1309 */       if (isPlayerSleeping() || !isEntityAlive())
/*      */       {
/* 1311 */         return EnumStatus.OTHER_PROBLEM;
/*      */       }
/*      */       
/* 1314 */       if (!this.worldObj.provider.isSurfaceWorld())
/*      */       {
/* 1316 */         return EnumStatus.NOT_POSSIBLE_HERE;
/*      */       }
/*      */       
/* 1319 */       if (this.worldObj.isDaytime())
/*      */       {
/* 1321 */         return EnumStatus.NOT_POSSIBLE_NOW;
/*      */       }
/*      */       
/* 1324 */       if (Math.abs(this.posX - bedLocation.getX()) > 3.0D || Math.abs(this.posY - bedLocation.getY()) > 2.0D || Math.abs(this.posZ - bedLocation.getZ()) > 3.0D)
/*      */       {
/* 1326 */         return EnumStatus.TOO_FAR_AWAY;
/*      */       }
/*      */       
/* 1329 */       double d0 = 8.0D;
/* 1330 */       double d1 = 5.0D;
/* 1331 */       List<EntityMob> list = this.worldObj.getEntitiesWithinAABB(EntityMob.class, new AxisAlignedBB(bedLocation.getX() - d0, bedLocation.getY() - d1, bedLocation.getZ() - d0, bedLocation.getX() + d0, bedLocation.getY() + d1, bedLocation.getZ() + d0));
/*      */       
/* 1333 */       if (!list.isEmpty())
/*      */       {
/* 1335 */         return EnumStatus.NOT_SAFE;
/*      */       }
/*      */     } 
/*      */     
/* 1339 */     if (isRiding())
/*      */     {
/* 1341 */       mountEntity((Entity)null);
/*      */     }
/*      */     
/* 1344 */     setSize(0.2F, 0.2F);
/*      */     
/* 1346 */     if (this.worldObj.isBlockLoaded(bedLocation)) {
/*      */       
/* 1348 */       EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(bedLocation).getValue((IProperty)BlockDirectional.FACING);
/* 1349 */       float f = 0.5F;
/* 1350 */       float f1 = 0.5F;
/*      */       
/* 1352 */       switch (enumfacing) {
/*      */         
/*      */         case SOUTH:
/* 1355 */           f1 = 0.9F;
/*      */           break;
/*      */         
/*      */         case NORTH:
/* 1359 */           f1 = 0.1F;
/*      */           break;
/*      */         
/*      */         case WEST:
/* 1363 */           f = 0.1F;
/*      */           break;
/*      */         
/*      */         case EAST:
/* 1367 */           f = 0.9F;
/*      */           break;
/*      */       } 
/* 1370 */       func_175139_a(enumfacing);
/* 1371 */       setPosition((bedLocation.getX() + f), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + f1));
/*      */     }
/*      */     else {
/*      */       
/* 1375 */       setPosition((bedLocation.getX() + 0.5F), (bedLocation.getY() + 0.6875F), (bedLocation.getZ() + 0.5F));
/*      */     } 
/*      */     
/* 1378 */     this.sleeping = true;
/* 1379 */     this.sleepTimer = 0;
/* 1380 */     this.playerLocation = bedLocation;
/* 1381 */     this.motionX = this.motionZ = this.motionY = 0.0D;
/*      */     
/* 1383 */     if (!this.worldObj.isRemote)
/*      */     {
/* 1385 */       this.worldObj.updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1388 */     return EnumStatus.OK;
/*      */   }
/*      */ 
/*      */   
/*      */   private void func_175139_a(EnumFacing p_175139_1_) {
/* 1393 */     this.renderOffsetX = 0.0F;
/* 1394 */     this.renderOffsetZ = 0.0F;
/*      */     
/* 1396 */     switch (p_175139_1_) {
/*      */       
/*      */       case SOUTH:
/* 1399 */         this.renderOffsetZ = -1.8F;
/*      */         break;
/*      */       
/*      */       case NORTH:
/* 1403 */         this.renderOffsetZ = 1.8F;
/*      */         break;
/*      */       
/*      */       case WEST:
/* 1407 */         this.renderOffsetX = 1.8F;
/*      */         break;
/*      */       
/*      */       case EAST:
/* 1411 */         this.renderOffsetX = -1.8F;
/*      */         break;
/*      */     } 
/*      */   }
/*      */   
/*      */   public void wakeUpPlayer(boolean immediately, boolean updateWorldFlag, boolean setSpawn) {
/* 1417 */     setSize(0.6F, 1.8F);
/* 1418 */     IBlockState iblockstate = this.worldObj.getBlockState(this.playerLocation);
/*      */     
/* 1420 */     if (this.playerLocation != null && iblockstate.getBlock() == Blocks.bed) {
/*      */       
/* 1422 */       this.worldObj.setBlockState(this.playerLocation, iblockstate.withProperty((IProperty)BlockBed.OCCUPIED, Boolean.valueOf(false)), 4);
/* 1423 */       BlockPos blockpos = BlockBed.getSafeExitLocation(this.worldObj, this.playerLocation, 0);
/*      */       
/* 1425 */       if (blockpos == null)
/*      */       {
/* 1427 */         blockpos = this.playerLocation.up();
/*      */       }
/*      */       
/* 1430 */       setPosition((blockpos.getX() + 0.5F), (blockpos.getY() + 0.1F), (blockpos.getZ() + 0.5F));
/*      */     } 
/*      */     
/* 1433 */     this.sleeping = false;
/*      */     
/* 1435 */     if (!this.worldObj.isRemote && updateWorldFlag)
/*      */     {
/* 1437 */       this.worldObj.updateAllPlayersSleepingFlag();
/*      */     }
/*      */     
/* 1440 */     this.sleepTimer = immediately ? 0 : 100;
/*      */     
/* 1442 */     if (setSpawn)
/*      */     {
/* 1444 */       setSpawnPoint(this.playerLocation, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private boolean isInBed() {
/* 1450 */     return (this.worldObj.getBlockState(this.playerLocation).getBlock() == Blocks.bed);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BlockPos getBedSpawnLocation(World worldIn, BlockPos bedLocation, boolean forceSpawn) {
/* 1455 */     Block block = worldIn.getBlockState(bedLocation).getBlock();
/*      */     
/* 1457 */     if (block != Blocks.bed) {
/*      */       
/* 1459 */       if (!forceSpawn)
/*      */       {
/* 1461 */         return null;
/*      */       }
/*      */ 
/*      */       
/* 1465 */       boolean flag = block.canSpawnInBlock();
/* 1466 */       boolean flag1 = worldIn.getBlockState(bedLocation.up()).getBlock().canSpawnInBlock();
/* 1467 */       return (flag && flag1) ? bedLocation : null;
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1472 */     return BlockBed.getSafeExitLocation(worldIn, bedLocation, 0);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public float getBedOrientationInDegrees() {
/* 1478 */     if (this.playerLocation != null) {
/*      */       
/* 1480 */       EnumFacing enumfacing = (EnumFacing)this.worldObj.getBlockState(this.playerLocation).getValue((IProperty)BlockDirectional.FACING);
/*      */       
/* 1482 */       switch (enumfacing) {
/*      */         
/*      */         case SOUTH:
/* 1485 */           return 90.0F;
/*      */         
/*      */         case NORTH:
/* 1488 */           return 270.0F;
/*      */         
/*      */         case WEST:
/* 1491 */           return 0.0F;
/*      */         
/*      */         case EAST:
/* 1494 */           return 180.0F;
/*      */       } 
/*      */     
/*      */     } 
/* 1498 */     return 0.0F;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPlayerSleeping() {
/* 1503 */     return this.sleeping;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPlayerFullyAsleep() {
/* 1508 */     return (this.sleeping && this.sleepTimer >= 100);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getSleepTimer() {
/* 1513 */     return this.sleepTimer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addChatComponentMessage(IChatComponent chatComponent) {}
/*      */ 
/*      */   
/*      */   public BlockPos getBedLocation() {
/* 1522 */     return this.spawnChunk;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isSpawnForced() {
/* 1527 */     return this.spawnForced;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setSpawnPoint(BlockPos pos, boolean forced) {
/* 1532 */     if (pos != null) {
/*      */       
/* 1534 */       this.spawnChunk = pos;
/* 1535 */       this.spawnForced = forced;
/*      */     }
/*      */     else {
/*      */       
/* 1539 */       this.spawnChunk = null;
/* 1540 */       this.spawnForced = false;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void triggerAchievement(StatBase achievementIn) {
/* 1546 */     addStat(achievementIn, 1);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void addStat(StatBase stat, int amount) {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void func_175145_a(StatBase p_175145_1_) {}
/*      */ 
/*      */   
/*      */   public void jump() {
/* 1559 */     super.jump();
/* 1560 */     triggerAchievement(StatList.jumpStat);
/*      */     
/* 1562 */     if (isSprinting()) {
/*      */       
/* 1564 */       addExhaustion(0.8F);
/*      */     }
/*      */     else {
/*      */       
/* 1568 */       addExhaustion(0.2F);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void moveEntityWithHeading(float strafe, float forward) {
/* 1574 */     double d0 = this.posX;
/* 1575 */     double d1 = this.posY;
/* 1576 */     double d2 = this.posZ;
/*      */     
/* 1578 */     if (this.capabilities.isFlying && this.ridingEntity == null) {
/*      */       
/* 1580 */       double d3 = this.motionY;
/* 1581 */       float f = this.jumpMovementFactor;
/* 1582 */       this.jumpMovementFactor = this.capabilities.getFlySpeed() * (isSprinting() ? 2 : true);
/* 1583 */       super.moveEntityWithHeading(strafe, forward);
/* 1584 */       this.motionY = d3 * 0.6D;
/* 1585 */       this.jumpMovementFactor = f;
/*      */     }
/*      */     else {
/*      */       
/* 1589 */       super.moveEntityWithHeading(strafe, forward);
/*      */     } 
/*      */     
/* 1592 */     addMovementStat(this.posX - d0, this.posY - d1, this.posZ - d2);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAIMoveSpeed() {
/* 1597 */     return (float)getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addMovementStat(double p_71000_1_, double p_71000_3_, double p_71000_5_) {
/* 1602 */     if (this.ridingEntity == null)
/*      */     {
/* 1604 */       if (isInsideOfMaterial(Material.water)) {
/*      */         
/* 1606 */         int i = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_3_ * p_71000_3_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1608 */         if (i > 0)
/*      */         {
/* 1610 */           addStat(StatList.distanceDoveStat, i);
/* 1611 */           addExhaustion(0.015F * i * 0.01F);
/*      */         }
/*      */       
/* 1614 */       } else if (isInWater()) {
/*      */         
/* 1616 */         int j = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1618 */         if (j > 0)
/*      */         {
/* 1620 */           addStat(StatList.distanceSwumStat, j);
/* 1621 */           addExhaustion(0.015F * j * 0.01F);
/*      */         }
/*      */       
/* 1624 */       } else if (isOnLadder()) {
/*      */         
/* 1626 */         if (p_71000_3_ > 0.0D)
/*      */         {
/* 1628 */           addStat(StatList.distanceClimbedStat, (int)Math.round(p_71000_3_ * 100.0D));
/*      */         }
/*      */       }
/* 1631 */       else if (this.onGround) {
/*      */         
/* 1633 */         int k = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1635 */         if (k > 0) {
/*      */           
/* 1637 */           addStat(StatList.distanceWalkedStat, k);
/*      */           
/* 1639 */           if (isSprinting())
/*      */           {
/* 1641 */             addStat(StatList.distanceSprintedStat, k);
/* 1642 */             addExhaustion(0.099999994F * k * 0.01F);
/*      */           }
/*      */           else
/*      */           {
/* 1646 */             if (isSneaking())
/*      */             {
/* 1648 */               addStat(StatList.distanceCrouchedStat, k);
/*      */             }
/*      */             
/* 1651 */             addExhaustion(0.01F * k * 0.01F);
/*      */           }
/*      */         
/*      */         } 
/*      */       } else {
/*      */         
/* 1657 */         int l = Math.round(MathHelper.sqrt_double(p_71000_1_ * p_71000_1_ + p_71000_5_ * p_71000_5_) * 100.0F);
/*      */         
/* 1659 */         if (l > 25)
/*      */         {
/* 1661 */           addStat(StatList.distanceFlownStat, l);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   private void addMountedMovementStat(double p_71015_1_, double p_71015_3_, double p_71015_5_) {
/* 1669 */     if (this.ridingEntity != null) {
/*      */       
/* 1671 */       int i = Math.round(MathHelper.sqrt_double(p_71015_1_ * p_71015_1_ + p_71015_3_ * p_71015_3_ + p_71015_5_ * p_71015_5_) * 100.0F);
/*      */       
/* 1673 */       if (i > 0)
/*      */       {
/* 1675 */         if (this.ridingEntity instanceof net.minecraft.entity.item.EntityMinecart) {
/*      */           
/* 1677 */           addStat(StatList.distanceByMinecartStat, i);
/*      */           
/* 1679 */           if (this.startMinecartRidingCoordinate == null)
/*      */           {
/* 1681 */             this.startMinecartRidingCoordinate = new BlockPos((Entity)this);
/*      */           }
/* 1683 */           else if (this.startMinecartRidingCoordinate.distanceSq(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)) >= 1000000.0D)
/*      */           {
/* 1685 */             triggerAchievement((StatBase)AchievementList.onARail);
/*      */           }
/*      */         
/* 1688 */         } else if (this.ridingEntity instanceof net.minecraft.entity.item.EntityBoat) {
/*      */           
/* 1690 */           addStat(StatList.distanceByBoatStat, i);
/*      */         }
/* 1692 */         else if (this.ridingEntity instanceof EntityPig) {
/*      */           
/* 1694 */           addStat(StatList.distanceByPigStat, i);
/*      */         }
/* 1696 */         else if (this.ridingEntity instanceof EntityHorse) {
/*      */           
/* 1698 */           addStat(StatList.distanceByHorseStat, i);
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public void fall(float distance, float damageMultiplier) {
/* 1706 */     if (!this.capabilities.allowFlying) {
/*      */       
/* 1708 */       if (distance >= 2.0F)
/*      */       {
/* 1710 */         addStat(StatList.distanceFallenStat, (int)Math.round(distance * 100.0D));
/*      */       }
/*      */       
/* 1713 */       super.fall(distance, damageMultiplier);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   protected void resetHeight() {
/* 1719 */     if (!isSpectator())
/*      */     {
/* 1721 */       super.resetHeight();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected String getFallSoundString(int damageValue) {
/* 1727 */     return (damageValue > 4) ? "game.player.hurt.fall.big" : "game.player.hurt.fall.small";
/*      */   }
/*      */ 
/*      */   
/*      */   public void onKillEntity(EntityLivingBase entityLivingIn) {
/* 1732 */     if (entityLivingIn instanceof net.minecraft.entity.monster.IMob)
/*      */     {
/* 1734 */       triggerAchievement((StatBase)AchievementList.killEnemy);
/*      */     }
/*      */     
/* 1737 */     EntityList.EntityEggInfo entitylist$entityegginfo = (EntityList.EntityEggInfo)EntityList.entityEggs.get(Integer.valueOf(EntityList.getEntityID((Entity)entityLivingIn)));
/*      */     
/* 1739 */     if (entitylist$entityegginfo != null)
/*      */     {
/* 1741 */       triggerAchievement(entitylist$entityegginfo.field_151512_d);
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public void setInWeb() {
/* 1747 */     if (!this.capabilities.isFlying)
/*      */     {
/* 1749 */       super.setInWeb();
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getCurrentArmor(int slotIn) {
/* 1755 */     return this.inventory.armorItemInSlot(slotIn);
/*      */   }
/*      */ 
/*      */   
/*      */   public void addExperience(int amount) {
/* 1760 */     addScore(amount);
/* 1761 */     int i = Integer.MAX_VALUE - this.experienceTotal;
/*      */     
/* 1763 */     if (amount > i)
/*      */     {
/* 1765 */       amount = i;
/*      */     }
/*      */     
/* 1768 */     this.experience += amount / xpBarCap();
/*      */     
/* 1770 */     for (this.experienceTotal += amount; this.experience >= 1.0F; this.experience /= xpBarCap()) {
/*      */       
/* 1772 */       this.experience = (this.experience - 1.0F) * xpBarCap();
/* 1773 */       addExperienceLevel(1);
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int getXPSeed() {
/* 1779 */     return this.xpSeed;
/*      */   }
/*      */ 
/*      */   
/*      */   public void removeExperienceLevel(int levels) {
/* 1784 */     this.experienceLevel -= levels;
/*      */     
/* 1786 */     if (this.experienceLevel < 0) {
/*      */       
/* 1788 */       this.experienceLevel = 0;
/* 1789 */       this.experience = 0.0F;
/* 1790 */       this.experienceTotal = 0;
/*      */     } 
/*      */     
/* 1793 */     this.xpSeed = this.rand.nextInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public void addExperienceLevel(int levels) {
/* 1798 */     this.experienceLevel += levels;
/*      */     
/* 1800 */     if (this.experienceLevel < 0) {
/*      */       
/* 1802 */       this.experienceLevel = 0;
/* 1803 */       this.experience = 0.0F;
/* 1804 */       this.experienceTotal = 0;
/*      */     } 
/*      */     
/* 1807 */     if (levels > 0 && this.experienceLevel % 5 == 0 && this.lastXPSound < this.ticksExisted - 100.0F) {
/*      */       
/* 1809 */       float f = (this.experienceLevel > 30) ? 1.0F : (this.experienceLevel / 30.0F);
/* 1810 */       this.worldObj.playSoundAtEntity((Entity)this, "random.levelup", f * 0.75F, 1.0F);
/* 1811 */       this.lastXPSound = this.ticksExisted;
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public int xpBarCap() {
/* 1817 */     return (this.experienceLevel >= 30) ? (112 + (this.experienceLevel - 30) * 9) : ((this.experienceLevel >= 15) ? (37 + (this.experienceLevel - 15) * 5) : (7 + this.experienceLevel * 2));
/*      */   }
/*      */ 
/*      */   
/*      */   public void addExhaustion(float p_71020_1_) {
/* 1822 */     if (!this.capabilities.disableDamage)
/*      */     {
/* 1824 */       if (!this.worldObj.isRemote)
/*      */       {
/* 1826 */         this.foodStats.addExhaustion(p_71020_1_);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   public FoodStats getFoodStats() {
/* 1833 */     return this.foodStats;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canEat(boolean ignoreHunger) {
/* 1838 */     return ((ignoreHunger || this.foodStats.needFood()) && !this.capabilities.disableDamage);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean shouldHeal() {
/* 1843 */     return (getHealth() > 0.0F && getHealth() < getMaxHealth());
/*      */   }
/*      */ 
/*      */   
/*      */   public void setItemInUse(ItemStack stack, int duration) {
/* 1848 */     if (stack != this.itemInUse) {
/*      */       
/* 1850 */       this.itemInUse = stack;
/* 1851 */       this.itemInUseCount = duration;
/*      */       
/* 1853 */       if (!this.worldObj.isRemote)
/*      */       {
/* 1855 */         setEating(true);
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isAllowEdit() {
/* 1862 */     return this.capabilities.allowEdit;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canPlayerEdit(BlockPos p_175151_1_, EnumFacing p_175151_2_, ItemStack p_175151_3_) {
/* 1867 */     if (this.capabilities.allowEdit)
/*      */     {
/* 1869 */       return true;
/*      */     }
/* 1871 */     if (p_175151_3_ == null)
/*      */     {
/* 1873 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1877 */     BlockPos blockpos = p_175151_1_.offset(p_175151_2_.getOpposite());
/* 1878 */     Block block = this.worldObj.getBlockState(blockpos).getBlock();
/* 1879 */     return (p_175151_3_.canPlaceOn(block) || p_175151_3_.canEditBlocks());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected int getExperiencePoints(EntityPlayer player) {
/* 1885 */     if (this.worldObj.getGameRules().getBoolean("keepInventory"))
/*      */     {
/* 1887 */       return 0;
/*      */     }
/*      */ 
/*      */     
/* 1891 */     int i = this.experienceLevel * 7;
/* 1892 */     return (i > 100) ? 100 : i;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isPlayer() {
/* 1898 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getAlwaysRenderNameTagForRender() {
/* 1903 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public void clonePlayer(EntityPlayer oldPlayer, boolean respawnFromEnd) {
/* 1908 */     if (respawnFromEnd) {
/*      */       
/* 1910 */       this.inventory.copyInventory(oldPlayer.inventory);
/* 1911 */       setHealth(oldPlayer.getHealth());
/* 1912 */       this.foodStats = oldPlayer.foodStats;
/* 1913 */       this.experienceLevel = oldPlayer.experienceLevel;
/* 1914 */       this.experienceTotal = oldPlayer.experienceTotal;
/* 1915 */       this.experience = oldPlayer.experience;
/* 1916 */       setScore(oldPlayer.getScore());
/* 1917 */       this.lastPortalPos = oldPlayer.lastPortalPos;
/* 1918 */       this.lastPortalVec = oldPlayer.lastPortalVec;
/* 1919 */       this.teleportDirection = oldPlayer.teleportDirection;
/*      */     }
/* 1921 */     else if (this.worldObj.getGameRules().getBoolean("keepInventory")) {
/*      */       
/* 1923 */       this.inventory.copyInventory(oldPlayer.inventory);
/* 1924 */       this.experienceLevel = oldPlayer.experienceLevel;
/* 1925 */       this.experienceTotal = oldPlayer.experienceTotal;
/* 1926 */       this.experience = oldPlayer.experience;
/* 1927 */       setScore(oldPlayer.getScore());
/*      */     } 
/*      */     
/* 1930 */     this.xpSeed = oldPlayer.xpSeed;
/* 1931 */     this.theInventoryEnderChest = oldPlayer.theInventoryEnderChest;
/* 1932 */     getDataWatcher().updateObject(10, Byte.valueOf(oldPlayer.getDataWatcher().getWatchableObjectByte(10)));
/*      */   }
/*      */ 
/*      */   
/*      */   protected boolean canTriggerWalking() {
/* 1937 */     return !this.capabilities.isFlying;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void sendPlayerAbilities() {}
/*      */ 
/*      */ 
/*      */   
/*      */   public void setGameType(WorldSettings.GameType gameType) {}
/*      */ 
/*      */   
/*      */   public String getName() {
/* 1950 */     return this.gameProfile.getName();
/*      */   }
/*      */ 
/*      */   
/*      */   public InventoryEnderChest getInventoryEnderChest() {
/* 1955 */     return this.theInventoryEnderChest;
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getEquipmentInSlot(int slotIn) {
/* 1960 */     return (slotIn == 0) ? this.inventory.getCurrentItem() : this.inventory.armorInventory[slotIn - 1];
/*      */   }
/*      */ 
/*      */   
/*      */   public ItemStack getHeldItem() {
/* 1965 */     return this.inventory.getCurrentItem();
/*      */   }
/*      */ 
/*      */   
/*      */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
/* 1970 */     this.inventory.armorInventory[slotIn] = stack;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isInvisibleToPlayer(EntityPlayer player) {
/* 1975 */     if (!isInvisible())
/*      */     {
/* 1977 */       return false;
/*      */     }
/* 1979 */     if (player.isSpectator())
/*      */     {
/* 1981 */       return false;
/*      */     }
/*      */ 
/*      */     
/* 1985 */     Team team = getTeam();
/* 1986 */     return (team == null || player == null || player.getTeam() != team || !team.getSeeFriendlyInvisiblesEnabled());
/*      */   }
/*      */ 
/*      */   
/*      */   public abstract boolean isSpectator();
/*      */ 
/*      */   
/*      */   public ItemStack[] getInventory() {
/* 1994 */     return this.inventory.armorInventory;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isPushedByWater() {
/* 1999 */     return !this.capabilities.isFlying;
/*      */   }
/*      */ 
/*      */   
/*      */   public Scoreboard getWorldScoreboard() {
/* 2004 */     return this.worldObj.getScoreboard();
/*      */   }
/*      */ 
/*      */   
/*      */   public Team getTeam() {
/* 2009 */     return (Team)getWorldScoreboard().getPlayersTeam(getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public IChatComponent getDisplayName() {
/* 2014 */     ChatComponentText chatComponentText = new ChatComponentText(ScorePlayerTeam.formatPlayerName(getTeam(), getName()));
/* 2015 */     chatComponentText.getChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/msg " + getName() + " "));
/* 2016 */     chatComponentText.getChatStyle().setChatHoverEvent(getHoverEvent());
/* 2017 */     chatComponentText.getChatStyle().setInsertion(getName());
/* 2018 */     return (IChatComponent)chatComponentText;
/*      */   }
/*      */ 
/*      */   
/*      */   public float getEyeHeight() {
/* 2023 */     float f = 1.62F;
/*      */     
/* 2025 */     if (isPlayerSleeping())
/*      */     {
/* 2027 */       f = 0.2F;
/*      */     }
/*      */     
/* 2030 */     if (isSneaking())
/*      */     {
/* 2032 */       f -= 0.08F;
/*      */     }
/*      */     
/* 2035 */     return f;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setAbsorptionAmount(float amount) {
/* 2040 */     if (amount < 0.0F)
/*      */     {
/* 2042 */       amount = 0.0F;
/*      */     }
/*      */     
/* 2045 */     getDataWatcher().updateObject(17, Float.valueOf(amount));
/*      */   }
/*      */ 
/*      */   
/*      */   public float getAbsorptionAmount() {
/* 2050 */     return getDataWatcher().getWatchableObjectFloat(17);
/*      */   }
/*      */ 
/*      */   
/*      */   public static UUID getUUID(GameProfile profile) {
/* 2055 */     UUID uuid = profile.getId();
/*      */     
/* 2057 */     if (uuid == null)
/*      */     {
/* 2059 */       uuid = getOfflineUUID(profile.getName());
/*      */     }
/*      */     
/* 2062 */     return uuid;
/*      */   }
/*      */ 
/*      */   
/*      */   public static UUID getOfflineUUID(String username) {
/* 2067 */     return UUID.nameUUIDFromBytes(("OfflinePlayer:" + username).getBytes(Charsets.UTF_8));
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean canOpen(LockCode code) {
/* 2072 */     if (code.isEmpty())
/*      */     {
/* 2074 */       return true;
/*      */     }
/*      */ 
/*      */     
/* 2078 */     ItemStack itemstack = getCurrentEquippedItem();
/* 2079 */     return (itemstack != null && itemstack.hasDisplayName()) ? itemstack.getDisplayName().equals(code.getLock()) : false;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isWearing(EnumPlayerModelParts p_175148_1_) {
/* 2085 */     return ((getDataWatcher().getWatchableObjectByte(10) & p_175148_1_.getPartMask()) == p_175148_1_.getPartMask());
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean sendCommandFeedback() {
/* 2090 */     return (MinecraftServer.getServer()).worldServers[0].getGameRules().getBoolean("sendCommandFeedback");
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/* 2095 */     if (inventorySlot >= 0 && inventorySlot < this.inventory.mainInventory.length) {
/*      */       
/* 2097 */       this.inventory.setInventorySlotContents(inventorySlot, itemStackIn);
/* 2098 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2102 */     int i = inventorySlot - 100;
/*      */     
/* 2104 */     if (i >= 0 && i < this.inventory.armorInventory.length) {
/*      */       
/* 2106 */       int k = i + 1;
/*      */       
/* 2108 */       if (itemStackIn != null && itemStackIn.getItem() != null)
/*      */       {
/* 2110 */         if (itemStackIn.getItem() instanceof net.minecraft.item.ItemArmor) {
/*      */           
/* 2112 */           if (EntityLiving.getArmorPosition(itemStackIn) != k)
/*      */           {
/* 2114 */             return false;
/*      */           }
/*      */         }
/* 2117 */         else if (k != 4 || (itemStackIn.getItem() != Items.skull && !(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock))) {
/*      */           
/* 2119 */           return false;
/*      */         } 
/*      */       }
/*      */       
/* 2123 */       this.inventory.setInventorySlotContents(i + this.inventory.mainInventory.length, itemStackIn);
/* 2124 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2128 */     int j = inventorySlot - 200;
/*      */     
/* 2130 */     if (j >= 0 && j < this.theInventoryEnderChest.getSizeInventory()) {
/*      */       
/* 2132 */       this.theInventoryEnderChest.setInventorySlotContents(j, itemStackIn);
/* 2133 */       return true;
/*      */     } 
/*      */ 
/*      */     
/* 2137 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean hasReducedDebug() {
/* 2145 */     return this.hasReducedDebug;
/*      */   }
/*      */ 
/*      */   
/*      */   public void setReducedDebug(boolean reducedDebug) {
/* 2150 */     this.hasReducedDebug = reducedDebug;
/*      */   }
/*      */   
/*      */   public enum EnumChatVisibility
/*      */   {
/* 2155 */     FULL(0, "options.chat.visibility.full"),
/* 2156 */     SYSTEM(1, "options.chat.visibility.system"),
/* 2157 */     HIDDEN(2, "options.chat.visibility.hidden");
/*      */     
/* 2159 */     private static final EnumChatVisibility[] ID_LOOKUP = new EnumChatVisibility[(values()).length];
/*      */     
/*      */     private final int chatVisibility;
/*      */     
/*      */     private final String resourceKey;
/*      */ 
/*      */     
/*      */     EnumChatVisibility(int id, String resourceKey) {
/*      */       this.chatVisibility = id;
/*      */       this.resourceKey = resourceKey;
/*      */     }
/*      */ 
/*      */     
/*      */     public int getChatVisibility() {
/*      */       return this.chatVisibility;
/*      */     }
/*      */     
/*      */     public static EnumChatVisibility getEnumChatVisibility(int id) {
/*      */       return ID_LOOKUP[id % ID_LOOKUP.length];
/*      */     }
/*      */     
/*      */     public String getResourceKey() {
/*      */       return this.resourceKey;
/*      */     }
/*      */     
/*      */     static {
/* 2185 */       for (EnumChatVisibility entityplayer$enumchatvisibility : values())
/*      */       {
/* 2187 */         ID_LOOKUP[entityplayer$enumchatvisibility.chatVisibility] = entityplayer$enumchatvisibility;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public enum EnumStatus
/*      */   {
/* 2194 */     OK,
/* 2195 */     NOT_POSSIBLE_HERE,
/* 2196 */     NOT_POSSIBLE_NOW,
/* 2197 */     TOO_FAR_AWAY,
/* 2198 */     OTHER_PROBLEM,
/* 2199 */     NOT_SAFE;
/*      */   }
/*      */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\player\EntityPlayer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */