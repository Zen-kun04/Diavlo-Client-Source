/*    */ package net.minecraft.pathfinding;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.entity.EntityLiving;
/*    */ import net.minecraft.util.MovingObjectPosition;
/*    */ import net.minecraft.util.Vec3;
/*    */ import net.minecraft.world.World;
/*    */ import net.minecraft.world.pathfinder.NodeProcessor;
/*    */ import net.minecraft.world.pathfinder.SwimNodeProcessor;
/*    */ 
/*    */ public class PathNavigateSwimmer extends PathNavigate {
/*    */   public PathNavigateSwimmer(EntityLiving entitylivingIn, World worldIn) {
/* 13 */     super(entitylivingIn, worldIn);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PathFinder getPathFinder() {
/* 18 */     return new PathFinder((NodeProcessor)new SwimNodeProcessor());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canNavigate() {
/* 23 */     return isInLiquid();
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getEntityPosition() {
/* 28 */     return new Vec3(this.theEntity.posX, this.theEntity.posY + this.theEntity.height * 0.5D, this.theEntity.posZ);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void pathFollow() {
/* 33 */     Vec3 vec3 = getEntityPosition();
/* 34 */     float f = this.theEntity.width * this.theEntity.width;
/* 35 */     int i = 6;
/*    */     
/* 37 */     if (vec3.squareDistanceTo(this.currentPath.getVectorFromIndex((Entity)this.theEntity, this.currentPath.getCurrentPathIndex())) < f)
/*    */     {
/* 39 */       this.currentPath.incrementPathIndex();
/*    */     }
/*    */     
/* 42 */     for (int j = Math.min(this.currentPath.getCurrentPathIndex() + i, this.currentPath.getCurrentPathLength() - 1); j > this.currentPath.getCurrentPathIndex(); j--) {
/*    */       
/* 44 */       Vec3 vec31 = this.currentPath.getVectorFromIndex((Entity)this.theEntity, j);
/*    */       
/* 46 */       if (vec31.squareDistanceTo(vec3) <= 36.0D && isDirectPathBetweenPoints(vec3, vec31, 0, 0, 0)) {
/*    */         
/* 48 */         this.currentPath.setCurrentPathIndex(j);
/*    */         
/*    */         break;
/*    */       } 
/*    */     } 
/* 53 */     checkForStuck(vec3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void removeSunnyPath() {
/* 58 */     super.removeSunnyPath();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ) {
/* 63 */     MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(posVec31, new Vec3(posVec32.xCoord, posVec32.yCoord + this.theEntity.height * 0.5D, posVec32.zCoord), false, true, false);
/* 64 */     return (movingobjectposition == null || movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.MISS);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathNavigateSwimmer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */