/*     */ package net.minecraft.entity.projectile;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.item.EntityXPOrb;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.EnumDyeColor;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemFishFood;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.stats.StatList;
/*     */ import net.minecraft.util.AxisAlignedBB;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.MovingObjectPosition;
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.util.WeightedRandom;
/*     */ import net.minecraft.util.WeightedRandomFishable;
/*     */ import net.minecraft.world.World;
/*     */ import net.minecraft.world.WorldServer;
/*     */ 
/*     */ public class EntityFishHook extends Entity {
/*  34 */   private static final List<WeightedRandomFishable> JUNK = Arrays.asList(new WeightedRandomFishable[] { (new WeightedRandomFishable(new ItemStack((Item)Items.leather_boots), 10)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.leather), 10), new WeightedRandomFishable(new ItemStack(Items.bone), 10), new WeightedRandomFishable(new ItemStack((Item)Items.potionitem), 10), new WeightedRandomFishable(new ItemStack(Items.string), 5), (new WeightedRandomFishable(new ItemStack((Item)Items.fishing_rod), 2)).setMaxDamagePercent(0.9F), new WeightedRandomFishable(new ItemStack(Items.bowl), 10), new WeightedRandomFishable(new ItemStack(Items.stick), 5), new WeightedRandomFishable(new ItemStack(Items.dye, 10, EnumDyeColor.BLACK.getDyeDamage()), 1), new WeightedRandomFishable(new ItemStack((Block)Blocks.tripwire_hook), 10), new WeightedRandomFishable(new ItemStack(Items.rotten_flesh), 10) });
/*  35 */   private static final List<WeightedRandomFishable> TREASURE = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Blocks.waterlily), 1), new WeightedRandomFishable(new ItemStack(Items.name_tag), 1), new WeightedRandomFishable(new ItemStack(Items.saddle), 1), (new WeightedRandomFishable(new ItemStack((Item)Items.bow), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack((Item)Items.fishing_rod), 1)).setMaxDamagePercent(0.25F).setEnchantable(), (new WeightedRandomFishable(new ItemStack(Items.book), 1)).setEnchantable() });
/*  36 */   private static final List<WeightedRandomFishable> FISH = Arrays.asList(new WeightedRandomFishable[] { new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 60), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.SALMON.getMetadata()), 25), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.CLOWNFISH.getMetadata()), 2), new WeightedRandomFishable(new ItemStack(Items.fish, 1, ItemFishFood.FishType.PUFFERFISH.getMetadata()), 13) });
/*     */   
/*     */   private int xTile;
/*     */   private int yTile;
/*     */   private int zTile;
/*     */   private Block inTile;
/*     */   private boolean inGround;
/*     */   public int shake;
/*     */   public EntityPlayer angler;
/*     */   private int ticksInGround;
/*     */   private int ticksInAir;
/*     */   private int ticksCatchable;
/*     */   private int ticksCaughtDelay;
/*     */   private int ticksCatchableDelay;
/*     */   private float fishApproachAngle;
/*     */   public Entity caughtEntity;
/*     */   private int fishPosRotationIncrements;
/*     */   private double fishX;
/*     */   private double fishY;
/*     */   private double fishZ;
/*     */   private double fishYaw;
/*     */   private double fishPitch;
/*     */   private double clientMotionX;
/*     */   private double clientMotionY;
/*     */   private double clientMotionZ;
/*     */   
/*     */   public static List<WeightedRandomFishable> func_174855_j() {
/*  63 */     return FISH;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFishHook(World worldIn) {
/*  68 */     super(worldIn);
/*  69 */     this.xTile = -1;
/*  70 */     this.yTile = -1;
/*  71 */     this.zTile = -1;
/*  72 */     setSize(0.25F, 0.25F);
/*  73 */     this.ignoreFrustumCheck = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFishHook(World worldIn, double x, double y, double z, EntityPlayer anglerIn) {
/*  78 */     this(worldIn);
/*  79 */     setPosition(x, y, z);
/*  80 */     this.ignoreFrustumCheck = true;
/*  81 */     this.angler = anglerIn;
/*  82 */     anglerIn.fishEntity = this;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityFishHook(World worldIn, EntityPlayer fishingPlayer) {
/*  87 */     super(worldIn);
/*  88 */     this.xTile = -1;
/*  89 */     this.yTile = -1;
/*  90 */     this.zTile = -1;
/*  91 */     this.ignoreFrustumCheck = true;
/*  92 */     this.angler = fishingPlayer;
/*  93 */     this.angler.fishEntity = this;
/*  94 */     setSize(0.25F, 0.25F);
/*  95 */     setLocationAndAngles(fishingPlayer.posX, fishingPlayer.posY + fishingPlayer.getEyeHeight(), fishingPlayer.posZ, fishingPlayer.rotationYaw, fishingPlayer.rotationPitch);
/*  96 */     this.posX -= (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/*  97 */     this.posY -= 0.10000000149011612D;
/*  98 */     this.posZ -= (MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * 0.16F);
/*  99 */     setPosition(this.posX, this.posY, this.posZ);
/* 100 */     float f = 0.4F;
/* 101 */     this.motionX = (-MathHelper.sin(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 102 */     this.motionZ = (MathHelper.cos(this.rotationYaw / 180.0F * 3.1415927F) * MathHelper.cos(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 103 */     this.motionY = (-MathHelper.sin(this.rotationPitch / 180.0F * 3.1415927F) * f);
/* 104 */     handleHookCasting(this.motionX, this.motionY, this.motionZ, 1.5F, 1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void entityInit() {}
/*     */ 
/*     */   
/*     */   public boolean isInRangeToRenderDist(double distance) {
/* 113 */     double d0 = getEntityBoundingBox().getAverageEdgeLength() * 4.0D;
/*     */     
/* 115 */     if (Double.isNaN(d0))
/*     */     {
/* 117 */       d0 = 4.0D;
/*     */     }
/*     */     
/* 120 */     d0 *= 64.0D;
/* 121 */     return (distance < d0 * d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleHookCasting(double p_146035_1_, double p_146035_3_, double p_146035_5_, float p_146035_7_, float p_146035_8_) {
/* 126 */     float f = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_3_ * p_146035_3_ + p_146035_5_ * p_146035_5_);
/* 127 */     p_146035_1_ /= f;
/* 128 */     p_146035_3_ /= f;
/* 129 */     p_146035_5_ /= f;
/* 130 */     p_146035_1_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 131 */     p_146035_3_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 132 */     p_146035_5_ += this.rand.nextGaussian() * 0.007499999832361937D * p_146035_8_;
/* 133 */     p_146035_1_ *= p_146035_7_;
/* 134 */     p_146035_3_ *= p_146035_7_;
/* 135 */     p_146035_5_ *= p_146035_7_;
/* 136 */     this.motionX = p_146035_1_;
/* 137 */     this.motionY = p_146035_3_;
/* 138 */     this.motionZ = p_146035_5_;
/* 139 */     float f1 = MathHelper.sqrt_double(p_146035_1_ * p_146035_1_ + p_146035_5_ * p_146035_5_);
/* 140 */     this.prevRotationYaw = this.rotationYaw = (float)(MathHelper.atan2(p_146035_1_, p_146035_5_) * 180.0D / Math.PI);
/* 141 */     this.prevRotationPitch = this.rotationPitch = (float)(MathHelper.atan2(p_146035_3_, f1) * 180.0D / Math.PI);
/* 142 */     this.ticksInGround = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPositionAndRotation2(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean p_180426_10_) {
/* 147 */     this.fishX = x;
/* 148 */     this.fishY = y;
/* 149 */     this.fishZ = z;
/* 150 */     this.fishYaw = yaw;
/* 151 */     this.fishPitch = pitch;
/* 152 */     this.fishPosRotationIncrements = posRotationIncrements;
/* 153 */     this.motionX = this.clientMotionX;
/* 154 */     this.motionY = this.clientMotionY;
/* 155 */     this.motionZ = this.clientMotionZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVelocity(double x, double y, double z) {
/* 160 */     this.clientMotionX = this.motionX = x;
/* 161 */     this.clientMotionY = this.motionY = y;
/* 162 */     this.clientMotionZ = this.motionZ = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/* 167 */     super.onUpdate();
/*     */     
/* 169 */     if (this.fishPosRotationIncrements > 0) {
/*     */       
/* 171 */       double d7 = this.posX + (this.fishX - this.posX) / this.fishPosRotationIncrements;
/* 172 */       double d8 = this.posY + (this.fishY - this.posY) / this.fishPosRotationIncrements;
/* 173 */       double d9 = this.posZ + (this.fishZ - this.posZ) / this.fishPosRotationIncrements;
/* 174 */       double d1 = MathHelper.wrapAngleTo180_double(this.fishYaw - this.rotationYaw);
/* 175 */       this.rotationYaw = (float)(this.rotationYaw + d1 / this.fishPosRotationIncrements);
/* 176 */       this.rotationPitch = (float)(this.rotationPitch + (this.fishPitch - this.rotationPitch) / this.fishPosRotationIncrements);
/* 177 */       this.fishPosRotationIncrements--;
/* 178 */       setPosition(d7, d8, d9);
/* 179 */       setRotation(this.rotationYaw, this.rotationPitch);
/*     */     }
/*     */     else {
/*     */       
/* 183 */       if (!this.worldObj.isRemote) {
/*     */         
/* 185 */         ItemStack itemstack = this.angler.getCurrentEquippedItem();
/*     */         
/* 187 */         if (this.angler.isDead || !this.angler.isEntityAlive() || itemstack == null || itemstack.getItem() != Items.fishing_rod || getDistanceSqToEntity((Entity)this.angler) > 1024.0D) {
/*     */           
/* 189 */           setDead();
/* 190 */           this.angler.fishEntity = null;
/*     */           
/*     */           return;
/*     */         } 
/* 194 */         if (this.caughtEntity != null) {
/*     */           
/* 196 */           if (!this.caughtEntity.isDead) {
/*     */             
/* 198 */             this.posX = this.caughtEntity.posX;
/* 199 */             double d17 = this.caughtEntity.height;
/* 200 */             this.posY = (this.caughtEntity.getEntityBoundingBox()).minY + d17 * 0.8D;
/* 201 */             this.posZ = this.caughtEntity.posZ;
/*     */             
/*     */             return;
/*     */           } 
/* 205 */           this.caughtEntity = null;
/*     */         } 
/*     */       } 
/*     */       
/* 209 */       if (this.shake > 0)
/*     */       {
/* 211 */         this.shake--;
/*     */       }
/*     */       
/* 214 */       if (this.inGround) {
/*     */         
/* 216 */         if (this.worldObj.getBlockState(new BlockPos(this.xTile, this.yTile, this.zTile)).getBlock() == this.inTile) {
/*     */           
/* 218 */           this.ticksInGround++;
/*     */           
/* 220 */           if (this.ticksInGround == 1200)
/*     */           {
/* 222 */             setDead();
/*     */           }
/*     */           
/*     */           return;
/*     */         } 
/*     */         
/* 228 */         this.inGround = false;
/* 229 */         this.motionX *= (this.rand.nextFloat() * 0.2F);
/* 230 */         this.motionY *= (this.rand.nextFloat() * 0.2F);
/* 231 */         this.motionZ *= (this.rand.nextFloat() * 0.2F);
/* 232 */         this.ticksInGround = 0;
/* 233 */         this.ticksInAir = 0;
/*     */       }
/*     */       else {
/*     */         
/* 237 */         this.ticksInAir++;
/*     */       } 
/*     */       
/* 240 */       Vec3 vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 241 */       Vec3 vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/* 242 */       MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(vec31, vec3);
/* 243 */       vec31 = new Vec3(this.posX, this.posY, this.posZ);
/* 244 */       vec3 = new Vec3(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
/*     */       
/* 246 */       if (movingobjectposition != null)
/*     */       {
/* 248 */         vec3 = new Vec3(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
/*     */       }
/*     */       
/* 251 */       Entity entity = null;
/* 252 */       List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, getEntityBoundingBox().addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
/* 253 */       double d0 = 0.0D;
/*     */       
/* 255 */       for (int i = 0; i < list.size(); i++) {
/*     */         
/* 257 */         Entity entity1 = list.get(i);
/*     */         
/* 259 */         if (entity1.canBeCollidedWith() && (entity1 != this.angler || this.ticksInAir >= 5)) {
/*     */           
/* 261 */           float f = 0.3F;
/* 262 */           AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand(f, f, f);
/* 263 */           MovingObjectPosition movingobjectposition1 = axisalignedbb.calculateIntercept(vec31, vec3);
/*     */           
/* 265 */           if (movingobjectposition1 != null) {
/*     */             
/* 267 */             double d2 = vec31.squareDistanceTo(movingobjectposition1.hitVec);
/*     */             
/* 269 */             if (d2 < d0 || d0 == 0.0D) {
/*     */               
/* 271 */               entity = entity1;
/* 272 */               d0 = d2;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 278 */       if (entity != null)
/*     */       {
/* 280 */         movingobjectposition = new MovingObjectPosition(entity);
/*     */       }
/*     */       
/* 283 */       if (movingobjectposition != null)
/*     */       {
/* 285 */         if (movingobjectposition.entityHit != null) {
/*     */           
/* 287 */           if (movingobjectposition.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, (Entity)this.angler), 0.0F))
/*     */           {
/* 289 */             this.caughtEntity = movingobjectposition.entityHit;
/*     */           }
/*     */         }
/*     */         else {
/*     */           
/* 294 */           this.inGround = true;
/*     */         } 
/*     */       }
/*     */       
/* 298 */       if (!this.inGround) {
/*     */         
/* 300 */         moveEntity(this.motionX, this.motionY, this.motionZ);
/* 301 */         float f5 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
/* 302 */         this.rotationYaw = (float)(MathHelper.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
/*     */         
/* 304 */         for (this.rotationPitch = (float)(MathHelper.atan2(this.motionY, f5) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 309 */         while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
/*     */         {
/* 311 */           this.prevRotationPitch += 360.0F;
/*     */         }
/*     */         
/* 314 */         while (this.rotationYaw - this.prevRotationYaw < -180.0F)
/*     */         {
/* 316 */           this.prevRotationYaw -= 360.0F;
/*     */         }
/*     */         
/* 319 */         while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
/*     */         {
/* 321 */           this.prevRotationYaw += 360.0F;
/*     */         }
/*     */         
/* 324 */         this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
/* 325 */         this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
/* 326 */         float f6 = 0.92F;
/*     */         
/* 328 */         if (this.onGround || this.isCollidedHorizontally)
/*     */         {
/* 330 */           f6 = 0.5F;
/*     */         }
/*     */         
/* 333 */         int j = 5;
/* 334 */         double d10 = 0.0D;
/*     */         
/* 336 */         for (int k = 0; k < j; k++) {
/*     */           
/* 338 */           AxisAlignedBB axisalignedbb1 = getEntityBoundingBox();
/* 339 */           double d3 = axisalignedbb1.maxY - axisalignedbb1.minY;
/* 340 */           double d4 = axisalignedbb1.minY + d3 * k / j;
/* 341 */           double d5 = axisalignedbb1.minY + d3 * (k + 1) / j;
/* 342 */           AxisAlignedBB axisalignedbb2 = new AxisAlignedBB(axisalignedbb1.minX, d4, axisalignedbb1.minZ, axisalignedbb1.maxX, d5, axisalignedbb1.maxZ);
/*     */           
/* 344 */           if (this.worldObj.isAABBInMaterial(axisalignedbb2, Material.water))
/*     */           {
/* 346 */             d10 += 1.0D / j;
/*     */           }
/*     */         } 
/*     */         
/* 350 */         if (!this.worldObj.isRemote && d10 > 0.0D) {
/*     */           
/* 352 */           WorldServer worldserver = (WorldServer)this.worldObj;
/* 353 */           int l = 1;
/* 354 */           BlockPos blockpos = (new BlockPos(this)).up();
/*     */           
/* 356 */           if (this.rand.nextFloat() < 0.25F && this.worldObj.isRainingAt(blockpos))
/*     */           {
/* 358 */             l = 2;
/*     */           }
/*     */           
/* 361 */           if (this.rand.nextFloat() < 0.5F && !this.worldObj.canSeeSky(blockpos))
/*     */           {
/* 363 */             l--;
/*     */           }
/*     */           
/* 366 */           if (this.ticksCatchable > 0) {
/*     */             
/* 368 */             this.ticksCatchable--;
/*     */             
/* 370 */             if (this.ticksCatchable <= 0)
/*     */             {
/* 372 */               this.ticksCaughtDelay = 0;
/* 373 */               this.ticksCatchableDelay = 0;
/*     */             }
/*     */           
/* 376 */           } else if (this.ticksCatchableDelay > 0) {
/*     */             
/* 378 */             this.ticksCatchableDelay -= l;
/*     */             
/* 380 */             if (this.ticksCatchableDelay <= 0) {
/*     */               
/* 382 */               this.motionY -= 0.20000000298023224D;
/* 383 */               playSound("random.splash", 0.25F, 1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.4F);
/* 384 */               float f8 = MathHelper.floor_double((getEntityBoundingBox()).minY);
/* 385 */               worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX, (f8 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
/* 386 */               worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, this.posX, (f8 + 1.0F), this.posZ, (int)(1.0F + this.width * 20.0F), this.width, 0.0D, this.width, 0.20000000298023224D, new int[0]);
/* 387 */               this.ticksCatchable = MathHelper.getRandomIntegerInRange(this.rand, 10, 30);
/*     */             }
/*     */             else {
/*     */               
/* 391 */               this.fishApproachAngle = (float)(this.fishApproachAngle + this.rand.nextGaussian() * 4.0D);
/* 392 */               float f7 = this.fishApproachAngle * 0.017453292F;
/* 393 */               float f10 = MathHelper.sin(f7);
/* 394 */               float f11 = MathHelper.cos(f7);
/* 395 */               double d13 = this.posX + (f10 * this.ticksCatchableDelay * 0.1F);
/* 396 */               double d15 = (MathHelper.floor_double((getEntityBoundingBox()).minY) + 1.0F);
/* 397 */               double d16 = this.posZ + (f11 * this.ticksCatchableDelay * 0.1F);
/* 398 */               Block block1 = worldserver.getBlockState(new BlockPos((int)d13, (int)d15 - 1, (int)d16)).getBlock();
/*     */               
/* 400 */               if (block1 == Blocks.water || block1 == Blocks.flowing_water)
/*     */               {
/* 402 */                 if (this.rand.nextFloat() < 0.15F)
/*     */                 {
/* 404 */                   worldserver.spawnParticle(EnumParticleTypes.WATER_BUBBLE, d13, d15 - 0.10000000149011612D, d16, 1, f10, 0.1D, f11, 0.0D, new int[0]);
/*     */                 }
/*     */                 
/* 407 */                 float f3 = f10 * 0.04F;
/* 408 */                 float f4 = f11 * 0.04F;
/* 409 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, f4, 0.01D, -f3, 1.0D, new int[0]);
/* 410 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_WAKE, d13, d15, d16, 0, -f4, 0.01D, f3, 1.0D, new int[0]);
/*     */               }
/*     */             
/*     */             } 
/* 414 */           } else if (this.ticksCaughtDelay > 0) {
/*     */             
/* 416 */             this.ticksCaughtDelay -= l;
/* 417 */             float f1 = 0.15F;
/*     */             
/* 419 */             if (this.ticksCaughtDelay < 20) {
/*     */               
/* 421 */               f1 = (float)(f1 + (20 - this.ticksCaughtDelay) * 0.05D);
/*     */             }
/* 423 */             else if (this.ticksCaughtDelay < 40) {
/*     */               
/* 425 */               f1 = (float)(f1 + (40 - this.ticksCaughtDelay) * 0.02D);
/*     */             }
/* 427 */             else if (this.ticksCaughtDelay < 60) {
/*     */               
/* 429 */               f1 = (float)(f1 + (60 - this.ticksCaughtDelay) * 0.01D);
/*     */             } 
/*     */             
/* 432 */             if (this.rand.nextFloat() < f1) {
/*     */               
/* 434 */               float f9 = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F) * 0.017453292F;
/* 435 */               float f2 = MathHelper.randomFloatClamp(this.rand, 25.0F, 60.0F);
/* 436 */               double d12 = this.posX + (MathHelper.sin(f9) * f2 * 0.1F);
/* 437 */               double d14 = (MathHelper.floor_double((getEntityBoundingBox()).minY) + 1.0F);
/* 438 */               double d6 = this.posZ + (MathHelper.cos(f9) * f2 * 0.1F);
/* 439 */               Block block = worldserver.getBlockState(new BlockPos((int)d12, (int)d14 - 1, (int)d6)).getBlock();
/*     */               
/* 441 */               if (block == Blocks.water || block == Blocks.flowing_water)
/*     */               {
/* 443 */                 worldserver.spawnParticle(EnumParticleTypes.WATER_SPLASH, d12, d14, d6, 2 + this.rand.nextInt(2), 0.10000000149011612D, 0.0D, 0.10000000149011612D, 0.0D, new int[0]);
/*     */               }
/*     */             } 
/*     */             
/* 447 */             if (this.ticksCaughtDelay <= 0)
/*     */             {
/* 449 */               this.fishApproachAngle = MathHelper.randomFloatClamp(this.rand, 0.0F, 360.0F);
/* 450 */               this.ticksCatchableDelay = MathHelper.getRandomIntegerInRange(this.rand, 20, 80);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 455 */             this.ticksCaughtDelay = MathHelper.getRandomIntegerInRange(this.rand, 100, 900);
/* 456 */             this.ticksCaughtDelay -= EnchantmentHelper.getLureModifier((EntityLivingBase)this.angler) * 20 * 5;
/*     */           } 
/*     */           
/* 459 */           if (this.ticksCatchable > 0)
/*     */           {
/* 461 */             this.motionY -= (this.rand.nextFloat() * this.rand.nextFloat() * this.rand.nextFloat()) * 0.2D;
/*     */           }
/*     */         } 
/*     */         
/* 465 */         double d11 = d10 * 2.0D - 1.0D;
/* 466 */         this.motionY += 0.03999999910593033D * d11;
/*     */         
/* 468 */         if (d10 > 0.0D) {
/*     */           
/* 470 */           f6 = (float)(f6 * 0.9D);
/* 471 */           this.motionY *= 0.8D;
/*     */         } 
/*     */         
/* 474 */         this.motionX *= f6;
/* 475 */         this.motionY *= f6;
/* 476 */         this.motionZ *= f6;
/* 477 */         setPosition(this.posX, this.posY, this.posZ);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 484 */     tagCompound.setShort("xTile", (short)this.xTile);
/* 485 */     tagCompound.setShort("yTile", (short)this.yTile);
/* 486 */     tagCompound.setShort("zTile", (short)this.zTile);
/* 487 */     ResourceLocation resourcelocation = (ResourceLocation)Block.blockRegistry.getNameForObject(this.inTile);
/* 488 */     tagCompound.setString("inTile", (resourcelocation == null) ? "" : resourcelocation.toString());
/* 489 */     tagCompound.setByte("shake", (byte)this.shake);
/* 490 */     tagCompound.setByte("inGround", (byte)(this.inGround ? 1 : 0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 495 */     this.xTile = tagCompund.getShort("xTile");
/* 496 */     this.yTile = tagCompund.getShort("yTile");
/* 497 */     this.zTile = tagCompund.getShort("zTile");
/*     */     
/* 499 */     if (tagCompund.hasKey("inTile", 8)) {
/*     */       
/* 501 */       this.inTile = Block.getBlockFromName(tagCompund.getString("inTile"));
/*     */     }
/*     */     else {
/*     */       
/* 505 */       this.inTile = Block.getBlockById(tagCompund.getByte("inTile") & 0xFF);
/*     */     } 
/*     */     
/* 508 */     this.shake = tagCompund.getByte("shake") & 0xFF;
/* 509 */     this.inGround = (tagCompund.getByte("inGround") == 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public int handleHookRetraction() {
/* 514 */     if (this.worldObj.isRemote)
/*     */     {
/* 516 */       return 0;
/*     */     }
/*     */ 
/*     */     
/* 520 */     int i = 0;
/*     */     
/* 522 */     if (this.caughtEntity != null) {
/*     */       
/* 524 */       double d0 = this.angler.posX - this.posX;
/* 525 */       double d2 = this.angler.posY - this.posY;
/* 526 */       double d4 = this.angler.posZ - this.posZ;
/* 527 */       double d6 = MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d4 * d4);
/* 528 */       double d8 = 0.1D;
/* 529 */       this.caughtEntity.motionX += d0 * d8;
/* 530 */       this.caughtEntity.motionY += d2 * d8 + MathHelper.sqrt_double(d6) * 0.08D;
/* 531 */       this.caughtEntity.motionZ += d4 * d8;
/* 532 */       i = 3;
/*     */     }
/* 534 */     else if (this.ticksCatchable > 0) {
/*     */       
/* 536 */       EntityItem entityitem = new EntityItem(this.worldObj, this.posX, this.posY, this.posZ, getFishingResult());
/* 537 */       double d1 = this.angler.posX - this.posX;
/* 538 */       double d3 = this.angler.posY - this.posY;
/* 539 */       double d5 = this.angler.posZ - this.posZ;
/* 540 */       double d7 = MathHelper.sqrt_double(d1 * d1 + d3 * d3 + d5 * d5);
/* 541 */       double d9 = 0.1D;
/* 542 */       entityitem.motionX = d1 * d9;
/* 543 */       entityitem.motionY = d3 * d9 + MathHelper.sqrt_double(d7) * 0.08D;
/* 544 */       entityitem.motionZ = d5 * d9;
/* 545 */       this.worldObj.spawnEntityInWorld((Entity)entityitem);
/* 546 */       this.angler.worldObj.spawnEntityInWorld((Entity)new EntityXPOrb(this.angler.worldObj, this.angler.posX, this.angler.posY + 0.5D, this.angler.posZ + 0.5D, this.rand.nextInt(6) + 1));
/* 547 */       i = 1;
/*     */     } 
/*     */     
/* 550 */     if (this.inGround)
/*     */     {
/* 552 */       i = 2;
/*     */     }
/*     */     
/* 555 */     setDead();
/* 556 */     this.angler.fishEntity = null;
/* 557 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private ItemStack getFishingResult() {
/* 563 */     float f = this.worldObj.rand.nextFloat();
/* 564 */     int i = EnchantmentHelper.getLuckOfSeaModifier((EntityLivingBase)this.angler);
/* 565 */     int j = EnchantmentHelper.getLureModifier((EntityLivingBase)this.angler);
/* 566 */     float f1 = 0.1F - i * 0.025F - j * 0.01F;
/* 567 */     float f2 = 0.05F + i * 0.01F - j * 0.01F;
/* 568 */     f1 = MathHelper.clamp_float(f1, 0.0F, 1.0F);
/* 569 */     f2 = MathHelper.clamp_float(f2, 0.0F, 1.0F);
/*     */     
/* 571 */     if (f < f1) {
/*     */       
/* 573 */       this.angler.triggerAchievement(StatList.junkFishedStat);
/* 574 */       return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, JUNK)).getItemStack(this.rand);
/*     */     } 
/*     */ 
/*     */     
/* 578 */     f -= f1;
/*     */     
/* 580 */     if (f < f2) {
/*     */       
/* 582 */       this.angler.triggerAchievement(StatList.treasureFishedStat);
/* 583 */       return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, TREASURE)).getItemStack(this.rand);
/*     */     } 
/*     */ 
/*     */     
/* 587 */     float f3 = f - f2;
/* 588 */     this.angler.triggerAchievement(StatList.fishCaughtStat);
/* 589 */     return ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, FISH)).getItemStack(this.rand);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDead() {
/* 596 */     super.setDead();
/*     */     
/* 598 */     if (this.angler != null)
/*     */     {
/* 600 */       this.angler.fishEntity = null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\projectile\EntityFishHook.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */