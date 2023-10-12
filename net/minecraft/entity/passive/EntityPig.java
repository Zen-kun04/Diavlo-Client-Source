/*     */ package net.minecraft.entity.passive;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityAgeable;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIControlledByPlayer;
/*     */ import net.minecraft.entity.ai.EntityAIFollowParent;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMate;
/*     */ import net.minecraft.entity.ai.EntityAIPanic;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAITempt;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.effect.EntityLightningBolt;
/*     */ import net.minecraft.entity.monster.EntityPigZombie;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.stats.AchievementList;
/*     */ import net.minecraft.stats.StatBase;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPig extends EntityAnimal {
/*     */   public EntityPig(World worldIn) {
/*  33 */     super(worldIn);
/*  34 */     setSize(0.9F, 0.9F);
/*  35 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  36 */     this.tasks.addTask(0, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  37 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIPanic((EntityCreature)this, 1.25D));
/*  38 */     this.tasks.addTask(2, (EntityAIBase)(this.aiControlledByPlayer = new EntityAIControlledByPlayer((EntityLiving)this, 0.3F)));
/*  39 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIMate(this, 1.0D));
/*  40 */     this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, Items.carrot_on_a_stick, false));
/*  41 */     this.tasks.addTask(4, (EntityAIBase)new EntityAITempt((EntityCreature)this, 1.2D, Items.carrot, false));
/*  42 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIFollowParent(this, 1.1D));
/*  43 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWander((EntityCreature)this, 1.0D));
/*  44 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  45 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*     */   }
/*     */   private final EntityAIControlledByPlayer aiControlledByPlayer;
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  50 */     super.applyEntityAttributes();
/*  51 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
/*  52 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeSteered() {
/*  57 */     ItemStack itemstack = ((EntityPlayer)this.riddenByEntity).getHeldItem();
/*  58 */     return (itemstack != null && itemstack.getItem() == Items.carrot_on_a_stick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  63 */     super.entityInit();
/*  64 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/*  69 */     super.writeEntityToNBT(tagCompound);
/*  70 */     tagCompound.setBoolean("Saddle", getSaddled());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/*  75 */     super.readEntityFromNBT(tagCompund);
/*  76 */     setSaddled(tagCompund.getBoolean("Saddle"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  81 */     return "mob.pig.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  86 */     return "mob.pig.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  91 */     return "mob.pig.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  96 */     playSound("mob.pig.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 101 */     if (super.interact(player))
/*     */     {
/* 103 */       return true;
/*     */     }
/* 105 */     if (!getSaddled() || this.worldObj.isRemote || (this.riddenByEntity != null && this.riddenByEntity != player))
/*     */     {
/* 107 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 111 */     player.mountEntity((Entity)this);
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 118 */     return isBurning() ? Items.cooked_porkchop : Items.porkchop;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 123 */     int i = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + lootingModifier);
/*     */     
/* 125 */     for (int j = 0; j < i; j++) {
/*     */       
/* 127 */       if (isBurning()) {
/*     */         
/* 129 */         dropItem(Items.cooked_porkchop, 1);
/*     */       }
/*     */       else {
/*     */         
/* 133 */         dropItem(Items.porkchop, 1);
/*     */       } 
/*     */     } 
/*     */     
/* 137 */     if (getSaddled())
/*     */     {
/* 139 */       dropItem(Items.saddle, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getSaddled() {
/* 145 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSaddled(boolean saddled) {
/* 150 */     if (saddled) {
/*     */       
/* 152 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)1));
/*     */     }
/*     */     else {
/*     */       
/* 156 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)0));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onStruckByLightning(EntityLightningBolt lightningBolt) {
/* 162 */     if (!this.worldObj.isRemote && !this.isDead) {
/*     */       
/* 164 */       EntityPigZombie entitypigzombie = new EntityPigZombie(this.worldObj);
/* 165 */       entitypigzombie.setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
/* 166 */       entitypigzombie.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
/* 167 */       entitypigzombie.setNoAI(isAIDisabled());
/*     */       
/* 169 */       if (hasCustomName()) {
/*     */         
/* 171 */         entitypigzombie.setCustomNameTag(getCustomNameTag());
/* 172 */         entitypigzombie.setAlwaysRenderNameTag(getAlwaysRenderNameTag());
/*     */       } 
/*     */       
/* 175 */       this.worldObj.spawnEntityInWorld((Entity)entitypigzombie);
/* 176 */       setDead();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void fall(float distance, float damageMultiplier) {
/* 182 */     super.fall(distance, damageMultiplier);
/*     */     
/* 184 */     if (distance > 5.0F && this.riddenByEntity instanceof EntityPlayer)
/*     */     {
/* 186 */       ((EntityPlayer)this.riddenByEntity).triggerAchievement((StatBase)AchievementList.flyPig);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityPig createChild(EntityAgeable ageable) {
/* 192 */     return new EntityPig(this.worldObj);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBreedingItem(ItemStack stack) {
/* 197 */     return (stack != null && stack.getItem() == Items.carrot);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAIControlledByPlayer getAIControlledByPlayer() {
/* 202 */     return this.aiControlledByPlayer;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\passive\EntityPig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */