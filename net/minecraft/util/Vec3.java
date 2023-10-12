/*     */ package net.minecraft.util;
/*     */ 
/*     */ 
/*     */ public class Vec3
/*     */ {
/*     */   public final double xCoord;
/*     */   public final double yCoord;
/*     */   public final double zCoord;
/*     */   
/*     */   public Vec3(double x, double y, double z) {
/*  11 */     if (x == -0.0D)
/*     */     {
/*  13 */       x = 0.0D;
/*     */     }
/*     */     
/*  16 */     if (y == -0.0D)
/*     */     {
/*  18 */       y = 0.0D;
/*     */     }
/*     */     
/*  21 */     if (z == -0.0D)
/*     */     {
/*  23 */       z = 0.0D;
/*     */     }
/*     */     
/*  26 */     this.xCoord = x;
/*  27 */     this.yCoord = y;
/*  28 */     this.zCoord = z;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3(Vec3i p_i46377_1_) {
/*  33 */     this(p_i46377_1_.getX(), p_i46377_1_.getY(), p_i46377_1_.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 subtractReverse(Vec3 vec) {
/*  38 */     return new Vec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 normalize() {
/*  43 */     double d0 = MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*  44 */     return (d0 < 1.0E-4D) ? new Vec3(0.0D, 0.0D, 0.0D) : new Vec3(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
/*     */   }
/*     */ 
/*     */   
/*     */   public double dotProduct(Vec3 vec) {
/*  49 */     return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 crossProduct(Vec3 vec) {
/*  54 */     return new Vec3(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 subtract(Vec3 vec) {
/*  59 */     return subtract(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 subtract(double x, double y, double z) {
/*  64 */     return addVector(-x, -y, -z);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 add(Vec3 vec) {
/*  69 */     return addVector(vec.xCoord, vec.yCoord, vec.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 addVector(double x, double y, double z) {
/*  74 */     return new Vec3(this.xCoord + x, this.yCoord + y, this.zCoord + z);
/*     */   }
/*     */ 
/*     */   
/*     */   public double distanceTo(Vec3 vec) {
/*  79 */     double d0 = vec.xCoord - this.xCoord;
/*  80 */     double d1 = vec.yCoord - this.yCoord;
/*  81 */     double d2 = vec.zCoord - this.zCoord;
/*  82 */     return MathHelper.sqrt_double(d0 * d0 + d1 * d1 + d2 * d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public double squareDistanceTo(Vec3 vec) {
/*  87 */     double d0 = vec.xCoord - this.xCoord;
/*  88 */     double d1 = vec.yCoord - this.yCoord;
/*  89 */     double d2 = vec.zCoord - this.zCoord;
/*  90 */     return d0 * d0 + d1 * d1 + d2 * d2;
/*     */   }
/*     */ 
/*     */   
/*     */   public double lengthVector() {
/*  95 */     return MathHelper.sqrt_double(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 getIntermediateWithXValue(Vec3 vec, double x) {
/* 100 */     double d0 = vec.xCoord - this.xCoord;
/* 101 */     double d1 = vec.yCoord - this.yCoord;
/* 102 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 104 */     if (d0 * d0 < 1.0000000116860974E-7D)
/*     */     {
/* 106 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 110 */     double d3 = (x - this.xCoord) / d0;
/* 111 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getIntermediateWithYValue(Vec3 vec, double y) {
/* 117 */     double d0 = vec.xCoord - this.xCoord;
/* 118 */     double d1 = vec.yCoord - this.yCoord;
/* 119 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 121 */     if (d1 * d1 < 1.0000000116860974E-7D)
/*     */     {
/* 123 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 127 */     double d3 = (y - this.yCoord) / d1;
/* 128 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Vec3 getIntermediateWithZValue(Vec3 vec, double z) {
/* 134 */     double d0 = vec.xCoord - this.xCoord;
/* 135 */     double d1 = vec.yCoord - this.yCoord;
/* 136 */     double d2 = vec.zCoord - this.zCoord;
/*     */     
/* 138 */     if (d2 * d2 < 1.0000000116860974E-7D)
/*     */     {
/* 140 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 144 */     double d3 = (z - this.zCoord) / d2;
/* 145 */     return (d3 >= 0.0D && d3 <= 1.0D) ? new Vec3(this.xCoord + d0 * d3, this.yCoord + d1 * d3, this.zCoord + d2 * d3) : null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 151 */     return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 rotatePitch(float pitch) {
/* 156 */     float f = MathHelper.cos(pitch);
/* 157 */     float f1 = MathHelper.sin(pitch);
/* 158 */     double d0 = this.xCoord;
/* 159 */     double d1 = this.yCoord * f + this.zCoord * f1;
/* 160 */     double d2 = this.zCoord * f - this.yCoord * f1;
/* 161 */     return new Vec3(d0, d1, d2);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3 rotateYaw(float yaw) {
/* 166 */     float f = MathHelper.cos(yaw);
/* 167 */     float f1 = MathHelper.sin(yaw);
/* 168 */     double d0 = this.xCoord * f + this.zCoord * f1;
/* 169 */     double d1 = this.yCoord;
/* 170 */     double d2 = this.zCoord * f - this.xCoord * f1;
/* 171 */     return new Vec3(d0, d1, d2);
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Vec3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */