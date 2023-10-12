/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.passive.EntityAnimal;
/*    */ 
/*    */ public class EntityAIFollowParent
/*    */   extends EntityAIBase {
/*    */   EntityAnimal childAnimal;
/*    */   EntityAnimal parentAnimal;
/*    */   double moveSpeed;
/*    */   private int delayCounter;
/*    */   
/*    */   public EntityAIFollowParent(EntityAnimal animal, double speed) {
/* 15 */     this.childAnimal = animal;
/* 16 */     this.moveSpeed = speed;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 21 */     if (this.childAnimal.getGrowingAge() >= 0)
/*    */     {
/* 23 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 27 */     List<EntityAnimal> list = this.childAnimal.worldObj.getEntitiesWithinAABB(this.childAnimal.getClass(), this.childAnimal.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
/* 28 */     EntityAnimal entityanimal = null;
/* 29 */     double d0 = Double.MAX_VALUE;
/*    */     
/* 31 */     for (EntityAnimal entityanimal1 : list) {
/*    */       
/* 33 */       if (entityanimal1.getGrowingAge() >= 0) {
/*    */         
/* 35 */         double d1 = this.childAnimal.getDistanceSqToEntity((Entity)entityanimal1);
/*    */         
/* 37 */         if (d1 <= d0) {
/*    */           
/* 39 */           d0 = d1;
/* 40 */           entityanimal = entityanimal1;
/*    */         } 
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     if (entityanimal == null)
/*    */     {
/* 47 */       return false;
/*    */     }
/* 49 */     if (d0 < 9.0D)
/*    */     {
/* 51 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 55 */     this.parentAnimal = entityanimal;
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 63 */     if (this.childAnimal.getGrowingAge() >= 0)
/*    */     {
/* 65 */       return false;
/*    */     }
/* 67 */     if (!this.parentAnimal.isEntityAlive())
/*    */     {
/* 69 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 73 */     double d0 = this.childAnimal.getDistanceSqToEntity((Entity)this.parentAnimal);
/* 74 */     return (d0 >= 9.0D && d0 <= 256.0D);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 80 */     this.delayCounter = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 85 */     this.parentAnimal = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 90 */     if (--this.delayCounter <= 0) {
/*    */       
/* 92 */       this.delayCounter = 10;
/* 93 */       this.childAnimal.getNavigator().tryMoveToEntityLiving((Entity)this.parentAnimal, this.moveSpeed);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIFollowParent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */