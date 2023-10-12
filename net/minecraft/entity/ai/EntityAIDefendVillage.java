/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.village.Village;
/*    */ 
/*    */ public class EntityAIDefendVillage
/*    */   extends EntityAITarget
/*    */ {
/*    */   EntityIronGolem irongolem;
/*    */   EntityLivingBase villageAgressorTarget;
/*    */   
/*    */   public EntityAIDefendVillage(EntityIronGolem ironGolemIn) {
/* 15 */     super((EntityCreature)ironGolemIn, false, true);
/* 16 */     this.irongolem = ironGolemIn;
/* 17 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 22 */     Village village = this.irongolem.getVillage();
/*    */     
/* 24 */     if (village == null)
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     this.villageAgressorTarget = village.findNearestVillageAggressor((EntityLivingBase)this.irongolem);
/*    */     
/* 32 */     if (this.villageAgressorTarget instanceof net.minecraft.entity.monster.EntityCreeper)
/*    */     {
/* 34 */       return false;
/*    */     }
/* 36 */     if (!isSuitableTarget(this.villageAgressorTarget, false)) {
/*    */       
/* 38 */       if (this.taskOwner.getRNG().nextInt(20) == 0) {
/*    */         
/* 40 */         this.villageAgressorTarget = (EntityLivingBase)village.getNearestTargetPlayer((EntityLivingBase)this.irongolem);
/* 41 */         return isSuitableTarget(this.villageAgressorTarget, false);
/*    */       } 
/*    */ 
/*    */       
/* 45 */       return false;
/*    */     } 
/*    */ 
/*    */ 
/*    */     
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 57 */     this.irongolem.setAttackTarget(this.villageAgressorTarget);
/* 58 */     super.startExecuting();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIDefendVillage.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */