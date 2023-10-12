/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.attributes.AttributeModifier;
/*     */ import net.minecraft.entity.ai.attributes.IAttributeInstance;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.ItemStack;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityPigZombie extends EntityZombie {
/*  24 */   private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
/*  25 */   private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, 0)).setSaved(false);
/*     */   
/*     */   private int angerLevel;
/*     */   private int randomSoundDelay;
/*     */   private UUID angerTargetUUID;
/*     */   
/*     */   public EntityPigZombie(World worldIn) {
/*  32 */     super(worldIn);
/*  33 */     this.isImmuneToFire = true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setRevengeTarget(EntityLivingBase livingBase) {
/*  38 */     super.setRevengeTarget(livingBase);
/*     */     
/*  40 */     if (livingBase != null)
/*     */     {
/*  42 */       this.angerTargetUUID = livingBase.getUniqueID();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAI() {
/*  48 */     this.targetTasks.addTask(1, (EntityAIBase)new AIHurtByAggressor(this));
/*  49 */     this.targetTasks.addTask(2, (EntityAIBase)new AITargetAggressor(this));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  54 */     super.applyEntityAttributes();
/*  55 */     getEntityAttribute(reinforcementChance).setBaseValue(0.0D);
/*  56 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
/*  57 */     getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(5.0D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  62 */     super.onUpdate();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/*  67 */     IAttributeInstance iattributeinstance = getEntityAttribute(SharedMonsterAttributes.movementSpeed);
/*     */     
/*  69 */     if (isAngry()) {
/*     */       
/*  71 */       if (!isChild() && !iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER))
/*     */       {
/*  73 */         iattributeinstance.applyModifier(ATTACK_SPEED_BOOST_MODIFIER);
/*     */       }
/*     */       
/*  76 */       this.angerLevel--;
/*     */     }
/*  78 */     else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
/*     */       
/*  80 */       iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
/*     */     } 
/*     */     
/*  83 */     if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0)
/*     */     {
/*  85 */       playSound("mob.zombiepig.zpigangry", getSoundVolume() * 2.0F, ((this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F) * 1.8F);
/*     */     }
/*     */     
/*  88 */     if (this.angerLevel > 0 && this.angerTargetUUID != null && getAITarget() == null) {
/*     */       
/*  90 */       EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
/*  91 */       setRevengeTarget((EntityLivingBase)entityplayer);
/*  92 */       this.attackingPlayer = entityplayer;
/*  93 */       this.recentlyHit = getRevengeTimer();
/*     */     } 
/*     */     
/*  96 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getCanSpawnHere() {
/* 101 */     return (this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNotColliding() {
/* 106 */     return (this.worldObj.checkNoEntityCollision(getEntityBoundingBox(), (Entity)this) && this.worldObj.getCollidingBoundingBoxes((Entity)this, getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(getEntityBoundingBox()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 111 */     super.writeEntityToNBT(tagCompound);
/* 112 */     tagCompound.setShort("Anger", (short)this.angerLevel);
/*     */     
/* 114 */     if (this.angerTargetUUID != null) {
/*     */       
/* 116 */       tagCompound.setString("HurtBy", this.angerTargetUUID.toString());
/*     */     }
/*     */     else {
/*     */       
/* 120 */       tagCompound.setString("HurtBy", "");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 126 */     super.readEntityFromNBT(tagCompund);
/* 127 */     this.angerLevel = tagCompund.getShort("Anger");
/* 128 */     String s = tagCompund.getString("HurtBy");
/*     */     
/* 130 */     if (s.length() > 0) {
/*     */       
/* 132 */       this.angerTargetUUID = UUID.fromString(s);
/* 133 */       EntityPlayer entityplayer = this.worldObj.getPlayerEntityByUUID(this.angerTargetUUID);
/* 134 */       setRevengeTarget((EntityLivingBase)entityplayer);
/*     */       
/* 136 */       if (entityplayer != null) {
/*     */         
/* 138 */         this.attackingPlayer = entityplayer;
/* 139 */         this.recentlyHit = getRevengeTimer();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityFrom(DamageSource source, float amount) {
/* 146 */     if (isEntityInvulnerable(source))
/*     */     {
/* 148 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 152 */     Entity entity = source.getEntity();
/*     */     
/* 154 */     if (entity instanceof EntityPlayer)
/*     */     {
/* 156 */       becomeAngryAt(entity);
/*     */     }
/*     */     
/* 159 */     return super.attackEntityFrom(source, amount);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void becomeAngryAt(Entity p_70835_1_) {
/* 165 */     this.angerLevel = 400 + this.rand.nextInt(400);
/* 166 */     this.randomSoundDelay = this.rand.nextInt(40);
/*     */     
/* 168 */     if (p_70835_1_ instanceof EntityLivingBase)
/*     */     {
/* 170 */       setRevengeTarget((EntityLivingBase)p_70835_1_);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAngry() {
/* 176 */     return (this.angerLevel > 0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/* 181 */     return "mob.zombiepig.zpig";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 186 */     return "mob.zombiepig.zpighurt";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 191 */     return "mob.zombiepig.zpigdeath";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 196 */     int i = this.rand.nextInt(2 + lootingModifier);
/*     */     
/* 198 */     for (int j = 0; j < i; j++)
/*     */     {
/* 200 */       dropItem(Items.rotten_flesh, 1);
/*     */     }
/*     */     
/* 203 */     i = this.rand.nextInt(2 + lootingModifier);
/*     */     
/* 205 */     for (int k = 0; k < i; k++)
/*     */     {
/* 207 */       dropItem(Items.gold_nugget, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean interact(EntityPlayer player) {
/* 213 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addRandomDrop() {
/* 218 */     dropItem(Items.gold_ingot, 1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty) {
/* 223 */     setCurrentItemOrArmor(0, new ItemStack(Items.golden_sword));
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 228 */     super.onInitialSpawn(difficulty, livingdata);
/* 229 */     setVillager(false);
/* 230 */     return livingdata;
/*     */   }
/*     */   
/*     */   static class AIHurtByAggressor
/*     */     extends EntityAIHurtByTarget
/*     */   {
/*     */     public AIHurtByAggressor(EntityPigZombie p_i45828_1_) {
/* 237 */       super(p_i45828_1_, true, new Class[0]);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
/* 242 */       super.setEntityAttackTarget(creatureIn, entityLivingBaseIn);
/*     */       
/* 244 */       if (creatureIn instanceof EntityPigZombie)
/*     */       {
/* 246 */         ((EntityPigZombie)creatureIn).becomeAngryAt((Entity)entityLivingBaseIn);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static class AITargetAggressor
/*     */     extends EntityAINearestAttackableTarget<EntityPlayer>
/*     */   {
/*     */     public AITargetAggressor(EntityPigZombie p_i45829_1_) {
/* 255 */       super(p_i45829_1_, EntityPlayer.class, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 260 */       return (((EntityPigZombie)this.taskOwner).isAngry() && super.shouldExecute());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityPigZombie.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */