/*     */ package net.minecraft.entity.boss;
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.List;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.item.EntityItem;
/*     */ import net.minecraft.entity.monster.EntityMob;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityWitherSkull;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityWither extends EntityMob implements IBossDisplayData, IRangedAttackMob {
/*  42 */   private float[] field_82220_d = new float[2];
/*  43 */   private float[] field_82221_e = new float[2];
/*  44 */   private float[] field_82217_f = new float[2];
/*  45 */   private float[] field_82218_g = new float[2];
/*  46 */   private int[] field_82223_h = new int[2];
/*  47 */   private int[] field_82224_i = new int[2]; private int blockBreakCounter;
/*     */   
/*  49 */   private static final Predicate<Entity> attackEntitySelector = new Predicate<Entity>()
/*     */     {
/*     */       public boolean apply(Entity p_apply_1_)
/*     */       {
/*  53 */         return (p_apply_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_apply_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD);
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public EntityWither(World worldIn) {
/*  59 */     super(worldIn);
/*  60 */     setHealth(getMaxHealth());
/*  61 */     setSize(0.9F, 3.5F);
/*  62 */     this.isImmuneToFire = true;
/*  63 */     ((PathNavigateGround)getNavigator()).setCanSwim(true);
/*  64 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  65 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
/*  66 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  67 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  68 */     this.tasks.addTask(7, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  69 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
/*  70 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityLiving.class, 0, false, false, attackEntitySelector));
/*  71 */     this.experienceValue = 50;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  76 */     super.entityInit();
/*  77 */     this.dataWatcher.addObject(17, new Integer(0));
/*  78 */     this.dataWatcher.addObject(18, new Integer(0));
/*  79 */     this.dataWatcher.addObject(19, new Integer(0));
/*  80 */     this.dataWatcher.addObject(20, new Integer(0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  85 */     super.writeEntityToNBT(tagCompound);
/*  86 */     tagCompound.setInteger("Invul", getInvulTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  91 */     super.readEntityFromNBT(tagCompund);
/*  92 */     setInvulTime(tagCompund.getInteger("Invul"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  97 */     return "mob.wither.idle";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 102 */     return "mob.wither.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 107 */     return "mob.wither.death";
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 112 */     this.motionY *= 0.6000000238418579D;
/*     */     
/* 114 */     if (!this.worldObj.isRemote && getWatchedTargetId(0) > 0) {
/*     */       
/* 116 */       Entity entity = this.worldObj.getEntityByID(getWatchedTargetId(0));
/*     */       
/* 118 */       if (entity != null) {
/*     */         
/* 120 */         if (this.posY < entity.posY || (!isArmored() && this.posY < entity.posY + 5.0D)) {
/*     */           
/* 122 */           if (this.motionY < 0.0D)
/*     */           {
/* 124 */             this.motionY = 0.0D;
/*     */           }
/*     */           
/* 127 */           this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
/*     */         } 
/*     */         
/* 130 */         double d0 = entity.posX - this.posX;
/* 131 */         double d1 = entity.posZ - this.posZ;
/* 132 */         double d3 = d0 * d0 + d1 * d1;
/*     */         
/* 134 */         if (d3 > 9.0D) {
/*     */           
/* 136 */           double d5 = MathHelper.sqrt_double(d3);
/* 137 */           this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
/* 138 */           this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 143 */     if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
/*     */     {
/* 145 */       this.rotationYaw = (float)MathHelper.atan2(this.motionZ, this.motionX) * 57.295776F - 90.0F;
/*     */     }
/*     */     
/* 148 */     super.onLivingUpdate();
/*     */     
/* 150 */     for (int i = 0; i < 2; i++) {
/*     */       
/* 152 */       this.field_82218_g[i] = this.field_82221_e[i];
/* 153 */       this.field_82217_f[i] = this.field_82220_d[i];
/*     */     } 
/*     */     
/* 156 */     for (int j = 0; j < 2; j++) {
/*     */       
/* 158 */       int k = getWatchedTargetId(j + 1);
/* 159 */       Entity entity1 = null;
/*     */       
/* 161 */       if (k > 0)
/*     */       {
/* 163 */         entity1 = this.worldObj.getEntityByID(k);
/*     */       }
/*     */       
/* 166 */       if (entity1 != null) {
/*     */         
/* 168 */         double d11 = func_82214_u(j + 1);
/* 169 */         double d12 = func_82208_v(j + 1);
/* 170 */         double d13 = func_82213_w(j + 1);
/* 171 */         double d6 = entity1.posX - d11;
/* 172 */         double d7 = entity1.posY + entity1.getEyeHeight() - d12;
/* 173 */         double d8 = entity1.posZ - d13;
/* 174 */         double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
/* 175 */         float f = (float)(MathHelper.atan2(d8, d6) * 180.0D / Math.PI) - 90.0F;
/* 176 */         float f1 = (float)-(MathHelper.atan2(d7, d9) * 180.0D / Math.PI);
/* 177 */         this.field_82220_d[j] = func_82204_b(this.field_82220_d[j], f1, 40.0F);
/* 178 */         this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], f, 10.0F);
/*     */       }
/*     */       else {
/*     */         
/* 182 */         this.field_82221_e[j] = func_82204_b(this.field_82221_e[j], this.renderYawOffset, 10.0F);
/*     */       } 
/*     */     } 
/*     */     
/* 186 */     boolean flag = isArmored();
/*     */     
/* 188 */     for (int l = 0; l < 3; l++) {
/*     */       
/* 190 */       double d10 = func_82214_u(l);
/* 191 */       double d2 = func_82208_v(l);
/* 192 */       double d4 = func_82213_w(l);
/* 193 */       this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.0D, 0.0D, 0.0D, new int[0]);
/*     */       
/* 195 */       if (flag && this.worldObj.rand.nextInt(4) == 0)
/*     */       {
/* 197 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.30000001192092896D, d2 + this.rand.nextGaussian() * 0.30000001192092896D, d4 + this.rand.nextGaussian() * 0.30000001192092896D, 0.699999988079071D, 0.699999988079071D, 0.5D, new int[0]);
/*     */       }
/*     */     } 
/*     */     
/* 201 */     if (getInvulTime() > 0)
/*     */     {
/* 203 */       for (int i1 = 0; i1 < 3; i1++)
/*     */       {
/* 205 */         this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + (this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian() * 1.0D, 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D, new int[0]);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 212 */     if (getInvulTime() > 0) {
/*     */       
/* 214 */       int j1 = getInvulTime() - 1;
/*     */       
/* 216 */       if (j1 <= 0) {
/*     */         
/* 218 */         this.worldObj.newExplosion((Entity)this, this.posX, this.posY + getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
/* 219 */         this.worldObj.playBroadcastSound(1013, new BlockPos((Entity)this), 0);
/*     */       } 
/*     */       
/* 222 */       setInvulTime(j1);
/*     */       
/* 224 */       if (this.ticksExisted % 10 == 0)
/*     */       {
/* 226 */         heal(10.0F);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 231 */       super.updateAITasks();
/*     */       
/* 233 */       for (int i = 1; i < 3; i++) {
/*     */         
/* 235 */         if (this.ticksExisted >= this.field_82223_h[i - 1]) {
/*     */           
/* 237 */           this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);
/*     */           
/* 239 */           if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD) {
/*     */             
/* 241 */             int j3 = i - 1;
/* 242 */             int k3 = this.field_82224_i[i - 1];
/* 243 */             this.field_82224_i[j3] = this.field_82224_i[i - 1] + 1;
/*     */             
/* 245 */             if (k3 > 15) {
/*     */               
/* 247 */               float f = 10.0F;
/* 248 */               float f1 = 5.0F;
/* 249 */               double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - f, this.posX + f);
/* 250 */               double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - f1, this.posY + f1);
/* 251 */               double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - f, this.posZ + f);
/* 252 */               launchWitherSkullToCoords(i + 1, d0, d1, d2, true);
/* 253 */               this.field_82224_i[i - 1] = 0;
/*     */             } 
/*     */           } 
/*     */           
/* 257 */           int k1 = getWatchedTargetId(i);
/*     */           
/* 259 */           if (k1 > 0) {
/*     */             
/* 261 */             Entity entity = this.worldObj.getEntityByID(k1);
/*     */             
/* 263 */             if (entity != null && entity.isEntityAlive() && getDistanceSqToEntity(entity) <= 900.0D && canEntityBeSeen(entity)) {
/*     */               
/* 265 */               if (entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.disableDamage)
/*     */               {
/* 267 */                 updateWatchedTargetId(i, 0);
/*     */               }
/*     */               else
/*     */               {
/* 271 */                 launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
/* 272 */                 this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
/* 273 */                 this.field_82224_i[i - 1] = 0;
/*     */               }
/*     */             
/*     */             } else {
/*     */               
/* 278 */               updateWatchedTargetId(i, 0);
/*     */             }
/*     */           
/*     */           } else {
/*     */             
/* 283 */             List<EntityLivingBase> list = this.worldObj.getEntitiesWithinAABB(EntityLivingBase.class, getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));
/*     */             
/* 285 */             for (int j2 = 0; j2 < 10 && !list.isEmpty(); j2++) {
/*     */               
/* 287 */               EntityLivingBase entitylivingbase = list.get(this.rand.nextInt(list.size()));
/*     */               
/* 289 */               if (entitylivingbase != this && entitylivingbase.isEntityAlive() && canEntityBeSeen((Entity)entitylivingbase)) {
/*     */                 
/* 291 */                 if (entitylivingbase instanceof EntityPlayer) {
/*     */                   
/* 293 */                   if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
/*     */                   {
/* 295 */                     updateWatchedTargetId(i, entitylivingbase.getEntityId());
/*     */                   }
/*     */                   
/*     */                   break;
/*     */                 } 
/* 300 */                 updateWatchedTargetId(i, entitylivingbase.getEntityId());
/*     */ 
/*     */                 
/*     */                 break;
/*     */               } 
/*     */               
/* 306 */               list.remove(entitylivingbase);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */       
/* 312 */       if (getAttackTarget() != null) {
/*     */         
/* 314 */         updateWatchedTargetId(0, getAttackTarget().getEntityId());
/*     */       }
/*     */       else {
/*     */         
/* 318 */         updateWatchedTargetId(0, 0);
/*     */       } 
/*     */       
/* 321 */       if (this.blockBreakCounter > 0) {
/*     */         
/* 323 */         this.blockBreakCounter--;
/*     */         
/* 325 */         if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing")) {
/*     */           
/* 327 */           int i1 = MathHelper.floor_double(this.posY);
/* 328 */           int l1 = MathHelper.floor_double(this.posX);
/* 329 */           int i2 = MathHelper.floor_double(this.posZ);
/* 330 */           boolean flag = false;
/*     */           
/* 332 */           for (int k2 = -1; k2 <= 1; k2++) {
/*     */             
/* 334 */             for (int l2 = -1; l2 <= 1; l2++) {
/*     */               
/* 336 */               for (int j = 0; j <= 3; j++) {
/*     */                 
/* 338 */                 int i3 = l1 + k2;
/* 339 */                 int k = i1 + j;
/* 340 */                 int l = i2 + l2;
/* 341 */                 BlockPos blockpos = new BlockPos(i3, k, l);
/* 342 */                 Block block = this.worldObj.getBlockState(blockpos).getBlock();
/*     */                 
/* 344 */                 if (block.getMaterial() != Material.air && canDestroyBlock(block))
/*     */                 {
/* 346 */                   flag = (this.worldObj.destroyBlock(blockpos, true) || flag);
/*     */                 }
/*     */               } 
/*     */             } 
/*     */           } 
/*     */           
/* 352 */           if (flag)
/*     */           {
/* 354 */             this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos((Entity)this), 0);
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 359 */       if (this.ticksExisted % 20 == 0)
/*     */       {
/* 361 */         heal(1.0F);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean canDestroyBlock(Block p_181033_0_) {
/* 368 */     return (p_181033_0_ != Blocks.bedrock && p_181033_0_ != Blocks.end_portal && p_181033_0_ != Blocks.end_portal_frame && p_181033_0_ != Blocks.command_block && p_181033_0_ != Blocks.barrier);
/*     */   }
/*     */ 
/*     */   
/*     */   public void func_82206_m() {
/* 373 */     setInvulTime(220);
/* 374 */     setHealth(getMaxHealth() / 3.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInWeb() {}
/*     */ 
/*     */   
/*     */   public int getTotalArmorValue() {
/* 383 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   private double func_82214_u(int p_82214_1_) {
/* 388 */     if (p_82214_1_ <= 0)
/*     */     {
/* 390 */       return this.posX;
/*     */     }
/*     */ 
/*     */     
/* 394 */     float f = (this.renderYawOffset + (180 * (p_82214_1_ - 1))) / 180.0F * 3.1415927F;
/* 395 */     float f1 = MathHelper.cos(f);
/* 396 */     return this.posX + f1 * 1.3D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private double func_82208_v(int p_82208_1_) {
/* 402 */     return (p_82208_1_ <= 0) ? (this.posY + 3.0D) : (this.posY + 2.2D);
/*     */   }
/*     */ 
/*     */   
/*     */   private double func_82213_w(int p_82213_1_) {
/* 407 */     if (p_82213_1_ <= 0)
/*     */     {
/* 409 */       return this.posZ;
/*     */     }
/*     */ 
/*     */     
/* 413 */     float f = (this.renderYawOffset + (180 * (p_82213_1_ - 1))) / 180.0F * 3.1415927F;
/* 414 */     float f1 = MathHelper.sin(f);
/* 415 */     return this.posZ + f1 * 1.3D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_) {
/* 421 */     float f = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);
/*     */     
/* 423 */     if (f > p_82204_3_)
/*     */     {
/* 425 */       f = p_82204_3_;
/*     */     }
/*     */     
/* 428 */     if (f < -p_82204_3_)
/*     */     {
/* 430 */       f = -p_82204_3_;
/*     */     }
/*     */     
/* 433 */     return p_82204_1_ + f;
/*     */   }
/*     */ 
/*     */   
/*     */   private void launchWitherSkullToEntity(int p_82216_1_, EntityLivingBase p_82216_2_) {
/* 438 */     launchWitherSkullToCoords(p_82216_1_, p_82216_2_.posX, p_82216_2_.posY + p_82216_2_.getEyeHeight() * 0.5D, p_82216_2_.posZ, (p_82216_1_ == 0 && this.rand.nextFloat() < 0.001F));
/*     */   }
/*     */ 
/*     */   
/*     */   private void launchWitherSkullToCoords(int p_82209_1_, double x, double y, double z, boolean invulnerable) {
/* 443 */     this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos((Entity)this), 0);
/* 444 */     double d0 = func_82214_u(p_82209_1_);
/* 445 */     double d1 = func_82208_v(p_82209_1_);
/* 446 */     double d2 = func_82213_w(p_82209_1_);
/* 447 */     double d3 = x - d0;
/* 448 */     double d4 = y - d1;
/* 449 */     double d5 = z - d2;
/* 450 */     EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, (EntityLivingBase)this, d3, d4, d5);
/*     */     
/* 452 */     if (invulnerable)
/*     */     {
/* 454 */       entitywitherskull.setInvulnerable(true);
/*     */     }
/*     */     
/* 457 */     entitywitherskull.posY = d1;
/* 458 */     entitywitherskull.posX = d0;
/* 459 */     entitywitherskull.posZ = d2;
/* 460 */     this.worldObj.spawnEntityInWorld((Entity)entitywitherskull);
/*     */   }
/*     */ 
/*     */   
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
/* 465 */     launchWitherSkullToEntity(0, target);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 470 */     if (isEntityInvulnerable(source))
/*     */     {
/* 472 */       return false;
/*     */     }
/* 474 */     if (source != DamageSource.drown && !(source.getEntity() instanceof EntityWither)) {
/*     */       
/* 476 */       if (getInvulTime() > 0 && source != DamageSource.outOfWorld)
/*     */       {
/* 478 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 482 */       if (isArmored()) {
/*     */         
/* 484 */         Entity entity = source.getSourceOfDamage();
/*     */         
/* 486 */         if (entity instanceof net.minecraft.entity.projectile.EntityArrow)
/*     */         {
/* 488 */           return false;
/*     */         }
/*     */       } 
/*     */       
/* 492 */       Entity entity1 = source.getEntity();
/*     */       
/* 494 */       if (entity1 != null && !(entity1 instanceof EntityPlayer) && entity1 instanceof EntityLivingBase && ((EntityLivingBase)entity1).getCreatureAttribute() == getCreatureAttribute())
/*     */       {
/* 496 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 500 */       if (this.blockBreakCounter <= 0)
/*     */       {
/* 502 */         this.blockBreakCounter = 20;
/*     */       }
/*     */       
/* 505 */       for (int i = 0; i < this.field_82224_i.length; i++)
/*     */       {
/* 507 */         this.field_82224_i[i] = this.field_82224_i[i] + 3;
/*     */       }
/*     */       
/* 510 */       return super.attackEntityFrom(source, amount);
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 516 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 522 */     EntityItem entityitem = dropItem(Items.nether_star, 1);
/*     */     
/* 524 */     if (entityitem != null)
/*     */     {
/* 526 */       entityitem.setNoDespawn();
/*     */     }
/*     */     
/* 529 */     if (!this.worldObj.isRemote)
/*     */     {
/* 531 */       for (EntityPlayer entityplayer : this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)))
/*     */       {
/* 533 */         entityplayer.triggerAchievement((StatBase)AchievementList.killWither);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void despawnEntity() {
/* 540 */     this.entityAge = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getBrightnessForRender(float partialTicks) {
/* 545 */     return 15728880;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void addPotionEffect(PotionEffect potioneffectIn) {}
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/* 558 */     super.applyEntityAttributes();
/* 559 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
/* 560 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
/* 561 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_82207_a(int p_82207_1_) {
/* 566 */     return this.field_82221_e[p_82207_1_];
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_82210_r(int p_82210_1_) {
/* 571 */     return this.field_82220_d[p_82210_1_];
/*     */   }
/*     */ 
/*     */   
/*     */   public int getInvulTime() {
/* 576 */     return this.dataWatcher.getWatchableObjectInt(20);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInvulTime(int p_82215_1_) {
/* 581 */     this.dataWatcher.updateObject(20, Integer.valueOf(p_82215_1_));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWatchedTargetId(int p_82203_1_) {
/* 586 */     return this.dataWatcher.getWatchableObjectInt(17 + p_82203_1_);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWatchedTargetId(int targetOffset, int newId) {
/* 591 */     this.dataWatcher.updateObject(17 + targetOffset, Integer.valueOf(newId));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isArmored() {
/* 596 */     return (getHealth() <= getMaxHealth() / 2.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 601 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mountEntity(Entity entityIn) {
/* 606 */     this.ridingEntity = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\boss\EntityWither.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */