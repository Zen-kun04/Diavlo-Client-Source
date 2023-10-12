/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.entity.EntityLivingBase;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIMoveTowardsTarget
/*    */   extends EntityAIBase {
/*    */   private EntityCreature theEntity;
/*    */   private EntityLivingBase targetEntity;
/*    */   private double movePosX;
/*    */   private double movePosY;
/*    */   private double movePosZ;
/*    */   private double speed;
/*    */   private float maxTargetDistance;
/*    */   
/*    */   public EntityAIMoveTowardsTarget(EntityCreature creature, double speedIn, float targetMaxDistance) {
/* 19 */     this.theEntity = creature;
/* 20 */     this.speed = speedIn;
/* 21 */     this.maxTargetDistance = targetMaxDistance;
/* 22 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 27 */     this.targetEntity = this.theEntity.getAttackTarget();
/*    */     
/* 29 */     if (this.targetEntity == null)
/*    */     {
/* 31 */       return false;
/*    */     }
/* 33 */     if (this.targetEntity.getDistanceSqToEntity((Entity)this.theEntity) > (this.maxTargetDistance * this.maxTargetDistance))
/*    */     {
/* 35 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 39 */     Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockTowards(this.theEntity, 16, 7, new Vec3(this.targetEntity.posX, this.targetEntity.posY, this.targetEntity.posZ));
/*    */     
/* 41 */     if (vec3 == null)
/*    */     {
/* 43 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 47 */     this.movePosX = vec3.xCoord;
/* 48 */     this.movePosY = vec3.yCoord;
/* 49 */     this.movePosZ = vec3.zCoord;
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 57 */     return (!this.theEntity.getNavigator().noPath() && this.targetEntity.isEntityAlive() && this.targetEntity.getDistanceSqToEntity((Entity)this.theEntity) < (this.maxTargetDistance * this.maxTargetDistance));
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 62 */     this.targetEntity = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 67 */     this.theEntity.getNavigator().tryMoveToXYZ(this.movePosX, this.movePosY, this.movePosZ, this.speed);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIMoveTowardsTarget.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */