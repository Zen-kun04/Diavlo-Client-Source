/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityCreature;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ import net.minecraft.util.BlockPos;
/*    */ import net.minecraft.village.Village;
/*    */ import net.minecraft.village.VillageDoorInfo;
/*    */ 
/*    */ public class EntityAIRestrictOpenDoor
/*    */   extends EntityAIBase {
/*    */   private EntityCreature entityObj;
/*    */   private VillageDoorInfo frontDoor;
/*    */   
/*    */   public EntityAIRestrictOpenDoor(EntityCreature creatureIn) {
/* 16 */     this.entityObj = creatureIn;
/*    */     
/* 18 */     if (!(creatureIn.getNavigator() instanceof PathNavigateGround))
/*    */     {
/* 20 */       throw new IllegalArgumentException("Unsupported mob type for RestrictOpenDoorGoal");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 26 */     if (this.entityObj.worldObj.isDaytime())
/*    */     {
/* 28 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 32 */     BlockPos blockpos = new BlockPos((Entity)this.entityObj);
/* 33 */     Village village = this.entityObj.worldObj.getVillageCollection().getNearestVillage(blockpos, 16);
/*    */     
/* 35 */     if (village == null)
/*    */     {
/* 37 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 41 */     this.frontDoor = village.getNearestDoor(blockpos);
/* 42 */     return (this.frontDoor == null) ? false : ((this.frontDoor.getDistanceToInsideBlockSq(blockpos) < 2.25D));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 49 */     return this.entityObj.worldObj.isDaytime() ? false : ((!this.frontDoor.getIsDetachedFromVillageFlag() && this.frontDoor.func_179850_c(new BlockPos((Entity)this.entityObj))));
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 54 */     ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(false);
/* 55 */     ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void resetTask() {
/* 60 */     ((PathNavigateGround)this.entityObj.getNavigator()).setBreakDoors(true);
/* 61 */     ((PathNavigateGround)this.entityObj.getNavigator()).setEnterDoors(true);
/* 62 */     this.frontDoor = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 67 */     this.frontDoor.incrementDoorOpeningRestrictionCounter();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIRestrictOpenDoor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */