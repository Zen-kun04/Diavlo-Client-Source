/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import net.minecraft.block.Block;
/*     */ import net.minecraft.block.BlockFlower;
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.block.state.IBlockState;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIAttackOnCollide;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAIDefendVillage;
/*     */ import net.minecraft.entity.ai.EntityAIHurtByTarget;
/*     */ import net.minecraft.entity.ai.EntityAILookAtVillager;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
/*     */ import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.nbt.NBTTagCompound;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.EnumParticleTypes;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.village.Village;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityIronGolem
/*     */   extends EntityGolem {
/*     */   private int homeCheckTimer;
/*     */   Village villageObj;
/*     */   private int attackTimer;
/*     */   private int holdRoseTick;
/*     */   
/*     */   public EntityIronGolem(World worldIn) {
/*  46 */     super(worldIn);
/*  47 */     setSize(1.4F, 2.9F);
/*  48 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  49 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIAttackOnCollide(this, 1.0D, true));
/*  50 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
/*  51 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIMoveThroughVillage(this, 0.6D, true));
/*  52 */     this.tasks.addTask(4, (EntityAIBase)new EntityAIMoveTowardsRestriction(this, 1.0D));
/*  53 */     this.tasks.addTask(5, (EntityAIBase)new EntityAILookAtVillager(this));
/*  54 */     this.tasks.addTask(6, (EntityAIBase)new EntityAIWander(this, 0.6D));
/*  55 */     this.tasks.addTask(7, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  56 */     this.tasks.addTask(8, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  57 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAIDefendVillage(this));
/*  58 */     this.targetTasks.addTask(2, (EntityAIBase)new EntityAIHurtByTarget(this, false, new Class[0]));
/*  59 */     this.targetTasks.addTask(3, (EntityAIBase)new AINearestAttackableTargetNonCreeper(this, EntityLiving.class, 10, false, true, IMob.VISIBLE_MOB_SELECTOR));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void entityInit() {
/*  64 */     super.entityInit();
/*  65 */     this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateAITasks() {
/*  70 */     if (--this.homeCheckTimer <= 0) {
/*     */       
/*  72 */       this.homeCheckTimer = 70 + this.rand.nextInt(50);
/*  73 */       this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos((Entity)this), 32);
/*     */       
/*  75 */       if (this.villageObj == null) {
/*     */         
/*  77 */         detachHome();
/*     */       }
/*     */       else {
/*     */         
/*  81 */         BlockPos blockpos = this.villageObj.getCenter();
/*  82 */         setHomePosAndDistance(blockpos, (int)(this.villageObj.getVillageRadius() * 0.6F));
/*     */       } 
/*     */     } 
/*     */     
/*  86 */     super.updateAITasks();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  91 */     super.applyEntityAttributes();
/*  92 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(100.0D);
/*  93 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
/*     */   }
/*     */ 
/*     */   
/*     */   protected int decreaseAirSupply(int p_70682_1_) {
/*  98 */     return p_70682_1_;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void collideWithEntity(Entity entityIn) {
/* 103 */     if (entityIn instanceof IMob && !(entityIn instanceof EntityCreeper) && getRNG().nextInt(20) == 0)
/*     */     {
/* 105 */       setAttackTarget((EntityLivingBase)entityIn);
/*     */     }
/*     */     
/* 108 */     super.collideWithEntity(entityIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/* 113 */     super.onLivingUpdate();
/*     */     
/* 115 */     if (this.attackTimer > 0)
/*     */     {
/* 117 */       this.attackTimer--;
/*     */     }
/*     */     
/* 120 */     if (this.holdRoseTick > 0)
/*     */     {
/* 122 */       this.holdRoseTick--;
/*     */     }
/*     */     
/* 125 */     if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0) {
/*     */       
/* 127 */       int i = MathHelper.floor_double(this.posX);
/* 128 */       int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
/* 129 */       int k = MathHelper.floor_double(this.posZ);
/* 130 */       IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
/* 131 */       Block block = iblockstate.getBlock();
/*     */       
/* 133 */       if (block.getMaterial() != Material.air)
/*     */       {
/* 135 */         this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, (getEntityBoundingBox()).minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] { Block.getStateId(iblockstate) });
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canAttackClass(Class<? extends EntityLivingBase> cls) {
/* 142 */     return (isPlayerCreated() && EntityPlayer.class.isAssignableFrom(cls)) ? false : ((cls == EntityCreeper.class) ? false : super.canAttackClass(cls));
/*     */   }
/*     */ 
/*     */   
/*     */   public void writeEntityToNBT(NBTTagCompound tagCompound) {
/* 147 */     super.writeEntityToNBT(tagCompound);
/* 148 */     tagCompound.setBoolean("PlayerCreated", isPlayerCreated());
/*     */   }
/*     */ 
/*     */   
/*     */   public void readEntityFromNBT(NBTTagCompound tagCompund) {
/* 153 */     super.readEntityFromNBT(tagCompund);
/* 154 */     setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean attackEntityAsMob(Entity entityIn) {
/* 159 */     this.attackTimer = 10;
/* 160 */     this.worldObj.setEntityState((Entity)this, (byte)4);
/* 161 */     boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage((EntityLivingBase)this), (7 + this.rand.nextInt(15)));
/*     */     
/* 163 */     if (flag) {
/*     */       
/* 165 */       entityIn.motionY += 0.4000000059604645D;
/* 166 */       applyEnchantments((EntityLivingBase)this, entityIn);
/*     */     } 
/*     */     
/* 169 */     playSound("mob.irongolem.throw", 1.0F, 1.0F);
/* 170 */     return flag;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStatusUpdate(byte id) {
/* 175 */     if (id == 4) {
/*     */       
/* 177 */       this.attackTimer = 10;
/* 178 */       playSound("mob.irongolem.throw", 1.0F, 1.0F);
/*     */     }
/* 180 */     else if (id == 11) {
/*     */       
/* 182 */       this.holdRoseTick = 400;
/*     */     }
/*     */     else {
/*     */       
/* 186 */       super.handleStatusUpdate(id);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Village getVillage() {
/* 192 */     return this.villageObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getAttackTimer() {
/* 197 */     return this.attackTimer;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setHoldingRose(boolean p_70851_1_) {
/* 202 */     this.holdRoseTick = p_70851_1_ ? 400 : 0;
/* 203 */     this.worldObj.setEntityState((Entity)this, (byte)11);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getHurtSound() {
/* 208 */     return "mob.irongolem.hit";
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getDeathSound() {
/* 213 */     return "mob.irongolem.death";
/*     */   }
/*     */ 
/*     */   
/*     */   protected void playStepSound(BlockPos pos, Block blockIn) {
/* 218 */     playSound("mob.irongolem.walk", 1.0F, 1.0F);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/* 223 */     int i = this.rand.nextInt(3);
/*     */     
/* 225 */     for (int j = 0; j < i; j++)
/*     */     {
/* 227 */       dropItemWithOffset(Item.getItemFromBlock((Block)Blocks.red_flower), 1, BlockFlower.EnumFlowerType.POPPY.getMeta());
/*     */     }
/*     */     
/* 230 */     int l = 3 + this.rand.nextInt(3);
/*     */     
/* 232 */     for (int k = 0; k < l; k++)
/*     */     {
/* 234 */       dropItem(Items.iron_ingot, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHoldRoseTick() {
/* 240 */     return this.holdRoseTick;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPlayerCreated() {
/* 245 */     return ((this.dataWatcher.getWatchableObjectByte(16) & 0x1) != 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlayerCreated(boolean p_70849_1_) {
/* 250 */     byte b0 = this.dataWatcher.getWatchableObjectByte(16);
/*     */     
/* 252 */     if (p_70849_1_) {
/*     */       
/* 254 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 0x1)));
/*     */     }
/*     */     else {
/*     */       
/* 258 */       this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & 0xFFFFFFFE)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeath(DamageSource cause) {
/* 264 */     if (!isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null)
/*     */     {
/* 266 */       this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
/*     */     }
/*     */     
/* 269 */     super.onDeath(cause);
/*     */   }
/*     */   
/*     */   static class AINearestAttackableTargetNonCreeper<T extends EntityLivingBase>
/*     */     extends EntityAINearestAttackableTarget<T>
/*     */   {
/*     */     public AINearestAttackableTargetNonCreeper(final EntityCreature creature, Class<T> classTarget, int chance, boolean p_i45858_4_, boolean p_i45858_5_, final Predicate<? super T> p_i45858_6_) {
/* 276 */       super(creature, classTarget, chance, p_i45858_4_, p_i45858_5_, p_i45858_6_);
/* 277 */       this.targetEntitySelector = new Predicate<T>()
/*     */         {
/*     */           public boolean apply(T p_apply_1_)
/*     */           {
/* 281 */             if (p_i45858_6_ != null && !p_i45858_6_.apply(p_apply_1_))
/*     */             {
/* 283 */               return false;
/*     */             }
/* 285 */             if (p_apply_1_ instanceof EntityCreeper)
/*     */             {
/* 287 */               return false;
/*     */             }
/*     */ 
/*     */             
/* 291 */             if (p_apply_1_ instanceof EntityPlayer) {
/*     */               
/* 293 */               double d0 = EntityIronGolem.AINearestAttackableTargetNonCreeper.this.getTargetDistance();
/*     */               
/* 295 */               if (p_apply_1_.isSneaking())
/*     */               {
/* 297 */                 d0 *= 0.800000011920929D;
/*     */               }
/*     */               
/* 300 */               if (p_apply_1_.isInvisible()) {
/*     */                 
/* 302 */                 float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */                 
/* 304 */                 if (f < 0.1F)
/*     */                 {
/* 306 */                   f = 0.1F;
/*     */                 }
/*     */                 
/* 309 */                 d0 *= (0.7F * f);
/*     */               } 
/*     */               
/* 312 */               if (p_apply_1_.getDistanceToEntity((Entity)creature) > d0)
/*     */               {
/* 314 */                 return false;
/*     */               }
/*     */             } 
/*     */             
/* 318 */             return EntityIronGolem.AINearestAttackableTargetNonCreeper.this.isSuitableTarget((EntityLivingBase)p_apply_1_, false);
/*     */           }
/*     */         };
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntityIronGolem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */