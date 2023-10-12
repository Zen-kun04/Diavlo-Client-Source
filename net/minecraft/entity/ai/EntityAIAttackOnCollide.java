/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.pathfinding.PathEntity;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.world.World;
/*     */ 
/*     */ public class EntityAIAttackOnCollide
/*     */   extends EntityAIBase
/*     */ {
/*     */   World worldObj;
/*     */   protected EntityCreature attacker;
/*     */   int attackTick;
/*     */   double speedTowardsTarget;
/*     */   boolean longMemory;
/*     */   PathEntity entityPathEntity;
/*     */   Class<? extends Entity> classTarget;
/*     */   private int delayCounter;
/*     */   private double targetX;
/*     */   private double targetY;
/*     */   private double targetZ;
/*     */   
/*     */   public EntityAIAttackOnCollide(EntityCreature creature, Class<? extends Entity> targetClass, double speedIn, boolean useLongMemory) {
/*  26 */     this(creature, speedIn, useLongMemory);
/*  27 */     this.classTarget = targetClass;
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAIAttackOnCollide(EntityCreature creature, double speedIn, boolean useLongMemory) {
/*  32 */     this.attacker = creature;
/*  33 */     this.worldObj = creature.worldObj;
/*  34 */     this.speedTowardsTarget = speedIn;
/*  35 */     this.longMemory = useLongMemory;
/*  36 */     setMutexBits(3);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  41 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/*     */     
/*  43 */     if (entitylivingbase == null)
/*     */     {
/*  45 */       return false;
/*     */     }
/*  47 */     if (!entitylivingbase.isEntityAlive())
/*     */     {
/*  49 */       return false;
/*     */     }
/*  51 */     if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass()))
/*     */     {
/*  53 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  57 */     this.entityPathEntity = this.attacker.getNavigator().getPathToEntityLiving((Entity)entitylivingbase);
/*  58 */     return (this.entityPathEntity != null);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  64 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/*  65 */     return (entitylivingbase == null) ? false : (!entitylivingbase.isEntityAlive() ? false : (!this.longMemory ? (!this.attacker.getNavigator().noPath()) : this.attacker.isWithinHomeDistanceFromPosition(new BlockPos((Entity)entitylivingbase))));
/*     */   }
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/*  70 */     this.attacker.getNavigator().setPath(this.entityPathEntity, this.speedTowardsTarget);
/*  71 */     this.delayCounter = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  76 */     this.attacker.getNavigator().clearPathEntity();
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  81 */     EntityLivingBase entitylivingbase = this.attacker.getAttackTarget();
/*  82 */     this.attacker.getLookHelper().setLookPositionWithEntity((Entity)entitylivingbase, 30.0F, 30.0F);
/*  83 */     double d0 = this.attacker.getDistanceSq(entitylivingbase.posX, (entitylivingbase.getEntityBoundingBox()).minY, entitylivingbase.posZ);
/*  84 */     double d1 = func_179512_a(entitylivingbase);
/*  85 */     this.delayCounter--;
/*     */     
/*  87 */     if ((this.longMemory || this.attacker.getEntitySenses().canSee((Entity)entitylivingbase)) && this.delayCounter <= 0 && ((this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D) || entitylivingbase.getDistanceSq(this.targetX, this.targetY, this.targetZ) >= 1.0D || this.attacker.getRNG().nextFloat() < 0.05F)) {
/*     */       
/*  89 */       this.targetX = entitylivingbase.posX;
/*  90 */       this.targetY = (entitylivingbase.getEntityBoundingBox()).minY;
/*  91 */       this.targetZ = entitylivingbase.posZ;
/*  92 */       this.delayCounter = 4 + this.attacker.getRNG().nextInt(7);
/*     */       
/*  94 */       if (d0 > 1024.0D) {
/*     */         
/*  96 */         this.delayCounter += 10;
/*     */       }
/*  98 */       else if (d0 > 256.0D) {
/*     */         
/* 100 */         this.delayCounter += 5;
/*     */       } 
/*     */       
/* 103 */       if (!this.attacker.getNavigator().tryMoveToEntityLiving((Entity)entitylivingbase, this.speedTowardsTarget))
/*     */       {
/* 105 */         this.delayCounter += 15;
/*     */       }
/*     */     } 
/*     */     
/* 109 */     this.attackTick = Math.max(this.attackTick - 1, 0);
/*     */     
/* 111 */     if (d0 <= d1 && this.attackTick <= 0) {
/*     */       
/* 113 */       this.attackTick = 20;
/*     */       
/* 115 */       if (this.attacker.getHeldItem() != null)
/*     */       {
/* 117 */         this.attacker.swingItem();
/*     */       }
/*     */       
/* 120 */       this.attacker.attackEntityAsMob((Entity)entitylivingbase);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected double func_179512_a(EntityLivingBase attackTarget) {
/* 126 */     return (this.attacker.width * 2.0F * this.attacker.width * 2.0F + attackTarget.width);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIAttackOnCollide.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */