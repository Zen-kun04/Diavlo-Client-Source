/*     */ package net.minecraft.entity.monster;
/*     */ 
/*     */ import net.minecraft.block.material.Material;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.entity.SharedMonsterAttributes;
/*     */ import net.minecraft.entity.ai.EntityAIArrowAttack;
/*     */ import net.minecraft.entity.ai.EntityAIBase;
/*     */ import net.minecraft.entity.ai.EntityAILookIdle;
/*     */ import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
/*     */ import net.minecraft.entity.ai.EntityAIWander;
/*     */ import net.minecraft.entity.ai.EntityAIWatchClosest;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.entity.projectile.EntitySnowball;
/*     */ import net.minecraft.init.Blocks;
/*     */ import net.minecraft.init.Items;
/*     */ import net.minecraft.item.Item;
/*     */ import net.minecraft.pathfinding.PathNavigateGround;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.DamageSource;
/*     */ import net.minecraft.util.MathHelper;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntitySnowman extends EntityGolem implements IRangedAttackMob {
/*     */   public EntitySnowman(World worldIn) {
/*  28 */     super(worldIn);
/*  29 */     setSize(0.7F, 1.9F);
/*  30 */     ((PathNavigateGround)getNavigator()).setAvoidsWater(true);
/*  31 */     this.tasks.addTask(1, (EntityAIBase)new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
/*  32 */     this.tasks.addTask(2, (EntityAIBase)new EntityAIWander(this, 1.0D));
/*  33 */     this.tasks.addTask(3, (EntityAIBase)new EntityAIWatchClosest((EntityLiving)this, EntityPlayer.class, 6.0F));
/*  34 */     this.tasks.addTask(4, (EntityAIBase)new EntityAILookIdle((EntityLiving)this));
/*  35 */     this.targetTasks.addTask(1, (EntityAIBase)new EntityAINearestAttackableTarget(this, EntityLiving.class, 10, true, false, IMob.mobSelector));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void applyEntityAttributes() {
/*  40 */     super.applyEntityAttributes();
/*  41 */     getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
/*  42 */     getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLivingUpdate() {
/*  47 */     super.onLivingUpdate();
/*     */     
/*  49 */     if (!this.worldObj.isRemote) {
/*     */       
/*  51 */       int i = MathHelper.floor_double(this.posX);
/*  52 */       int j = MathHelper.floor_double(this.posY);
/*  53 */       int k = MathHelper.floor_double(this.posZ);
/*     */       
/*  55 */       if (isWet())
/*     */       {
/*  57 */         attackEntityFrom(DamageSource.drown, 1.0F);
/*     */       }
/*     */       
/*  60 */       if (this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) > 1.0F)
/*     */       {
/*  62 */         attackEntityFrom(DamageSource.onFire, 1.0F);
/*     */       }
/*     */       
/*  65 */       for (int l = 0; l < 4; l++) {
/*     */         
/*  67 */         i = MathHelper.floor_double(this.posX + ((l % 2 * 2 - 1) * 0.25F));
/*  68 */         j = MathHelper.floor_double(this.posY);
/*  69 */         k = MathHelper.floor_double(this.posZ + ((l / 2 % 2 * 2 - 1) * 0.25F));
/*  70 */         BlockPos blockpos = new BlockPos(i, j, k);
/*     */         
/*  72 */         if (this.worldObj.getBlockState(blockpos).getBlock().getMaterial() == Material.air && this.worldObj.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(blockpos) < 0.8F && Blocks.snow_layer.canPlaceBlockAt(this.worldObj, blockpos))
/*     */         {
/*  74 */           this.worldObj.setBlockState(blockpos, Blocks.snow_layer.getDefaultState());
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected Item getDropItem() {
/*  82 */     return Items.snowball;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
/*  87 */     int i = this.rand.nextInt(16);
/*     */     
/*  89 */     for (int j = 0; j < i; j++)
/*     */     {
/*  91 */       dropItem(Items.snowball, 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_) {
/*  97 */     EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, (EntityLivingBase)this);
/*  98 */     double d0 = target.posY + target.getEyeHeight() - 1.100000023841858D;
/*  99 */     double d1 = target.posX - this.posX;
/* 100 */     double d2 = d0 - entitysnowball.posY;
/* 101 */     double d3 = target.posZ - this.posZ;
/* 102 */     float f = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
/* 103 */     entitysnowball.setThrowableHeading(d1, d2 + f, d3, 1.6F, 12.0F);
/* 104 */     playSound("random.bow", 1.0F, 1.0F / (getRNG().nextFloat() * 0.4F + 0.8F));
/* 105 */     this.worldObj.spawnEntityInWorld((Entity)entitysnowball);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getEyeHeight() {
/* 110 */     return 1.7F;
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\monster\EntitySnowman.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */