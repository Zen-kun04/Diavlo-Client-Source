/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.entity.player.EntityPlayer;
/*    */ 
/*    */ public class EntityAIWatchClosest
/*    */   extends EntityAIBase
/*    */ {
/*    */   protected EntityLiving theWatcher;
/*    */   protected Entity closestEntity;
/*    */   protected float maxDistanceForPlayer;
/*    */   private int lookTime;
/*    */   private float chance;
/*    */   protected Class<? extends Entity> watchedClass;
/*    */   
/*    */   public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance) {
/* 18 */     this.theWatcher = entitylivingIn;
/* 19 */     this.watchedClass = watchTargetClass;
/* 20 */     this.maxDistanceForPlayer = maxDistance;
/* 21 */     this.chance = 0.02F;
/* 22 */     setMutexBits(2);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityAIWatchClosest(EntityLiving entitylivingIn, Class<? extends Entity> watchTargetClass, float maxDistance, float chanceIn) {
/* 27 */     this.theWatcher = entitylivingIn;
/* 28 */     this.watchedClass = watchTargetClass;
/* 29 */     this.maxDistanceForPlayer = maxDistance;
/* 30 */     this.chance = chanceIn;
/* 31 */     setMutexBits(2);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 36 */     if (this.theWatcher.getRNG().nextFloat() >= this.chance)
/*    */     {
/* 38 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 42 */     if (this.theWatcher.getAttackTarget() != null)
/*    */     {
/* 44 */       this.closestEntity = (Entity)this.theWatcher.getAttackTarget();
/*    */     }
/*    */     
/* 47 */     if (this.watchedClass == EntityPlayer.class) {
/*    */       
/* 49 */       this.closestEntity = (Entity)this.theWatcher.worldObj.getClosestPlayerToEntity((Entity)this.theWatcher, this.maxDistanceForPlayer);
/*    */     }
/*    */     else {
/*    */       
/* 53 */       this.closestEntity = this.theWatcher.worldObj.findNearestEntityWithinAABB(this.watchedClass, this.theWatcher.getEntityBoundingBox().expand(this.maxDistanceForPlayer, 3.0D, this.maxDistanceForPlayer), (Entity)this.theWatcher);
/*    */     } 
/*    */     
/* 56 */     return (this.closestEntity != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 62 */     return !this.closestEntity.isEntityAlive() ? false : ((this.theWatcher.getDistanceSqToEntity(this.closestEntity) > (this.maxDistanceForPlayer * this.maxDistanceForPlayer)) ? false : ((this.lookTime > 0)));
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 67 */     this.lookTime = 40 + this.theWatcher.getRNG().nextInt(40);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 72 */     this.closestEntity = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 77 */     this.theWatcher.getLookHelper().setLookPosition(this.closestEntity.posX, this.closestEntity.posY + this.closestEntity.getEyeHeight(), this.closestEntity.posZ, 10.0F, this.theWatcher.getVerticalFaceSpeed());
/* 78 */     this.lookTime--;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIWatchClosest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */