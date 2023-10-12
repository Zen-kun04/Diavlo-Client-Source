/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIMoveTowardsRestriction
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntity;
/*    */   private double movePosX;
/*    */   private double movePosY;
/*    */   private double movePosZ;
/*    */   private double movementSpeed;
/*    */   
/*    */   public EntityAIMoveTowardsRestriction(EntityCreature creatureIn, double speedIn) {
/* 17 */     this.theEntity = creatureIn;
/* 18 */     this.movementSpeed = speedIn;
/* 19 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 24 */     if (this.theEntity.isWithinHomeDistanceCurrentPosition())
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     BlockPos blockpos = this.theEntity.getHomePosition();
/* 31 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(blockpos.getX(), blockpos.getY(), blockpos.getZ()));
/*    */     
/* 33 */     if (vec3 == null)
/*    */     {
/* 35 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 39 */     this.movePosX = vec3.xCoord;
/* 40 */     this.movePosY = vec3.yCoord;
/* 41 */     this.movePosZ = vec3.zCoord;
/* 42 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 49 */     return !this.theEntity.getNavigator().noPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 54 */     this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.movementSpeed);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsRestriction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */