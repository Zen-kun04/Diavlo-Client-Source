/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ 
/*    */ public class EntityAILookIdle
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityLiving idleEntity;
/*    */   private double lookX;
/*    */   private double lookZ;
/*    */   private int idleTime;
/*    */   
/*    */   public EntityAILookIdle(EntityLiving entitylivingIn) {
/* 14 */     this.idleEntity = entitylivingIn;
/* 15 */     setMutexBits(3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 20 */     return (this.idleEntity.getRNG().nextFloat() < 0.02F);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 25 */     return (this.idleTime >= 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 30 */     double d0 = 6.283185307179586D * this.idleEntity.getRNG().nextDouble();
/* 31 */     this.lookX = Math.cos(d0);
/* 32 */     this.lookZ = Math.sin(d0);
/* 33 */     this.idleTime = 20 + this.idleEntity.getRNG().nextInt(20);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 38 */     this.idleTime--;
/* 39 */     this.idleEntity.getLookHelper().setLookPosition(this.idleEntity.posX + this.lookX, this.idleEntity.posY + this.idleEntity.getEyeHeight(), this.idleEntity.posZ + this.lookZ, 10.0F, this.idleEntity.getVerticalFaceSpeed());
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAILookIdle.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */