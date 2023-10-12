/*     */ package net.minecraft.util;
/*     */ 
/*     */ 
/*     */ public class Vec3d
/*     */ {
/*     */   public final double xCoord;
/*     */   public final double yCoord;
/*     */   public final double zCoord;
/*     */   
/*     */   public Vec3d(double x, double y, double z) {
/*  11 */     if (x == -0.0D) {
/*  12 */       x = 0.0D;
/*     */     }
/*  14 */     if (y == -0.0D) {
/*  15 */       y = 0.0D;
/*     */     }
/*  17 */     if (z == -0.0D) {
/*  18 */       z = 0.0D;
/*     */     }
/*  20 */     this.xCoord = x;
/*  21 */     this.yCoord = y;
/*  22 */     this.zCoord = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d(Vec3i p_i46377_1_) {
/*  27 */     this(p_i46377_1_.getX(), p_i46377_1_.getY(), p_i46377_1_.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d subtractReverse(Vec3d vec) {
/*  32 */     return new Vec3d(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3d normalize() {
/*  38 */     double d0 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*  39 */     return (d0 < 1.0E-4D) ? new Vec3d(0.0D, 0.0D, 0.0D) : new Vec3d(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double dotProduct(Vec3d vec) {
/*  45 */     return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d crossProduct(Vec3d vec) {
/*  50 */     return new Vec3d(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3d subtract(Vec3d vec) {
/*  57 */     return subtract(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d subtract(double x, double y, double z) {
/*  62 */     return addVector(-x, -y, -z);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d add(Vec3d vec) {
/*  67 */     return addVector(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d addVector(double x, double y, double z) {
/*  72 */     return new Vec3d(this.xCoord + x, this.yCoord + y, this.zCoord + z);
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceTo(Vec3d vec) {
/*  77 */     double d0 = vec.xCoord - this.xCoord;
/*  78 */     double d1 = vec.yCoord - this.yCoord;
/*  79 */     double d2 = vec.zCoord - this.zCoord;
/*  80 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public double squareDistanceTo(Vec3d vec) {
/*  85 */     double d0 = vec.xCoord - this.xCoord;
/*  86 */     double d1 = vec.yCoord - this.yCoord;
/*  87 */     double d2 = vec.zCoord - this.zCoord;
/*  88 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */ 
/*     */   
/*     */   public double lengthVector() {
/*  93 */     return 
/*  94 */       MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d getIntermediateWithXValue(Vec3d vec, double x) {
/*  99 */     double d0 = vec.xCoord - this.xCoord;
/* 100 */     double d1 = vec.yCoord - this.yCoord;
/* 101 */     double d2 = vec.zCoord - this.zCoord;
/* 102 */     if (d0 * d0 < 1.0000000116860974E-7D) {
/* 103 */       return null;
/*     */     }
/* 105 */     double d3 = (x - this.xCoord) / d0;
/* 106 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3d getIntermediateWithYValue(Vec3d vec, double y) {
/* 112 */     double d0 = vec.xCoord - this.xCoord;
/* 113 */     double d1 = vec.yCoord - this.yCoord;
/* 114 */     double d2 = vec.zCoord - this.zCoord;
/* 115 */     if (d1 * d1 < 1.0000000116860974E-7D) {
/* 116 */       return null;
/*     */     }
/* 118 */     double d3 = (y - this.yCoord) / d1;
/* 119 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3d getIntermediateWithZValue(Vec3d vec, double z) {
/* 125 */     double d0 = vec.xCoord - this.xCoord;
/* 126 */     double d1 = vec.yCoord - this.yCoord;
/* 127 */     double d2 = vec.zCoord - this.zCoord;
/* 128 */     if (d2 * d2 < 1.0000000116860974E-7D) {
/* 129 */       return null;
/*     */     }
/* 131 */     double d3 = (z - this.zCoord) / d2;
/* 132 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3d(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 138 */     return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d rotatePitch(float pitch) {
/* 143 */     float f = MathHelper.cos(pitch);
/* 144 */     float f1 = MathHelper.sin(pitch);
/* 145 */     double d0 = this.xCoord;
/* 146 */     double d1 = this.yCoord * f + this.zCoord * f1;
/* 147 */     double d2 = this.zCoord * f - this.yCoord * f1;
/* 148 */     return new Vec3d(d0, d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3d rotateYaw(float yaw) {
/* 153 */     float f = MathHelper.cos(yaw);
/* 154 */     float f1 = MathHelper.sin(yaw);
/* 155 */     double d0 = this.xCoord * f + this.zCoord * f1;
/* 156 */     double d1 = this.yCoord;
/* 157 */     double d2 = this.zCoord * f - this.xCoord * f1;
/* 158 */     return new Vec3d(d0, d1, d2);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Vec3d.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */