/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityCreeper;
/*    */ 
/*    */ public class EntityAICreeperSwell
/*    */   extends EntityAIBase {
/*    */   EntityCreeper swellingCreeper;
/*    */   EntityLivingBase creeperAttackTarget;
/*    */   
/*    */   public EntityAICreeperSwell(EntityCreeper entitycreeperIn) {
/* 13 */     this.swellingCreeper = entitycreeperIn;
/* 14 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 19 */     EntityLivingBase entitylivingbase = this.swellingCreeper.getAttackTarget();
/* 20 */     return (this.swellingCreeper.getCreeperState() > 0 || (entitylivingbase != null && this.swellingCreeper.getDistanceSqToEntity((Entity)entitylivingbase) < 9.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 25 */     this.swellingCreeper.getNavigator().clearPathEntity();
/* 26 */     this.creeperAttackTarget = this.swellingCreeper.getAttackTarget();
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 31 */     this.creeperAttackTarget = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 36 */     if (this.creeperAttackTarget == null) {
/*    */       
/* 38 */       this.swellingCreeper.setCreeperState(-1);
/*    */     }
/* 40 */     else if (this.swellingCreeper.getDistanceSqToEntity((Entity)this.creeperAttackTarget) > 49.0D) {
/*    */       
/* 42 */       this.swellingCreeper.setCreeperState(-1);
/*    */     }
/* 44 */     else if (!this.swellingCreeper.getEntitySenses().canSee((Entity)this.creeperAttackTarget)) {
/*    */       
/* 46 */       this.swellingCreeper.setCreeperState(-1);
/*    */     }
/*    */     else {
/*    */       
/* 50 */       this.swellingCreeper.setCreeperState(1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAICreeperSwell.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */