/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityLiving;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.IRangedAttackMob;
/*     */ import net.minecraft.util.MathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EntityAIArrowAttack
/*     */   extends EntityAIBase
/*     */ {
/*     */   private final EntityLiving entityHost;
/*     */   private final IRangedAttackMob rangedAttackEntityHost;
/*     */   private EntityLivingBase attackTarget;
/*     */   
/*     */   public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int p_i1649_4_, float p_i1649_5_) {
/*  23 */     this(attacker, movespeed, p_i1649_4_, p_i1649_4_, p_i1649_5_);
/*     */   }
/*     */   private double entityMoveSpeed;
/*     */   private int field_75318_f;
/*     */   private int field_96561_g;
/*  28 */   private int rangedAttackTime = -1;
/*     */   public EntityAIArrowAttack(IRangedAttackMob attacker, double movespeed, int p_i1650_4_, int maxAttackTime, float maxAttackDistanceIn) {
/*  30 */     if (!(attacker instanceof EntityLivingBase))
/*     */     {
/*  32 */       throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
/*     */     }
/*     */ 
/*     */     
/*  36 */     this.rangedAttackEntityHost = attacker;
/*  37 */     this.entityHost = (EntityLiving)attacker;
/*  38 */     this.entityMoveSpeed = movespeed;
/*  39 */     this.field_96561_g = p_i1650_4_;
/*  40 */     this.maxRangedAttackTime = maxAttackTime;
/*  41 */     this.field_96562_i = maxAttackDistanceIn;
/*  42 */     this.maxAttackDistance = maxAttackDistanceIn * maxAttackDistanceIn;
/*  43 */     setMutexBits(3);
/*     */   }
/*     */   private int maxRangedAttackTime; private float field_96562_i;
/*     */   private float maxAttackDistance;
/*     */   
/*     */   public boolean shouldExecute() {
/*  49 */     EntityLivingBase entitylivingbase = this.entityHost.getAttackTarget();
/*     */     
/*  51 */     if (entitylivingbase == null)
/*     */     {
/*  53 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  57 */     this.attackTarget = entitylivingbase;
/*  58 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean continueExecuting() {
/*  64 */     return (shouldExecute() || !this.entityHost.getNavigator().noPath());
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetTask() {
/*  69 */     this.attackTarget = null;
/*  70 */     this.field_75318_f = 0;
/*  71 */     this.rangedAttackTime = -1;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateTask() {
/*  76 */     double d0 = this.entityHost.getDistanceSq(this.attackTarget.posX, (this.attackTarget.getEntityBoundingBox()).minY, this.attackTarget.posZ);
/*  77 */     boolean flag = this.entityHost.getEntitySenses().canSee((Entity)this.attackTarget);
/*     */     
/*  79 */     if (flag) {
/*     */       
/*  81 */       this.field_75318_f++;
/*     */     }
/*     */     else {
/*     */       
/*  85 */       this.field_75318_f = 0;
/*     */     } 
/*     */     
/*  88 */     if (d0 <= this.maxAttackDistance && this.field_75318_f >= 20) {
/*     */       
/*  90 */       this.entityHost.getNavigator().clearPathEntity();
/*     */     }
/*     */     else {
/*     */       
/*  94 */       this.entityHost.getNavigator().tryMoveToEntityLiving((Entity)this.attackTarget, this.entityMoveSpeed);
/*     */     } 
/*     */     
/*  97 */     this.entityHost.getLookHelper().setLookPositionWithEntity((Entity)this.attackTarget, 30.0F, 30.0F);
/*     */     
/*  99 */     if (--this.rangedAttackTime == 0) {
/*     */       
/* 101 */       if (d0 > this.maxAttackDistance || !flag) {
/*     */         return;
/*     */       }
/*     */ 
/*     */       
/* 106 */       float f = MathHelper.sqrt_double(d0) / this.field_96562_i;
/* 107 */       float lvt_5_1_ = MathHelper.clamp_float(f, 0.1F, 1.0F);
/* 108 */       this.rangedAttackEntityHost.attackEntityWithRangedAttack(this.attackTarget, lvt_5_1_);
/* 109 */       this.rangedAttackTime = MathHelper.floor_float(f * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
/*     */     }
/* 111 */     else if (this.rangedAttackTime < 0) {
/*     */       
/* 113 */       float f2 = MathHelper.sqrt_double(d0) / this.field_96562_i;
/* 114 */       this.rangedAttackTime = MathHelper.floor_float(f2 * (this.maxRangedAttackTime - this.field_96561_g) + this.field_96561_g);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIArrowAttack.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */