/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ public class EntityAIOpenDoor
/*    */   extends EntityAIDoorInteract
/*    */ {
/*    */   boolean closeDoor;
/*    */   int closeDoorTemporisation;
/*    */   
/*    */   public EntityAIOpenDoor(EntityLiving entitylivingIn, boolean shouldClose) {
/* 12 */     super(entitylivingIn);
/* 13 */     this.theEntity = entitylivingIn;
/* 14 */     this.closeDoor = shouldClose;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 19 */     return (this.closeDoor && this.closeDoorTemporisation > 0 && super.continueExecuting());
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 24 */     this.closeDoorTemporisation = 20;
/* 25 */     this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 30 */     if (this.closeDoor)
/*    */     {
/* 32 */       this.doorBlock.toggleDoor(this.theEntity.worldObj, this.doorPosition, false);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 38 */     this.closeDoorTemporisation--;
/* 39 */     super.updateTask();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIOpenDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */