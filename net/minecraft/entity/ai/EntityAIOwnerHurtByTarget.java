/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ 
/*    */ public class EntityAIOwnerHurtByTarget
/*    */   extends EntityAITarget {
/*    */   EntityTameable theDefendingTameable;
/*    */   EntityLivingBase theOwnerAttacker;
/*    */   private int field_142051_e;
/*    */   
/*    */   public EntityAIOwnerHurtByTarget(EntityTameable theDefendingTameableIn) {
/* 14 */     super((EntityCreature)theDefendingTameableIn, false);
/* 15 */     this.theDefendingTameable = theDefendingTameableIn;
/* 16 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 21 */     if (!this.theDefendingTameable.isTamed())
/*    */     {
/* 23 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 27 */     EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
/*    */     
/* 29 */     if (entitylivingbase == null)
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 35 */     this.theOwnerAttacker = entitylivingbase.getAITarget();
/* 36 */     int i = entitylivingbase.getRevengeTimer();
/* 37 */     return (i != this.field_142051_e && isSuitableTarget(this.theOwnerAttacker, false) && this.theDefendingTameable.shouldAttackEntity(this.theOwnerAttacker, entitylivingbase));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 44 */     this.taskOwner.setAttackTarget(this.theOwnerAttacker);
/* 45 */     EntityLivingBase entitylivingbase = this.theDefendingTameable.getOwner();
/*    */     
/* 47 */     if (entitylivingbase != null)
/*    */     {
/* 49 */       this.field_142051_e = entitylivingbase.getRevengeTimer();
/*    */     }
/*    */     
/* 52 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIOwnerHurtByTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */