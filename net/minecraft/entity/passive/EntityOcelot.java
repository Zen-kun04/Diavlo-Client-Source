/*     */ package net.minecraft.entity.passive;
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAvoidEntity;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIFollowOwner;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIOcelotAttack;
/*     */ import net.minecraft.entity.ai.EntityAIOcelotSit;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITargetNonTamed;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.StatCollector;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityOcelot extends EntityTameable {
/*     */   private EntityAIAvoidEntity<EntityPlayer> avoidEntity;
/*     */   
/*     */   public EntityOcelot(World worldIn) {
/*  41 */     super(worldIn);
/*  42 */     setSize(0.6F, 0.7F);
/*  43 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  44 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  45 */     this.tasks.addTask(2, (EntityAIBase)this.aiSit);
/*  46 */     this.tasks.addTask(3, (EntityAIBase)(this.aiTempt = new EntityAITempt((EntityCreature)this, 0.6D, Items.fish, true)));
/*  47 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowOwner(this, 1.0D, 10.0F, 5.0F));
/*  48 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIOcelotSit(this, 0.8D));
/*  49 */     this.tasks.addTask(7, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.3F));
/*  50 */     this.tasks.addTask(8, (EntityAIBase)new EntityAIOcelotAttack((EntityLiving)this));
/*  51 */     this.tasks.addTask(9, (EntityAIBase)new EntityAIMate(this, 0.8D));
/*  52 */     this.tasks.addTask(10, (EntityAIBase)new EntityAIWander((EntityCreature)this, 0.8D));
/*  53 */     this.tasks.addTask(11, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 10.0F));
/*  54 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
/*     */   }
/*     */   private EntityAITempt aiTempt;
/*     */   
/*     */   protected void entityInit() {
/*  59 */     super.entityInit();
/*  60 */     this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateAITasks() {
/*  65 */     if (getMoveHelper().isUpdating()) {
/*     */       
/*  67 */       double d0 = getMoveHelper().getSpeed();
/*     */       
/*  69 */       if (d0 == 0.6D)
/*     */       {
/*  71 */         setSneaking(true);
/*  72 */         setSprinting(false);
/*     */       }
/*  74 */       else if (d0 == 1.33D)
/*     */       {
/*  76 */         setSneaking(false);
/*  77 */         setSprinting(true);
/*     */       }
/*     */       else
/*     */       {
/*  81 */         setSneaking(false);
/*  82 */         setSprinting(false);
/*     */       }
/*     */     
/*     */     } else {
/*     */       
/*  87 */       setSneaking(false);
/*  88 */       setSprinting(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canDespawn() {
/*  94 */     return (!isTamed() && this.ticksExisted > 2400);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  99 */     super.applyEntityAttributes();
/* 100 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/* 101 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {}
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 110 */     super.writeEntityToNBT(tagCompound);
/* 111 */     tagCompound.setInteger("CatType", getTameSkin());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 116 */     super.readEntityFromNBT(tagCompund);
/* 117 */     setTameSkin(tagCompund.getInteger("CatType"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 122 */     return isTamed() ? (isInLove() ? "mob.cat.purr" : ((this.rand.nextInt(4) == 0) ? "mob.cat.purreow" : "mob.cat.meow")) : "";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 127 */     return "mob.cat.hitt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 132 */     return "mob.cat.hitt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected float getSoundVolume() {
/* 137 */     return 0.4F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 142 */     return Items.leather;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 147 */     return entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), 3.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 152 */     if (isEntityInvulnerable(source))
/*     */     {
/* 154 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 158 */     this.aiSit.setSitting(false);
/* 159 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 169 */     ItemStack itemstack = player.inventory.getCurrentItem();
/*     */     
/* 171 */     if (isTamed()) {
/*     */       
/* 173 */       if (isOwner((EntityLivingBase)player) && !this.worldObj.isRemote && !isBreedingItem(itemstack))
/*     */       {
/* 175 */         this.aiSit.setSitting(!isSitting());
/*     */       }
/*     */     }
/* 178 */     else if (this.aiTempt.isRunning() && itemstack != null && itemstack.getItem() == Items.fish && player.getDistanceSqToEntity((Entity)this) < 9.0D) {
/*     */       
/* 180 */       if (!player.capabilities.isCreativeMode)
/*     */       {
/* 182 */         itemstack.stackSize--;
/*     */       }
/*     */       
/* 185 */       if (itemstack.stackSize <= 0)
/*     */       {
/* 187 */         player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);
/*     */       }
/*     */       
/* 190 */       if (!this.worldObj.isRemote)
/*     */       {
/* 192 */         if (this.rand.nextInt(3) == 0) {
/*     */           
/* 194 */           setTamed(true);
/* 195 */           setTameSkin(1 + this.worldObj.rand.nextInt(3));
/* 196 */           setOwnerId(player.getUniqueID().toString());
/* 197 */           playTameEffect(true);
/* 198 */           this.aiSit.setSitting(true);
/* 199 */           this.worldObj.setEntityState((Entity)this, (byte)7);
/*     */         }
/*     */         else {
/*     */           
/* 203 */           playTameEffect(false);
/* 204 */           this.worldObj.setEntityState((Entity)this, (byte)6);
/*     */         } 
/*     */       }
/*     */       
/* 208 */       return true;
/*     */     } 
/*     */     
/* 211 */     return super.interact(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityOcelot createChild(EntityAgeable ageable) {
/* 216 */     EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
/*     */     
/* 218 */     if (isTamed()) {
/*     */       
/* 220 */       entityocelot.setOwnerId(getOwnerId());
/* 221 */       entityocelot.setTamed(true);
/* 222 */       entityocelot.setTameSkin(getTameSkin());
/*     */     } 
/*     */     
/* 225 */     return entityocelot;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreedingItem(ItemStack stack) {
/* 230 */     return (stack != null && stack.getItem() == Items.fish);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canMateWith(EntityAnimal otherAnimal) {
/* 235 */     if (otherAnimal == this)
/*     */     {
/* 237 */       return false;
/*     */     }
/* 239 */     if (!isTamed())
/*     */     {
/* 241 */       return false;
/*     */     }
/* 243 */     if (!(otherAnimal instanceof EntityOcelot))
/*     */     {
/* 245 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 249 */     EntityOcelot entityocelot = (EntityOcelot)otherAnimal;
/* 250 */     return !entityocelot.isTamed() ? false : ((isInLove() && entityocelot.isInLove()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getTameSkin() {
/* 256 */     return this.dataWatcher.getWatchableObjectByte(18);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTameSkin(int skinId) {
/* 261 */     this.dataWatcher.updateObject(18, Byte.valueOf((byte)skinId));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 266 */     return (this.worldObj.rand.nextInt(3) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotColliding() {
/* 271 */     if (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox())) {
/*     */       
/* 273 */       BlockPos blockpos = new BlockPos(this.posX, (getEntityBoundingBox()).minY, this.posZ);
/*     */       
/* 275 */       if (blockpos.getY() < this.worldObj.getSeaLevel())
/*     */       {
/* 277 */         return false;
/*     */       }
/*     */       
/* 280 */       Block block = this.worldObj.getBlockState(blockpos.down()).getBlock();
/*     */       
/* 282 */       if (block == Blocks.grass || block.getMaterial() == Material.leaves)
/*     */       {
/* 284 */         return true;
/*     */       }
/*     */     } 
/*     */     
/* 288 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 293 */     return hasCustomName() ? getCustomNameTag() : (isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getName());
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTamed(boolean tamed) {
/* 298 */     super.setTamed(tamed);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupTamedAI() {
/* 303 */     if (this.avoidEntity == null)
/*     */     {
/* 305 */       this.avoidEntity = new EntityAIAvoidEntity((EntityCreature)this, EntityPlayer.class, 16.0F, 0.8D, 1.33D);
/*     */     }
/*     */     
/* 308 */     this.tasks.removeTask((EntityAIBase)this.avoidEntity);
/*     */     
/* 310 */     if (!isTamed())
/*     */     {
/* 312 */       this.tasks.addTask(4, (EntityAIBase)this.avoidEntity);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 318 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 320 */     if (this.worldObj.rand.nextInt(7) == 0)
/*     */     {
/* 322 */       for (int i = 0; i < 2; i++) {
/*     */         
/* 324 */         EntityOcelot entityocelot = new EntityOcelot(this.worldObj);
/* 325 */         entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 326 */         entityocelot.setGrowingAge(-24000);
/* 327 */         this.worldObj.spawnEntityInWorld((Entity)entityocelot);
/*     */       } 
/*     */     }
/*     */     
/* 331 */     return livingdata;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityOcelot.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */