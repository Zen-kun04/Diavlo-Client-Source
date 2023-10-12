/*     */ package net.minecraft.entity.item;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ 
/*     */ public class EntityBoat
/*     */   extends Entity
/*     */ {
/*     */   private boolean isBoatEmpty;
/*     */   private double speedMultiplier;
/*     */   private int boatPosRotationIncrements;
/*     */   private double boatX;
/*     */   private double boatY;
/*     */   private double boatZ;
/*     */   private double boatYaw;
/*     */   private double boatPitch;
/*     */   private double velocityX;
/*     */   private double velocityY;
/*     */   private double velocityZ;
/*     */   
/*     */   public EntityBoat(World worldIn) {
/*  37 */     super(worldIn);
/*  38 */     this.isBoatEmpty = true;
/*  39 */     this.speedMultiplier = 0.07D;
/*  40 */     this.preventEntitySpawning = true;
/*  41 */     setSize(1.5F, 0.6F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/*  46 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  51 */     this.dataWatcher.addObject(17, new Integer(0));
/*  52 */     this.dataWatcher.addObject(18, new Integer(1));
/*  53 */     this.dataWatcher.addObject(19, new Float(0.0F));
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBox(Entity entityIn) {
/*  58 */     return entityIn.getEntityBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB getCollisionBoundingBox() {
/*  63 */     return getEntityBoundingBox();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBePushed() {
/*  68 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityBoat(World worldIn, double p_i1705_2_, double p_i1705_4_, double p_i1705_6_) {
/*  73 */     this(worldIn);
/*  74 */     setPosition(p_i1705_2_, p_i1705_4_, p_i1705_6_);
/*  75 */     this.motionX = 0.0D;
/*  76 */     this.motionY = 0.0D;
/*  77 */     this.motionZ = 0.0D;
/*  78 */     this.prevPosX = p_i1705_2_;
/*  79 */     this.prevPosY = p_i1705_4_;
/*  80 */     this.prevPosZ = p_i1705_6_;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMountedYOffset() {
/*  85 */     return -0.3D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/*  90 */     if (isEntityInvulnerable(source))
/*     */     {
/*  92 */       return false;
/*     */     }
/*  94 */     if (!this.worldObj.isRemote && !this.isDead) {
/*     */       
/*  96 */       if (this.riddenByEntity != null && this.riddenByEntity == source.getEntity() && source instanceof net.minecraft.util.EntityDamageSourceIndirect)
/*     */       {
/*  98 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 102 */       setForwardDirection(-getForwardDirection());
/* 103 */       setTimeSinceHit(10);
/* 104 */       setDamageTaken(getDamageTaken() + amount * 10.0F);
/* 105 */       setBeenAttacked();
/* 106 */       boolean flag = (source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode);
/*     */       
/* 108 */       if (flag || getDamageTaken() > 40.0F) {
/*     */         
/* 110 */         if (this.riddenByEntity != null)
/*     */         {
/* 112 */           this.riddenByEntity.mountEntity(this);
/*     */         }
/*     */         
/* 115 */         if (!flag && this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */         {
/* 117 */           dropItemWithOffset(Items.boat, 1, 0.0F);
/*     */         }
/*     */         
/* 120 */         setDead();
/*     */       } 
/*     */       
/* 123 */       return true;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 128 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void performHurtAnimation() {
/* 134 */     setForwardDirection(-getForwardDirection());
/* 135 */     setTimeSinceHit(10);
/* 136 */     setDamageTaken(getDamageTaken() * 11.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 141 */     return !this.isDead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 148 */     this.prevPosX = this.posX = x;
/* 149 */     this.prevPosY = this.posY = y;
/* 150 */     this.prevPosZ = this.posZ = z;
/* 151 */     this.rotationYaw = yaw;
/* 152 */     this.rotationPitch = pitch;
/* 153 */     this.boatPosRotationIncrements = 0;
/* 154 */     setPosition(x, y, z);
/* 155 */     this.motionX = this.velocityX = 0.0D;
/* 156 */     this.motionY = this.velocityY = 0.0D;
/* 157 */     this.motionZ = this.velocityZ = 0.0D;
/*     */ 
/*     */ 
/*     */     
/* 161 */     if (this.isBoatEmpty) {
/*     */       
/* 163 */       this.boatPosRotationIncrements = posRotationIncrements + 5;
/*     */     }
/*     */     else {
/*     */       
/* 167 */       double d0 = x - this.posX;
/* 168 */       double d1 = y - this.posY;
/* 169 */       double d2 = z - this.posZ;
/* 170 */       double d3 = d0 * d0 + d1 * d1 + d2 * d2;
/*     */       
/* 172 */       if (d3 <= 1.0D) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 177 */       this.boatPosRotationIncrements = 3;
/*     */     } 
/*     */     
/* 180 */     this.boatX = x;
/* 181 */     this.boatY = y;
/* 182 */     this.boatZ = z;
/* 183 */     this.boatYaw = yaw;
/* 184 */     this.boatPitch = pitch;
/* 185 */     this.motionX = this.velocityX;
/* 186 */     this.motionY = this.velocityY;
/* 187 */     this.motionZ = this.velocityZ;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/* 193 */     this.velocityX = this.motionX = x;
/* 194 */     this.velocityY = this.motionY = y;
/* 195 */     this.velocityZ = this.motionZ = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 200 */     super.onUpdate();
/*     */     
/* 202 */     if (getTimeSinceHit() > 0)
/*     */     {
/* 204 */       setTimeSinceHit(getTimeSinceHit() - 1);
/*     */     }
/*     */     
/* 207 */     if (getDamageTaken() > 0.0F)
/*     */     {
/* 209 */       setDamageTaken(getDamageTaken() - 1.0F);
/*     */     }
/*     */     
/* 212 */     this.prevPosX = this.posX;
/* 213 */     this.prevPosY = this.posY;
/* 214 */     this.prevPosZ = this.posZ;
/* 215 */     int i = 5;
/* 216 */     double d0 = 0.0D;
/*     */     
/* 218 */     for (int j = 0; j < i; j++) {
/*     */       
/* 220 */       double d1 = (getEntityBoundingBox()).minY + ((getEntityBoundingBox()).maxY - (getEntityBoundingBox()).minY) * (j + 0) / i - 0.125D;
/* 221 */       double d3 = (getEntityBoundingBox()).minY + ((getEntityBoundingBox()).maxY - (getEntityBoundingBox()).minY) * (j + 1) / i - 0.125D;
/* 222 */       AxisAlignedBB axisalignedbb = new AxisAlignedBB((getEntityBoundingBox()).minX, d1, (getEntityBoundingBox()).minZ, (getEntityBoundingBox()).maxX, d3, (getEntityBoundingBox()).maxZ);
/*     */       
/* 224 */       if (this.worldObj.isAABBInMaterial(axisalignedbb, Material.water))
/*     */       {
/* 226 */         d0 += 1.0D / i;
/*     */       }
/*     */     } 
/*     */     
/* 230 */     double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */     
/* 232 */     if (d9 > 0.2975D) {
/*     */       
/* 234 */       double d2 = Math.cos(this.rotationYaw * Math.PI / 180.0D);
/* 235 */       double d4 = Math.sin(this.rotationYaw * Math.PI / 180.0D);
/*     */       
/* 237 */       for (int k = 0; k < 1.0D + d9 * 60.0D; k++) {
/*     */         
/* 239 */         double d5 = (this.rand.nextFloat() * 2.0F - 1.0F);
/* 240 */         double d6 = (this.rand.nextInt(2) * 2 - 1) * 0.7D;
/*     */         
/* 242 */         if (this.rand.nextBoolean()) {
/*     */           
/* 244 */           double d7 = this.posX - d2 * d5 * 0.8D + d4 * d6;
/* 245 */           double d8 = this.posZ - d4 * d5 * 0.8D - d2 * d6;
/* 246 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d7, this.posY - 0.125D, d8, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         }
/*     */         else {
/*     */           
/* 250 */           double d24 = this.posX + d2 + d4 * d5 * 0.7D;
/* 251 */           double d25 = this.posZ + d4 - d2 * d5 * 0.7D;
/* 252 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_SPLASH, d24, this.posY - 0.125D, d25, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 257 */     if (this.worldObj.isRemote && this.isBoatEmpty) {
/*     */       
/* 259 */       if (this.boatPosRotationIncrements > 0)
/*     */       {
/* 261 */         double d12 = this.posX + (this.boatX - this.posX) / this.boatPosRotationIncrements;
/* 262 */         double d16 = this.posY + (this.boatY - this.posY) / this.boatPosRotationIncrements;
/* 263 */         double d19 = this.posZ + (this.boatZ - this.posZ) / this.boatPosRotationIncrements;
/* 264 */         double d22 = MathHelper.wrapAngleTo180_double(this.boatYaw - this.rotationYaw);
/* 265 */         this.rotationYaw = (float)(this.rotationYaw + d22 / this.boatPosRotationIncrements);
/* 266 */         this.rotationPitch = (float)(this.rotationPitch + (this.boatPitch - this.rotationPitch) / this.boatPosRotationIncrements);
/* 267 */         this.boatPosRotationIncrements--;
/* 268 */         setPosition(d12, d16, d19);
/* 269 */         setRotation(this.rotationYaw, this.rotationPitch);
/*     */       }
/*     */       else
/*     */       {
/* 273 */         double d13 = this.posX + this.motionX;
/* 274 */         double d17 = this.posY + this.motionY;
/* 275 */         double d20 = this.posZ + this.motionZ;
/* 276 */         setPosition(d13, d17, d20);
/*     */         
/* 278 */         if (this.onGround) {
/*     */           
/* 280 */           this.motionX *= 0.5D;
/* 281 */           this.motionY *= 0.5D;
/* 282 */           this.motionZ *= 0.5D;
/*     */         } 
/*     */         
/* 285 */         this.motionX *= 0.9900000095367432D;
/* 286 */         this.motionY *= 0.949999988079071D;
/* 287 */         this.motionZ *= 0.9900000095367432D;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 292 */       if (d0 < 1.0D) {
/*     */         
/* 294 */         double d10 = d0 * 2.0D - 1.0D;
/* 295 */         this.motionY += 0.03999999910593033D * d10;
/*     */       }
/*     */       else {
/*     */         
/* 299 */         if (this.motionY < 0.0D)
/*     */         {
/* 301 */           this.motionY /= 2.0D;
/*     */         }
/*     */         
/* 304 */         this.motionY += 0.007000000216066837D;
/*     */       } 
/*     */       
/* 307 */       if (this.riddenByEntity instanceof EntityLivingBase) {
/*     */         
/* 309 */         EntityLivingBase entitylivingbase = (EntityLivingBase)this.riddenByEntity;
/* 310 */         float f = this.riddenByEntity.rotationYaw + -entitylivingbase.moveStrafing * 90.0F;
/* 311 */         this.motionX += -Math.sin((f * 3.1415927F / 180.0F)) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806D;
/* 312 */         this.motionZ += Math.cos((f * 3.1415927F / 180.0F)) * this.speedMultiplier * entitylivingbase.moveForward * 0.05000000074505806D;
/*     */       } 
/*     */       
/* 315 */       double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */       
/* 317 */       if (d11 > 0.35D) {
/*     */         
/* 319 */         double d14 = 0.35D / d11;
/* 320 */         this.motionX *= d14;
/* 321 */         this.motionZ *= d14;
/* 322 */         d11 = 0.35D;
/*     */       } 
/*     */       
/* 325 */       if (d11 > d9 && this.speedMultiplier < 0.35D) {
/*     */         
/* 327 */         this.speedMultiplier += (0.35D - this.speedMultiplier) / 35.0D;
/*     */         
/* 329 */         if (this.speedMultiplier > 0.35D)
/*     */         {
/* 331 */           this.speedMultiplier = 0.35D;
/*     */         }
/*     */       }
/*     */       else {
/*     */         
/* 336 */         this.speedMultiplier -= (this.speedMultiplier - 0.07D) / 35.0D;
/*     */         
/* 338 */         if (this.speedMultiplier < 0.07D)
/*     */         {
/* 340 */           this.speedMultiplier = 0.07D;
/*     */         }
/*     */       } 
/*     */       
/* 344 */       for (int i1 = 0; i1 < 4; i1++) {
/*     */         
/* 346 */         int l1 = MathHelper.floor_double(this.posX + ((i1 % 2) - 0.5D) * 0.8D);
/* 347 */         int i2 = MathHelper.floor_double(this.posZ + ((i1 / 2) - 0.5D) * 0.8D);
/*     */         
/* 349 */         for (int j2 = 0; j2 < 2; j2++) {
/*     */           
/* 351 */           int l = MathHelper.floor_double(this.posY) + j2;
/* 352 */           BlockPos blockpos = new BlockPos(l1, l, i2);
/* 353 */           Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */           
/* 355 */           if (block == Blocks.snow_layer) {
/*     */             
/* 357 */             this.worldObj.setBlockToAir(blockpos);
/* 358 */             this.isCollidedHorizontally = false;
/*     */           }
/* 360 */           else if (block == Blocks.waterlily) {
/*     */             
/* 362 */             this.worldObj.destroyBlock(blockpos, true);
/* 363 */             this.isCollidedHorizontally = false;
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 368 */       if (this.onGround) {
/*     */         
/* 370 */         this.motionX *= 0.5D;
/* 371 */         this.motionY *= 0.5D;
/* 372 */         this.motionZ *= 0.5D;
/*     */       } 
/*     */       
/* 375 */       moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */       
/* 377 */       if (this.isCollidedHorizontally && d9 > 0.2975D) {
/*     */         
/* 379 */         if (!this.worldObj.isRemote && !this.isDead) {
/*     */           
/* 381 */           setDead();
/*     */           
/* 383 */           if (this.worldObj.getGameRules().getBoolean("doEntityDrops"))
/*     */           {
/* 385 */             for (int j1 = 0; j1 < 3; j1++)
/*     */             {
/* 387 */               dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
/*     */             }
/*     */             
/* 390 */             for (int k1 = 0; k1 < 2; k1++)
/*     */             {
/* 392 */               dropItemWithOffset(Items.stick, 1, 0.0F);
/*     */             }
/*     */           }
/*     */         
/*     */         } 
/*     */       } else {
/*     */         
/* 399 */         this.motionX *= 0.9900000095367432D;
/* 400 */         this.motionY *= 0.949999988079071D;
/* 401 */         this.motionZ *= 0.9900000095367432D;
/*     */       } 
/*     */       
/* 404 */       this.rotationPitch = 0.0F;
/* 405 */       double d15 = this.rotationYaw;
/* 406 */       double d18 = this.prevPosX - this.posX;
/* 407 */       double d21 = this.prevPosZ - this.posZ;
/*     */       
/* 409 */       if (d18 * d18 + d21 * d21 > 0.001D)
/*     */       {
/* 411 */         d15 = (float)(MathHelper.atan2(d21, d18) * 180.0D / Math.PI);
/*     */       }
/*     */       
/* 414 */       double d23 = MathHelper.wrapAngleTo180_double(d15 - this.rotationYaw);
/*     */       
/* 416 */       if (d23 > 20.0D)
/*     */       {
/* 418 */         d23 = 20.0D;
/*     */       }
/*     */       
/* 421 */       if (d23 < -20.0D)
/*     */       {
/* 423 */         d23 = -20.0D;
/*     */       }
/*     */       
/* 426 */       this.rotationYaw = (float)(this.rotationYaw + d23);
/* 427 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */       
/* 429 */       if (!this.worldObj.isRemote) {
/*     */         
/* 431 */         List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().expand(0.20000000298023224D, 0.0D, 0.20000000298023224D));
/*     */         
/* 433 */         if (list != null && !list.isEmpty())
/*     */         {
/* 435 */           for (int k2 = 0; k2 < list.size(); k2++) {
/*     */             
/* 437 */             Entity entity = list.get(k2);
/*     */             
/* 439 */             if (entity != this.riddenByEntity && entity.canBePushed() && entity instanceof EntityBoat)
/*     */             {
/* 441 */               entity.applyEntityCollision(this);
/*     */             }
/*     */           } 
/*     */         }
/*     */         
/* 446 */         if (this.riddenByEntity != null && this.riddenByEntity.isDead)
/*     */         {
/* 448 */           this.riddenByEntity = null;
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRiderPosition() {
/* 456 */     if (this.riddenByEntity != null) {
/*     */       
/* 458 */       double d0 = Math.cos(this.rotationYaw * Math.PI / 180.0D) * 0.4D;
/* 459 */       double d1 = Math.sin(this.rotationYaw * Math.PI / 180.0D) * 0.4D;
/* 460 */       this.riddenByEntity.setPosition(this.posX + d0, this.posY + getMountedYOffset() + this.riddenByEntity.getYOffset(), this.posZ + d1);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void readEntityFromNBT(NBTTagCompound tagCompund) {}
/*     */ 
/*     */   
/*     */   public boolean interactFirst(EntityPlayer playerIn) {
/* 474 */     if (this.riddenByEntity != null && this.riddenByEntity instanceof EntityPlayer && this.riddenByEntity != playerIn)
/*     */     {
/* 476 */       return true;
/*     */     }
/*     */ 
/*     */     
/* 480 */     if (!this.worldObj.isRemote)
/*     */     {
/* 482 */       playerIn.mountEntity(this);
/*     */     }
/*     */     
/* 485 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void updateFallState(double y, boolean onGroundIn, Block blockIn, BlockPos pos) {
/* 491 */     if (onGroundIn) {
/*     */       
/* 493 */       if (this.fallDistance > 3.0F)
/*     */       {
/* 495 */         fall(this.fallDistance, 1.0F);
/*     */         
/* 497 */         if (!this.worldObj.isRemote && !this.isDead) {
/*     */           
/* 499 */           setDead();
/*     */           
/* 501 */           if (this.worldObj.getGameRules().getBoolean("doEntityDrops")) {
/*     */             
/* 503 */             for (int i = 0; i < 3; i++)
/*     */             {
/* 505 */               dropItemWithOffset(Item.getItemFromBlock(Blocks.planks), 1, 0.0F);
/*     */             }
/*     */             
/* 508 */             for (int j = 0; j < 2; j++)
/*     */             {
/* 510 */               dropItemWithOffset(Items.stick, 1, 0.0F);
/*     */             }
/*     */           } 
/*     */         } 
/*     */         
/* 515 */         this.fallDistance = 0.0F;
/*     */       }
/*     */     
/* 518 */     } else if (this.worldObj.getBlockState((new BlockPos(this)).down()).getBlock().getMaterial() != Material.water && y < 0.0D) {
/*     */       
/* 520 */       this.fallDistance = (float)(this.fallDistance - y);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDamageTaken(float p_70266_1_) {
/* 526 */     this.dataWatcher.updateObject(19, Float.valueOf(p_70266_1_));
/*     */   }
/*     */ 
/*     */   
/*     */   public float getDamageTaken() {
/* 531 */     return this.dataWatcher.getWatchableObjectFloat(19);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTimeSinceHit(int p_70265_1_) {
/* 536 */     this.dataWatcher.updateObject(17, Integer.valueOf(p_70265_1_));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTimeSinceHit() {
/* 541 */     return this.dataWatcher.getWatchableObjectInt(17);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setForwardDirection(int p_70269_1_) {
/* 546 */     this.dataWatcher.updateObject(18, Integer.valueOf(p_70269_1_));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getForwardDirection() {
/* 551 */     return this.dataWatcher.getWatchableObjectInt(18);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsBoatEmpty(boolean p_70270_1_) {
/* 556 */     this.isBoatEmpty = p_70270_1_;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\item\EntityBoat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */