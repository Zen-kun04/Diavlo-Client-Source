/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIPanic
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntityCreature;
/*    */   protected double speed;
/*    */   private double randPosX;
/*    */   private double randPosY;
/*    */   private double randPosZ;
/*    */   
/*    */   public EntityAIPanic(EntityCreature creature, double speedIn) {
/* 16 */     this.theEntityCreature = creature;
/* 17 */     this.speed = speedIn;
/* 18 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 23 */     if (this.theEntityCreature.getAITarget() == null && !this.theEntityCreature.isBurning())
/*    */     {
/* 25 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 29 */     Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.theEntityCreature, 5, 4);
/*    */     
/* 31 */     if (vec3 == null)
/*    */     {
/* 33 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 37 */     this.randPosX = vec3.xCoord;
/* 38 */     this.randPosY = vec3.yCoord;
/* 39 */     this.randPosZ = vec3.zCoord;
/* 40 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 47 */     this.theEntityCreature.getNavigator().tryMoveToXYZ(this.randPosX, this.randPosY, this.randPosZ, this.speed);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 52 */     return !this.theEntityCreature.getNavigator().noPath();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIPanic.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */