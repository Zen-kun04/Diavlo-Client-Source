/*     */ package net.minecraft.entity.monster;
/*     */ import java.util.Calendar;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.enchantment.Enchantment;
/*     */ import net.minecraft.enchantment.EnchantmentHelper;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIFleeSun;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIRestrictSun;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.passive.EntityWolf;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntityArrow;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySkeleton extends EntityMob implements IRangedAttackMob {
/*  44 */   private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
/*  45 */   private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);
/*     */ 
/*     */   
/*     */   public EntitySkeleton(World worldIn) {
/*  49 */     super(worldIn);
/*  50 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  51 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIRestrictSun(this));
/*  52 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIFleeSun(this, 1.0D));
/*  53 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
/*  54 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  55 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  56 */     this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  57 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*  58 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
/*  59 */     this.targetTasks.addTask(3, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
/*     */     
/*  61 */     if (worldIn != null && !worldIn.isRemote)
/*     */     {
/*  63 */       setCombatTask();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  69 */     super.applyEntityAttributes();
/*  70 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  75 */     super.entityInit();
/*  76 */     this.dataWatcher.addObject(13, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  81 */     return "mob.skeleton.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  86 */     return "mob.skeleton.hurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  91 */     return "mob.skeleton.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  96 */     playSound("mob.skeleton.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 101 */     if (super.attackEntityAsMob(entityIn)) {
/*     */       
/* 103 */       if (getSkeletonType() == 1 && entityIn instanceof EntityLivingBase)
/*     */       {
/* 105 */         ((EntityLivingBase)entityIn).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
/*     */       }
/*     */       
/* 108 */       return true;
/*     */     } 
/*     */ 
/*     */     
/* 112 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 118 */     return EnumCreatureAttribute.UNDEAD;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 123 */     if (this.worldObj.isDaytime() && !this.worldObj.isRemote) {
/*     */       
/* 125 */       float f = getBrightness(1.0F);
/* 126 */       BlockPos blockpos = new BlockPos(this.posX, Math.round(this.posY), this.posZ);
/*     */       
/* 128 */       if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canSeeSky(blockpos)) {
/*     */         
/* 130 */         boolean flag = true;
/* 131 */         ItemStack itemstack = getEquipmentInSlot(4);
/*     */         
/* 133 */         if (itemstack != null) {
/*     */           
/* 135 */           if (itemstack.isItemStackDamageable()) {
/*     */             
/* 137 */             itemstack.setItemDamage(itemstack.getItemDamage() + this.rand.nextInt(2));
/*     */             
/* 139 */             if (itemstack.getItemDamage() >= itemstack.getMaxDamage()) {
/*     */               
/* 141 */               renderBrokenItemStack(itemstack);
/* 142 */               setCurrentItemOrArmor(4, (ItemStack)null);
/*     */             } 
/*     */           } 
/*     */           
/* 146 */           flag = false;
/*     */         } 
/*     */         
/* 149 */         if (flag)
/*     */         {
/* 151 */           setFire(8);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 156 */     if (this.worldObj.isRemote && getSkeletonType() == 1)
/*     */     {
/* 158 */       setSize(0.72F, 2.535F);
/*     */     }
/*     */     
/* 161 */     super.onLivingUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateRidden() {
/* 166 */     super.updateRidden();
/*     */     
/* 168 */     if (this.ridingEntity instanceof EntityCreature) {
/*     */       
/* 170 */       EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
/* 171 */       this.renderYawOffset = entitycreature.renderYawOffset;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 177 */     super.onDeath(cause);
/*     */     
/* 179 */     if (cause.getSourceOfDamage() instanceof EntityArrow && cause.getEntity() instanceof EntityPlayer) {
/*     */       
/* 181 */       EntityPlayer entityplayer = (EntityPlayer)cause.getEntity();
/* 182 */       double d0 = entityplayer.posX - this.posX;
/* 183 */       double d1 = entityplayer.posZ - this.posZ;
/*     */       
/* 185 */       if (d0 * d0 + d1 * d1 >= 2500.0D)
/*     */       {
/* 187 */         entityplayer.triggerAchievement((StatBase)AchievementList.snipeSkeleton);
/*     */       }
/*     */     }
/* 190 */     else if (cause.getEntity() instanceof EntityCreeper && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled()) {
/*     */       
/* 192 */       ((EntityCreeper)cause.getEntity()).func_175493_co();
/* 193 */       entityDropItem(new ItemStack(Items.skull, 1, (getSkeletonType() == 1) ? 1 : 0), 0.0F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 199 */     return Items.arrow;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 204 */     if (getSkeletonType() == 1) {
/*     */       
/* 206 */       int i = this.rand.nextInt(3 + lootingModifier) - 1;
/*     */       
/* 208 */       for (int j = 0; j < i; j++)
/*     */       {
/* 210 */         dropItem(Items.coal, 1);
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 215 */       int k = this.rand.nextInt(3 + lootingModifier);
/*     */       
/* 217 */       for (int i1 = 0; i1 < k; i1++)
/*     */       {
/* 219 */         dropItem(Items.arrow, 1);
/*     */       }
/*     */     } 
/*     */     
/* 223 */     int l = this.rand.nextInt(3 + lootingModifier);
/*     */     
/* 225 */     for (int j1 = 0; j1 < l; j1++)
/*     */     {
/* 227 */       dropItem(Items.bone, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRandomDrop() {
/* 233 */     if (getSkeletonType() == 1)
/*     */     {
/* 235 */       entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
/* 241 */     super.setEquipmentBasedOnDifficulty(difficulty);
/* 242 */     setCurrentItemOrArmor(0, new ItemStack((Item)Items.bow));
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 247 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 249 */     if (this.worldObj.provider instanceof net.minecraft.world.WorldProviderHell && getRNG().nextInt(5) > 0) {
/*     */       
/* 251 */       this.tasks.addTask(4, (EntityAIBase)this.aiAttackOnCollide);
/* 252 */       setSkeletonType(1);
/* 253 */       setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
/* 254 */       getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
/*     */     }
/*     */     else {
/*     */       
/* 258 */       this.tasks.addTask(4, (EntityAIBase)this.aiArrowAttack);
/* 259 */       setEquipmentBasedOnDifficulty(difficulty);
/* 260 */       setEnchantmentBasedOnDifficulty(difficulty);
/*     */     } 
/*     */     
/* 263 */     setCanPickUpLoot((this.rand.nextFloat() < 0.55F * difficulty.getClampedAdditionalDifficulty()));
/*     */     
/* 265 */     if (getEquipmentInSlot(4) == null) {
/*     */       
/* 267 */       Calendar calendar = this.worldObj.getCurrentDate();
/*     */       
/* 269 */       if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F) {
/*     */         
/* 271 */         setCurrentItemOrArmor(4, new ItemStack((this.rand.nextFloat() < 0.1F) ? Blocks.lit_pumpkin : Blocks.pumpkin));
/* 272 */         this.equipmentDropChances[4] = 0.0F;
/*     */       } 
/*     */     } 
/*     */     
/* 276 */     return livingdata;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCombatTask() {
/* 281 */     this.tasks.removeTask((EntityAIBase)this.aiAttackOnCollide);
/* 282 */     this.tasks.removeTask((EntityAIBase)this.aiArrowAttack);
/* 283 */     ItemStack itemstack = getHeldItem();
/*     */     
/* 285 */     if (itemstack != null && itemstack.getItem() == Items.bow) {
/*     */       
/* 287 */       this.tasks.addTask(4, (EntityAIBase)this.aiArrowAttack);
/*     */     }
/*     */     else {
/*     */       
/* 291 */       this.tasks.addTask(4, (EntityAIBase)this.aiAttackOnCollide);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
/* 297 */     EntityArrow entityarrow = new EntityArrow(this.worldObj, (EntityLivingBase)this, target, 1.6F, (14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
/* 298 */     int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, getHeldItem());
/* 299 */     int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, getHeldItem());
/* 300 */     entityarrow.setDamage((p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
/*     */     
/* 302 */     if (i > 0)
/*     */     {
/* 304 */       entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
/*     */     }
/*     */     
/* 307 */     if (j > 0)
/*     */     {
/* 309 */       entityarrow.setKnockbackStrength(j);
/*     */     }
/*     */     
/* 312 */     if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, getHeldItem()) > 0 || getSkeletonType() == 1)
/*     */     {
/* 314 */       entityarrow.setFire(100);
/*     */     }
/*     */     
/* 317 */     playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
/* 318 */     this.worldObj.spawnEntityInWorld((Entity)entityarrow);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSkeletonType() {
/* 323 */     return this.dataWatcher.getWatchableObjectByte(13);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSkeletonType(int p_82201_1_) {
/* 328 */     this.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
/* 329 */     this.isImmuneToFire = (p_82201_1_ == 1);
/*     */     
/* 331 */     if (p_82201_1_ == 1) {
/*     */       
/* 333 */       setSize(0.72F, 2.535F);
/*     */     }
/*     */     else {
/*     */       
/* 337 */       setSize(0.6F, 1.95F);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 343 */     super.readEntityFromNBT(tagCompund);
/*     */     
/* 345 */     if (tagCompund.hasKey("SkeletonType", 99)) {
/*     */       
/* 347 */       int i = tagCompund.getByte("SkeletonType");
/* 348 */       setSkeletonType(i);
/*     */     } 
/*     */     
/* 351 */     setCombatTask();
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 356 */     super.writeEntityToNBT(tagCompound);
/* 357 */     tagCompound.setByte("SkeletonType", (byte)getSkeletonType());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCurrentItemOrArmor(int slotIn, ItemStack stack) {
/* 362 */     super.setCurrentItemOrArmor(slotIn, stack);
/*     */     
/* 364 */     if (!this.worldObj.isRemote && slotIn == 0)
/*     */     {
/* 366 */       setCombatTask();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 372 */     return (getSkeletonType() == 1) ? super.getEyeHeight() : 1.74F;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getYOffset() {
/* 377 */     return isChild() ? 0.0D : -0.35D;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntitySkeleton.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */