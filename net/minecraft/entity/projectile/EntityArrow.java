/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.network.Packet;
/*     */ import net.minecraft.network.play.server.S2BPacketChangeGameState;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.IBlockAccess;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityArrow extends Entity implements IProjectile {
/*  30 */   private int xTile = -1;
/*  31 */   private int yTile = -1;
/*  32 */   private int zTile = -1;
/*     */   private Block inTile;
/*     */   private int inData;
/*     */   private boolean inGround;
/*     */   public int canBePickedUp;
/*     */   public int arrowShake;
/*     */   public Entity shootingEntity;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*  41 */   private double damage = 2.0D;
/*     */   
/*     */   private int knockbackStrength;
/*     */   
/*     */   public EntityArrow(World worldIn) {
/*  46 */     super(worldIn);
/*  47 */     this.renderDistanceWeight = 10.0D;
/*  48 */     setSize(0.5F, 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityArrow(World worldIn, double x, double y, double z) {
/*  53 */     super(worldIn);
/*  54 */     this.renderDistanceWeight = 10.0D;
/*  55 */     setSize(0.5F, 0.5F);
/*  56 */     setPosition(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityArrow(World worldIn, EntityLivingBase shooter, EntityLivingBase target, float velocity, float innacuracy) {
/*  61 */     super(worldIn);
/*  62 */     this.renderDistanceWeight = 10.0D;
/*  63 */     this.shootingEntity = (Entity)shooter;
/*     */     
/*  65 */     if (shooter instanceof EntityPlayer)
/*     */     {
/*  67 */       this.canBePickedUp = 1;
/*     */     }
/*     */     
/*  70 */     this.posY = shooter.posY + shooter.getEyeHeight() - 0.10000000149011612D;
/*  71 */     double d0 = target.posX - shooter.posX;
/*  72 */     double d1 = (target.getEntityBoundingBox()).minY + (target.height / 3.0F) - this.posY;
/*  73 */     double d2 = target.posZ - shooter.posZ;
/*  74 */     double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
/*     */     
/*  76 */     if (d3 >= 1.0E-7D) {
/*     */       
/*  78 */       float f = (float)(MathHelper.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
/*  79 */       float f1 = (float)-(MathHelper.atan2(d1, d3) * 180.0D / Math.PI);
/*  80 */       double d4 = d0 / d3;
/*  81 */       double d5 = d2 / d3;
/*  82 */       setLocationAndAngles(shooter.posX + d4, this.posY, shooter.posZ + d5, f, f1);
/*  83 */       float f2 = (float)(d3 * 0.20000000298023224D);
/*  84 */       setThrowableHeading(d0, d1 + f2, d2, velocity, innacuracy);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityArrow(World worldIn, EntityLivingBase shooter, float velocity) {
/*  90 */     super(worldIn);
/*  91 */     this.renderDistanceWeight = 10.0D;
/*  92 */     this.shootingEntity = (Entity)shooter;
/*     */     
/*  94 */     if (shooter instanceof EntityPlayer)
/*     */     {
/*  96 */       this.canBePickedUp = 1;
/*     */     }
/*     */     
/*  99 */     setSize(0.5F, 0.5F);
/* 100 */     setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
/* 101 */     this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/* 102 */     this.posY -= 0.10000000149011612D;
/* 103 */     this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/* 104 */     setPosition(this.posX, this.posY, this.posZ);
/* 105 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
/* 106 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F));
/* 107 */     this.motionY = -MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F);
/* 108 */     setThrowableHeading(this.motionX, this.motionY, this.motionZ, velocity * 1.5F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 113 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
/* 118 */     float f = MathHelper.sqrt_double(x * x + y * y + z * z);
/* 119 */     x /= f;
/* 120 */     y /= f;
/* 121 */     z /= f;
/* 122 */     x += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * inaccuracy;
/* 123 */     y += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * inaccuracy;
/* 124 */     z += this.rand.nextGaussian() * (this.rand.nextBoolean() ? -1 : true) * 0.007499999832361937D * inaccuracy;
/* 125 */     x *= velocity;
/* 126 */     y *= velocity;
/* 127 */     z *= velocity;
/* 128 */     this.motionX = x;
/* 129 */     this.motionY = y;
/* 130 */     this.motionZ = z;
/* 131 */     float f1 = MathHelper.sqrt_double(x * x + z * z);
/* 132 */     this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/* 133 */     this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f1) * 180.0D / Math.PI);
/* 134 */     this.ticksInGround = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 139 */     setPosition(x, y, z);
/* 140 */     setRotation(yaw, pitch);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/* 145 */     this.motionX = x;
/* 146 */     this.motionY = y;
/* 147 */     this.motionZ = z;
/*     */     
/* 149 */     if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
/*     */       
/* 151 */       float f = MathHelper.sqrt_double(x * x + z * z);
/* 152 */       this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/* 153 */       this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f) * 180.0D / Math.PI);
/* 154 */       this.prevRotationPitch = this.rotationPitch;
/* 155 */       this.prevRotationYaw = this.rotationYaw;
/* 156 */       setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 157 */       this.ticksInGround = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 163 */     super.onUpdate();
/*     */     
/* 165 */     if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
/*     */       
/* 167 */       float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 168 */       this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/* 169 */       this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f) * 180.0D / Math.PI);
/*     */     } 
/*     */     
/* 172 */     BlockPos blockpos = new BlockPos(this.xTile, this.yTile, this.zTile);
/* 173 */     IBlockState iblockstate = this.worldObj.getBlockState(blockpos);
/* 174 */     Block block = iblockstate.getBlock();
/*     */     
/* 176 */     if (block.getMaterial() != Material.air) {
/*     */       
/* 178 */       block.setBlockBoundsBasedOnState((IBlockAccess)this.worldObj, blockpos);
/* 179 */       AxisAlignedBB axisalignedbb = block.getCollisionBoundingBox(this.worldObj, blockpos, iblockstate);
/*     */       
/* 181 */       if (axisalignedbb != null && axisalignedbb.isVecInside(new Vec3(this.posX, this.posY, this.posZ)))
/*     */       {
/* 183 */         this.inGround = true;
/*     */       }
/*     */     } 
/*     */     
/* 187 */     if (this.arrowShake > 0)
/*     */     {
/* 189 */       this.arrowShake--;
/*     */     }
/*     */     
/* 192 */     if (this.inGround) {
/*     */       
/* 194 */       int j = block.getMetaFromState(iblockstate);
/*     */       
/* 196 */       if (block == this.inTile && j == this.inData)
/*     */       {
/* 198 */         this.ticksInGround++;
/*     */         
/* 200 */         if (this.ticksInGround >= 1200)
/*     */         {
/* 202 */           setDead();
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 207 */         this.inGround = false;
/* 208 */         this.motionX *= (this.rand.nextFloat() * 0.2F);
/* 209 */         this.motionY *= (this.rand.nextFloat() * 0.2F);
/* 210 */         this.motionZ *= (this.rand.nextFloat() * 0.2F);
/* 211 */         this.ticksInGround = 0;
/* 212 */         this.ticksInAir = 0;
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/* 217 */       this.ticksInAir++;
/* 218 */       Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 219 */       Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 220 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3, false, true, false);
/* 221 */       vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 222 */       vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 224 */       if (movingobjectposition != null)
/*     */       {
/* 226 */         vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 229 */       Entity entity = null;
/* 230 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 231 */       double d0 = 0.0D;
/*     */       
/* 233 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 235 */         Entity entity1 = list.get(i);
/*     */         
/* 237 */         if (entity1.canBeCollidedWith() && (entity1 != this.shootingEntity || this.ticksInAir >= 5)) {
/*     */           
/* 239 */           float f1 = 0.3F;
/* 240 */           AxisAlignedBB axisalignedbb1 = entity1.getEntityBoundingBox().expand(f1, f1, f1);
/* 241 */           MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec31, vec3);
/*     */           
/* 243 */           if (movingobjectposition1 != null) {
/*     */             
/* 245 */             double d1 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 247 */             if (d1 < d0 || d0 == 0.0D) {
/*     */               
/* 249 */               entity = entity1;
/* 250 */               d0 = d1;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 256 */       if (entity != null)
/*     */       {
/* 258 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 261 */       if (movingobjectposition != null && movingobjectposition.entityHit != null && movingobjectposition.entityHit instanceof EntityPlayer) {
/*     */         
/* 263 */         EntityPlayer entityplayer = (EntityPlayer)movingobjectposition.entityHit;
/*     */         
/* 265 */         if (entityplayer.capabilities.disableDamage || (this.shootingEntity instanceof EntityPlayer && !((EntityPlayer)this.shootingEntity).canAttackPlayer(entityplayer)))
/*     */         {
/* 267 */           movingobjectposition = null;
/*     */         }
/*     */       } 
/*     */       
/* 271 */       if (movingobjectposition != null)
/*     */       {
/* 273 */         if (movingobjectposition.entityHit != null) {
/*     */           DamageSource damagesource;
/* 275 */           float f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/* 276 */           int l = MathHelper.ceiling_double_int(f2 * this.damage);
/*     */           
/* 278 */           if (getIsCritical())
/*     */           {
/* 280 */             l += this.rand.nextInt(l / 2 + 2);
/*     */           }
/*     */ 
/*     */ 
/*     */           
/* 285 */           if (this.shootingEntity == null) {
/*     */             
/* 287 */             damagesource = DamageSource.causeArrowDamage(this, this);
/*     */           }
/*     */           else {
/*     */             
/* 291 */             damagesource = DamageSource.causeArrowDamage(this, this.shootingEntity);
/*     */           } 
/*     */           
/* 294 */           if (isBurning() && !(movingobjectposition.entityHit instanceof net.minecraft.entity.monster.EntityEnderman))
/*     */           {
/* 296 */             movingobjectposition.entityHit.setFire(5);
/*     */           }
/*     */           
/* 299 */           if (movingobjectposition.entityHit.attackEntityFrom(damagesource, l))
/*     */           {
/* 301 */             if (movingobjectposition.entityHit instanceof EntityLivingBase) {
/*     */               
/* 303 */               EntityLivingBase entitylivingbase = (EntityLivingBase)movingobjectposition.entityHit;
/*     */               
/* 305 */               if (!this.worldObj.isRemote)
/*     */               {
/* 307 */                 entitylivingbase.setArrowCountInEntity(entitylivingbase.getArrowCountInEntity() + 1);
/*     */               }
/*     */               
/* 310 */               if (this.knockbackStrength > 0) {
/*     */                 
/* 312 */                 float f7 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/*     */                 
/* 314 */                 if (f7 > 0.0F)
/*     */                 {
/* 316 */                   movingobjectposition.entityHit.addVelocity(this.motionX * this.knockbackStrength * 0.6000000238418579D / f7, 0.1D, this.motionZ * this.knockbackStrength * 0.6000000238418579D / f7);
/*     */                 }
/*     */               } 
/*     */               
/* 320 */               if (this.shootingEntity instanceof EntityLivingBase) {
/*     */                 
/* 322 */                 EnchantmentHelper.applyThornEnchantments(entitylivingbase, this.shootingEntity);
/* 323 */                 EnchantmentHelper.applyArthropodEnchantments((EntityLivingBase)this.shootingEntity, (Entity)entitylivingbase);
/*     */               } 
/*     */               
/* 326 */               if (this.shootingEntity != null && movingobjectposition.entityHit != this.shootingEntity && movingobjectposition.entityHit instanceof EntityPlayer && this.shootingEntity instanceof EntityPlayerMP)
/*     */               {
/* 328 */                 ((EntityPlayerMP)this.shootingEntity).playerNetServerHandler.sendPacket((Packet)new S2BPacketChangeGameState(6, 0.0F));
/*     */               }
/*     */             } 
/*     */             
/* 332 */             playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
/*     */             
/* 334 */             if (!(movingobjectposition.entityHit instanceof net.minecraft.entity.monster.EntityEnderman))
/*     */             {
/* 336 */               setDead();
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 341 */             this.motionX *= -0.10000000149011612D;
/* 342 */             this.motionY *= -0.10000000149011612D;
/* 343 */             this.motionZ *= -0.10000000149011612D;
/* 344 */             this.rotationYaw += 180.0F;
/* 345 */             this.prevRotationYaw += 180.0F;
/* 346 */             this.ticksInAir = 0;
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 351 */           BlockPos blockpos1 = movingobjectposition.getBlockPos();
/* 352 */           this.xTile = blockpos1.getX();
/* 353 */           this.yTile = blockpos1.getY();
/* 354 */           this.zTile = blockpos1.getZ();
/* 355 */           IBlockState iblockstate1 = this.worldObj.getBlockState(blockpos1);
/* 356 */           this.inTile = iblockstate1.getBlock();
/* 357 */           this.inData = this.inTile.getMetaFromState(iblockstate1);
/* 358 */           this.motionX = (float)(movingobjectposition.hitVec.xCoord - this.posX);
/* 359 */           this.motionY = (float)(movingobjectposition.hitVec.yCoord - this.posY);
/* 360 */           this.motionZ = (float)(movingobjectposition.hitVec.zCoord - this.posZ);
/* 361 */           float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
/* 362 */           this.posX -= this.motionX / f5 * 0.05000000074505806D;
/* 363 */           this.posY -= this.motionY / f5 * 0.05000000074505806D;
/* 364 */           this.posZ -= this.motionZ / f5 * 0.05000000074505806D;
/* 365 */           playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
/* 366 */           this.inGround = true;
/* 367 */           this.arrowShake = 7;
/* 368 */           setIsCritical(false);
/*     */           
/* 370 */           if (this.inTile.getMaterial() != Material.air)
/*     */           {
/* 372 */             this.inTile.onEntityCollidedWithBlock(this.worldObj, blockpos1, iblockstate1, this);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 377 */       if (getIsCritical())
/*     */       {
/* 379 */         for (int k = 0; k < 4; k++)
/*     */         {
/* 381 */           this.worldObj.spawnParticle(EnumParticleTypes.CRIT, this.posX + this.motionX * k / 4.0D, this.posY + this.motionY * k / 4.0D, this.posZ + this.motionZ * k / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ, new int[0]);
/*     */         }
/*     */       }
/*     */       
/* 385 */       this.posX += this.motionX;
/* 386 */       this.posY += this.motionY;
/* 387 */       this.posZ += this.motionZ;
/* 388 */       float f3 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 389 */       this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/*     */       
/* 391 */       for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f3) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 396 */       while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */       {
/* 398 */         this.prevRotationPitch += 360.0F;
/*     */       }
/*     */       
/* 401 */       while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */       {
/* 403 */         this.prevRotationYaw -= 360.0F;
/*     */       }
/*     */       
/* 406 */       while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */       {
/* 408 */         this.prevRotationYaw += 360.0F;
/*     */       }
/*     */       
/* 411 */       this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 412 */       this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/* 413 */       float f4 = 0.99F;
/* 414 */       float f6 = 0.05F;
/*     */       
/* 416 */       if (isInWater()) {
/*     */         
/* 418 */         for (int i1 = 0; i1 < 4; i1++) {
/*     */           
/* 420 */           float f8 = 0.25F;
/* 421 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f8, this.posY - this.motionY * f8, this.posZ - this.motionZ * f8, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         } 
/*     */         
/* 424 */         f4 = 0.6F;
/*     */       } 
/*     */       
/* 427 */       if (isWet())
/*     */       {
/* 429 */         extinguish();
/*     */       }
/*     */       
/* 432 */       this.motionX *= f4;
/* 433 */       this.motionY *= f4;
/* 434 */       this.motionZ *= f4;
/* 435 */       this.motionY -= f6;
/* 436 */       setPosition(this.posX, this.posY, this.posZ);
/* 437 */       doBlockCollisions();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 443 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 444 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 445 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 446 */     tagCompound.setShort("life", (short)this.ticksInGround);
/* 447 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 448 */     tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 449 */     tagCompound.setByte("inData", (byte)this.inData);
/* 450 */     tagCompound.setByte("shake", (byte)this.arrowShake);
/* 451 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/* 452 */     tagCompound.setByte("pickup", (byte)this.canBePickedUp);
/* 453 */     tagCompound.setDouble("damage", this.damage);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 458 */     this.xTile = tagCompund.getShort("xTile");
/* 459 */     this.yTile = tagCompund.getShort("yTile");
/* 460 */     this.zTile = tagCompund.getShort("zTile");
/* 461 */     this.ticksInGround = tagCompund.getShort("life");
/*     */     
/* 463 */     if (tagCompund.hasKey("inTile", 8)) {
/*     */       
/* 465 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else {
/*     */       
/* 469 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     } 
/*     */     
/* 472 */     this.inData = tagCompund.getByte("inData") & 0xFF;
/* 473 */     this.arrowShake = tagCompund.getByte("shake") & 0xFF;
/* 474 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */     
/* 476 */     if (tagCompund.hasKey("damage", 99))
/*     */     {
/* 478 */       this.damage = tagCompund.getDouble("damage");
/*     */     }
/*     */     
/* 481 */     if (tagCompund.hasKey("pickup", 99)) {
/*     */       
/* 483 */       this.canBePickedUp = tagCompund.getByte("pickup");
/*     */     }
/* 485 */     else if (tagCompund.hasKey("player", 99)) {
/*     */       
/* 487 */       this.canBePickedUp = tagCompund.getBoolean("player") ? 1 : 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCollideWithPlayer(EntityPlayer entityIn) {
/* 493 */     if (!this.worldObj.isRemote && this.inGround && this.arrowShake <= 0) {
/*     */       
/* 495 */       boolean flag = (this.canBePickedUp == 1 || (this.canBePickedUp == 2 && entityIn.capabilities.isCreativeMode));
/*     */       
/* 497 */       if (this.canBePickedUp == 1 && !entityIn.inventory.addItemStackToInventory(new ItemStack(Items.arrow, 1)))
/*     */       {
/* 499 */         flag = false;
/*     */       }
/*     */       
/* 502 */       if (flag) {
/*     */         
/* 504 */         playSound("random.pop", 0.2F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
/* 505 */         entityIn.onItemPickup(this, 1);
/* 506 */         setDead();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canTriggerWalking() {
/* 513 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDamage(double damageIn) {
/* 518 */     this.damage = damageIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getDamage() {
/* 523 */     return this.damage;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setKnockbackStrength(int knockbackStrengthIn) {
/* 528 */     this.knockbackStrength = knockbackStrengthIn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackWithItem() {
/* 533 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 538 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsCritical(boolean critical) {
/* 543 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 545 */     if (critical) {
/*     */       
/* 547 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else {
/*     */       
/* 551 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getIsCritical() {
/* 557 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/* 558 */     return ((b0 & 0x1) != 0);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityArrow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */