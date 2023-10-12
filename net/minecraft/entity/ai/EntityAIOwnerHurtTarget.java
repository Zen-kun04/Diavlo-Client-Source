/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ 
/*    */ public class EntityAIOwnerHurtTarget
/*    */   extends EntityAITarget {
/*    */   EntityTameable theEntityTameable;
/*    */   EntityLivingBase theTarget;
/*    */   private int field_142050_e;
/*    */   
/*    */   public EntityAIOwnerHurtTarget(EntityTameable theEntityTameableIn) {
/* 14 */     super((EntityCreature)theEntityTameableIn, false);
/* 15 */     this.theEntityTameable = theEntityTameableIn;
/* 16 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 21 */     if (!this.theEntityTameable.isTamed())
/*    */     {
/* 23 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 27 */     EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
/*    */     
/* 29 */     if (entitylivingbase == null)
/*    */     {
/* 31 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 35 */     this.theTarget = entitylivingbase.getLastAttacker();
/* 36 */     int i = entitylivingbase.getLastAttackerTime();
/* 37 */     return (i != this.field_142050_e && isSuitableTarget(this.theTarget, false) && this.theEntityTameable.shouldAttackEntity(this.theTarget, entitylivingbase));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 44 */     this.taskOwner.setAttackTarget(this.theTarget);
/* 45 */     EntityLivingBase entitylivingbase = this.theEntityTameable.getOwner();
/*    */     
/* 47 */     if (entitylivingbase != null)
/*    */     {
/* 49 */       this.field_142050_e = entitylivingbase.getLastAttackerTime();
/*    */     }
/*    */     
/* 52 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIOwnerHurtTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */