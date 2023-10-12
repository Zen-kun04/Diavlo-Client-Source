/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ 
/*    */ public class DestroyBlockProgress
/*    */ {
/*    */   private final int miningPlayerEntId;
/*    */   private final BlockPos position;
/*    */   private int partialBlockProgress;
/*    */   private int createdAtCloudUpdateTick;
/*    */   
/*    */   public DestroyBlockProgress(int miningPlayerEntIdIn, BlockPos positionIn) {
/* 14 */     this.miningPlayerEntId = miningPlayerEntIdIn;
/* 15 */     this.position = positionIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getPosition() {
/* 20 */     return this.position;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setPartialBlockDamage(int damage) {
/* 25 */     if (damage > 10)
/*    */     {
/* 27 */       damage = 10;
/*    */     }
/*    */     
/* 30 */     this.partialBlockProgress = damage;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getPartialBlockDamage() {
/* 35 */     return this.partialBlockProgress;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCloudUpdateTick(int createdAtCloudUpdateTickIn) {
/* 40 */     this.createdAtCloudUpdateTick = createdAtCloudUpdateTickIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCreationCloudUpdateTick() {
/* 45 */     return this.createdAtCloudUpdateTick;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\client\renderer\DestroyBlockProgress.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */