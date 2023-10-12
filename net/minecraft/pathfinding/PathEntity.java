/*    */ package net.minecraft.pathfinding;
/*    */ 
/*    */ import net.minecraft.entity.Entity;
/*    */ import net.minecraft.util.Vec3;
/*    */ 
/*    */ 
/*    */ public class PathEntity
/*    */ {
/*    */   private final PathPoint[] points;
/*    */   private int currentPathIndex;
/*    */   private int pathLength;
/*    */   
/*    */   public PathEntity(PathPoint[] pathpoints) {
/* 14 */     this.points = pathpoints;
/* 15 */     this.pathLength = pathpoints.length;
/*    */   }
/*    */ 
/*    */   
/*    */   public void incrementPathIndex() {
/* 20 */     this.currentPathIndex++;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isFinished() {
/* 25 */     return (this.currentPathIndex >= this.pathLength);
/*    */   }
/*    */ 
/*    */   
/*    */   public PathPoint getFinalPathPoint() {
/* 30 */     return (this.pathLength > 0) ? this.points[this.pathLength - 1] : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public PathPoint getPathPointFromIndex(int index) {
/* 35 */     return this.points[index];
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCurrentPathLength() {
/* 40 */     return this.pathLength;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCurrentPathLength(int length) {
/* 45 */     this.pathLength = length;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCurrentPathIndex() {
/* 50 */     return this.currentPathIndex;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setCurrentPathIndex(int currentPathIndexIn) {
/* 55 */     this.currentPathIndex = currentPathIndexIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getVectorFromIndex(Entity entityIn, int index) {
/* 60 */     double d0 = (this.points[index]).xCoord + (int)(entityIn.width + 1.0F) * 0.5D;
/* 61 */     double d1 = (this.points[index]).yCoord;
/* 62 */     double d2 = (this.points[index]).zCoord + (int)(entityIn.width + 1.0F) * 0.5D;
/* 63 */     return new Vec3(d0, d1, d2);
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3 getPosition(Entity entityIn) {
/* 68 */     return getVectorFromIndex(entityIn, this.currentPathIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSamePath(PathEntity pathentityIn) {
/* 73 */     if (pathentityIn == null)
/*    */     {
/* 75 */       return false;
/*    */     }
/* 77 */     if (pathentityIn.points.length != this.points.length)
/*    */     {
/* 79 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 83 */     for (int i = 0; i < this.points.length; i++) {
/*    */       
/* 85 */       if ((this.points[i]).xCoord != (pathentityIn.points[i]).xCoord || (this.points[i]).yCoord != (pathentityIn.points[i]).yCoord || (this.points[i]).zCoord != (pathentityIn.points[i]).zCoord)
/*    */       {
/* 87 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 91 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isDestinationSame(Vec3 vec) {
/* 97 */     PathPoint pathpoint = getFinalPathPoint();
/* 98 */     return (pathpoint == null) ? false : ((pathpoint.xCoord == (int)vec.xCoord && pathpoint.zCoord == (int)vec.zCoord));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */