/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import java.util.Random;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.EnumCreatureAttribute;
/*     */ import net.minecraft.entity.IEntityLivingData;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILeapAtTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAISwimming;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigate;
/*     */ import net.minecraft.pathfinding.PathNavigateClimber;
/*     */ import net.minecraft.potion.Potion;
/*     */ import net.minecraft.potion.PotionEffect;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.EnumDifficulty;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySpider extends EntityMob {
/*     */   public EntitySpider(World worldIn) {
/*  34 */     super(worldIn);
/*  35 */     setSize(1.4F, 0.9F);
/*  36 */     this.tasks.addTask(1, (EntityAIBase)new EntityAISwimming((EntityLiving)this));
/*  37 */     this.tasks.addTask(3, (EntityAIBase)new EntityAILeapAtTarget((EntityLiving)this, 0.4F));
/*  38 */     this.tasks.addTask(4, (EntityAIBase)new AISpiderAttack(this, (Class)EntityPlayer.class));
/*  39 */     this.tasks.addTask(4, (EntityAIBase)new AISpiderAttack(this, (Class)EntityIronGolem.class));
/*  40 */     this.tasks.addTask(5, (EntityAIBase)new EntityAIWander(this, 0.8D));
/*  41 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 8.0F));
/*  42 */     this.tasks.addTask(6, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  43 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*  44 */     this.targetTasks.addTask(2, (EntityAIBase)new AISpiderTarget<>(this, EntityPlayer.class));
/*  45 */     this.targetTasks.addTask(3, (EntityAIBase)new AISpiderTarget<>(this, EntityIronGolem.class));
/*     */   }
/*     */ 
/*     */   
/*     */   public double getMountedYOffset() {
/*  50 */     return (this.height * 0.5F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected PathNavigate getNewNavigator(World worldIn) {
/*  55 */     return (PathNavigate)new PathNavigateClimber((EntityLiving)this, worldIn);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  60 */     super.entityInit();
/*  61 */     this.dataWatcher.addObject(16, new Byte((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onUpdate() {
/*  66 */     super.onUpdate();
/*     */     
/*  68 */     if (!this.worldObj.isRemote)
/*     */     {
/*  70 */       setBesideClimbableBlock(this.isCollidedHorizontally);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  76 */     super.applyEntityAttributes();
/*  77 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
/*  78 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getLivingSound() {
/*  83 */     return "mob.spider.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/*  88 */     return "mob.spider.say";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/*  93 */     return "mob.spider.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/*  98 */     playSound("mob.spider.step", 0.15F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/* 103 */     return Items.string;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 108 */     super.dropFewItems(wasRecentlyHit, lootingModifier);
/*     */     
/* 110 */     if (wasRecentlyHit && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + lootingModifier) > 0))
/*     */     {
/* 112 */       dropItem(Items.spider_eye, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnLadder() {
/* 118 */     return isBesideClimbableBlock();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInWeb() {}
/*     */ 
/*     */   
/*     */   public EnumCreatureAttribute getCreatureAttribute() {
/* 127 */     return EnumCreatureAttribute.ARTHROPOD;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPotionApplicable(PotionEffect potioneffectIn) {
/* 132 */     return (potioneffectIn.getPotionID() == Potion.poison.id) ? false : super.isPotionApplicable(potioneffectIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBesideClimbableBlock() {
/* 137 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBesideClimbableBlock(boolean p_70839_1_) {
/* 142 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 144 */     if (p_70839_1_) {
/*     */       
/* 146 */       b0 = (byte)(b0 | 0x1);
/*     */     }
/*     */     else {
/*     */       
/* 150 */       b0 = (byte)(b0 & 0xFFFFFFFE);
/*     */     } 
/*     */     
/* 153 */     this.dataWatcher.updateObject(16, Byte.valueOf(b0));
/*     */   }
/*     */ 
/*     */   
/*     */   public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
/* 158 */     livingdata = super.onInitialSpawn(difficulty, livingdata);
/*     */     
/* 160 */     if (this.worldObj.rand.nextInt(100) == 0) {
/*     */       
/* 162 */       EntitySkeleton entityskeleton = new EntitySkeleton(this.worldObj);
/* 163 */       entityskeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
/* 164 */       entityskeleton.onInitialSpawn(difficulty, (IEntityLivingData)null);
/* 165 */       this.worldObj.spawnEntityInWorld((Entity)entityskeleton);
/* 166 */       entityskeleton.mountEntity((Entity)this);
/*     */     } 
/*     */     
/* 169 */     if (livingdata == null) {
/*     */       
/* 171 */       livingdata = new GroupData();
/*     */       
/* 173 */       if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
/*     */       {
/* 175 */         ((GroupData)livingdata).func_111104_a(this.worldObj.rand);
/*     */       }
/*     */     } 
/*     */     
/* 179 */     if (livingdata instanceof GroupData) {
/*     */       
/* 181 */       int i = ((GroupData)livingdata).potionEffectId;
/*     */       
/* 183 */       if (i > 0 && Potion.potionTypes[i] != null)
/*     */       {
/* 185 */         addPotionEffect(new PotionEffect(i, 2147483647));
/*     */       }
/*     */     } 
/*     */     
/* 189 */     return livingdata;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 194 */     return 0.65F;
/*     */   }
/*     */   
/*     */   static class AISpiderAttack
/*     */     extends EntityAIAttackOnCollide
/*     */   {
/*     */     public AISpiderAttack(EntitySpider spider, Class<? extends Entity> targetClass) {
/* 201 */       super(spider, targetClass, 1.0D, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean continueExecuting() {
/* 206 */       float f = this.attacker.getBrightness(1.0F);
/*     */       
/* 208 */       if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0) {
/*     */         
/* 210 */         this.attacker.setAttackTarget((EntityLivingBase)null);
/* 211 */         return false;
/*     */       } 
/*     */ 
/*     */       
/* 215 */       return super.continueExecuting();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     protected double func_179512_a(EntityLivingBase attackTarget) {
/* 221 */       return (4.0F + attackTarget.width);
/*     */     }
/*     */   }
/*     */   
/*     */   static class AISpiderTarget<T extends EntityLivingBase>
/*     */     extends EntityAINearestAttackableTarget
/*     */   {
/*     */     public AISpiderTarget(EntitySpider spider, Class<T> classTarget) {
/* 229 */       super(spider, classTarget, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean shouldExecute() {
/* 234 */       float f = this.taskOwner.getBrightness(1.0F);
/* 235 */       return (f >= 0.5F) ? false : super.shouldExecute();
/*     */     }
/*     */   }
/*     */   
/*     */   public static class GroupData
/*     */     implements IEntityLivingData
/*     */   {
/*     */     public int potionEffectId;
/*     */     
/*     */     public void func_111104_a(Random rand) {
/* 245 */       int i = rand.nextInt(5);
/*     */       
/* 247 */       if (i <= 1) {
/*     */         
/* 249 */         this.potionEffectId = Potion.moveSpeed.id;
/*     */       }
/* 251 */       else if (i <= 2) {
/*     */         
/* 253 */         this.potionEffectId = Potion.damageBoost.id;
/*     */       }
/* 255 */       else if (i <= 3) {
/*     */         
/* 257 */         this.potionEffectId = Potion.regeneration.id;
/*     */       }
/* 259 */       else if (i <= 4) {
/*     */         
/* 261 */         this.potionEffectId = Potion.invisibility.id;
/*     */       } 
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntitySpider.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */