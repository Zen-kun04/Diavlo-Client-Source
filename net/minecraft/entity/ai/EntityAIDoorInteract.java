/*    */ package net.minecraft.entity.ai;
/*    */ 
/*    */ import net.minecraft.block.Block;
/*    */ import net.minecraft.block.BlockDoor;
/*    */ import net.minecraft.block.material.Material;
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.pathfinding.PathEntity;
/*    */ import net.minecraft.pathfinding.PathNavigateGround;
/*    */ import net.minecraft.pathfinding.PathPoint;
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public abstract class EntityAIDoorInteract extends EntityAIBase {
/*    */   protected EntityLiving theEntity;
/* 15 */   protected BlockPos doorPosition = BlockPos.ORIGIN;
/*    */   
/*    */   protected BlockDoor doorBlock;
/*    */   boolean hasStoppedDoorInteraction;
/*    */   float entityPositionX;
/*    */   float entityPositionZ;
/*    */   
/*    */   public EntityAIDoorInteract(EntityLiving entityIn) {
/* 23 */     this.theEntity = entityIn;
/*    */     
/* 25 */     if (!(entityIn.getNavigator() instanceof PathNavigateGround))
/*    */     {
/* 27 */       throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldExecute() {
/* 33 */     if (!this.theEntity.isCollidedHorizontally)
/*    */     {
/* 35 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 39 */     PathNavigateGround pathnavigateground = (PathNavigateGround)this.theEntity.getNavigator();
/* 40 */     PathEntity pathentity = pathnavigateground.getPath();
/*    */     
/* 42 */     if (pathentity != null && !pathentity.isFinished() && pathnavigateground.getEnterDoors()) {
/*    */       
/* 44 */       for (int i = 0; i < Math.min(pathentity.getCurrentPathIndex() + 2, pathentity.getCurrentPathLength()); i++) {
/*    */         
/* 46 */         PathPoint pathpoint = pathentity.getPathPointFromIndex(i);
/* 47 */         this.doorPosition = new BlockPos(pathpoint.xCoord, pathpoint.yCoord + 1, pathpoint.zCoord);
/*    */         
/* 49 */         if (this.theEntity.getDistanceSq(this.doorPosition.getX(), this.theEntity.posY, this.doorPosition.getZ()) <= 2.25D) {
/*    */           
/* 51 */           this.doorBlock = getBlockDoor(this.doorPosition);
/*    */           
/* 53 */           if (this.doorBlock != null)
/*    */           {
/* 55 */             return true;
/*    */           }
/*    */         } 
/*    */       } 
/*    */       
/* 60 */       this.doorPosition = (new BlockPos((Entity)this.theEntity)).up();
/* 61 */       this.doorBlock = getBlockDoor(this.doorPosition);
/* 62 */       return (this.doorBlock != null);
/*    */     } 
/*    */ 
/*    */     
/* 66 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean continueExecuting() {
/* 73 */     return !this.hasStoppedDoorInteraction;
/*    */   }
/*    */ 
/*    */   
/*    */   public void startExecuting() {
/* 78 */     this.hasStoppedDoorInteraction = false;
/* 79 */     this.entityPositionX = (float)((this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
/* 80 */     this.entityPositionZ = (float)((this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public void updateTask() {
/* 85 */     float f = (float)((this.doorPosition.getX() + 0.5F) - this.theEntity.posX);
/* 86 */     float f1 = (float)((this.doorPosition.getZ() + 0.5F) - this.theEntity.posZ);
/* 87 */     float f2 = this.entityPositionX * f + this.entityPositionZ * f1;
/*    */     
/* 89 */     if (f2 < 0.0F)
/*    */     {
/* 91 */       this.hasStoppedDoorInteraction = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   private BlockDoor getBlockDoor(BlockPos pos) {
/* 97 */     Block block = this.theEntity.worldObj.getBlockState(pos).getBlock();
/* 98 */     return (block instanceof BlockDoor && block.getMaterial() == Material.wood) ? (BlockDoor)block : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\entity\ai\EntityAIDoorInteract.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */