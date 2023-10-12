/*     */ package net.minecraft.entity.item;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.World;
/*     */ import org.apache.logging.log4j.LogManager;
/*     */ import org.apache.logging.log4j.Logger;
/*     */ 
/*     */ public class EntityItem extends Entity {
/*  22 */   private static final Logger logger = LogManager.getLogger();
/*     */   
/*     */   private int age;
/*     */   private int delayBeforeCanPickup;
/*     */   private int health;
/*     */   private String thrower;
/*     */   private String owner;
/*     */   public float hoverStart;
/*     */   
/*     */   public EntityItem(World worldIn, double x, double y, double z) {
/*  32 */     super(worldIn);
/*  33 */     this.health = 5;
/*  34 */     this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
/*  35 */     setSize(0.25F, 0.25F);
/*  36 */     setPosition(x, y, z);
/*  37 */     this.rotationYaw = (float)(Math.random() * 360.0D);
/*  38 */     this.motionX = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
/*  39 */     this.motionY = 0.20000000298023224D;
/*  40 */     this.motionZ = (float)(Math.random() * 0.20000000298023224D - 0.10000000149011612D);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityItem(World worldIn, double x, double y, double z, ItemStack stack) {
/*  45 */     this(worldIn, x, y, z);
/*  46 */     setEntityItemStack(stack);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  51 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityItem(World worldIn) {
/*  56 */     super(worldIn);
/*  57 */     this.health = 5;
/*  58 */     this.hoverStart = (float)(Math.random() * Math.PI * 2.0D);
/*  59 */     setSize(0.25F, 0.25F);
/*  60 */     setEntityItemStack(new ItemStack(Blocks.air, 0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  65 */     getDataWatcher().addObjectByDataType(10, 5);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  70 */     if (getEntityItem() == null) {
/*     */       
/*  72 */       setDead();
/*     */     }
/*     */     else {
/*     */       
/*  76 */       super.onUpdate();
/*     */       
/*  78 */       if (this.delayBeforeCanPickup > 0 && this.delayBeforeCanPickup != 32767)
/*     */       {
/*  80 */         this.delayBeforeCanPickup--;
/*     */       }
/*     */       
/*  83 */       this.prevPosX = this.posX;
/*  84 */       this.prevPosY = this.posY;
/*  85 */       this.prevPosZ = this.posZ;
/*  86 */       this.motionY -= 0.03999999910593033D;
/*  87 */       this.noClip = pushOutOfBlocks(this.posX, ((getEntityBoundingBox()).minY + (getEntityBoundingBox()).maxY) / 2.0D, this.posZ);
/*  88 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/*  89 */       boolean flag = ((int)this.prevPosX != (int)this.posX || (int)this.prevPosY != (int)this.posY || (int)this.prevPosZ != (int)this.posZ);
/*     */       
/*  91 */       if (flag || this.ticksExisted % 25 == 0) {
/*     */         
/*  93 */         if (this.worldObj.getBlockState(new BlockPos(this)).getBlock().getMaterial() == Material.lava) {
/*     */           
/*  95 */           this.motionY = 0.20000000298023224D;
/*  96 */           this.motionX = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*  97 */           this.motionZ = ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F);
/*  98 */           playSound("random.fizz", 0.4F, 2.0F + this.rand.nextFloat() * 0.4F);
/*     */         } 
/*     */         
/* 101 */         if (!this.worldObj.isRemote)
/*     */         {
/* 103 */           searchForOtherItemsNearby();
/*     */         }
/*     */       } 
/*     */       
/* 107 */       float f = 0.98F;
/*     */       
/* 109 */       if (this.onGround)
/*     */       {
/* 111 */         f = (this.worldObj.getBlockState(new BlockPos(MathHelper.floor_double(this.posX), MathHelper.floor_double((getEntityBoundingBox()).minY) - 1, MathHelper.floor_double(this.posZ))).getBlock()).slipperiness * 0.98F;
/*     */       }
/*     */       
/* 114 */       this.motionX *= f;
/* 115 */       this.motionY *= 0.9800000190734863D;
/* 116 */       this.motionZ *= f;
/*     */       
/* 118 */       if (this.onGround)
/*     */       {
/* 120 */         this.motionY *= -0.5D;
/*     */       }
/*     */       
/* 123 */       if (this.age != -32768)
/*     */       {
/* 125 */         this.age++;
/*     */       }
/*     */       
/* 128 */       handleWaterMovement();
/*     */       
/* 130 */       if (!this.worldObj.isRemote && this.age >= 6000)
/*     */       {
/* 132 */         setDead();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void searchForOtherItemsNearby() {
/* 139 */     for (EntityItem entityitem : this.worldObj.getEntitiesWithinAABB(EntityItem.class, getEntityBoundingBox().expand(0.5D, 0.0D, 0.5D)))
/*     */     {
/* 141 */       combineItems(entityitem);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean combineItems(EntityItem other) {
/* 147 */     if (other == this)
/*     */     {
/* 149 */       return false;
/*     */     }
/* 151 */     if (other.isEntityAlive() && isEntityAlive()) {
/*     */       
/* 153 */       ItemStack itemstack = getEntityItem();
/* 154 */       ItemStack itemstack1 = other.getEntityItem();
/*     */       
/* 156 */       if (this.delayBeforeCanPickup != 32767 && other.delayBeforeCanPickup != 32767) {
/*     */         
/* 158 */         if (this.age != -32768 && other.age != -32768) {
/*     */           
/* 160 */           if (itemstack1.getItem() != itemstack.getItem())
/*     */           {
/* 162 */             return false;
/*     */           }
/* 164 */           if ((itemstack1.hasTagCompound() ^ itemstack.hasTagCompound()) != 0)
/*     */           {
/* 166 */             return false;
/*     */           }
/* 168 */           if (itemstack1.hasTagCompound() && !itemstack1.getTagCompound().equals(itemstack.getTagCompound()))
/*     */           {
/* 170 */             return false;
/*     */           }
/* 172 */           if (itemstack1.getItem() == null)
/*     */           {
/* 174 */             return false;
/*     */           }
/* 176 */           if (itemstack1.getItem().getHasSubtypes() && itemstack1.getMetadata() != itemstack.getMetadata())
/*     */           {
/* 178 */             return false;
/*     */           }
/* 180 */           if (itemstack1.stackSize < itemstack.stackSize)
/*     */           {
/* 182 */             return other.combineItems(this);
/*     */           }
/* 184 */           if (itemstack1.stackSize + itemstack.stackSize > itemstack1.getMaxStackSize())
/*     */           {
/* 186 */             return false;
/*     */           }
/*     */ 
/*     */           
/* 190 */           itemstack1.stackSize += itemstack.stackSize;
/* 191 */           other.delayBeforeCanPickup = Math.max(other.delayBeforeCanPickup, this.delayBeforeCanPickup);
/* 192 */           other.age = Math.min(other.age, this.age);
/* 193 */           other.setEntityItemStack(itemstack1);
/* 194 */           setDead();
/* 195 */           return true;
/*     */         } 
/*     */ 
/*     */ 
/*     */         
/* 200 */         return false;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 205 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 210 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAgeToCreativeDespawnTime() {
/* 216 */     this.age = 4800;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean handleWaterMovement() {
/* 221 */     if (this.worldObj.handleMaterialAcceleration(getEntityBoundingBox(), Material.water, this)) {
/*     */       
/* 223 */       if (!this.inWater && !this.firstUpdate)
/*     */       {
/* 225 */         resetHeight();
/*     */       }
/*     */       
/* 228 */       this.inWater = true;
/*     */     }
/*     */     else {
/*     */       
/* 232 */       this.inWater = false;
/*     */     } 
/*     */     
/* 235 */     return this.inWater;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dealFireDamage(int amount) {
/* 240 */     attackEntityFrom(DamageSource.inFire, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 245 */     if (isEntityInvulnerable(source))
/*     */     {
/* 247 */       return false;
/*     */     }
/* 249 */     if (getEntityItem() != null && getEntityItem().getItem() == Items.nether_star && source.isExplosion())
/*     */     {
/* 251 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 255 */     setBeenAttacked();
/* 256 */     this.health = (int)(this.health - amount);
/*     */     
/* 258 */     if (this.health <= 0)
/*     */     {
/* 260 */       setDead();
/*     */     }
/*     */     
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 269 */     tagCompound.setShort("Health", (short)(byte)this.health);
/* 270 */     tagCompound.setShort("Age", (short)this.age);
/* 271 */     tagCompound.setShort("PickupDelay", (short)this.delayBeforeCanPickup);
/*     */     
/* 273 */     if (getThrower() != null)
/*     */     {
/* 275 */       tagCompound.setString("Thrower", this.thrower);
/*     */     }
/*     */     
/* 278 */     if (getOwner() != null)
/*     */     {
/* 280 */       tagCompound.setString("Owner", this.owner);
/*     */     }
/*     */     
/* 283 */     if (getEntityItem() != null)
/*     */     {
/* 285 */       tagCompound.setTag("Item", (NBTBase)getEntityItem().writeToNBT(new NBTTagCompound()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 291 */     this.health = tagCompund.getShort("Health") & 0xFF;
/* 292 */     this.age = tagCompund.getShort("Age");
/*     */     
/* 294 */     if (tagCompund.hasKey("PickupDelay"))
/*     */     {
/* 296 */       this.delayBeforeCanPickup = tagCompund.getShort("PickupDelay");
/*     */     }
/*     */     
/* 299 */     if (tagCompund.hasKey("Owner"))
/*     */     {
/* 301 */       this.owner = tagCompund.getString("Owner");
/*     */     }
/*     */     
/* 304 */     if (tagCompund.hasKey("Thrower"))
/*     */     {
/* 306 */       this.thrower = tagCompund.getString("Thrower");
/*     */     }
/*     */     
/* 309 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Item");
/* 310 */     setEntityItemStack(ItemStack.loadItemStackFromNBT(nbttagcompound));
/*     */     
/* 312 */     if (getEntityItem() == null)
/*     */     {
/* 314 */       setDead();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn) {
/* 320 */     if (!this.worldObj.isRemote) {
/*     */       
/* 322 */       ItemStack itemstack = getEntityItem();
/* 323 */       int i = itemstack.stackSize;
/*     */       
/* 325 */       if (this.delayBeforeCanPickup == 0 && (this.owner == null || 6000 - this.age <= 200 || this.owner.equals(entityIn.getName())) && entityIn.inventory.addItemStackToInventory(itemstack)) {
/*     */         
/* 327 */         if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log))
/*     */         {
/* 329 */           entityIn.triggerAchievement((StatBase)AchievementList.mineWood);
/*     */         }
/*     */         
/* 332 */         if (itemstack.getItem() == Item.getItemFromBlock(Blocks.log2))
/*     */         {
/* 334 */           entityIn.triggerAchievement((StatBase)AchievementList.mineWood);
/*     */         }
/*     */         
/* 337 */         if (itemstack.getItem() == Items.leather)
/*     */         {
/* 339 */           entityIn.triggerAchievement((StatBase)AchievementList.killCow);
/*     */         }
/*     */         
/* 342 */         if (itemstack.getItem() == Items.diamond)
/*     */         {
/* 344 */           entityIn.triggerAchievement((StatBase)AchievementList.diamonds);
/*     */         }
/*     */         
/* 347 */         if (itemstack.getItem() == Items.blaze_rod)
/*     */         {
/* 349 */           entityIn.triggerAchievement((StatBase)AchievementList.blazeRod);
/*     */         }
/*     */         
/* 352 */         if (itemstack.getItem() == Items.diamond && getThrower() != null) {
/*     */           
/* 354 */           EntityPlayer entityplayer = this.worldObj.getPlayerEntityByName(getThrower());
/*     */           
/* 356 */           if (entityplayer != null && entityplayer != entityIn)
/*     */           {
/* 358 */             entityplayer.triggerAchievement((StatBase)AchievementList.diamondsToYou);
/*     */           }
/*     */         } 
/*     */         
/* 362 */         if (!isSilent())
/*     */         {
/* 364 */           this.worldObj.playSoundAtEntity((Entity)entityIn, "random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/*     */         }
/*     */         
/* 367 */         entityIn.onItemPickup(this, i);
/*     */         
/* 369 */         if (itemstack.stackSize <= 0)
/*     */         {
/* 371 */           setDead();
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 379 */     return hasCustomName() ? getCustomNameTag() : StatCollector.translateToLocal("item." + getEntityItem().getUnlocalizedName());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 384 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void travelToDimension(int dimensionId) {
/* 389 */     super.travelToDimension(dimensionId);
/*     */     
/* 391 */     if (!this.worldObj.isRemote)
/*     */     {
/* 393 */       searchForOtherItemsNearby();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getEntityItem() {
/* 399 */     ItemStack itemstack = getDataWatcher().getWatchableObjectItemStack(10);
/*     */     
/* 401 */     if (itemstack == null) {
/*     */       
/* 403 */       if (this.worldObj != null)
/*     */       {
/* 405 */         logger.error("Item entity " + getEntityId() + " has no item?!");
/*     */       }
/*     */       
/* 408 */       return new ItemStack(Blocks.stone);
/*     */     } 
/*     */ 
/*     */     
/* 412 */     return itemstack;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setEntityItemStack(ItemStack stack) {
/* 418 */     getDataWatcher().updateObject(10, stack);
/* 419 */     getDataWatcher().setObjectWatched(10);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOwner() {
/* 424 */     return this.owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOwner(String owner) {
/* 429 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThrower() {
/* 434 */     return this.thrower;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThrower(String thrower) {
/* 439 */     this.thrower = thrower;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAge() {
/* 444 */     return this.age;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDefaultPickupDelay() {
/* 449 */     this.delayBeforeCanPickup = 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNoPickupDelay() {
/* 454 */     this.delayBeforeCanPickup = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInfinitePickupDelay() {
/* 459 */     this.delayBeforeCanPickup = 32767;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPickupDelay(int ticks) {
/* 464 */     this.delayBeforeCanPickup = ticks;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean cannotPickup() {
/* 469 */     return (this.delayBeforeCanPickup > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNoDespawn() {
/* 474 */     this.age = -6000;
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_174870_v() {
/* 479 */     setInfinitePickupDelay();
/* 480 */     this.age = 5999;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */