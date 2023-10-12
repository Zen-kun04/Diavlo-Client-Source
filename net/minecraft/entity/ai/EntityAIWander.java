/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ public class EntityAIWander
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature entity;
/*    */   private double xPosition;
/*    */   private double yPosition;
/*    */   private double zPosition;
/*    */   private double speed;
/*    */   private int executionChance;
/*    */   private boolean mustUpdate;
/*    */   
/*    */   public EntityAIWander(EntityCreature creatureIn, double speedIn) {
/* 18 */     this(creatureIn, speedIn, 120);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityAIWander(EntityCreature creatureIn, double speedIn, int chance) {
/* 23 */     this.entity = creatureIn;
/* 24 */     this.speed = speedIn;
/* 25 */     this.executionChance = chance;
/* 26 */     setMutexBits(1);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 31 */     if (!this.mustUpdate) {
/*    */       
/* 33 */       if (this.entity.getAge() >= 100)
/*    */       {
/* 35 */         return false;
/*    */       }
/*    */       
/* 38 */       if (this.entity.getRNG().nextInt(this.executionChance) != 0)
/*    */       {
/* 40 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 44 */     Vec3 vec3 = RandomPositionGenerator.findRandomTarget(this.entity, 10, 7);
/*    */     
/* 46 */     if (vec3 == null)
/*    */     {
/* 48 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 52 */     this.xPosition = vec3.xCoord;
/* 53 */     this.yPosition = vec3.yCoord;
/* 54 */     this.zPosition = vec3.zCoord;
/* 55 */     this.mustUpdate = false;
/* 56 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 62 */     return !this.entity.getNavigator().noPath();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 67 */     this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
/*    */   }
/*    */ 
/*    */   
/*    */   public void makeUpdate() {
/* 72 */     this.mustUpdate = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setExecutionChance(int newchance) {
/* 77 */     this.executionChance = newchance;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIWander.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */