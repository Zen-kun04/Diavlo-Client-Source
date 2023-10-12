/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IProjectile;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public abstract class EntityThrowable
/*     */   extends Entity
/*     */   implements IProjectile {
/*  24 */   private int xTile = -1;
/*  25 */   private int yTile = -1;
/*  26 */   private int zTile = -1;
/*     */   
/*     */   private Block inTile;
/*     */   protected boolean inGround;
/*     */   public int throwableShake;
/*     */   private EntityLivingBase thrower;
/*     */   private String throwerName;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*     */   
/*     */   public EntityThrowable(World worldIn) {
/*  37 */     super(worldIn);
/*  38 */     setSize(0.25F, 0.25F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  47 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  49 */     if (Double.isNaN(d0))
/*     */     {
/*  51 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  54 */     d0 *= 64.0D;
/*  55 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityThrowable(World worldIn, EntityLivingBase throwerIn) {
/*  60 */     super(worldIn);
/*  61 */     this.thrower = throwerIn;
/*  62 */     setSize(0.25F, 0.25F);
/*  63 */     setLocationAndAngles(throwerIn.posX, throwerIn.posY + throwerIn.getEyeHeight(), throwerIn.posZ, throwerIn.rotationYaw, throwerIn.rotationPitch);
/*  64 */     this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/*  65 */     this.posY -= 0.10000000149011612D;
/*  66 */     this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/*  67 */     setPosition(this.posX, this.posY, this.posZ);
/*  68 */     float f = 0.4F;
/*  69 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/*  70 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/*  71 */     this.motionY = (-MathHelper.sin((this.rotationPitch + getInaccuracy()) / 180.0F * 3.1415927F) * f);
/*  72 */     setThrowableHeading(this.motionX, this.motionY, this.motionZ, getVelocity(), 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityThrowable(World worldIn, double x, double y, double z) {
/*  77 */     super(worldIn);
/*  78 */     this.ticksInGround = 0;
/*  79 */     setSize(0.25F, 0.25F);
/*  80 */     setPosition(x, y, z);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getVelocity() {
/*  85 */     return 1.5F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getInaccuracy() {
/*  90 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setThrowableHeading(double x, double y, double z, float velocity, float inaccuracy) {
/*  95 */     float f = MathHelper.sqrt_double(x * x + y * y + z * z);
/*  96 */     x /= f;
/*  97 */     y /= f;
/*  98 */     z /= f;
/*  99 */     x += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 100 */     y += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 101 */     z += this.rand.nextGaussian() * 0.007499999832361937D * inaccuracy;
/* 102 */     x *= velocity;
/* 103 */     y *= velocity;
/* 104 */     z *= velocity;
/* 105 */     this.motionX = x;
/* 106 */     this.motionY = y;
/* 107 */     this.motionZ = z;
/* 108 */     float f1 = MathHelper.sqrt_double(x * x + z * z);
/* 109 */     this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/* 110 */     this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f1) * 180.0D / Math.PI);
/* 111 */     this.ticksInGround = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/* 116 */     this.motionX = x;
/* 117 */     this.motionY = y;
/* 118 */     this.motionZ = z;
/*     */     
/* 120 */     if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
/*     */       
/* 122 */       float f = MathHelper.sqrt_double(x * x + z * z);
/* 123 */       this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(x, z) * 180.0D / Math.PI);
/* 124 */       this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(y, f) * 180.0D / Math.PI);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 130 */     this.lastTickPosX = this.posX;
/* 131 */     this.lastTickPosY = this.posY;
/* 132 */     this.lastTickPosZ = this.posZ;
/* 133 */     super.onUpdate();
/*     */     
/* 135 */     if (this.throwableShake > 0)
/*     */     {
/* 137 */       this.throwableShake--;
/*     */     }
/*     */     
/* 140 */     if (this.inGround) {
/*     */       
/* 142 */       if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
/*     */         
/* 144 */         this.ticksInGround++;
/*     */         
/* 146 */         if (this.ticksInGround == 1200)
/*     */         {
/* 148 */           setDead();
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 154 */       this.inGround = false;
/* 155 */       this.motionX *= (this.rand.nextFloat() * 0.2F);
/* 156 */       this.motionY *= (this.rand.nextFloat() * 0.2F);
/* 157 */       this.motionZ *= (this.rand.nextFloat() * 0.2F);
/* 158 */       this.ticksInGround = 0;
/* 159 */       this.ticksInAir = 0;
/*     */     }
/*     */     else {
/*     */       
/* 163 */       this.ticksInAir++;
/*     */     } 
/*     */     
/* 166 */     Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 167 */     Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 168 */     MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
/* 169 */     vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 170 */     vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */     
/* 172 */     if (movingobjectposition != null)
/*     */     {
/* 174 */       vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */     }
/*     */     
/* 177 */     if (!this.worldObj.isRemote) {
/*     */       
/* 179 */       Entity entity = null;
/* 180 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 181 */       double d0 = 0.0D;
/* 182 */       EntityLivingBase entitylivingbase = getThrower();
/*     */       
/* 184 */       for (int j = 0; j < list.size(); j++) {
/*     */         
/* 186 */         Entity entity1 = list.get(j);
/*     */         
/* 188 */         if (entity1.canBeCollidedWith() && (entity1 != entitylivingbase || this.ticksInAir >= 5)) {
/*     */           
/* 190 */           float f = 0.3F;
/* 191 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 192 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
/*     */           
/* 194 */           if (movingobjectposition1 != null) {
/*     */             
/* 196 */             double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 198 */             if (d1 < d0 || d0 == 0.0D) {
/*     */               
/* 200 */               entity = entity1;
/* 201 */               d0 = d1;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 207 */       if (entity != null)
/*     */       {
/* 209 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */     } 
/*     */     
/* 213 */     if (movingobjectposition != null)
/*     */     {
/* 215 */       if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK && this.worldObj.getBlockState(movingobjectposition.getBlockPos()).getBlock() == Blocks.portal) {
/*     */         
/* 217 */         setPortal(movingobjectposition.getBlockPos());
/*     */       }
/*     */       else {
/*     */         
/* 221 */         onImpact(movingobjectposition);
/*     */       } 
/*     */     }
/*     */     
/* 225 */     this.posX += this.motionX;
/* 226 */     this.posY += this.motionY;
/* 227 */     this.posZ += this.motionZ;
/* 228 */     float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 229 */     this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/*     */     
/* 231 */     for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f1) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 236 */     while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */     {
/* 238 */       this.prevRotationPitch += 360.0F;
/*     */     }
/*     */     
/* 241 */     while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */     {
/* 243 */       this.prevRotationYaw -= 360.0F;
/*     */     }
/*     */     
/* 246 */     while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */     {
/* 248 */       this.prevRotationYaw += 360.0F;
/*     */     }
/*     */     
/* 251 */     this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 252 */     this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/* 253 */     float f2 = 0.99F;
/* 254 */     float f3 = getGravityVelocity();
/*     */     
/* 256 */     if (isInWater()) {
/*     */       
/* 258 */       for (int i = 0; i < 4; i++) {
/*     */         
/* 260 */         float f4 = 0.25F;
/* 261 */         this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f4, this.posY - this.motionY * f4, this.posZ - this.motionZ * f4, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */       } 
/*     */       
/* 264 */       f2 = 0.8F;
/*     */     } 
/*     */     
/* 267 */     this.motionX *= f2;
/* 268 */     this.motionY *= f2;
/* 269 */     this.motionZ *= f2;
/* 270 */     this.motionY -= f3;
/* 271 */     setPosition(this.posX, this.posY, this.posZ);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getGravityVelocity() {
/* 276 */     return 0.03F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void onImpact(MovingObjectPosition paramMovingObjectPosition);
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 283 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 284 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 285 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 286 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 287 */     tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 288 */     tagCompound.setByte("shake", (byte)this.throwableShake);
/* 289 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/*     */     
/* 291 */     if ((this.throwerName == null || this.throwerName.length() == 0) && this.thrower instanceof net.minecraft.entity.player.EntityPlayer)
/*     */     {
/* 293 */       this.throwerName = this.thrower.getName();
/*     */     }
/*     */     
/* 296 */     tagCompound.setString("ownerName", (this.throwerName == null) ? "" : this.throwerName);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 301 */     this.xTile = tagCompund.getShort("xTile");
/* 302 */     this.yTile = tagCompund.getShort("yTile");
/* 303 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 305 */     if (tagCompund.hasKey("inTile", 8)) {
/*     */       
/* 307 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else {
/*     */       
/* 311 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     } 
/*     */     
/* 314 */     this.throwableShake = tagCompund.getByte("shake") & 0xFF;
/* 315 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/* 316 */     this.thrower = null;
/* 317 */     this.throwerName = tagCompund.getString("ownerName");
/*     */     
/* 319 */     if (this.throwerName != null && this.throwerName.length() == 0)
/*     */     {
/* 321 */       this.throwerName = null;
/*     */     }
/*     */     
/* 324 */     this.thrower = getThrower();
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityLivingBase getThrower() {
/* 329 */     if (this.thrower == null && this.throwerName != null && this.throwerName.length() > 0) {
/*     */       
/* 331 */       this.thrower = (EntityLivingBase)this.worldObj.getPlayerEntityByName(this.throwerName);
/*     */       
/* 333 */       if (this.thrower == null && this.worldObj instanceof WorldServer) {
/*     */         
/*     */         try {
/*     */           
/* 337 */           Entity entity = ((WorldServer)this.worldObj).getEntityFromUuid(UUID.fromString(this.throwerName));
/*     */           
/* 339 */           if (entity instanceof EntityLivingBase)
/*     */           {
/* 341 */             this.thrower = (EntityLivingBase)entity;
/*     */           }
/*     */         }
/* 344 */         catch (Throwable var2) {
/*     */           
/* 346 */           this.thrower = null;
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 351 */     return this.thrower;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityThrowable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */