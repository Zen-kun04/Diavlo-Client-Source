/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.AxisAlignedBB;
/*    */ 
/*    */ public class EntityAIHurtByTarget
/*    */   extends EntityAITarget
/*    */ {
/*    */   private boolean entityCallsForHelp;
/*    */   private int revengeTimerOld;
/*    */   private final Class[] targetClasses;
/*    */   
/*    */   public EntityAIHurtByTarget(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class... targetClassesIn) {
/* 15 */     super(creatureIn, false);
/* 16 */     this.entityCallsForHelp = entityCallsForHelpIn;
/* 17 */     this.targetClasses = targetClassesIn;
/* 18 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 23 */     int i = this.taskOwner.getRevengeTimer();
/* 24 */     return (i != this.revengeTimerOld && isSuitableTarget(this.taskOwner.getAITarget(), false));
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 29 */     this.taskOwner.setAttackTarget(this.taskOwner.getAITarget());
/* 30 */     this.revengeTimerOld = this.taskOwner.getRevengeTimer();
/*    */     
/* 32 */     if (this.entityCallsForHelp) {
/*    */       
/* 34 */       double d0 = getTargetDistance();
/*    */       
/* 36 */       for (EntityCreature entitycreature : this.taskOwner.worldObj.getEntitiesWithinAABB(this.taskOwner.getClass(), (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0))) {
/*    */         
/* 38 */         if (this.taskOwner != entitycreature && entitycreature.getAttackTarget() == null && !entitycreature.isOnSameTeam(this.taskOwner.getAITarget())) {
/*    */           
/* 40 */           boolean flag = false;
/*    */           
/* 42 */           for (Class<?> oclass : this.targetClasses) {
/*    */             
/* 44 */             if (entitycreature.getClass() == oclass) {
/*    */               
/* 46 */               flag = true;
/*    */               
/*    */               break;
/*    */             } 
/*    */           } 
/* 51 */           if (!flag)
/*    */           {
/* 53 */             setEntityAttackTarget(entitycreature, this.taskOwner.getAITarget());
/*    */           }
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 59 */     super.startExecuting();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setEntityAttackTarget(EntityCreature creatureIn, EntityLivingBase entityLivingBaseIn) {
/* 64 */     creatureIn.setAttackTarget(entityLivingBaseIn);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIHurtByTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */