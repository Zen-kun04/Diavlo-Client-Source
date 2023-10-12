/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ 
/*    */ public class EntityAIRestrictSun
/*    */   extends EntityAIBase
/*    */ {
/*    */   private EntityCreature theEntity;
/*    */   
/*    */   public EntityAIRestrictSun(EntityCreature creature) {
/* 12 */     this.theEntity = creature;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 17 */     return this.theEntity.worldObj.isDaytime();
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 22 */     ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 27 */     ((PathNavigateGround)this.theEntity.getNavigator()).setAvoidSun(false);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIRestrictSun.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */