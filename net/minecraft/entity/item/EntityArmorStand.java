/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemArmor;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.Rotations;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class EntityArmorStand
/*     */   extends EntityLivingBase
/*     */ {
/*  28 */   private static final Rotations DEFAULT_HEAD_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
/*  29 */   private static final Rotations DEFAULT_BODY_ROTATION = new Rotations(0.0F, 0.0F, 0.0F);
/*  30 */   private static final Rotations DEFAULT_LEFTARM_ROTATION = new Rotations(-10.0F, 0.0F, -10.0F);
/*  31 */   private static final Rotations DEFAULT_RIGHTARM_ROTATION = new Rotations(-15.0F, 0.0F, 10.0F);
/*  32 */   private static final Rotations DEFAULT_LEFTLEG_ROTATION = new Rotations(-1.0F, 0.0F, -1.0F);
/*  33 */   private static final Rotations DEFAULT_RIGHTLEG_ROTATION = new Rotations(1.0F, 0.0F, 1.0F);
/*     */   
/*     */   private final ItemStack[] contents;
/*     */   private boolean canInteract;
/*     */   private long punchCooldown;
/*     */   private int disabledSlots;
/*     */   private boolean field_181028_bj;
/*     */   private Rotations headRotation;
/*     */   private Rotations bodyRotation;
/*     */   private Rotations leftArmRotation;
/*     */   private Rotations rightArmRotation;
/*     */   private Rotations leftLegRotation;
/*     */   private Rotations rightLegRotation;
/*     */   
/*     */   public EntityArmorStand(World worldIn) {
/*  48 */     super(worldIn);
/*  49 */     this.contents = new ItemStack[5];
/*  50 */     this.headRotation = DEFAULT_HEAD_ROTATION;
/*  51 */     this.bodyRotation = DEFAULT_BODY_ROTATION;
/*  52 */     this.leftArmRotation = DEFAULT_LEFTARM_ROTATION;
/*  53 */     this.rightArmRotation = DEFAULT_RIGHTARM_ROTATION;
/*  54 */     this.leftLegRotation = DEFAULT_LEFTLEG_ROTATION;
/*  55 */     this.rightLegRotation = DEFAULT_RIGHTLEG_ROTATION;
/*  56 */     setSilent(true);
/*  57 */     this.noClip = hasNoGravity();
/*  58 */     setSize(0.5F, 1.975F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityArmorStand(World worldIn, double posX, double posY, double posZ) {
/*  63 */     this(worldIn);
/*  64 */     setPosition(posX, posY, posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isServerWorld() {
/*  69 */     return (super.isServerWorld() && !hasNoGravity());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  74 */     super.entityInit();
/*  75 */     this.dataWatcher.addObject(10, Byte.valueOf((byte)0));
/*  76 */     this.dataWatcher.addObject(11, DEFAULT_HEAD_ROTATION);
/*  77 */     this.dataWatcher.addObject(12, DEFAULT_BODY_ROTATION);
/*  78 */     this.dataWatcher.addObject(13, DEFAULT_LEFTARM_ROTATION);
/*  79 */     this.dataWatcher.addObject(14, DEFAULT_RIGHTARM_ROTATION);
/*  80 */     this.dataWatcher.addObject(15, DEFAULT_LEFTLEG_ROTATION);
/*  81 */     this.dataWatcher.addObject(16, DEFAULT_RIGHTLEG_ROTATION);
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getHeldItem() {
/*  86 */     return this.contents[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getEquipmentInSlot(int slotIn) {
/*  91 */     return this.contents[slotIn];
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCurrentArmor(int slotIn) {
/*  96 */     return this.contents[slotIn + 1];
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
/* 101 */     this.contents[slotIn] = stack;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack[] getInventory() {
/* 106 */     return this.contents;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean replaceItemInInventory(int inventorySlot, ItemStack itemStackIn) {
/*     */     int i;
/* 113 */     if (inventorySlot == 99) {
/*     */       
/* 115 */       i = 0;
/*     */     }
/*     */     else {
/*     */       
/* 119 */       i = inventorySlot - 100 + 1;
/*     */       
/* 121 */       if (i < 0 || i >= this.contents.length)
/*     */       {
/* 123 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 127 */     if (itemStackIn != null && EntityLiving.getArmorPosition(itemStackIn) != i && (i != 4 || !(itemStackIn.getItem() instanceof net.minecraft.item.ItemBlock)))
/*     */     {
/* 129 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 133 */     setCurrentItemOrArmor(i, itemStackIn);
/* 134 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 140 */     super.writeEntityToNBT(tagCompound);
/* 141 */     NBTTagList nbttaglist = new NBTTagList();
/*     */     
/* 143 */     for (int i = 0; i < this.contents.length; i++) {
/*     */       
/* 145 */       NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */       
/* 147 */       if (this.contents[i] != null)
/*     */       {
/* 149 */         this.contents[i].writeToNBT(nbttagcompound);
/*     */       }
/*     */       
/* 152 */       nbttaglist.appendTag((NBTBase)nbttagcompound);
/*     */     } 
/*     */     
/* 155 */     tagCompound.setTag("Equipment", (NBTBase)nbttaglist);
/*     */     
/* 157 */     if (getAlwaysRenderNameTag() && (getCustomNameTag() == null || getCustomNameTag().length() == 0))
/*     */     {
/* 159 */       tagCompound.setBoolean("CustomNameVisible", getAlwaysRenderNameTag());
/*     */     }
/*     */     
/* 162 */     tagCompound.setBoolean("Invisible", isInvisible());
/* 163 */     tagCompound.setBoolean("Small", isSmall());
/* 164 */     tagCompound.setBoolean("ShowArms", getShowArms());
/* 165 */     tagCompound.setInteger("DisabledSlots", this.disabledSlots);
/* 166 */     tagCompound.setBoolean("NoGravity", hasNoGravity());
/* 167 */     tagCompound.setBoolean("NoBasePlate", hasNoBasePlate());
/*     */     
/* 169 */     if (hasMarker())
/*     */     {
/* 171 */       tagCompound.setBoolean("Marker", hasMarker());
/*     */     }
/*     */     
/* 174 */     tagCompound.setTag("Pose", (NBTBase)readPoseFromNBT());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 179 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 181 */     if (tagCompund.hasKey("Equipment", 9)) {
/*     */       
/* 183 */       NBTTagList nbttaglist = tagCompund.getTagList("Equipment", 10);
/*     */       
/* 185 */       for (int i = 0; i < this.contents.length; i++)
/*     */       {
/* 187 */         this.contents[i] = ItemStack.loadItemStackFromNBT(nbttaglist.getCompoundTagAt(i));
/*     */       }
/*     */     } 
/*     */     
/* 191 */     setInvisible(tagCompund.getBoolean("Invisible"));
/* 192 */     setSmall(tagCompund.getBoolean("Small"));
/* 193 */     setShowArms(tagCompund.getBoolean("ShowArms"));
/* 194 */     this.disabledSlots = tagCompund.getInteger("DisabledSlots");
/* 195 */     setNoGravity(tagCompund.getBoolean("NoGravity"));
/* 196 */     setNoBasePlate(tagCompund.getBoolean("NoBasePlate"));
/* 197 */     setMarker(tagCompund.getBoolean("Marker"));
/* 198 */     this.field_181028_bj = !hasMarker();
/* 199 */     this.noClip = hasNoGravity();
/* 200 */     NBTTagCompound nbttagcompound = tagCompund.getCompoundTag("Pose");
/* 201 */     writePoseToNBT(nbttagcompound);
/*     */   }
/*     */ 
/*     */   
/*     */   private void writePoseToNBT(NBTTagCompound tagCompound) {
/* 206 */     NBTTagList nbttaglist = tagCompound.getTagList("Head", 5);
/*     */     
/* 208 */     if (nbttaglist.tagCount() > 0) {
/*     */       
/* 210 */       setHeadRotation(new Rotations(nbttaglist));
/*     */     }
/*     */     else {
/*     */       
/* 214 */       setHeadRotation(DEFAULT_HEAD_ROTATION);
/*     */     } 
/*     */     
/* 217 */     NBTTagList nbttaglist1 = tagCompound.getTagList("Body", 5);
/*     */     
/* 219 */     if (nbttaglist1.tagCount() > 0) {
/*     */       
/* 221 */       setBodyRotation(new Rotations(nbttaglist1));
/*     */     }
/*     */     else {
/*     */       
/* 225 */       setBodyRotation(DEFAULT_BODY_ROTATION);
/*     */     } 
/*     */     
/* 228 */     NBTTagList nbttaglist2 = tagCompound.getTagList("LeftArm", 5);
/*     */     
/* 230 */     if (nbttaglist2.tagCount() > 0) {
/*     */       
/* 232 */       setLeftArmRotation(new Rotations(nbttaglist2));
/*     */     }
/*     */     else {
/*     */       
/* 236 */       setLeftArmRotation(DEFAULT_LEFTARM_ROTATION);
/*     */     } 
/*     */     
/* 239 */     NBTTagList nbttaglist3 = tagCompound.getTagList("RightArm", 5);
/*     */     
/* 241 */     if (nbttaglist3.tagCount() > 0) {
/*     */       
/* 243 */       setRightArmRotation(new Rotations(nbttaglist3));
/*     */     }
/*     */     else {
/*     */       
/* 247 */       setRightArmRotation(DEFAULT_RIGHTARM_ROTATION);
/*     */     } 
/*     */     
/* 250 */     NBTTagList nbttaglist4 = tagCompound.getTagList("LeftLeg", 5);
/*     */     
/* 252 */     if (nbttaglist4.tagCount() > 0) {
/*     */       
/* 254 */       setLeftLegRotation(new Rotations(nbttaglist4));
/*     */     }
/*     */     else {
/*     */       
/* 258 */       setLeftLegRotation(DEFAULT_LEFTLEG_ROTATION);
/*     */     } 
/*     */     
/* 261 */     NBTTagList nbttaglist5 = tagCompound.getTagList("RightLeg", 5);
/*     */     
/* 263 */     if (nbttaglist5.tagCount() > 0) {
/*     */       
/* 265 */       setRightLegRotation(new Rotations(nbttaglist5));
/*     */     }
/*     */     else {
/*     */       
/* 269 */       setRightLegRotation(DEFAULT_RIGHTLEG_ROTATION);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private NBTTagCompound readPoseFromNBT() {
/* 275 */     NBTTagCompound nbttagcompound = new NBTTagCompound();
/*     */     
/* 277 */     if (!DEFAULT_HEAD_ROTATION.equals(this.headRotation))
/*     */     {
/* 279 */       nbttagcompound.setTag("Head", (NBTBase)this.headRotation.writeToNBT());
/*     */     }
/*     */     
/* 282 */     if (!DEFAULT_BODY_ROTATION.equals(this.bodyRotation))
/*     */     {
/* 284 */       nbttagcompound.setTag("Body", (NBTBase)this.bodyRotation.writeToNBT());
/*     */     }
/*     */     
/* 287 */     if (!DEFAULT_LEFTARM_ROTATION.equals(this.leftArmRotation))
/*     */     {
/* 289 */       nbttagcompound.setTag("LeftArm", (NBTBase)this.leftArmRotation.writeToNBT());
/*     */     }
/*     */     
/* 292 */     if (!DEFAULT_RIGHTARM_ROTATION.equals(this.rightArmRotation))
/*     */     {
/* 294 */       nbttagcompound.setTag("RightArm", (NBTBase)this.rightArmRotation.writeToNBT());
/*     */     }
/*     */     
/* 297 */     if (!DEFAULT_LEFTLEG_ROTATION.equals(this.leftLegRotation))
/*     */     {
/* 299 */       nbttagcompound.setTag("LeftLeg", (NBTBase)this.leftLegRotation.writeToNBT());
/*     */     }
/*     */     
/* 302 */     if (!DEFAULT_RIGHTLEG_ROTATION.equals(this.rightLegRotation))
/*     */     {
/* 304 */       nbttagcompound.setTag("RightLeg", (NBTBase)this.rightLegRotation.writeToNBT());
/*     */     }
/*     */     
/* 307 */     return nbttagcompound;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBePushed() {
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void collideWithEntity(Entity entityIn) {}
/*     */ 
/*     */   
/*     */   protected void collideWithNearbyEntities() {
/* 321 */     List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, getEntityBoundingBox());
/*     */     
/* 323 */     if (list != null && !list.isEmpty())
/*     */     {
/* 325 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 327 */         Entity entity = list.get(i);
/*     */         
/* 329 */         if (entity instanceof EntityMinecart && ((EntityMinecart)entity).getMinecartType() == EntityMinecart.EnumMinecartType.RIDEABLE && getDistanceSqToEntity(entity) <= 0.2D)
/*     */         {
/* 331 */           entity.applyEntityCollision((Entity)this);
/*     */         }
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interactAt(EntityPlayer player, Vec3 targetVec3) {
/* 339 */     if (hasMarker())
/*     */     {
/* 341 */       return false;
/*     */     }
/* 343 */     if (!this.worldObj.isRemote && !player.isSpectator()) {
/*     */       
/* 345 */       int i = 0;
/* 346 */       ItemStack itemstack = player.getCurrentEquippedItem();
/* 347 */       boolean flag = (itemstack != null);
/*     */       
/* 349 */       if (flag && itemstack.getItem() instanceof ItemArmor) {
/*     */         
/* 351 */         ItemArmor itemarmor = (ItemArmor)itemstack.getItem();
/*     */         
/* 353 */         if (itemarmor.armorType == 3) {
/*     */           
/* 355 */           i = 1;
/*     */         }
/* 357 */         else if (itemarmor.armorType == 2) {
/*     */           
/* 359 */           i = 2;
/*     */         }
/* 361 */         else if (itemarmor.armorType == 1) {
/*     */           
/* 363 */           i = 3;
/*     */         }
/* 365 */         else if (itemarmor.armorType == 0) {
/*     */           
/* 367 */           i = 4;
/*     */         } 
/*     */       } 
/*     */       
/* 371 */       if (flag && (itemstack.getItem() == Items.skull || itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin)))
/*     */       {
/* 373 */         i = 4;
/*     */       }
/*     */       
/* 376 */       double d4 = 0.1D;
/* 377 */       double d0 = 0.9D;
/* 378 */       double d1 = 0.4D;
/* 379 */       double d2 = 1.6D;
/* 380 */       int j = 0;
/* 381 */       boolean flag1 = isSmall();
/* 382 */       double d3 = flag1 ? (targetVec3.yCoord * 2.0D) : targetVec3.yCoord;
/*     */       
/* 384 */       if (d3 >= 0.1D && d3 < 0.1D + (flag1 ? 0.8D : 0.45D) && this.contents[1] != null) {
/*     */         
/* 386 */         j = 1;
/*     */       }
/* 388 */       else if (d3 >= 0.9D + (flag1 ? 0.3D : 0.0D) && d3 < 0.9D + (flag1 ? 1.0D : 0.7D) && this.contents[3] != null) {
/*     */         
/* 390 */         j = 3;
/*     */       }
/* 392 */       else if (d3 >= 0.4D && d3 < 0.4D + (flag1 ? 1.0D : 0.8D) && this.contents[2] != null) {
/*     */         
/* 394 */         j = 2;
/*     */       }
/* 396 */       else if (d3 >= 1.6D && this.contents[4] != null) {
/*     */         
/* 398 */         j = 4;
/*     */       } 
/*     */       
/* 401 */       boolean flag2 = (this.contents[j] != null);
/*     */       
/* 403 */       if ((this.disabledSlots & 1 << j) != 0 || (this.disabledSlots & 1 << i) != 0) {
/*     */         
/* 405 */         j = i;
/*     */         
/* 407 */         if ((this.disabledSlots & 1 << i) != 0) {
/*     */           
/* 409 */           if ((this.disabledSlots & 0x1) != 0)
/*     */           {
/* 411 */             return true;
/*     */           }
/*     */           
/* 414 */           j = 0;
/*     */         } 
/*     */       } 
/*     */       
/* 418 */       if (flag && i == 0 && !getShowArms())
/*     */       {
/* 420 */         return true;
/*     */       }
/*     */ 
/*     */       
/* 424 */       if (flag) {
/*     */         
/* 426 */         func_175422_a(player, i);
/*     */       }
/* 428 */       else if (flag2) {
/*     */         
/* 430 */         func_175422_a(player, j);
/*     */       } 
/*     */       
/* 433 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 438 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void func_175422_a(EntityPlayer p_175422_1_, int p_175422_2_) {
/* 444 */     ItemStack itemstack = this.contents[p_175422_2_];
/*     */     
/* 446 */     if (itemstack == null || (this.disabledSlots & 1 << p_175422_2_ + 8) == 0)
/*     */     {
/* 448 */       if (itemstack != null || (this.disabledSlots & 1 << p_175422_2_ + 16) == 0) {
/*     */         
/* 450 */         int i = p_175422_1_.inventory.currentItem;
/* 451 */         ItemStack itemstack1 = p_175422_1_.inventory.getStackInSlot(i);
/*     */         
/* 453 */         if (p_175422_1_.capabilities.isCreativeMode && (itemstack == null || itemstack.getItem() == Item.getItemFromBlock(Blocks.air)) && itemstack1 != null) {
/*     */           
/* 455 */           ItemStack itemstack3 = itemstack1.copy();
/* 456 */           itemstack3.stackSize = 1;
/* 457 */           setCurrentItemOrArmor(p_175422_2_, itemstack3);
/*     */         }
/* 459 */         else if (itemstack1 != null && itemstack1.stackSize > 1) {
/*     */           
/* 461 */           if (itemstack == null)
/*     */           {
/* 463 */             ItemStack itemstack2 = itemstack1.copy();
/* 464 */             itemstack2.stackSize = 1;
/* 465 */             setCurrentItemOrArmor(p_175422_2_, itemstack2);
/* 466 */             itemstack1.stackSize--;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 471 */           setCurrentItemOrArmor(p_175422_2_, itemstack1);
/* 472 */           p_175422_1_.inventory.setInventorySlotContents(i, itemstack);
/*     */         } 
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 480 */     if (this.worldObj.isRemote)
/*     */     {
/* 482 */       return false;
/*     */     }
/* 484 */     if (DamageSource.outOfWorld.equals(source)) {
/*     */       
/* 486 */       setDead();
/* 487 */       return false;
/*     */     } 
/* 489 */     if (!isEntityInvulnerable(source) && !this.canInteract && !hasMarker()) {
/*     */       
/* 491 */       if (source.isExplosion()) {
/*     */         
/* 493 */         dropContents();
/* 494 */         setDead();
/* 495 */         return false;
/*     */       } 
/* 497 */       if (DamageSource.inFire.equals(source)) {
/*     */         
/* 499 */         if (!isBurning()) {
/*     */           
/* 501 */           setFire(5);
/*     */         }
/*     */         else {
/*     */           
/* 505 */           damageArmorStand(0.15F);
/*     */         } 
/*     */         
/* 508 */         return false;
/*     */       } 
/* 510 */       if (DamageSource.onFire.equals(source) && getHealth() > 0.5F) {
/*     */         
/* 512 */         damageArmorStand(4.0F);
/* 513 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 517 */       boolean flag = "arrow".equals(source.getDamageType());
/* 518 */       boolean flag1 = "player".equals(source.getDamageType());
/*     */       
/* 520 */       if (!flag1 && !flag)
/*     */       {
/* 522 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 526 */       if (source.getSourceOfDamage() instanceof net.minecraft.entity.projectile.EntityArrow)
/*     */       {
/* 528 */         source.getSourceOfDamage().setDead();
/*     */       }
/*     */       
/* 531 */       if (source.getEntity() instanceof EntityPlayer && !((EntityPlayer)source.getEntity()).capabilities.allowEdit)
/*     */       {
/* 533 */         return false;
/*     */       }
/* 535 */       if (source.isCreativePlayer()) {
/*     */         
/* 537 */         playParticles();
/* 538 */         setDead();
/* 539 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 543 */       long i = this.worldObj.getTotalWorldTime();
/*     */       
/* 545 */       if (i - this.punchCooldown > 5L && !flag) {
/*     */         
/* 547 */         this.punchCooldown = i;
/*     */       }
/*     */       else {
/*     */         
/* 551 */         dropBlock();
/* 552 */         playParticles();
/* 553 */         setDead();
/*     */       } 
/*     */       
/* 556 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 563 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/* 569 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/* 571 */     if (Double.isNaN(d0) || d0 == 0.0D)
/*     */     {
/* 573 */       d0 = 4.0D;
/*     */     }
/*     */     
/* 576 */     d0 *= 64.0D;
/* 577 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void playParticles() {
/* 582 */     if (this.worldObj instanceof WorldServer)
/*     */     {
/* 584 */       ((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY + this.height / 1.5D, this.posZ, 10, (this.width / 4.0F), (this.height / 4.0F), (this.width / 4.0F), 0.05D, new int[] { Block.getStateId(Blocks.planks.getDefaultState()) });
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void damageArmorStand(float p_175406_1_) {
/* 590 */     float f = getHealth();
/* 591 */     f -= p_175406_1_;
/*     */     
/* 593 */     if (f <= 0.5F) {
/*     */       
/* 595 */       dropContents();
/* 596 */       setDead();
/*     */     }
/*     */     else {
/*     */       
/* 600 */       setHealth(f);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void dropBlock() {
/* 606 */     Block.spawnAsEntity(this.worldObj, new BlockPos((Entity)this), new ItemStack((Item)Items.armor_stand));
/* 607 */     dropContents();
/*     */   }
/*     */ 
/*     */   
/*     */   private void dropContents() {
/* 612 */     for (int i = 0; i < this.contents.length; i++) {
/*     */       
/* 614 */       if (this.contents[i] != null && (this.contents[i]).stackSize > 0) {
/*     */         
/* 616 */         if (this.contents[i] != null)
/*     */         {
/* 618 */           Block.spawnAsEntity(this.worldObj, (new BlockPos((Entity)this)).up(), this.contents[i]);
/*     */         }
/*     */         
/* 621 */         this.contents[i] = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float updateDistance(float p_110146_1_, float p_110146_2_) {
/* 628 */     this.prevRenderYawOffset = this.prevRotationYaw;
/* 629 */     this.renderYawOffset = this.rotationYaw;
/* 630 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 635 */     return isChild() ? (this.height * 0.5F) : (this.height * 0.9F);
/*     */   }
/*     */ 
/*     */   
/*     */   public void moveEntityWithHeading(float strafe, float forward) {
/* 640 */     if (!hasNoGravity())
/*     */     {
/* 642 */       super.moveEntityWithHeading(strafe, forward);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 648 */     super.onUpdate();
/* 649 */     Rotations rotations = this.dataWatcher.getWatchableObjectRotations(11);
/*     */     
/* 651 */     if (!this.headRotation.equals(rotations))
/*     */     {
/* 653 */       setHeadRotation(rotations);
/*     */     }
/*     */     
/* 656 */     Rotations rotations1 = this.dataWatcher.getWatchableObjectRotations(12);
/*     */     
/* 658 */     if (!this.bodyRotation.equals(rotations1))
/*     */     {
/* 660 */       setBodyRotation(rotations1);
/*     */     }
/*     */     
/* 663 */     Rotations rotations2 = this.dataWatcher.getWatchableObjectRotations(13);
/*     */     
/* 665 */     if (!this.leftArmRotation.equals(rotations2))
/*     */     {
/* 667 */       setLeftArmRotation(rotations2);
/*     */     }
/*     */     
/* 670 */     Rotations rotations3 = this.dataWatcher.getWatchableObjectRotations(14);
/*     */     
/* 672 */     if (!this.rightArmRotation.equals(rotations3))
/*     */     {
/* 674 */       setRightArmRotation(rotations3);
/*     */     }
/*     */     
/* 677 */     Rotations rotations4 = this.dataWatcher.getWatchableObjectRotations(15);
/*     */     
/* 679 */     if (!this.leftLegRotation.equals(rotations4))
/*     */     {
/* 681 */       setLeftLegRotation(rotations4);
/*     */     }
/*     */     
/* 684 */     Rotations rotations5 = this.dataWatcher.getWatchableObjectRotations(16);
/*     */     
/* 686 */     if (!this.rightLegRotation.equals(rotations5))
/*     */     {
/* 688 */       setRightLegRotation(rotations5);
/*     */     }
/*     */     
/* 691 */     boolean flag = hasMarker();
/*     */     
/* 693 */     if (!this.field_181028_bj && flag) {
/*     */       
/* 695 */       func_181550_a(false);
/*     */     }
/*     */     else {
/*     */       
/* 699 */       if (!this.field_181028_bj || flag) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 704 */       func_181550_a(true);
/*     */     } 
/*     */     
/* 707 */     this.field_181028_bj = flag;
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_181550_a(boolean p_181550_1_) {
/* 712 */     double d0 = this.posX;
/* 713 */     double d1 = this.posY;
/* 714 */     double d2 = this.posZ;
/*     */     
/* 716 */     if (p_181550_1_) {
/*     */       
/* 718 */       setSize(0.5F, 1.975F);
/*     */     }
/*     */     else {
/*     */       
/* 722 */       setSize(0.0F, 0.0F);
/*     */     } 
/*     */     
/* 725 */     setPosition(d0, d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updatePotionMetadata() {
/* 730 */     setInvisible(this.canInteract);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvisible(boolean invisible) {
/* 735 */     this.canInteract = invisible;
/* 736 */     super.setInvisible(invisible);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isChild() {
/* 741 */     return isSmall();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKillCommand() {
/* 746 */     setDead();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isImmuneToExplosions() {
/* 751 */     return isInvisible();
/*     */   }
/*     */ 
/*     */   
/*     */   private void setSmall(boolean p_175420_1_) {
/* 756 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 758 */     if (p_175420_1_) {
/*     */       
/* 760 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else {
/*     */       
/* 764 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     } 
/*     */     
/* 767 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSmall() {
/* 772 */     return ((this.dataWatcher.getWatchableObjectByte(10) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setNoGravity(boolean p_175425_1_) {
/* 777 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 779 */     if (p_175425_1_) {
/*     */       
/* 781 */       b0 = (byte)(b0 | 0x2);
/*     */     }
/*     */     else {
/*     */       
/* 785 */       b0 = (byte)(b0 & 0xFFFFFFFD);
/*     */     } 
/*     */     
/* 788 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoGravity() {
/* 793 */     return ((this.dataWatcher.getWatchableObjectByte(10) & 0x2) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setShowArms(boolean p_175413_1_) {
/* 798 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 800 */     if (p_175413_1_) {
/*     */       
/* 802 */       b0 = (byte)(b0 | 0x4);
/*     */     }
/*     */     else {
/*     */       
/* 806 */       b0 = (byte)(b0 & 0xFFFFFFFB);
/*     */     } 
/*     */     
/* 809 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getShowArms() {
/* 814 */     return ((this.dataWatcher.getWatchableObjectByte(10) & 0x4) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setNoBasePlate(boolean p_175426_1_) {
/* 819 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 821 */     if (p_175426_1_) {
/*     */       
/* 823 */       b0 = (byte)(b0 | 0x8);
/*     */     }
/*     */     else {
/*     */       
/* 827 */       b0 = (byte)(b0 & 0xFFFFFFF7);
/*     */     } 
/*     */     
/* 830 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNoBasePlate() {
/* 835 */     return ((this.dataWatcher.getWatchableObjectByte(10) & 0x8) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   private void setMarker(boolean p_181027_1_) {
/* 840 */     byte b0 = this.dataWatcher.getWatchableObjectByte(10);
/*     */     
/* 842 */     if (p_181027_1_) {
/*     */       
/* 844 */       b0 = (byte)(b0 | 0x10);
/*     */     }
/*     */     else {
/*     */       
/* 848 */       b0 = (byte)(b0 & 0xFFFFFFEF);
/*     */     } 
/*     */     
/* 851 */     this.dataWatcher.updateObject(10, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMarker() {
/* 856 */     return ((this.dataWatcher.getWatchableObjectByte(10) & 0x10) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeadRotation(Rotations p_175415_1_) {
/* 861 */     this.headRotation = p_175415_1_;
/* 862 */     this.dataWatcher.updateObject(11, p_175415_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBodyRotation(Rotations p_175424_1_) {
/* 867 */     this.bodyRotation = p_175424_1_;
/* 868 */     this.dataWatcher.updateObject(12, p_175424_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLeftArmRotation(Rotations p_175405_1_) {
/* 873 */     this.leftArmRotation = p_175405_1_;
/* 874 */     this.dataWatcher.updateObject(13, p_175405_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRightArmRotation(Rotations p_175428_1_) {
/* 879 */     this.rightArmRotation = p_175428_1_;
/* 880 */     this.dataWatcher.updateObject(14, p_175428_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLeftLegRotation(Rotations p_175417_1_) {
/* 885 */     this.leftLegRotation = p_175417_1_;
/* 886 */     this.dataWatcher.updateObject(15, p_175417_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRightLegRotation(Rotations p_175427_1_) {
/* 891 */     this.rightLegRotation = p_175427_1_;
/* 892 */     this.dataWatcher.updateObject(16, p_175427_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getHeadRotation() {
/* 897 */     return this.headRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getBodyRotation() {
/* 902 */     return this.bodyRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getLeftArmRotation() {
/* 907 */     return this.leftArmRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getRightArmRotation() {
/* 912 */     return this.rightArmRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getLeftLegRotation() {
/* 917 */     return this.leftLegRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public Rotations getRightLegRotation() {
/* 922 */     return this.rightLegRotation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 927 */     return (super.canBeCollidedWith() && !hasMarker());
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityArmorStand.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */