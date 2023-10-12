/*     */ package net.minecraft.util;
/*     */ 
/*     */ 
/*     */ public class AxisAlignedBB
/*     */ {
/*     */   public final double minX;
/*     */   public final double minY;
/*     */   public final double minZ;
/*     */   public final double maxX;
/*     */   public final double maxY;
/*     */   public final double maxZ;
/*     */   
/*     */   public AxisAlignedBB(double x1, double y1, double z1, double x2, double y2, double z2) {
/*  14 */     this.minX = Math.min(x1, x2);
/*  15 */     this.minY = Math.min(y1, y2);
/*  16 */     this.minZ = Math.min(z1, z2);
/*  17 */     this.maxX = Math.max(x1, x2);
/*  18 */     this.maxY = Math.max(y1, y2);
/*  19 */     this.maxZ = Math.max(z1, z2);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB(BlockPos pos1, BlockPos pos2) {
/*  24 */     this.minX = pos1.getX();
/*  25 */     this.minY = pos1.getY();
/*  26 */     this.minZ = pos1.getZ();
/*  27 */     this.maxX = pos2.getX();
/*  28 */     this.maxY = pos2.getY();
/*  29 */     this.maxZ = pos2.getZ();
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB addCoord(double x, double y, double z) {
/*  34 */     double d0 = this.minX;
/*  35 */     double d1 = this.minY;
/*  36 */     double d2 = this.minZ;
/*  37 */     double d3 = this.maxX;
/*  38 */     double d4 = this.maxY;
/*  39 */     double d5 = this.maxZ;
/*     */     
/*  41 */     if (x < 0.0D) {
/*     */       
/*  43 */       d0 += x;
/*     */     }
/*  45 */     else if (x > 0.0D) {
/*     */       
/*  47 */       d3 += x;
/*     */     } 
/*     */     
/*  50 */     if (y < 0.0D) {
/*     */       
/*  52 */       d1 += y;
/*     */     }
/*  54 */     else if (y > 0.0D) {
/*     */       
/*  56 */       d4 += y;
/*     */     } 
/*     */     
/*  59 */     if (z < 0.0D) {
/*     */       
/*  61 */       d2 += z;
/*     */     }
/*  63 */     else if (z > 0.0D) {
/*     */       
/*  65 */       d5 += z;
/*     */     } 
/*     */     
/*  68 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB expand(double x, double y, double z) {
/*  73 */     double d0 = this.minX - x;
/*  74 */     double d1 = this.minY - y;
/*  75 */     double d2 = this.minZ - z;
/*  76 */     double d3 = this.maxX + x;
/*  77 */     double d4 = this.maxY + y;
/*  78 */     double d5 = this.maxZ + z;
/*  79 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB union(AxisAlignedBB other) {
/*  84 */     double d0 = Math.min(this.minX, other.minX);
/*  85 */     double d1 = Math.min(this.minY, other.minY);
/*  86 */     double d2 = Math.min(this.minZ, other.minZ);
/*  87 */     double d3 = Math.max(this.maxX, other.maxX);
/*  88 */     double d4 = Math.max(this.maxY, other.maxY);
/*  89 */     double d5 = Math.max(this.maxZ, other.maxZ);
/*  90 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */ 
/*     */   
/*     */   public static AxisAlignedBB fromBounds(double x1, double y1, double z1, double x2, double y2, double z2) {
/*  95 */     double d0 = Math.min(x1, x2);
/*  96 */     double d1 = Math.min(y1, y2);
/*  97 */     double d2 = Math.min(z1, z2);
/*  98 */     double d3 = Math.max(x1, x2);
/*  99 */     double d4 = Math.max(y1, y2);
/* 100 */     double d5 = Math.max(z1, z2);
/* 101 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB offset(double x, double y, double z) {
/* 106 */     return new AxisAlignedBB(this.minX + x, this.minY + y, this.minZ + z, this.maxX + x, this.maxY + y, this.maxZ + z);
/*     */   }
/*     */ 
/*     */   
/*     */   public double calculateXOffset(AxisAlignedBB other, double offsetX) {
/* 111 */     if (other.maxY > this.minY && other.minY < this.maxY && other.maxZ > this.minZ && other.minZ < this.maxZ) {
/*     */       
/* 113 */       if (offsetX > 0.0D && other.maxX <= this.minX) {
/*     */         
/* 115 */         double d1 = this.minX - other.maxX;
/*     */         
/* 117 */         if (d1 < offsetX)
/*     */         {
/* 119 */           offsetX = d1;
/*     */         }
/*     */       }
/* 122 */       else if (offsetX < 0.0D && other.minX >= this.maxX) {
/*     */         
/* 124 */         double d0 = this.maxX - other.minX;
/*     */         
/* 126 */         if (d0 > offsetX)
/*     */         {
/* 128 */           offsetX = d0;
/*     */         }
/*     */       } 
/*     */       
/* 132 */       return offsetX;
/*     */     } 
/*     */ 
/*     */     
/* 136 */     return offsetX;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double calculateYOffset(AxisAlignedBB other, double offsetY) {
/* 142 */     if (other.maxX > this.minX && other.minX < this.maxX && other.maxZ > this.minZ && other.minZ < this.maxZ) {
/*     */       
/* 144 */       if (offsetY > 0.0D && other.maxY <= this.minY) {
/*     */         
/* 146 */         double d1 = this.minY - other.maxY;
/*     */         
/* 148 */         if (d1 < offsetY)
/*     */         {
/* 150 */           offsetY = d1;
/*     */         }
/*     */       }
/* 153 */       else if (offsetY < 0.0D && other.minY >= this.maxY) {
/*     */         
/* 155 */         double d0 = this.maxY - other.minY;
/*     */         
/* 157 */         if (d0 > offsetY)
/*     */         {
/* 159 */           offsetY = d0;
/*     */         }
/*     */       } 
/*     */       
/* 163 */       return offsetY;
/*     */     } 
/*     */ 
/*     */     
/* 167 */     return offsetY;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double calculateZOffset(AxisAlignedBB other, double offsetZ) {
/* 173 */     if (other.maxX > this.minX && other.minX < this.maxX && other.maxY > this.minY && other.minY < this.maxY) {
/*     */       
/* 175 */       if (offsetZ > 0.0D && other.maxZ <= this.minZ) {
/*     */         
/* 177 */         double d1 = this.minZ - other.maxZ;
/*     */         
/* 179 */         if (d1 < offsetZ)
/*     */         {
/* 181 */           offsetZ = d1;
/*     */         }
/*     */       }
/* 184 */       else if (offsetZ < 0.0D && other.minZ >= this.maxZ) {
/*     */         
/* 186 */         double d0 = this.maxZ - other.minZ;
/*     */         
/* 188 */         if (d0 > offsetZ)
/*     */         {
/* 190 */           offsetZ = d0;
/*     */         }
/*     */       } 
/*     */       
/* 194 */       return offsetZ;
/*     */     } 
/*     */ 
/*     */     
/* 198 */     return offsetZ;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean intersectsWith(AxisAlignedBB other) {
/* 204 */     return (other.maxX > this.minX && other.minX < this.maxX) ? ((other.maxY > this.minY && other.minY < this.maxY) ? ((other.maxZ > this.minZ && other.minZ < this.maxZ)) : false) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVecInside(Vec3 vec) {
/* 209 */     return (vec.xCoord > this.minX && vec.xCoord < this.maxX) ? ((vec.yCoord > this.minY && vec.yCoord < this.maxY) ? ((vec.zCoord > this.minZ && vec.zCoord < this.maxZ)) : false) : false;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getAverageEdgeLength() {
/* 214 */     double d0 = this.maxX - this.minX;
/* 215 */     double d1 = this.maxY - this.minY;
/* 216 */     double d2 = this.maxZ - this.minZ;
/* 217 */     return (d0 + d1 + d2) / 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public AxisAlignedBB contract(double x, double y, double z) {
/* 222 */     double d0 = this.minX + x;
/* 223 */     double d1 = this.minY + y;
/* 224 */     double d2 = this.minZ + z;
/* 225 */     double d3 = this.maxX - x;
/* 226 */     double d4 = this.maxY - y;
/* 227 */     double d5 = this.maxZ - z;
/* 228 */     return new AxisAlignedBB(d0, d1, d2, d3, d4, d5);
/*     */   }
/*     */ 
/*     */   
/*     */   public MovingObjectPosition calculateIntercept(Vec3 vecA, Vec3 vecB) {
/* 233 */     Vec3 vec3 = vecA.getIntermediateWithXValue(vecB, this.minX);
/* 234 */     Vec3 vec31 = vecA.getIntermediateWithXValue(vecB, this.maxX);
/* 235 */     Vec3 vec32 = vecA.getIntermediateWithYValue(vecB, this.minY);
/* 236 */     Vec3 vec33 = vecA.getIntermediateWithYValue(vecB, this.maxY);
/* 237 */     Vec3 vec34 = vecA.getIntermediateWithZValue(vecB, this.minZ);
/* 238 */     Vec3 vec35 = vecA.getIntermediateWithZValue(vecB, this.maxZ);
/*     */     
/* 240 */     if (!isVecInYZ(vec3))
/*     */     {
/* 242 */       vec3 = null;
/*     */     }
/*     */     
/* 245 */     if (!isVecInYZ(vec31))
/*     */     {
/* 247 */       vec31 = null;
/*     */     }
/*     */     
/* 250 */     if (!isVecInXZ(vec32))
/*     */     {
/* 252 */       vec32 = null;
/*     */     }
/*     */     
/* 255 */     if (!isVecInXZ(vec33))
/*     */     {
/* 257 */       vec33 = null;
/*     */     }
/*     */     
/* 260 */     if (!isVecInXY(vec34))
/*     */     {
/* 262 */       vec34 = null;
/*     */     }
/*     */     
/* 265 */     if (!isVecInXY(vec35))
/*     */     {
/* 267 */       vec35 = null;
/*     */     }
/*     */     
/* 270 */     Vec3 vec36 = null;
/*     */     
/* 272 */     if (vec3 != null)
/*     */     {
/* 274 */       vec36 = vec3;
/*     */     }
/*     */     
/* 277 */     if (vec31 != null && (vec36 == null || vecA.squareDistanceTo(vec31) < vecA.squareDistanceTo(vec36)))
/*     */     {
/* 279 */       vec36 = vec31;
/*     */     }
/*     */     
/* 282 */     if (vec32 != null && (vec36 == null || vecA.squareDistanceTo(vec32) < vecA.squareDistanceTo(vec36)))
/*     */     {
/* 284 */       vec36 = vec32;
/*     */     }
/*     */     
/* 287 */     if (vec33 != null && (vec36 == null || vecA.squareDistanceTo(vec33) < vecA.squareDistanceTo(vec36)))
/*     */     {
/* 289 */       vec36 = vec33;
/*     */     }
/*     */     
/* 292 */     if (vec34 != null && (vec36 == null || vecA.squareDistanceTo(vec34) < vecA.squareDistanceTo(vec36)))
/*     */     {
/* 294 */       vec36 = vec34;
/*     */     }
/*     */     
/* 297 */     if (vec35 != null && (vec36 == null || vecA.squareDistanceTo(vec35) < vecA.squareDistanceTo(vec36)))
/*     */     {
/* 299 */       vec36 = vec35;
/*     */     }
/*     */     
/* 302 */     if (vec36 == null)
/*     */     {
/* 304 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 308 */     EnumFacing enumfacing = null;
/*     */     
/* 310 */     if (vec36 == vec3) {
/*     */       
/* 312 */       enumfacing = EnumFacing.WEST;
/*     */     }
/* 314 */     else if (vec36 == vec31) {
/*     */       
/* 316 */       enumfacing = EnumFacing.EAST;
/*     */     }
/* 318 */     else if (vec36 == vec32) {
/*     */       
/* 320 */       enumfacing = EnumFacing.DOWN;
/*     */     }
/* 322 */     else if (vec36 == vec33) {
/*     */       
/* 324 */       enumfacing = EnumFacing.UP;
/*     */     }
/* 326 */     else if (vec36 == vec34) {
/*     */       
/* 328 */       enumfacing = EnumFacing.NORTH;
/*     */     }
/*     */     else {
/*     */       
/* 332 */       enumfacing = EnumFacing.SOUTH;
/*     */     } 
/*     */     
/* 335 */     return new MovingObjectPosition(vec36, enumfacing);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private boolean isVecInYZ(Vec3 vec) {
/* 341 */     return (vec == null) ? false : ((vec.yCoord >= this.minY && vec.yCoord <= this.maxY && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isVecInXZ(Vec3 vec) {
/* 346 */     return (vec == null) ? false : ((vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.zCoord >= this.minZ && vec.zCoord <= this.maxZ));
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean isVecInXY(Vec3 vec) {
/* 351 */     return (vec == null) ? false : ((vec.xCoord >= this.minX && vec.xCoord <= this.maxX && vec.yCoord >= this.minY && vec.yCoord <= this.maxY));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 356 */     return "box[" + this.minX + ", " + this.minY + ", " + this.minZ + " -> " + this.maxX + ", " + this.maxY + ", " + this.maxZ + "]";
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasNaN() {
/* 361 */     return (Double.isNaN(this.minX) || Double.isNaN(this.minY) || Double.isNaN(this.minZ) || Double.isNaN(this.maxX) || Double.isNaN(this.maxY) || Double.isNaN(this.maxZ));
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\AxisAlignedBB.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */