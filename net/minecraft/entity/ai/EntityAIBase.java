/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ 
/*    */ public abstract class EntityAIBase
/*    */ {
/*    */   private int mutexBits;
/*    */   
/*    */   public abstract boolean shouldExecute();
/*    */   
/*    */   public boolean continueExecuting() {
/* 11 */     return shouldExecute();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isInterruptible() {
/* 16 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void resetTask() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void updateTask() {}
/*    */ 
/*    */   
/*    */   public void setMutexBits(int mutexBitsIn) {
/* 33 */     this.mutexBits = mutexBitsIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMutexBits() {
/* 38 */     return this.mutexBits;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIBase.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */