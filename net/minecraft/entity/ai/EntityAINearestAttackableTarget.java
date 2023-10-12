/*     */ package net.minecraft.entity.ai;
/*     */ 
/*     */ import com.google.common.base.Predicate;
/*     */ import com.google.common.base.Predicates;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import net.minecraft.entity.Entity;
/*     */ import net.minecraft.entity.EntityCreature;
/*     */ import net.minecraft.entity.EntityLivingBase;
/*     */ import net.minecraft.entity.player.EntityPlayer;
/*     */ import net.minecraft.util.EntitySelectors;
/*     */ 
/*     */ public class EntityAINearestAttackableTarget<T extends EntityLivingBase>
/*     */   extends EntityAITarget
/*     */ {
/*     */   protected final Class<T> targetClass;
/*     */   private final int targetChance;
/*     */   protected final Sorter theNearestAttackableTargetSorter;
/*     */   protected Predicate<? super T> targetEntitySelector;
/*     */   protected EntityLivingBase targetEntity;
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight) {
/*  24 */     this(creature, classTarget, checkSight, false);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, boolean checkSight, boolean onlyNearby) {
/*  29 */     this(creature, classTarget, 10, checkSight, onlyNearby, (Predicate<? super T>)null);
/*     */   }
/*     */ 
/*     */   
/*     */   public EntityAINearestAttackableTarget(EntityCreature creature, Class<T> classTarget, int chance, boolean checkSight, boolean onlyNearby, final Predicate<? super T> targetSelector) {
/*  34 */     super(creature, checkSight, onlyNearby);
/*  35 */     this.targetClass = classTarget;
/*  36 */     this.targetChance = chance;
/*  37 */     this.theNearestAttackableTargetSorter = new Sorter((Entity)creature);
/*  38 */     setMutexBits(1);
/*  39 */     this.targetEntitySelector = new Predicate<T>()
/*     */       {
/*     */         public boolean apply(T p_apply_1_)
/*     */         {
/*  43 */           if (targetSelector != null && !targetSelector.apply(p_apply_1_))
/*     */           {
/*  45 */             return false;
/*     */           }
/*     */ 
/*     */           
/*  49 */           if (p_apply_1_ instanceof EntityPlayer) {
/*     */             
/*  51 */             double d0 = EntityAINearestAttackableTarget.this.getTargetDistance();
/*     */             
/*  53 */             if (p_apply_1_.isSneaking())
/*     */             {
/*  55 */               d0 *= 0.800000011920929D;
/*     */             }
/*     */             
/*  58 */             if (p_apply_1_.isInvisible()) {
/*     */               
/*  60 */               float f = ((EntityPlayer)p_apply_1_).getArmorVisibility();
/*     */               
/*  62 */               if (f < 0.1F)
/*     */               {
/*  64 */                 f = 0.1F;
/*     */               }
/*     */               
/*  67 */               d0 *= (0.7F * f);
/*     */             } 
/*     */             
/*  70 */             if (p_apply_1_.getDistanceToEntity((Entity)EntityAINearestAttackableTarget.this.taskOwner) > d0)
/*     */             {
/*  72 */               return false;
/*     */             }
/*     */           } 
/*     */           
/*  76 */           return EntityAINearestAttackableTarget.this.isSuitableTarget((EntityLivingBase)p_apply_1_, false);
/*     */         }
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean shouldExecute() {
/*  84 */     if (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)
/*     */     {
/*  86 */       return false;
/*     */     }
/*     */ 
/*     */     
/*  90 */     double d0 = getTargetDistance();
/*  91 */     List<T> list = this.taskOwner.worldObj.getEntitiesWithinAABB(this.targetClass, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), Predicates.and(this.targetEntitySelector, EntitySelectors.NOT_SPECTATING));
/*  92 */     Collections.sort(list, this.theNearestAttackableTargetSorter);
/*     */     
/*  94 */     if (list.isEmpty())
/*     */     {
/*  96 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 100 */     this.targetEntity = (EntityLivingBase)list.get(0);
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void startExecuting() {
/* 108 */     this.taskOwner.setAttackTarget(this.targetEntity);
/* 109 */     super.startExecuting();
/*     */   }
/*     */   
/*     */   public static class Sorter
/*     */     implements Comparator<Entity>
/*     */   {
/*     */     private final Entity theEntity;
/*     */     
/*     */     public Sorter(Entity theEntityIn) {
/* 118 */       this.theEntity = theEntityIn;
/*     */     }
/*     */ 
/*     */     
/*     */     public int compare(Entity p_compare_1_, Entity p_compare_2_) {
/* 123 */       double d0 = this.theEntity.getDistanceSqToEntity(p_compare_1_);
/* 124 */       double d1 = this.theEntity.getDistanceSqToEntity(p_compare_2_);
/* 125 */       return (d0 < d1) ? -1 : ((d0 > d1) ? 1 : 0);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAINearestAttackableTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */