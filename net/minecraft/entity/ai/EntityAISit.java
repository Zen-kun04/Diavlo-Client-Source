/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.passive.EntityTameable;
/*    */ 
/*    */ public class EntityAISit
/*    */   extends EntityAIBase {
/*    */   private EntityTameable theEntity;
/*    */   private boolean isSitting;
/*    */   
/*    */   public EntityAISit(EntityTameable entityIn) {
/* 13 */     this.theEntity = entityIn;
/* 14 */     setMutexBits(5);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 19 */     if (!this.theEntity.isTamed())
/*    */     {
/* 21 */       return false;
/*    */     }
/* 23 */     if (this.theEntity.isInWater())
/*    */     {
/* 25 */       return false;
/*    */     }
/* 27 */     if (!this.theEntity.onGround)
/*    */     {
/* 29 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 33 */     EntityLivingBase entitylivingbase = this.theEntity.getOwner();
/* 34 */     return (entitylivingbase == null) ? true : ((this.theEntity.getDistanceSqToEntity((Entity)entitylivingbase) < 144.0D && entitylivingbase.getAITarget() != null) ? false : this.isSitting);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 40 */     this.theEntity.getNavigator().clearPathEntity();
/* 41 */     this.theEntity.setSitting(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 46 */     this.theEntity.setSitting(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setSitting(boolean sitting) {
/* 51 */     this.isSitting = sitting;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAISit.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */