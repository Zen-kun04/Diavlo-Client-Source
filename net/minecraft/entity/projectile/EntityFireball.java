/*     */ package net.minecraft.entity.projectile;
/*     */ 
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.nbt.NBTBase;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.nbt.NBTTagList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public abstract class EntityFireball extends Entity {
/*  21 */   private int xTile = -1;
/*  22 */   private int yTile = -1;
/*  23 */   private int zTile = -1;
/*     */   
/*     */   private Block inTile;
/*     */   private boolean inGround;
/*     */   public EntityLivingBase shootingEntity;
/*     */   private int ticksAlive;
/*     */   private int ticksInAir;
/*     */   public double accelerationX;
/*     */   public double accelerationY;
/*     */   public double accelerationZ;
/*     */   
/*     */   public EntityFireball(World worldIn) {
/*  35 */     super(worldIn);
/*  36 */     setSize(1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/*  45 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/*  47 */     if (Double.isNaN(d0))
/*     */     {
/*  49 */       d0 = 4.0D;
/*     */     }
/*     */     
/*  52 */     d0 *= 64.0D;
/*  53 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFireball(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
/*  58 */     super(worldIn);
/*  59 */     setSize(1.0F, 1.0F);
/*  60 */     setLocationAndAngles(x, y, z, this.rotationYaw, this.rotationPitch);
/*  61 */     setPosition(x, y, z);
/*  62 */     double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
/*  63 */     this.accelerationX = accelX / d0 * 0.1D;
/*  64 */     this.accelerationY = accelY / d0 * 0.1D;
/*  65 */     this.accelerationZ = accelZ / d0 * 0.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFireball(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
/*  70 */     super(worldIn);
/*  71 */     this.shootingEntity = shooter;
/*  72 */     setSize(1.0F, 1.0F);
/*  73 */     setLocationAndAngles(shooter.posX, shooter.posY, shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
/*  74 */     setPosition(this.posX, this.posY, this.posZ);
/*  75 */     this.motionX = this.motionY = this.motionZ = 0.0D;
/*  76 */     accelX += this.rand.nextGaussian() * 0.4D;
/*  77 */     accelY += this.rand.nextGaussian() * 0.4D;
/*  78 */     accelZ += this.rand.nextGaussian() * 0.4D;
/*  79 */     double d0 = MathHelper.sqrt_double(accelX * accelX + accelY * accelY + accelZ * accelZ);
/*  80 */     this.accelerationX = accelX / d0 * 0.1D;
/*  81 */     this.accelerationY = accelY / d0 * 0.1D;
/*  82 */     this.accelerationZ = accelZ / d0 * 0.1D;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  87 */     if (this.worldObj.isRemote || ((this.shootingEntity == null || !this.shootingEntity.isDead) && this.worldObj.isBlockLoaded(new BlockPos(this)))) {
/*     */       
/*  89 */       super.onUpdate();
/*  90 */       setFire(1);
/*     */       
/*  92 */       if (this.inGround) {
/*     */         
/*  94 */         if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
/*     */           
/*  96 */           this.ticksAlive++;
/*     */           
/*  98 */           if (this.ticksAlive == 600)
/*     */           {
/* 100 */             setDead();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 106 */         this.inGround = false;
/* 107 */         this.motionX *= (this.rand.nextFloat() * 0.2F);
/* 108 */         this.motionY *= (this.rand.nextFloat() * 0.2F);
/* 109 */         this.motionZ *= (this.rand.nextFloat() * 0.2F);
/* 110 */         this.ticksAlive = 0;
/* 111 */         this.ticksInAir = 0;
/*     */       }
/*     */       else {
/*     */         
/* 115 */         this.ticksInAir++;
/*     */       } 
/*     */       
/* 118 */       Vec3 vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 119 */       Vec3 vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 120 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec3, vec31);
/* 121 */       vec3 = new Vec3(this.posX, this.posY, this.posZ);
/* 122 */       vec31 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 124 */       if (movingobjectposition != null)
/*     */       {
/* 126 */         vec31 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 129 */       Entity entity = null;
/* 130 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 131 */       double d0 = 0.0D;
/*     */       
/* 133 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 135 */         Entity entity1 = list.get(i);
/*     */         
/* 137 */         if (entity1.canBeCollidedWith() && (!entity1.isEntityEqual((Entity)this.shootingEntity) || this.ticksInAir >= 25)) {
/*     */           
/* 139 */           float f = 0.3F;
/* 140 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 141 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec3, vec31);
/*     */           
/* 143 */           if (movingobjectposition1 != null) {
/*     */             
/* 145 */             double d1 = vec3.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 147 */             if (d1 < d0 || d0 == 0.0D) {
/*     */               
/* 149 */               entity = entity1;
/* 150 */               d0 = d1;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 156 */       if (entity != null)
/*     */       {
/* 158 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 161 */       if (movingobjectposition != null)
/*     */       {
/* 163 */         onImpact(movingobjectposition);
/*     */       }
/*     */       
/* 166 */       this.posX += this.motionX;
/* 167 */       this.posY += this.motionY;
/* 168 */       this.posZ += this.motionZ;
/* 169 */       float f1 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 170 */       this.rotationYaw = (float)(MathHelper.atan2(this.motionZ, this.motionX) * 180.0D / Math.PI) + 90.0F;
/*     */       
/* 172 */       for (this.rotationPitch = (float)(MathHelper.atan2(f1, this.motionY) * 180.0D / Math.PI) - 90.0F; this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 177 */       while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */       {
/* 179 */         this.prevRotationPitch += 360.0F;
/*     */       }
/*     */       
/* 182 */       while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */       {
/* 184 */         this.prevRotationYaw -= 360.0F;
/*     */       }
/*     */       
/* 187 */       while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */       {
/* 189 */         this.prevRotationYaw += 360.0F;
/*     */       }
/*     */       
/* 192 */       this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 193 */       this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/* 194 */       float f2 = getMotionFactor();
/*     */       
/* 196 */       if (isInWater()) {
/*     */         
/* 198 */         for (int j = 0; j < 4; j++) {
/*     */           
/* 200 */           float f3 = 0.25F;
/* 201 */           this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * f3, this.posY - this.motionY * f3, this.posZ - this.motionZ * f3, this.motionX, this.motionY, this.motionZ, new int[0]);
/*     */         } 
/*     */         
/* 204 */         f2 = 0.8F;
/*     */       } 
/*     */       
/* 207 */       this.motionX += this.accelerationX;
/* 208 */       this.motionY += this.accelerationY;
/* 209 */       this.motionZ += this.accelerationZ;
/* 210 */       this.motionX *= f2;
/* 211 */       this.motionY *= f2;
/* 212 */       this.motionZ *= f2;
/* 213 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY + 0.5D, this.posZ, 0.0D, 0.0D, 0.0D, new int[0]);
/* 214 */       setPosition(this.posX, this.posY, this.posZ);
/*     */     }
/*     */     else {
/*     */       
/* 218 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getMotionFactor() {
/* 224 */     return 0.95F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract void onImpact(MovingObjectPosition paramMovingObjectPosition);
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 231 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 232 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 233 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 234 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 235 */     tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 236 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/* 237 */     tagCompound.setTag("direction", (NBTBase)newDoubleNBTList(new double[] { this.motionX, this.motionY, this.motionZ }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 242 */     this.xTile = tagCompund.getShort("xTile");
/* 243 */     this.yTile = tagCompund.getShort("yTile");
/* 244 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 246 */     if (tagCompund.hasKey("inTile", 8)) {
/*     */       
/* 248 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else {
/*     */       
/* 252 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     } 
/*     */     
/* 255 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */     
/* 257 */     if (tagCompund.hasKey("direction", 9)) {
/*     */       
/* 259 */       NBTTagList nbttaglist = tagCompund.getTagList("direction", 6);
/* 260 */       this.motionX = nbttaglist.getDoubleAt(0);
/* 261 */       this.motionY = nbttaglist.getDoubleAt(1);
/* 262 */       this.motionZ = nbttaglist.getDoubleAt(2);
/*     */     }
/*     */     else {
/*     */       
/* 266 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 272 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getCollisionBorderSize() {
/* 277 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 282 */     if (isEntityInvulnerable(source))
/*     */     {
/* 284 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 288 */     setBeenAttacked();
/*     */     
/* 290 */     if (source.getEntity() != null) {
/*     */       
/* 292 */       Vec3 vec3 = source.getEntity().getLookVec();
/*     */       
/* 294 */       if (vec3 != null) {
/*     */         
/* 296 */         this.motionX = vec3.xCoord;
/* 297 */         this.motionY = vec3.yCoord;
/* 298 */         this.motionZ = vec3.zCoord;
/* 299 */         this.accelerationX = this.motionX * 0.1D;
/* 300 */         this.accelerationY = this.motionY * 0.1D;
/* 301 */         this.accelerationZ = this.motionZ * 0.1D;
/*     */       } 
/*     */       
/* 304 */       if (source.getEntity() instanceof EntityLivingBase)
/*     */       {
/* 306 */         this.shootingEntity = (EntityLivingBase)source.getEntity();
/*     */       }
/*     */       
/* 309 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 313 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float getBrightness(float partialTicks) {
/* 320 */     return 1.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/* 325 */     return 15728880;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityFireball.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */