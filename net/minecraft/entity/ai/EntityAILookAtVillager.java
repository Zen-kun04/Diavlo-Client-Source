/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.monster.EntityIronGolem;
/*    */ import net.minecraft.entity.passive.EntityVillager;
/*    */ 
/*    */ public class EntityAILookAtVillager
/*    */   extends EntityAIBase {
/*    */   private EntityIronGolem theGolem;
/*    */   private EntityVillager theVillager;
/*    */   private int lookTime;
/*    */   
/*    */   public EntityAILookAtVillager(EntityIronGolem theGolemIn) {
/* 14 */     this.theGolem = theGolemIn;
/* 15 */     setMutexBits(3);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 20 */     if (!this.theGolem.worldObj.isDaytime())
/*    */     {
/* 22 */       return false;
/*    */     }
/* 24 */     if (this.theGolem.getRNG().nextInt(8000) != 0)
/*    */     {
/* 26 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 30 */     this.theVillager = (EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.getEntityBoundingBox().expand(6.0D, 2.0D, 6.0D), (Entity)this.theGolem);
/* 31 */     return (this.theVillager != null);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 37 */     return (this.lookTime > 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 42 */     this.lookTime = 400;
/* 43 */     this.theGolem.setHoldingRose(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 48 */     this.theGolem.setHoldingRose(false);
/* 49 */     this.theVillager = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 54 */     this.theGolem.getLookHelper().setLookPositionWithEntity((Entity)this.theVillager, 30.0F, 30.0F);
/* 55 */     this.lookTime--;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAILookAtVillager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */