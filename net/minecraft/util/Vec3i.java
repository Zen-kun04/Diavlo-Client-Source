/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.base.Objects;
/*    */ 
/*    */ public class Vec3i
/*    */   implements Comparable<Vec3i> {
/*  7 */   public static final Vec3i NULL_VECTOR = new Vec3i(0, 0, 0);
/*    */   
/*    */   private final int x;
/*    */   private final int y;
/*    */   private final int z;
/*    */   
/*    */   public Vec3i(int xIn, int yIn, int zIn) {
/* 14 */     this.x = xIn;
/* 15 */     this.y = yIn;
/* 16 */     this.z = zIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3i(double xIn, double yIn, double zIn) {
/* 21 */     this(MathHelper.floor_double(xIn), MathHelper.floor_double(yIn), MathHelper.floor_double(zIn));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 26 */     if (this == p_equals_1_)
/*    */     {
/* 28 */       return true;
/*    */     }
/* 30 */     if (!(p_equals_1_ instanceof Vec3i))
/*    */     {
/* 32 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 36 */     Vec3i vec3i = (Vec3i)p_equals_1_;
/* 37 */     return (getX() != vec3i.getX()) ? false : ((getY() != vec3i.getY()) ? false : ((getZ() == vec3i.getZ())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 43 */     return (getY() + getZ() * 31) * 31 + getX();
/*    */   }
/*    */ 
/*    */   
/*    */   public int compareTo(Vec3i p_compareTo_1_) {
/* 48 */     return (getY() == p_compareTo_1_.getY()) ? ((getZ() == p_compareTo_1_.getZ()) ? (getX() - p_compareTo_1_.getX()) : (getZ() - p_compareTo_1_.getZ())) : (getY() - p_compareTo_1_.getY());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getX() {
/* 53 */     return this.x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getY() {
/* 58 */     return this.y;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getZ() {
/* 63 */     return this.z;
/*    */   }
/*    */ 
/*    */   
/*    */   public Vec3i crossProduct(Vec3i vec) {
/* 68 */     return new Vec3i(getY() * vec.getZ() - getZ() * vec.getY(), getZ() * vec.getX() - getX() * vec.getZ(), getX() * vec.getY() - getY() * vec.getX());
/*    */   }
/*    */ 
/*    */   
/*    */   public double distanceSq(double toX, double toY, double toZ) {
/* 73 */     double d0 = getX() - toX;
/* 74 */     double d1 = getY() - toY;
/* 75 */     double d2 = getZ() - toZ;
/* 76 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*    */   }
/*    */ 
/*    */   
/*    */   public double distanceSqToCenter(double xIn, double yIn, double zIn) {
/* 81 */     double d0 = getX() + 0.5D - xIn;
/* 82 */     double d1 = getY() + 0.5D - yIn;
/* 83 */     double d2 = getZ() + 0.5D - zIn;
/* 84 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*    */   }
/*    */ 
/*    */   
/*    */   public double distanceSq(Vec3i to) {
/* 89 */     return distanceSq(to.getX(), to.getY(), to.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 94 */     return Objects.toStringHelper(this).add("x", getX()).add("y", getY()).add("z", getZ()).toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Vec3i.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */