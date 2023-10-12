/*     */ package net.minecraft.entity.boss;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockTorch;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityMultiPart;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.item.EntityEnderCrystal;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.monster.IMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntityDamageSource;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.Explosion;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityDragon extends EntityLiving implements IBossDisplayData, IEntityMultiPart, IMob {
/*     */   public double targetX;
/*     */   public double targetY;
/*     */   public double targetZ;
/*  35 */   public double[][] ringBuffer = new double[64][3];
/*  36 */   public int ringBufferIndex = -1;
/*     */   
/*     */   public EntityDragonPart[] dragonPartArray;
/*     */   public EntityDragonPart dragonPartHead;
/*     */   public EntityDragonPart dragonPartBody;
/*     */   public EntityDragonPart dragonPartTail1;
/*     */   public EntityDragonPart dragonPartTail2;
/*     */   public EntityDragonPart dragonPartTail3;
/*     */   public EntityDragonPart dragonPartWing1;
/*     */   public EntityDragonPart dragonPartWing2;
/*     */   public float prevAnimTime;
/*     */   public float animTime;
/*     */   public boolean forceNewTarget;
/*     */   public boolean slowed;
/*     */   private Entity target;
/*     */   public int deathTicks;
/*     */   public EntityEnderCrystal healingEnderCrystal;
/*     */   
/*     */   public EntityDragon(World worldIn) {
/*  55 */     super(worldIn);
/*  56 */     this.dragonPartArray = new EntityDragonPart[] { this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F) };
/*  57 */     setHealth(getMaxHealth());
/*  58 */     setSize(16.0F, 8.0F);
/*  59 */     this.noClip = true;
/*  60 */     this.isImmuneToFire = true;
/*  61 */     this.targetY = 100.0D;
/*  62 */     this.ignoreFrustumCheck = true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  67 */     super.applyEntityAttributes();
/*  68 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(200.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  73 */     super.entityInit();
/*     */   }
/*     */ 
/*     */   
/*     */   public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_) {
/*  78 */     if (getHealth() <= 0.0F)
/*     */     {
/*  80 */       p_70974_2_ = 0.0F;
/*     */     }
/*     */     
/*  83 */     p_70974_2_ = 1.0F - p_70974_2_;
/*  84 */     int i = this.ringBufferIndex - p_70974_1_ * 1 & 0x3F;
/*  85 */     int j = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 0x3F;
/*  86 */     double[] adouble = new double[3];
/*  87 */     double d0 = this.ringBuffer[i][0];
/*  88 */     double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[j][0] - d0);
/*  89 */     adouble[0] = d0 + d1 * p_70974_2_;
/*  90 */     d0 = this.ringBuffer[i][1];
/*  91 */     d1 = this.ringBuffer[j][1] - d0;
/*  92 */     adouble[1] = d0 + d1 * p_70974_2_;
/*  93 */     adouble[2] = this.ringBuffer[i][2] + (this.ringBuffer[j][2] - this.ringBuffer[i][2]) * p_70974_2_;
/*  94 */     return adouble;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  99 */     if (this.worldObj.isRemote) {
/*     */       
/* 101 */       float f = MathHelper.cos(this.animTime * 3.1415927F * 2.0F);
/* 102 */       float f1 = MathHelper.cos(this.prevAnimTime * 3.1415927F * 2.0F);
/*     */       
/* 104 */       if (f1 <= -0.3F && f >= -0.3F && !isSilent())
/*     */       {
/* 106 */         this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", 5.0F, 0.8F + this.rand.nextFloat() * 0.3F, false);
/*     */       }
/*     */     } 
/*     */     
/* 110 */     this.prevAnimTime = this.animTime;
/*     */     
/* 112 */     if (getHealth() <= 0.0F) {
/*     */       
/* 114 */       float f11 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 115 */       float f13 = (this.rand.nextFloat() - 0.5F) * 4.0F;
/* 116 */       float f14 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 117 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f11, this.posY + 2.0D + f13, this.posZ + f14, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     }
/*     */     else {
/*     */       
/* 121 */       updateDragonEnderCrystal();
/* 122 */       float f10 = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
/* 123 */       f10 *= (float)Math.pow(2.0D, this.motionY);
/*     */       
/* 125 */       if (this.slowed) {
/*     */         
/* 127 */         this.animTime += f10 * 0.5F;
/*     */       }
/*     */       else {
/*     */         
/* 131 */         this.animTime += f10;
/*     */       } 
/*     */       
/* 134 */       this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
/*     */       
/* 136 */       if (isAIDisabled()) {
/*     */         
/* 138 */         this.animTime = 0.5F;
/*     */       }
/*     */       else {
/*     */         
/* 142 */         if (this.ringBufferIndex < 0)
/*     */         {
/* 144 */           for (int i = 0; i < this.ringBuffer.length; i++) {
/*     */             
/* 146 */             this.ringBuffer[i][0] = this.rotationYaw;
/* 147 */             this.ringBuffer[i][1] = this.posY;
/*     */           } 
/*     */         }
/*     */         
/* 151 */         if (++this.ringBufferIndex == this.ringBuffer.length)
/*     */         {
/* 153 */           this.ringBufferIndex = 0;
/*     */         }
/*     */         
/* 156 */         this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
/* 157 */         this.ringBuffer[this.ringBufferIndex][1] = this.posY;
/*     */         
/* 159 */         if (this.worldObj.isRemote) {
/*     */           
/* 161 */           if (this.newPosRotationIncrements > 0)
/*     */           {
/* 163 */             double d10 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
/* 164 */             double d0 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
/* 165 */             double d1 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
/* 166 */             double d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
/* 167 */             this.rotationYaw = (float)(this.rotationYaw + d2 / this.newPosRotationIncrements);
/* 168 */             this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
/* 169 */             this.newPosRotationIncrements--;
/* 170 */             setPosition(d10, d0, d1);
/* 171 */             setRotation(this.rotationYaw, this.rotationPitch);
/*     */           }
/*     */         
/*     */         } else {
/*     */           
/* 176 */           double d11 = this.targetX - this.posX;
/* 177 */           double d12 = this.targetY - this.posY;
/* 178 */           double d13 = this.targetZ - this.posZ;
/* 179 */           double d14 = d11 * d11 + d12 * d12 + d13 * d13;
/*     */           
/* 181 */           if (this.target != null) {
/*     */             
/* 183 */             this.targetX = this.target.posX;
/* 184 */             this.targetZ = this.target.posZ;
/* 185 */             double d3 = this.targetX - this.posX;
/* 186 */             double d5 = this.targetZ - this.posZ;
/* 187 */             double d7 = Math.sqrt(d3 * d3 + d5 * d5);
/* 188 */             double d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;
/*     */             
/* 190 */             if (d8 > 10.0D)
/*     */             {
/* 192 */               d8 = 10.0D;
/*     */             }
/*     */             
/* 195 */             this.targetY = (this.target.getEntityBoundingBox()).minY + d8;
/*     */           }
/*     */           else {
/*     */             
/* 199 */             this.targetX += this.rand.nextGaussian() * 2.0D;
/* 200 */             this.targetZ += this.rand.nextGaussian() * 2.0D;
/*     */           } 
/*     */           
/* 203 */           if (this.forceNewTarget || d14 < 100.0D || d14 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
/*     */           {
/* 205 */             setNewTarget();
/*     */           }
/*     */           
/* 208 */           d12 /= MathHelper.sqrt_double(d11 * d11 + d13 * d13);
/* 209 */           float f17 = 0.6F;
/* 210 */           d12 = MathHelper.clamp_double(d12, -f17, f17);
/* 211 */           this.motionY += d12 * 0.10000000149011612D;
/* 212 */           this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
/* 213 */           double d4 = 180.0D - MathHelper.atan2(d11, d13) * 180.0D / Math.PI;
/* 214 */           double d6 = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw);
/*     */           
/* 216 */           if (d6 > 50.0D)
/*     */           {
/* 218 */             d6 = 50.0D;
/*     */           }
/*     */           
/* 221 */           if (d6 < -50.0D)
/*     */           {
/* 223 */             d6 = -50.0D;
/*     */           }
/*     */           
/* 226 */           Vec3 vec3 = (new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ)).normalize();
/* 227 */           double d15 = -MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F);
/* 228 */           Vec3 vec31 = (new Vec3(MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F), this.motionY, d15)).normalize();
/* 229 */           float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;
/*     */           
/* 231 */           if (f5 < 0.0F)
/*     */           {
/* 233 */             f5 = 0.0F;
/*     */           }
/*     */           
/* 236 */           this.randomYawVelocity *= 0.8F;
/* 237 */           float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
/* 238 */           double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;
/*     */           
/* 240 */           if (d9 > 40.0D)
/*     */           {
/* 242 */             d9 = 40.0D;
/*     */           }
/*     */           
/* 245 */           this.randomYawVelocity = (float)(this.randomYawVelocity + d6 * 0.699999988079071D / d9 / f6);
/* 246 */           this.rotationYaw += this.randomYawVelocity * 0.1F;
/* 247 */           float f7 = (float)(2.0D / (d9 + 1.0D));
/* 248 */           float f8 = 0.06F;
/* 249 */           moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + 1.0F - f7));
/*     */           
/* 251 */           if (this.slowed) {
/*     */             
/* 253 */             moveEntity(this.motionX * 0.800000011920929D, this.motionY * 0.800000011920929D, this.motionZ * 0.800000011920929D);
/*     */           }
/*     */           else {
/*     */             
/* 257 */             moveEntity(this.motionX, this.motionY, this.motionZ);
/*     */           } 
/*     */           
/* 260 */           Vec3 vec32 = (new Vec3(this.motionX, this.motionY, this.motionZ)).normalize();
/* 261 */           float f9 = ((float)vec32.dotProduct(vec31) + 1.0F) / 2.0F;
/* 262 */           f9 = 0.8F + 0.15F * f9;
/* 263 */           this.motionX *= f9;
/* 264 */           this.motionZ *= f9;
/* 265 */           this.motionY *= 0.9100000262260437D;
/*     */         } 
/*     */         
/* 268 */         this.renderYawOffset = this.rotationYaw;
/* 269 */         this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
/* 270 */         this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
/* 271 */         this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
/* 272 */         this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
/* 273 */         this.dragonPartBody.height = 3.0F;
/* 274 */         this.dragonPartBody.width = 5.0F;
/* 275 */         this.dragonPartWing1.height = 2.0F;
/* 276 */         this.dragonPartWing1.width = 4.0F;
/* 277 */         this.dragonPartWing2.height = 3.0F;
/* 278 */         this.dragonPartWing2.width = 4.0F;
/* 279 */         float f12 = (float)(getMovementOffsets(5, 1.0F)[1] - getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * 3.1415927F;
/* 280 */         float f2 = MathHelper.cos(f12);
/* 281 */         float f15 = -MathHelper.sin(f12);
/* 282 */         float f3 = this.rotationYaw * 3.1415927F / 180.0F;
/* 283 */         float f16 = MathHelper.sin(f3);
/* 284 */         float f4 = MathHelper.cos(f3);
/* 285 */         this.dragonPartBody.onUpdate();
/* 286 */         this.dragonPartBody.setLocationAndAngles(this.posX + (f16 * 0.5F), this.posY, this.posZ - (f4 * 0.5F), 0.0F, 0.0F);
/* 287 */         this.dragonPartWing1.onUpdate();
/* 288 */         this.dragonPartWing1.setLocationAndAngles(this.posX + (f4 * 4.5F), this.posY + 2.0D, this.posZ + (f16 * 4.5F), 0.0F, 0.0F);
/* 289 */         this.dragonPartWing2.onUpdate();
/* 290 */         this.dragonPartWing2.setLocationAndAngles(this.posX - (f4 * 4.5F), this.posY + 2.0D, this.posZ - (f16 * 4.5F), 0.0F, 0.0F);
/*     */         
/* 292 */         if (!this.worldObj.isRemote && this.hurtTime == 0) {
/*     */           
/* 294 */           collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartWing1.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
/* 295 */           collideWithEntities(this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartWing2.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D).offset(0.0D, -2.0D, 0.0D)));
/* 296 */           attackEntitiesInList(this.worldObj.getEntitiesWithinAABBExcludingEntity((Entity)this, this.dragonPartHead.getEntityBoundingBox().expand(1.0D, 1.0D, 1.0D)));
/*     */         } 
/*     */         
/* 299 */         double[] adouble1 = getMovementOffsets(5, 1.0F);
/* 300 */         double[] adouble = getMovementOffsets(0, 1.0F);
/* 301 */         float f18 = MathHelper.sin(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
/* 302 */         float f19 = MathHelper.cos(this.rotationYaw * 3.1415927F / 180.0F - this.randomYawVelocity * 0.01F);
/* 303 */         this.dragonPartHead.onUpdate();
/* 304 */         this.dragonPartHead.setLocationAndAngles(this.posX + (f18 * 5.5F * f2), this.posY + (adouble[1] - adouble1[1]) * 1.0D + (f15 * 5.5F), this.posZ - (f19 * 5.5F * f2), 0.0F, 0.0F);
/*     */         
/* 306 */         for (int j = 0; j < 3; j++) {
/*     */           
/* 308 */           EntityDragonPart entitydragonpart = null;
/*     */           
/* 310 */           if (j == 0)
/*     */           {
/* 312 */             entitydragonpart = this.dragonPartTail1;
/*     */           }
/*     */           
/* 315 */           if (j == 1)
/*     */           {
/* 317 */             entitydragonpart = this.dragonPartTail2;
/*     */           }
/*     */           
/* 320 */           if (j == 2)
/*     */           {
/* 322 */             entitydragonpart = this.dragonPartTail3;
/*     */           }
/*     */           
/* 325 */           double[] adouble2 = getMovementOffsets(12 + j * 2, 1.0F);
/* 326 */           float f20 = this.rotationYaw * 3.1415927F / 180.0F + simplifyAngle(adouble2[0] - adouble1[0]) * 3.1415927F / 180.0F * 1.0F;
/* 327 */           float f21 = MathHelper.sin(f20);
/* 328 */           float f22 = MathHelper.cos(f20);
/* 329 */           float f23 = 1.5F;
/* 330 */           float f24 = (j + 1) * 2.0F;
/* 331 */           entitydragonpart.onUpdate();
/* 332 */           entitydragonpart.setLocationAndAngles(this.posX - ((f16 * f23 + f21 * f24) * f2), this.posY + (adouble2[1] - adouble1[1]) * 1.0D - ((f24 + f23) * f15) + 1.5D, this.posZ + ((f4 * f23 + f22 * f24) * f2), 0.0F, 0.0F);
/*     */         } 
/*     */         
/* 335 */         if (!this.worldObj.isRemote)
/*     */         {
/* 337 */           this.slowed = destroyBlocksInAABB(this.dragonPartHead.getEntityBoundingBox()) | destroyBlocksInAABB(this.dragonPartBody.getEntityBoundingBox());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateDragonEnderCrystal() {
/* 345 */     if (this.healingEnderCrystal != null)
/*     */     {
/* 347 */       if (this.healingEnderCrystal.isDead) {
/*     */         
/* 349 */         if (!this.worldObj.isRemote)
/*     */         {
/* 351 */           attackEntityFromPart(this.dragonPartHead, DamageSource.setExplosionSource((Explosion)null), 10.0F);
/*     */         }
/*     */         
/* 354 */         this.healingEnderCrystal = null;
/*     */       }
/* 356 */       else if (this.ticksExisted % 10 == 0 && getHealth() < getMaxHealth()) {
/*     */         
/* 358 */         setHealth(getHealth() + 1.0F);
/*     */       } 
/*     */     }
/*     */     
/* 362 */     if (this.rand.nextInt(10) == 0) {
/*     */       
/* 364 */       float f = 32.0F;
/* 365 */       List<EntityEnderCrystal> list = this.worldObj.getEntitiesWithinAABB(EntityEnderCrystal.class, getEntityBoundingBox().expand(f, f, f));
/* 366 */       EntityEnderCrystal entityendercrystal = null;
/* 367 */       double d0 = Double.MAX_VALUE;
/*     */       
/* 369 */       for (EntityEnderCrystal entityendercrystal1 : list) {
/*     */         
/* 371 */         double d1 = entityendercrystal1.getDistanceSqToEntity((Entity)this);
/*     */         
/* 373 */         if (d1 < d0) {
/*     */           
/* 375 */           d0 = d1;
/* 376 */           entityendercrystal = entityendercrystal1;
/*     */         } 
/*     */       } 
/*     */       
/* 380 */       this.healingEnderCrystal = entityendercrystal;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void collideWithEntities(List<Entity> p_70970_1_) {
/* 386 */     double d0 = ((this.dragonPartBody.getEntityBoundingBox()).minX + (this.dragonPartBody.getEntityBoundingBox()).maxX) / 2.0D;
/* 387 */     double d1 = ((this.dragonPartBody.getEntityBoundingBox()).minZ + (this.dragonPartBody.getEntityBoundingBox()).maxZ) / 2.0D;
/*     */     
/* 389 */     for (Entity entity : p_70970_1_) {
/*     */       
/* 391 */       if (entity instanceof EntityLivingBase) {
/*     */         
/* 393 */         double d2 = entity.posX - d0;
/* 394 */         double d3 = entity.posZ - d1;
/* 395 */         double d4 = d2 * d2 + d3 * d3;
/* 396 */         entity.addVelocity(d2 / d4 * 4.0D, 0.20000000298023224D, d3 / d4 * 4.0D);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void attackEntitiesInList(List<Entity> p_70971_1_) {
/* 403 */     for (int i = 0; i < p_70971_1_.size(); i++) {
/*     */       
/* 405 */       Entity entity = p_70971_1_.get(i);
/*     */       
/* 407 */       if (entity instanceof EntityLivingBase) {
/*     */         
/* 409 */         entity.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 10.0F);
/* 410 */         applyEnchantments((EntityLivingBase)this, entity);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void setNewTarget() {
/* 417 */     this.forceNewTarget = false;
/* 418 */     List<EntityPlayer> list = Lists.newArrayList(this.worldObj.playerEntities);
/* 419 */     Iterator<EntityPlayer> iterator = list.iterator();
/*     */     
/* 421 */     while (iterator.hasNext()) {
/*     */       
/* 423 */       if (((EntityPlayer)iterator.next()).isSpectator())
/*     */       {
/* 425 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */     
/* 429 */     if (this.rand.nextInt(2) == 0 && !list.isEmpty()) {
/*     */       
/* 431 */       this.target = (Entity)list.get(this.rand.nextInt(list.size()));
/*     */     } else {
/*     */       boolean flag;
/*     */ 
/*     */       
/*     */       do {
/* 437 */         this.targetX = 0.0D;
/* 438 */         this.targetY = (70.0F + this.rand.nextFloat() * 50.0F);
/* 439 */         this.targetZ = 0.0D;
/* 440 */         this.targetX += (this.rand.nextFloat() * 120.0F - 60.0F);
/* 441 */         this.targetZ += (this.rand.nextFloat() * 120.0F - 60.0F);
/* 442 */         double d0 = this.posX - this.targetX;
/* 443 */         double d1 = this.posY - this.targetY;
/* 444 */         double d2 = this.posZ - this.targetZ;
/* 445 */         flag = (d0 * d0 + d1 * d1 + d2 * d2 > 100.0D);
/*     */       }
/* 447 */       while (!flag);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 453 */       this.target = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private float simplifyAngle(double p_70973_1_) {
/* 459 */     return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean destroyBlocksInAABB(AxisAlignedBB p_70972_1_) {
/* 464 */     int i = MathHelper.floor_double(p_70972_1_.minX);
/* 465 */     int j = MathHelper.floor_double(p_70972_1_.minY);
/* 466 */     int k = MathHelper.floor_double(p_70972_1_.minZ);
/* 467 */     int l = MathHelper.floor_double(p_70972_1_.maxX);
/* 468 */     int i1 = MathHelper.floor_double(p_70972_1_.maxY);
/* 469 */     int j1 = MathHelper.floor_double(p_70972_1_.maxZ);
/* 470 */     boolean flag = false;
/* 471 */     boolean flag1 = false;
/*     */     
/* 473 */     for (int k1 = i; k1 <= l; k1++) {
/*     */       
/* 475 */       for (int l1 = j; l1 <= i1; l1++) {
/*     */         
/* 477 */         for (int i2 = k; i2 <= j1; i2++) {
/*     */           
/* 479 */           BlockPos blockpos = new BlockPos(k1, l1, i2);
/* 480 */           Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */           
/* 482 */           if (block.getMaterial() != Material.air)
/*     */           {
/* 484 */             if (block != Blocks.barrier && block != Blocks.obsidian && block != Blocks.end_stone && block != Blocks.bedrock && block != Blocks.command_block && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
/*     */               
/* 486 */               flag1 = (this.worldObj.setBlockToAir(blockpos) || flag1);
/*     */             }
/*     */             else {
/*     */               
/* 490 */               flag = true;
/*     */             } 
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 497 */     if (flag1) {
/*     */       
/* 499 */       double d0 = p_70972_1_.minX + (p_70972_1_.maxX - p_70972_1_.minX) * this.rand.nextFloat();
/* 500 */       double d1 = p_70972_1_.minY + (p_70972_1_.maxY - p_70972_1_.minY) * this.rand.nextFloat();
/* 501 */       double d2 = p_70972_1_.minZ + (p_70972_1_.maxZ - p_70972_1_.minZ) * this.rand.nextFloat();
/* 502 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     } 
/*     */     
/* 505 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFromPart(EntityDragonPart dragonPart, DamageSource source, float p_70965_3_) {
/* 510 */     if (dragonPart != this.dragonPartHead)
/*     */     {
/* 512 */       p_70965_3_ = p_70965_3_ / 4.0F + 1.0F;
/*     */     }
/*     */     
/* 515 */     float f = this.rotationYaw * 3.1415927F / 180.0F;
/* 516 */     float f1 = MathHelper.sin(f);
/* 517 */     float f2 = MathHelper.cos(f);
/* 518 */     this.targetX = this.posX + (f1 * 5.0F) + ((this.rand.nextFloat() - 0.5F) * 2.0F);
/* 519 */     this.targetY = this.posY + (this.rand.nextFloat() * 3.0F) + 1.0D;
/* 520 */     this.targetZ = this.posZ - (f2 * 5.0F) + ((this.rand.nextFloat() - 0.5F) * 2.0F);
/* 521 */     this.target = null;
/*     */     
/* 523 */     if (source.getEntity() instanceof EntityPlayer || source.isExplosion())
/*     */     {
/* 525 */       attackDragonFrom(source, p_70965_3_);
/*     */     }
/*     */     
/* 528 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 533 */     if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
/*     */     {
/* 535 */       attackDragonFrom(source, amount);
/*     */     }
/*     */     
/* 538 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean attackDragonFrom(DamageSource source, float amount) {
/* 543 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onKillCommand() {
/* 548 */     setDead();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onDeathUpdate() {
/* 553 */     this.deathTicks++;
/*     */     
/* 555 */     if (this.deathTicks >= 180 && this.deathTicks <= 200) {
/*     */       
/* 557 */       float f = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 558 */       float f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
/* 559 */       float f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
/* 560 */       this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */     } 
/*     */     
/* 563 */     boolean flag = this.worldObj.getGameRules().getBoolean("doMobLoot");
/*     */     
/* 565 */     if (!this.worldObj.isRemote) {
/*     */       
/* 567 */       if (this.deathTicks > 150 && this.deathTicks % 5 == 0 && flag) {
/*     */         
/* 569 */         int i = 1000;
/*     */         
/* 571 */         while (i > 0) {
/*     */           
/* 573 */           int k = EntityXPOrb.getXPSplit(i);
/* 574 */           i -= k;
/* 575 */           this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, k));
/*     */         } 
/*     */       } 
/*     */       
/* 579 */       if (this.deathTicks == 1)
/*     */       {
/* 581 */         this.worldObj.playBroadcastSound(1018, new BlockPos((Entity)this), 0);
/*     */       }
/*     */     } 
/*     */     
/* 585 */     moveEntity(0.0D, 0.10000000149011612D, 0.0D);
/* 586 */     this.renderYawOffset = this.rotationYaw += 20.0F;
/*     */     
/* 588 */     if (this.deathTicks == 200 && !this.worldObj.isRemote) {
/*     */       
/* 590 */       if (flag) {
/*     */         
/* 592 */         int j = 2000;
/*     */         
/* 594 */         while (j > 0) {
/*     */           
/* 596 */           int l = EntityXPOrb.getXPSplit(j);
/* 597 */           j -= l;
/* 598 */           this.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.worldObj, this.posX, this.posY, this.posZ, l));
/*     */         } 
/*     */       } 
/*     */       
/* 602 */       generatePortal(new BlockPos(this.posX, 64.0D, this.posZ));
/* 603 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void generatePortal(BlockPos pos) {
/* 609 */     int i = 4;
/* 610 */     double d0 = 12.25D;
/* 611 */     double d1 = 6.25D;
/*     */     
/* 613 */     for (int j = -1; j <= 32; j++) {
/*     */       
/* 615 */       for (int k = -4; k <= 4; k++) {
/*     */         
/* 617 */         for (int l = -4; l <= 4; l++) {
/*     */           
/* 619 */           double d2 = (k * k + l * l);
/*     */           
/* 621 */           if (d2 <= 12.25D) {
/*     */             
/* 623 */             BlockPos blockpos = pos.add(k, j, l);
/*     */             
/* 625 */             if (j < 0) {
/*     */               
/* 627 */               if (d2 <= 6.25D)
/*     */               {
/* 629 */                 this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
/*     */               }
/*     */             }
/* 632 */             else if (j > 0) {
/*     */               
/* 634 */               this.worldObj.setBlockState(blockpos, Blocks.air.getDefaultState());
/*     */             }
/* 636 */             else if (d2 > 6.25D) {
/*     */               
/* 638 */               this.worldObj.setBlockState(blockpos, Blocks.bedrock.getDefaultState());
/*     */             }
/*     */             else {
/*     */               
/* 642 */               this.worldObj.setBlockState(blockpos, Blocks.end_portal.getDefaultState());
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 649 */     this.worldObj.setBlockState(pos, Blocks.bedrock.getDefaultState());
/* 650 */     this.worldObj.setBlockState(pos.up(), Blocks.bedrock.getDefaultState());
/* 651 */     BlockPos blockpos1 = pos.up(2);
/* 652 */     this.worldObj.setBlockState(blockpos1, Blocks.bedrock.getDefaultState());
/* 653 */     this.worldObj.setBlockState(blockpos1.west(), Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)EnumFacing.EAST));
/* 654 */     this.worldObj.setBlockState(blockpos1.east(), Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)EnumFacing.WEST));
/* 655 */     this.worldObj.setBlockState(blockpos1.north(), Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)EnumFacing.SOUTH));
/* 656 */     this.worldObj.setBlockState(blockpos1.south(), Blocks.torch.getDefaultState().withProperty((IProperty)BlockTorch.FACING, (Comparable)EnumFacing.NORTH));
/* 657 */     this.worldObj.setBlockState(pos.up(3), Blocks.bedrock.getDefaultState());
/* 658 */     this.worldObj.setBlockState(pos.up(4), Blocks.dragon_egg.getDefaultState());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void despawnEntity() {}
/*     */ 
/*     */   
/*     */   public Entity[] getParts() {
/* 667 */     return (Entity[])this.dragonPartArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeCollidedWith() {
/* 672 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public World getWorld() {
/* 677 */     return this.worldObj;
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 682 */     return "mob.enderdragon.growl";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 687 */     return "mob.enderdragon.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 692 */     return 5.0F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\boss\EntityDragon.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */