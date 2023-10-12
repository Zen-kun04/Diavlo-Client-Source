/*     */ package net.minecraft.entity.passive;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockCarrot;
/*     */ import net.minecraft.block.properties.IProperty;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIMoveToBlock;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.ai.EntityJumpHelper;
/*     */ import net.minecraft.entity.ai.EntityMoveHelper;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.util.Vec3;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityRabbit extends EntityAnimal {
/*  44 */   private int field_175540_bm = 0; private AIAvoidEntity<EntityWolf> aiAvoidWolves;
/*  45 */   private int field_175535_bn = 0;
/*     */   private boolean field_175536_bo = false;
/*     */   private boolean field_175537_bp = false;
/*  48 */   private int currentMoveTypeDuration = 0;
/*  49 */   private EnumMoveType moveType = EnumMoveType.HOP;
/*  50 */   private int carrotTicks = 0;
/*  51 */   private EntityPlayer field_175543_bt = null;
/*     */ 
/*     */   
/*     */   public EntityRabbit(World worldIn) {
/*  55 */     super(worldIn);
/*  56 */     setSize(0.6F, 0.7F);
/*  57 */     this.jumpHelper = new RabbitJumpHelper(this);
/*  58 */     this.moveHelper = new RabbitMoveHelper(this);
/*  59 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  60 */     this.navigator.setHeightRequirement(2.5F);
/*  61 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  62 */     this.tasks.addTask(1, (EntityAIBase)new AIPanic(this, 1.33D));
/*  63 */     this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0D, Items.carrot, false));
/*  64 */     this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0D, Items.golden_carrot, false));
/*  65 */     this.tasks.addTask(2, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.0D, Item.getItemFromBlock((Block)Blocks.yellow_flower), false));
/*  66 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIMate(this, 0.8D));
/*  67 */     this.tasks.addTask(5, (EntityAIBase)new AIRaidFarm(this));
/*  68 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.6D));
/*  69 */     this.tasks.addTask(11, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 10.0F));
/*  70 */     this.aiAvoidWolves = new AIAvoidEntity<>(this, EntityWolf.class, 16.0F, 1.33D, 1.33D);
/*  71 */     this.tasks.addTask(4, (EntityAIBase)this.aiAvoidWolves);
/*  72 */     setMovementSpeed(0.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getJumpUpwardsMotion() {
/*  77 */     return (this.moveHelper.isUpdating() && this.moveHelper.getY() > this.posY + 0.5D) ? 0.5F : this.moveType.func_180074_b();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMoveType(EnumMoveType type) {
/*  82 */     this.moveType = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public float func_175521_o(float p_175521_1_) {
/*  87 */     return (this.field_175535_bn == 0) ? 0.0F : ((this.field_175540_bm + p_175521_1_) / this.field_175535_bn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMovementSpeed(double newSpeed) {
/*  92 */     getNavigator().setSpeed(newSpeed);
/*  93 */     this.moveHelper.setMoveTo(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ(), newSpeed);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setJumping(boolean jump, EnumMoveType moveTypeIn) {
/*  98 */     setJumping(jump);
/*     */     
/* 100 */     if (!jump) {
/*     */       
/* 102 */       if (this.moveType == EnumMoveType.ATTACK)
/*     */       {
/* 104 */         this.moveType = EnumMoveType.HOP;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 109 */       setMovementSpeed(1.5D * moveTypeIn.getSpeed());
/* 110 */       playSound(getJumpingSound(), getSoundVolume(), ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 0.8F);
/*     */     } 
/*     */     
/* 113 */     this.field_175536_bo = jump;
/*     */   }
/*     */ 
/*     */   
/*     */   public void doMovementAction(EnumMoveType movetype) {
/* 118 */     setJumping(true, movetype);
/* 119 */     this.field_175535_bn = movetype.func_180073_d();
/* 120 */     this.field_175540_bm = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean func_175523_cj() {
/* 125 */     return this.field_175536_bo;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/* 130 */     super.entityInit();
/* 131 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAITasks() {
/* 136 */     if (this.moveHelper.getSpeed() > 0.8D) {
/*     */       
/* 138 */       setMoveType(EnumMoveType.SPRINT);
/*     */     }
/* 140 */     else if (this.moveType != EnumMoveType.ATTACK) {
/*     */       
/* 142 */       setMoveType(EnumMoveType.HOP);
/*     */     } 
/*     */     
/* 145 */     if (this.currentMoveTypeDuration > 0)
/*     */     {
/* 147 */       this.currentMoveTypeDuration--;
/*     */     }
/*     */     
/* 150 */     if (this.carrotTicks > 0) {
/*     */       
/* 152 */       this.carrotTicks -= this.rand.nextInt(3);
/*     */       
/* 154 */       if (this.carrotTicks < 0)
/*     */       {
/* 156 */         this.carrotTicks = 0;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     if (this.onGround) {
/*     */       
/* 162 */       if (!this.field_175537_bp) {
/*     */         
/* 164 */         setJumping(false, EnumMoveType.NONE);
/* 165 */         func_175517_cu();
/*     */       } 
/*     */       
/* 168 */       if (getRabbitType() == 99 && this.currentMoveTypeDuration == 0) {
/*     */         
/* 170 */         EntityLivingBase entitylivingbase = getAttackTarget();
/*     */         
/* 172 */         if (entitylivingbase != null && getDistanceSqToEntity((Entity)entitylivingbase) < 16.0D) {
/*     */           
/* 174 */           calculateRotationYaw(entitylivingbase.posX, entitylivingbase.posZ);
/* 175 */           this.moveHelper.setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, this.moveHelper.getSpeed());
/* 176 */           doMovementAction(EnumMoveType.ATTACK);
/* 177 */           this.field_175537_bp = true;
/*     */         } 
/*     */       } 
/*     */       
/* 181 */       RabbitJumpHelper entityrabbit$rabbitjumphelper = (RabbitJumpHelper)this.jumpHelper;
/*     */       
/* 183 */       if (!entityrabbit$rabbitjumphelper.getIsJumping()) {
/*     */         
/* 185 */         if (this.moveHelper.isUpdating() && this.currentMoveTypeDuration == 0)
/*     */         {
/* 187 */           PathEntity pathentity = this.navigator.getPath();
/* 188 */           Vec3 vec3 = new Vec3(this.moveHelper.getX(), this.moveHelper.getY(), this.moveHelper.getZ());
/*     */           
/* 190 */           if (pathentity != null && pathentity.getCurrentPathIndex() < pathentity.getCurrentPathLength())
/*     */           {
/* 192 */             vec3 = pathentity.getPosition((Entity)this);
/*     */           }
/*     */           
/* 195 */           calculateRotationYaw(vec3.xCoord, vec3.zCoord);
/* 196 */           doMovementAction(this.moveType);
/*     */         }
/*     */       
/* 199 */       } else if (!entityrabbit$rabbitjumphelper.func_180065_d()) {
/*     */         
/* 201 */         func_175518_cr();
/*     */       } 
/*     */     } 
/*     */     
/* 205 */     this.field_175537_bp = this.onGround;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void spawnRunningParticles() {}
/*     */ 
/*     */   
/*     */   private void calculateRotationYaw(double x, double z) {
/* 214 */     this.rotationYaw = (float)(MathHelper.atan2(z - this.posZ, x - this.posX) * 180.0D / Math.PI) - 90.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175518_cr() {
/* 219 */     ((RabbitJumpHelper)this.jumpHelper).func_180066_a(true);
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175520_cs() {
/* 224 */     ((RabbitJumpHelper)this.jumpHelper).func_180066_a(false);
/*     */   }
/*     */ 
/*     */   
/*     */   private void updateMoveTypeDuration() {
/* 229 */     this.currentMoveTypeDuration = getMoveTypeDuration();
/*     */   }
/*     */ 
/*     */   
/*     */   private void func_175517_cu() {
/* 234 */     updateMoveTypeDuration();
/* 235 */     func_175520_cs();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 240 */     super.onLivingUpdate();
/*     */     
/* 242 */     if (this.field_175540_bm != this.field_175535_bn) {
/*     */       
/* 244 */       if (this.field_175540_bm == 0 && !this.worldObj.isRemote)
/*     */       {
/* 246 */         this.worldObj.setEntityState((Entity)this, (byte)1);
/*     */       }
/*     */       
/* 249 */       this.field_175540_bm++;
/*     */     }
/* 251 */     else if (this.field_175535_bn != 0) {
/*     */       
/* 253 */       this.field_175540_bm = 0;
/* 254 */       this.field_175535_bn = 0;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/* 260 */     super.applyEntityAttributes();
/* 261 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/* 262 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 267 */     super.writeEntityToNBT(tagCompound);
/* 268 */     tagCompound.setInteger("RabbitType", getRabbitType());
/* 269 */     tagCompound.setInteger("MoreCarrotTicks", this.carrotTicks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 274 */     super.readEntityFromNBT(tagCompund);
/* 275 */     setRabbitType(tagCompund.getInteger("RabbitType"));
/* 276 */     this.carrotTicks = tagCompund.getInteger("MoreCarrotTicks");
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getJumpingSound() {
/* 281 */     return "mob.rabbit.hop";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 286 */     return "mob.rabbit.idle";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 291 */     return "mob.rabbit.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 296 */     return "mob.rabbit.death";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 301 */     if (getRabbitType() == 99) {
/*     */       
/* 303 */       playSound("mob.attack", 1.0F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
/* 304 */       return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 8.0F);
/*     */     } 
/*     */ 
/*     */     
/* 308 */     return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 3.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTotalArmorValue() {
/* 314 */     return (getRabbitType() == 99) ? 8 : super.getTotalArmorValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 319 */     return isEntityInvulnerable(source) ? false : super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRandomDrop() {
/* 324 */     entityDropItem(new ItemStack(Items.rabbit_foot, 1), 0.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 329 */     int i = this.rand.nextInt(2) + this.rand.nextInt(1 + lootingModifier);
/*     */     
/* 331 */     for (int j = 0; j < i; j++)
/*     */     {
/* 333 */       dropItem(Items.rabbit_hide, 1);
/*     */     }
/*     */     
/* 336 */     i = this.rand.nextInt(2);
/*     */     
/* 338 */     for (int k = 0; k < i; k++) {
/*     */       
/* 340 */       if (isBurning()) {
/*     */         
/* 342 */         dropItem(Items.cooked_rabbit, 1);
/*     */       }
/*     */       else {
/*     */         
/* 346 */         dropItem(Items.rabbit, 1);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isRabbitBreedingItem(Item itemIn) {
/* 353 */     return (itemIn == Items.carrot || itemIn == Items.golden_carrot || itemIn == Item.getItemFromBlock((Block)Blocks.yellow_flower));
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityRabbit createChild(EntityAgeable ageable) {
/* 358 */     EntityRabbit entityrabbit = new EntityRabbit(this.worldObj);
/*     */     
/* 360 */     if (ageable instanceof EntityRabbit)
/*     */     {
/* 362 */       entityrabbit.setRabbitType(this.rand.nextBoolean() ? getRabbitType() : ((EntityRabbit)ageable).getRabbitType());
/*     */     }
/*     */     
/* 365 */     return entityrabbit;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreedingItem(ItemStack stack) {
/* 370 */     return (stack != null && isRabbitBreedingItem(stack.getItem()));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRabbitType() {
/* 375 */     return this.dataWatcher.getWatchableObjectByte(18);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRabbitType(int rabbitTypeId) {
/* 380 */     if (rabbitTypeId == 99) {
/*     */       
/* 382 */       this.tasks.removeTask((EntityAIBase)this.aiAvoidWolves);
/* 383 */       this.tasks.addTask(4, (EntityAIBase)new AIEvilAttack(this));
/* 384 */       this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget((EntityCreature)this, false, new Class[0]));
/* 385 */       this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityPlayer.class, true));
/* 386 */       this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget((EntityCreature)this, EntityWolf.class, true));
/*     */       
/* 388 */       if (!hasCustomName())
/*     */       {
/* 390 */         setCustomNameTag(StatCollector.translateToLocal("entity.KillerBunny.name"));
/*     */       }
/*     */     } 
/*     */     
/* 394 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)rabbitTypeId));
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 399 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/* 400 */     int i = this.rand.nextInt(6);
/* 401 */     boolean flag = false;
/*     */     
/* 403 */     if (livingdata instanceof RabbitTypeData) {
/*     */       
/* 405 */       i = ((RabbitTypeData)livingdata).typeData;
/* 406 */       flag = true;
/*     */     }
/*     */     else {
/*     */       
/* 410 */       livingdata = new RabbitTypeData(i);
/*     */     } 
/*     */     
/* 413 */     setRabbitType(i);
/*     */     
/* 415 */     if (flag)
/*     */     {
/* 417 */       setGrowingAge(-24000);
/*     */     }
/*     */     
/* 420 */     return livingdata;
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isCarrotEaten() {
/* 425 */     return (this.carrotTicks == 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getMoveTypeDuration() {
/* 430 */     return this.moveType.getDuration();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createEatingParticles() {
/* 435 */     this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + (this.rand.nextFloat() * this.width * 2.0F) - this.width, this.posY + 0.5D + (this.rand.nextFloat() * this.height), this.posZ + (this.rand.nextFloat() * this.width * 2.0F) - this.width, 0.0D, 0.0D, 0.0D, new int[] { Block.getStateId(Blocks.carrots.getStateFromMeta(7)) });
/* 436 */     this.carrotTicks = 100;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 441 */     if (id == 1) {
/*     */       
/* 443 */       createRunningParticles();
/* 444 */       this.field_175535_bn = 10;
/* 445 */       this.field_175540_bm = 0;
/*     */     }
/*     */     else {
/*     */       
/* 449 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */   
/*     */   static class AIAvoidEntity<T extends Entity>
/*     */     extends EntityAIAvoidEntity<T>
/*     */   {
/*     */     private EntityRabbit entityInstance;
/*     */     
/*     */     public AIAvoidEntity(EntityRabbit rabbit, Class<T> p_i46403_2_, float p_i46403_3_, double p_i46403_4_, double p_i46403_6_) {
/* 459 */       super((EntityCreature)rabbit, p_i46403_2_, p_i46403_3_, p_i46403_4_, p_i46403_6_);
/* 460 */       this.entityInstance = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 465 */       super.updateTask();
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIEvilAttack
/*     */     extends EntityAIAttackOnCollide
/*     */   {
/*     */     public AIEvilAttack(EntityRabbit rabbit) {
/* 473 */       super((EntityCreature)rabbit, EntityLivingBase.class, 1.4D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     protected double func_179512_a(EntityLivingBase attackTarget) {
/* 478 */       return (4.0F + attackTarget.width);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIPanic
/*     */     extends EntityAIPanic
/*     */   {
/*     */     private EntityRabbit theEntity;
/*     */     
/*     */     public AIPanic(EntityRabbit rabbit, double speedIn) {
/* 488 */       super((EntityCreature)rabbit, speedIn);
/* 489 */       this.theEntity = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 494 */       super.updateTask();
/* 495 */       this.theEntity.setMovementSpeed(this.speed);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AIRaidFarm
/*     */     extends EntityAIMoveToBlock
/*     */   {
/*     */     private final EntityRabbit rabbit;
/*     */     private boolean field_179498_d;
/*     */     private boolean field_179499_e = false;
/*     */     
/*     */     public AIRaidFarm(EntityRabbit rabbitIn) {
/* 507 */       super((EntityCreature)rabbitIn, 0.699999988079071D, 16);
/* 508 */       this.rabbit = rabbitIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 513 */       if (this.runDelay <= 0) {
/*     */         
/* 515 */         if (!this.rabbit.worldObj.getGameRules().getBoolean("mobGriefing"))
/*     */         {
/* 517 */           return false;
/*     */         }
/*     */         
/* 520 */         this.field_179499_e = false;
/* 521 */         this.field_179498_d = this.rabbit.isCarrotEaten();
/*     */       } 
/*     */       
/* 524 */       return super.shouldExecute();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 529 */       return (this.field_179499_e && super.continueExecuting());
/*     */     }
/*     */ 
/*     */     
/*     */     public void startExecuting() {
/* 534 */       super.startExecuting();
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetTask() {
/* 539 */       super.resetTask();
/*     */     }
/*     */ 
/*     */     
/*     */     public void updateTask() {
/* 544 */       super.updateTask();
/* 545 */       this.rabbit.getLookHelper().setLookPosition(this.destinationBlock.getX() + 0.5D, (this.destinationBlock.getY() + 1), this.destinationBlock.getZ() + 0.5D, 10.0F, this.rabbit.getVerticalFaceSpeed());
/*     */       
/* 547 */       if (getIsAboveDestination()) {
/*     */         
/* 549 */         World world = this.rabbit.worldObj;
/* 550 */         BlockPos blockpos = this.destinationBlock.up();
/* 551 */         IBlockState iblockstate = world.getBlockState(blockpos);
/* 552 */         Block block = iblockstate.getBlock();
/*     */         
/* 554 */         if (this.field_179499_e && block instanceof BlockCarrot && ((Integer)iblockstate.getValue((IProperty)BlockCarrot.AGE)).intValue() == 7) {
/*     */           
/* 556 */           world.setBlockState(blockpos, Blocks.air.getDefaultState(), 2);
/* 557 */           world.destroyBlock(blockpos, true);
/* 558 */           this.rabbit.createEatingParticles();
/*     */         } 
/*     */         
/* 561 */         this.field_179499_e = false;
/* 562 */         this.runDelay = 10;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     protected boolean shouldMoveTo(World worldIn, BlockPos pos) {
/* 568 */       Block block = worldIn.getBlockState(pos).getBlock();
/*     */       
/* 570 */       if (block == Blocks.farmland) {
/*     */         
/* 572 */         pos = pos.up();
/* 573 */         IBlockState iblockstate = worldIn.getBlockState(pos);
/* 574 */         block = iblockstate.getBlock();
/*     */         
/* 576 */         if (block instanceof BlockCarrot && ((Integer)iblockstate.getValue((IProperty)BlockCarrot.AGE)).intValue() == 7 && this.field_179498_d && !this.field_179499_e) {
/*     */           
/* 578 */           this.field_179499_e = true;
/* 579 */           return true;
/*     */         } 
/*     */       } 
/*     */       
/* 583 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   enum EnumMoveType
/*     */   {
/* 589 */     NONE(0.0F, 0.0F, 30, 1),
/* 590 */     HOP(0.8F, 0.2F, 20, 10),
/* 591 */     STEP(1.0F, 0.45F, 14, 14),
/* 592 */     SPRINT(1.75F, 0.4F, 1, 8),
/* 593 */     ATTACK(2.0F, 0.7F, 7, 8);
/*     */     
/*     */     private final float speed;
/*     */     
/*     */     private final float field_180077_g;
/*     */     private final int duration;
/*     */     private final int field_180085_i;
/*     */     
/*     */     EnumMoveType(float typeSpeed, float p_i45866_4_, int typeDuration, int p_i45866_6_) {
/* 602 */       this.speed = typeSpeed;
/* 603 */       this.field_180077_g = p_i45866_4_;
/* 604 */       this.duration = typeDuration;
/* 605 */       this.field_180085_i = p_i45866_6_;
/*     */     }
/*     */ 
/*     */     
/*     */     public float getSpeed() {
/* 610 */       return this.speed;
/*     */     }
/*     */ 
/*     */     
/*     */     public float func_180074_b() {
/* 615 */       return this.field_180077_g;
/*     */     }
/*     */ 
/*     */     
/*     */     public int getDuration() {
/* 620 */       return this.duration;
/*     */     }
/*     */ 
/*     */     
/*     */     public int func_180073_d() {
/* 625 */       return this.field_180085_i;
/*     */     }
/*     */   }
/*     */   
/*     */   public class RabbitJumpHelper
/*     */     extends EntityJumpHelper
/*     */   {
/*     */     private EntityRabbit theEntity;
/*     */     private boolean field_180068_d = false;
/*     */     
/*     */     public RabbitJumpHelper(EntityRabbit rabbit) {
/* 636 */       super((EntityLiving)rabbit);
/* 637 */       this.theEntity = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean getIsJumping() {
/* 642 */       return this.isJumping;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean func_180065_d() {
/* 647 */       return this.field_180068_d;
/*     */     }
/*     */ 
/*     */     
/*     */     public void func_180066_a(boolean p_180066_1_) {
/* 652 */       this.field_180068_d = p_180066_1_;
/*     */     }
/*     */ 
/*     */     
/*     */     public void doJump() {
/* 657 */       if (this.isJumping) {
/*     */         
/* 659 */         this.theEntity.doMovementAction(EntityRabbit.EnumMoveType.STEP);
/* 660 */         this.isJumping = false;
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   static class RabbitMoveHelper
/*     */     extends EntityMoveHelper
/*     */   {
/*     */     private EntityRabbit theEntity;
/*     */     
/*     */     public RabbitMoveHelper(EntityRabbit rabbit) {
/* 671 */       super((EntityLiving)rabbit);
/* 672 */       this.theEntity = rabbit;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onUpdateMoveHelper() {
/* 677 */       if (this.theEntity.onGround && !this.theEntity.func_175523_cj())
/*     */       {
/* 679 */         this.theEntity.setMovementSpeed(0.0D);
/*     */       }
/*     */       
/* 682 */       super.onUpdateMoveHelper();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class RabbitTypeData
/*     */     implements IEntityLivingData
/*     */   {
/*     */     public int typeData;
/*     */     
/*     */     public RabbitTypeData(int type) {
/* 692 */       this.typeData = type;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityRabbit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */