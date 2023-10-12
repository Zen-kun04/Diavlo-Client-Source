/*    */ package net.minecraft.pathfinding;
/*    */ 
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ public class PathPoint
/*    */ {
/*    */   public final int xCoord;
/*    */   public final int yCoord;
/*    */   public final int zCoord;
/*    */   private final int hash;
/* 11 */   int index = -1;
/*    */   
/*    */   float totalPathDistance;
/*    */   float distanceToNext;
/*    */   float distanceToTarget;
/*    */   PathPoint previous;
/*    */   public boolean visited;
/*    */   
/*    */   public PathPoint(int x, int y, int z) {
/* 20 */     this.xCoord = x;
/* 21 */     this.yCoord = y;
/* 22 */     this.zCoord = z;
/* 23 */     this.hash = makeHash(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public static int makeHash(int x, int y, int z) {
/* 28 */     return y & 0xFF | (x & 0x7FFF) << 8 | (z & 0x7FFF) << 24 | ((x < 0) ? Integer.MIN_VALUE : 0) | ((z < 0) ? 32768 : 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public float distanceTo(PathPoint pathpointIn) {
/* 33 */     float f = (pathpointIn.xCoord - this.xCoord);
/* 34 */     float f1 = (pathpointIn.yCoord - this.yCoord);
/* 35 */     float f2 = (pathpointIn.zCoord - this.zCoord);
/* 36 */     return MathHelper.sqrt_float(f * f + f1 * f1 + f2 * f2);
/*    */   }
/*    */ 
/*    */   
/*    */   public float distanceToSquared(PathPoint pathpointIn) {
/* 41 */     float f = (pathpointIn.xCoord - this.xCoord);
/* 42 */     float f1 = (pathpointIn.yCoord - this.yCoord);
/* 43 */     float f2 = (pathpointIn.zCoord - this.zCoord);
/* 44 */     return f * f + f1 * f1 + f2 * f2;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 49 */     if (!(p_equals_1_ instanceof PathPoint))
/*    */     {
/* 51 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 55 */     PathPoint pathpoint = (PathPoint)p_equals_1_;
/* 56 */     return (this.hash == pathpoint.hash && this.xCoord == pathpoint.xCoord && this.yCoord == pathpoint.yCoord && this.zCoord == pathpoint.zCoord);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 62 */     return this.hash;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isAssigned() {
/* 67 */     return (this.index >= 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 72 */     return this.xCoord + ", " + this.yCoord + ", " + this.zCoord;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\pathfinding\PathPoint.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */