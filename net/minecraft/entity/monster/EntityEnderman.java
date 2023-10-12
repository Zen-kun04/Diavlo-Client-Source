/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.collect.Sets;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.player.EntityPlayerMP;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityEnderman
/*     */   extends EntityMob {
/*  44 */   private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
/*  45 */   private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
/*  46 */   private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
/*     */   
/*     */   private boolean isAggressive;
/*     */   
/*     */   public EntityEnderman(World worldIn) {
/*  51 */     super(worldIn);
/*  52 */     setSize(0.6F, 2.9F);
/*  53 */     this.stepHeight = 1.0F;
/*  54 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  55 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIAttackOnCollide(this, 1.0D, false));
/*  56 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  57 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  58 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  59 */     this.tasks.addTask(10, new AIPlaceBlock(this));
/*  60 */     this.tasks.addTask(11, new AITakeBlock(this));
/*  61 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*  62 */     this.targetTasks.addTask(2, (EntityAIBase)new AIFindPlayer(this));
/*  63 */     this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<EntityEndermite>()
/*     */           {
/*     */             public boolean apply(EntityEndermite p_apply_1_)
/*     */             {
/*  67 */               return p_apply_1_.isSpawnedByPlayer();
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  74 */     super.applyEntityAttributes();
/*  75 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
/*  76 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*  77 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
/*  78 */     getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  83 */     super.entityInit();
/*  84 */     this.dataWatcher.addObject(16, new Short((short)0));
/*  85 */     this.dataWatcher.addObject(17, new Byte((byte)0));
/*  86 */     this.dataWatcher.addObject(18, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  91 */     super.writeEntityToNBT(tagCompound);
/*  92 */     IBlockState iblockstate = getHeldBlockState();
/*  93 */     tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
/*  94 */     tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
/*     */   }
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*     */     IBlockState iblockstate;
/*  99 */     super.readEntityFromNBT(tagCompund);
/*     */ 
/*     */     
/* 102 */     if (tagCompund.hasKey("carried", 8)) {
/*     */       
/* 104 */       iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
/*     */     }
/*     */     else {
/*     */       
/* 108 */       iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 0xFFFF);
/*     */     } 
/*     */     
/* 111 */     setHeldBlockState(iblockstate);
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean shouldAttackPlayer(EntityPlayer player) {
/* 116 */     ItemStack itemstack = player.inventory.armorInventory[3];
/*     */     
/* 118 */     if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
/*     */     {
/* 120 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 124 */     Vec3 vec3 = player.getLook(1.0F).normalize();
/* 125 */     Vec3 vec31 = new Vec3(this.posX - player.posX, (getEntityBoundingBox()).minY + (this.height / 2.0F) - player.posY + player.getEyeHeight(), this.posZ - player.posZ);
/* 126 */     double d0 = vec31.lengthVector();
/* 127 */     vec31 = vec31.normalize();
/* 128 */     double d1 = vec3.dotProduct(vec31);
/* 129 */     return (d1 > 1.0D - 0.025D / d0) ? player.canEntityBeSeen((Entity)this) : false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 135 */     return 2.55F;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 140 */     if (this.worldObj.isRemote)
/*     */     {
/* 142 */       for (int i = 0; i < 2; i++)
/*     */       {
/* 144 */         this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
/*     */       }
/*     */     }
/*     */     
/* 148 */     this.isJumping = false;
/* 149 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/* 154 */     if (isWet())
/*     */     {
/* 156 */       attackEntityFrom(DamageSource.drown, 1.0F);
/*     */     }
/*     */     
/* 159 */     if (isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0)
/*     */     {
/* 161 */       setScreaming(false);
/*     */     }
/*     */     
/* 164 */     if (this.worldObj.isDaytime()) {
/*     */       
/* 166 */       float f = getBrightness(1.0F);
/*     */       
/* 168 */       if (f > 0.5F && this.worldObj.canSeeSky(new BlockPos((Entity)this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F) {
/*     */         
/* 170 */         setAttackTarget((EntityLivingBase)null);
/* 171 */         setScreaming(false);
/* 172 */         this.isAggressive = false;
/* 173 */         teleportRandomly();
/*     */       } 
/*     */     } 
/*     */     
/* 177 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean teleportRandomly() {
/* 182 */     double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
/* 183 */     double d1 = this.posY + (this.rand.nextInt(64) - 32);
/* 184 */     double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
/* 185 */     return teleportTo(d0, d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean teleportToEntity(Entity p_70816_1_) {
/* 190 */     Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, (getEntityBoundingBox()).minY + (this.height / 2.0F) - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
/* 191 */     vec3 = vec3.normalize();
/* 192 */     double d0 = 16.0D;
/* 193 */     double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
/* 194 */     double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
/* 195 */     double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
/* 196 */     return teleportTo(d1, d2, d3);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean teleportTo(double x, double y, double z) {
/* 201 */     double d0 = this.posX;
/* 202 */     double d1 = this.posY;
/* 203 */     double d2 = this.posZ;
/* 204 */     this.posX = x;
/* 205 */     this.posY = y;
/* 206 */     this.posZ = z;
/* 207 */     boolean flag = false;
/* 208 */     BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
/*     */     
/* 210 */     if (this.worldObj.isBlockLoaded(blockpos)) {
/*     */       
/* 212 */       boolean flag1 = false;
/*     */       
/* 214 */       while (!flag1 && blockpos.getY() > 0) {
/*     */         
/* 216 */         BlockPos blockpos1 = blockpos.down();
/* 217 */         Block block = this.worldObj.getBlockState(blockpos1).getBlock();
/*     */         
/* 219 */         if (block.getMaterial().blocksMovement()) {
/*     */           
/* 221 */           flag1 = true;
/*     */           
/*     */           continue;
/*     */         } 
/* 225 */         this.posY--;
/* 226 */         blockpos = blockpos1;
/*     */       } 
/*     */ 
/*     */       
/* 230 */       if (flag1) {
/*     */         
/* 232 */         setPositionAndUpdate(this.posX, this.posY, this.posZ);
/*     */         
/* 234 */         if (this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox()))
/*     */         {
/* 236 */           flag = true;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 241 */     if (!flag) {
/*     */       
/* 243 */       setPosition(d0, d1, d2);
/* 244 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 248 */     int i = 128;
/*     */     
/* 250 */     for (int j = 0; j < i; j++) {
/*     */       
/* 252 */       double d6 = j / (i - 1.0D);
/* 253 */       float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 254 */       float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 255 */       float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
/* 256 */       double d3 = d0 + (this.posX - d0) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
/* 257 */       double d4 = d1 + (this.posY - d1) * d6 + this.rand.nextDouble() * this.height;
/* 258 */       double d5 = d2 + (this.posZ - d2) * d6 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
/* 259 */       this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, f, f1, f2, new int[0]);
/*     */     } 
/*     */     
/* 262 */     this.worldObj.playSoundEffect(d0, d1, d2, "mob.endermen.portal", 1.0F, 1.0F);
/* 263 */     playSound("mob.endermen.portal", 1.0F, 1.0F);
/* 264 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 270 */     return isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 275 */     return "mob.endermen.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 280 */     return "mob.endermen.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 285 */     return Items.ender_pearl;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 290 */     Item item = getDropItem();
/*     */     
/* 292 */     if (item != null) {
/*     */       
/* 294 */       int i = this.rand.nextInt(2 + lootingModifier);
/*     */       
/* 296 */       for (int j = 0; j < i; j++)
/*     */       {
/* 298 */         dropItem(item, 1);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHeldBlockState(IBlockState state) {
/* 305 */     this.dataWatcher.updateObject(16, Short.valueOf((short)(Block.getStateId(state) & 0xFFFF)));
/*     */   }
/*     */ 
/*     */   
/*     */   public IBlockState getHeldBlockState() {
/* 310 */     return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 315 */     if (isEntityInvulnerable(source))
/*     */     {
/* 317 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 321 */     if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite)) {
/*     */       
/* 323 */       if (!this.worldObj.isRemote)
/*     */       {
/* 325 */         setScreaming(true);
/*     */       }
/*     */       
/* 328 */       if (source instanceof net.minecraft.util.EntityDamageSource && source.getEntity() instanceof EntityPlayer)
/*     */       {
/* 330 */         if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative()) {
/*     */           
/* 332 */           setScreaming(false);
/*     */         }
/*     */         else {
/*     */           
/* 336 */           this.isAggressive = true;
/*     */         } 
/*     */       }
/*     */       
/* 340 */       if (source instanceof net.minecraft.util.EntityDamageSourceIndirect) {
/*     */         
/* 342 */         this.isAggressive = false;
/*     */         
/* 344 */         for (int i = 0; i < 64; i++) {
/*     */           
/* 346 */           if (teleportRandomly())
/*     */           {
/* 348 */             return true;
/*     */           }
/*     */         } 
/*     */         
/* 352 */         return false;
/*     */       } 
/*     */     } 
/*     */     
/* 356 */     boolean flag = super.attackEntityFrom(source, amount);
/*     */     
/* 358 */     if (source.isUnblockable() && this.rand.nextInt(10) != 0)
/*     */     {
/* 360 */       teleportRandomly();
/*     */     }
/*     */     
/* 363 */     return flag;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isScreaming() {
/* 369 */     return (this.dataWatcher.getWatchableObjectByte(18) > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScreaming(boolean screaming) {
/* 374 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)(screaming ? 1 : 0)));
/*     */   }
/*     */ 
/*     */   
/*     */   static {
/* 379 */     carriableBlocks.add(Blocks.grass);
/* 380 */     carriableBlocks.add(Blocks.dirt);
/* 381 */     carriableBlocks.add(Blocks.sand);
/* 382 */     carriableBlocks.add(Blocks.gravel);
/* 383 */     carriableBlocks.add(Blocks.yellow_flower);
/* 384 */     carriableBlocks.add(Blocks.red_flower);
/* 385 */     carriableBlocks.add(Blocks.brown_mushroom);
/* 386 */     carriableBlocks.add(Blocks.red_mushroom);
/* 387 */     carriableBlocks.add(Blocks.tnt);
/* 388 */     carriableBlocks.add(Blocks.cactus);
/* 389 */     carriableBlocks.add(Blocks.clay);
/* 390 */     carriableBlocks.add(Blocks.pumpkin);
/* 391 */     carriableBlocks.add(Blocks.melon_block);
/* 392 */     carriableBlocks.add(Blocks.mycelium);
/*     */   }
/*     */   
/*     */   static class AIFindPlayer
/*     */     extends EntityAINearestAttackableTarget
/*     */   {
/*     */     private EntityPlayer player;
/*     */     private int field_179450_h;
/*     */     private int field_179451_i;
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AIFindPlayer(EntityEnderman p_i45842_1_) {
/* 404 */       super(p_i45842_1_, EntityPlayer.class, true);
/* 405 */       this.enderman = p_i45842_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 410 */       double d0 = getTargetDistance();
/* 411 */       List<EntityPlayer> list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
/* 412 */       Collections.sort(list, (Comparator<? super EntityPlayer>)this.theNearestAttackableTargetSorter);
/*     */       
/* 414 */       if (list.isEmpty())
/*     */       {
/* 416 */         return false;
/*     */       }
/*     */ 
/*     */       
/* 420 */       this.player = list.get(0);
/* 421 */       return true;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 427 */       this.field_179450_h = 5;
/* 428 */       this.field_179451_i = 0;
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetTask() {
/* 433 */       this.player = null;
/* 434 */       this.enderman.setScreaming(false);
/* 435 */       IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 436 */       iattributeinstance.removeModifier(EntityEnderman.attackingSpeedBoostModifier);
/* 437 */       super.resetTask();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 442 */       if (this.player != null) {
/*     */         
/* 444 */         if (!this.enderman.shouldAttackPlayer(this.player))
/*     */         {
/* 446 */           return false;
/*     */         }
/*     */ 
/*     */         
/* 450 */         this.enderman.isAggressive = true;
/* 451 */         this.enderman.faceEntity((Entity)this.player, 10.0F, 10.0F);
/* 452 */         return true;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 457 */       return super.continueExecuting();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 463 */       if (this.player != null) {
/*     */         
/* 465 */         if (--this.field_179450_h <= 0)
/*     */         {
/* 467 */           this.targetEntity = (EntityLivingBase)this.player;
/* 468 */           this.player = null;
/* 469 */           super.startExecuting();
/* 470 */           this.enderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
/* 471 */           this.enderman.setScreaming(true);
/* 472 */           IAttributeInstance iattributeinstance = this.enderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/* 473 */           iattributeinstance.applyModifier(EntityEnderman.attackingSpeedBoostModifier);
/*     */         }
/*     */       
/*     */       } else {
/*     */         
/* 478 */         if (this.targetEntity != null)
/*     */         {
/* 480 */           if (this.targetEntity instanceof EntityPlayer && this.enderman.shouldAttackPlayer((EntityPlayer)this.targetEntity)) {
/*     */             
/* 482 */             if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) < 16.0D)
/*     */             {
/* 484 */               this.enderman.teleportRandomly();
/*     */             }
/*     */             
/* 487 */             this.field_179451_i = 0;
/*     */           }
/* 489 */           else if (this.targetEntity.getDistanceSqToEntity((Entity)this.enderman) > 256.0D && this.field_179451_i++ >= 30 && this.enderman.teleportToEntity((Entity)this.targetEntity)) {
/*     */             
/* 491 */             this.field_179451_i = 0;
/*     */           } 
/*     */         }
/*     */         
/* 495 */         super.updateTask();
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIPlaceBlock
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AIPlaceBlock(EntityEnderman p_i45843_1_) {
/* 506 */       this.enderman = p_i45843_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 511 */       return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : ((this.enderman.getHeldBlockState().getBlock().getMaterial() == Material.air) ? false : ((this.enderman.getRNG().nextInt(2000) == 0)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 516 */       Random random = this.enderman.getRNG();
/* 517 */       World world = this.enderman.worldObj;
/* 518 */       int i = MathHelper.floor_double(this.enderman.posX - 1.0D + random.nextDouble() * 2.0D);
/* 519 */       int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 2.0D);
/* 520 */       int k = MathHelper.floor_double(this.enderman.posZ - 1.0D + random.nextDouble() * 2.0D);
/* 521 */       BlockPos blockpos = new BlockPos(i, j, k);
/* 522 */       Block block = world.getBlockState(blockpos).getBlock();
/* 523 */       Block block1 = world.getBlockState(blockpos.down()).getBlock();
/*     */       
/* 525 */       if (func_179474_a(world, blockpos, this.enderman.getHeldBlockState().getBlock(), block, block1)) {
/*     */         
/* 527 */         world.setBlockState(blockpos, this.enderman.getHeldBlockState(), 3);
/* 528 */         this.enderman.setHeldBlockState(Blocks.air.getDefaultState());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_) {
/* 534 */       return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : ((p_179474_4_.getMaterial() != Material.air) ? false : ((p_179474_5_.getMaterial() == Material.air) ? false : p_179474_5_.isFullCube()));
/*     */     }
/*     */   }
/*     */   
/*     */   static class AITakeBlock
/*     */     extends EntityAIBase
/*     */   {
/*     */     private EntityEnderman enderman;
/*     */     
/*     */     public AITakeBlock(EntityEnderman p_i45841_1_) {
/* 544 */       this.enderman = p_i45841_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 549 */       return !this.enderman.worldObj.getGameRules().getBoolean("mobGriefing") ? false : ((this.enderman.getHeldBlockState().getBlock().getMaterial() != Material.air) ? false : ((this.enderman.getRNG().nextInt(20) == 0)));
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 554 */       Random random = this.enderman.getRNG();
/* 555 */       World world = this.enderman.worldObj;
/* 556 */       int i = MathHelper.floor_double(this.enderman.posX - 2.0D + random.nextDouble() * 4.0D);
/* 557 */       int j = MathHelper.floor_double(this.enderman.posY + random.nextDouble() * 3.0D);
/* 558 */       int k = MathHelper.floor_double(this.enderman.posZ - 2.0D + random.nextDouble() * 4.0D);
/* 559 */       BlockPos blockpos = new BlockPos(i, j, k);
/* 560 */       IBlockState iblockstate = world.getBlockState(blockpos);
/* 561 */       Block block = iblockstate.getBlock();
/*     */       
/* 563 */       if (EntityEnderman.carriableBlocks.contains(block)) {
/*     */         
/* 565 */         this.enderman.setHeldBlockState(iblockstate);
/* 566 */         world.setBlockState(blockpos, Blocks.air.getDefaultState());
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityEnderman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */