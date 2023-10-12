/*     */ package net.minecraft.world.gen.structure;
/*     */ 
/*     */ import com.google.common.base.Objects;
/*     */ import net.minecraft.nbt.NBTTagIntArray;
/*     */ import net.minecraft.util.BlockPos;
/*     */ import net.minecraft.util.EnumFacing;
/*     */ import net.minecraft.util.Vec3i;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StructureBoundingBox
/*     */ {
/*     */   public int minX;
/*     */   public int minY;
/*     */   public int minZ;
/*     */   public int maxX;
/*     */   public int maxY;
/*     */   public int maxZ;
/*     */   
/*     */   public StructureBoundingBox() {}
/*     */   
/*     */   public StructureBoundingBox(int[] coords) {
/*  24 */     if (coords.length == 6) {
/*     */       
/*  26 */       this.minX = coords[0];
/*  27 */       this.minY = coords[1];
/*  28 */       this.minZ = coords[2];
/*  29 */       this.maxX = coords[3];
/*  30 */       this.maxY = coords[4];
/*  31 */       this.maxZ = coords[5];
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureBoundingBox getNewBoundingBox() {
/*  37 */     return new StructureBoundingBox(2147483647, 2147483647, 2147483647, -2147483648, -2147483648, -2147483648);
/*     */   }
/*     */ 
/*     */   
/*     */   public static StructureBoundingBox getComponentToAddBoundingBox(int p_175897_0_, int p_175897_1_, int p_175897_2_, int p_175897_3_, int p_175897_4_, int p_175897_5_, int p_175897_6_, int p_175897_7_, int p_175897_8_, EnumFacing p_175897_9_) {
/*  42 */     switch (p_175897_9_) {
/*     */       
/*     */       case NORTH:
/*  45 */         return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ - p_175897_8_ + 1 + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_5_);
/*     */       
/*     */       case SOUTH:
/*  48 */         return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
/*     */       
/*     */       case WEST:
/*  51 */         return new StructureBoundingBox(p_175897_0_ - p_175897_8_ + 1 + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
/*     */       
/*     */       case EAST:
/*  54 */         return new StructureBoundingBox(p_175897_0_ + p_175897_5_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_3_, p_175897_0_ + p_175897_8_ - 1 + p_175897_5_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_6_ - 1 + p_175897_3_);
/*     */     } 
/*     */     
/*  57 */     return new StructureBoundingBox(p_175897_0_ + p_175897_3_, p_175897_1_ + p_175897_4_, p_175897_2_ + p_175897_5_, p_175897_0_ + p_175897_6_ - 1 + p_175897_3_, p_175897_1_ + p_175897_7_ - 1 + p_175897_4_, p_175897_2_ + p_175897_8_ - 1 + p_175897_5_);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static StructureBoundingBox func_175899_a(int p_175899_0_, int p_175899_1_, int p_175899_2_, int p_175899_3_, int p_175899_4_, int p_175899_5_) {
/*  63 */     return new StructureBoundingBox(Math.min(p_175899_0_, p_175899_3_), Math.min(p_175899_1_, p_175899_4_), Math.min(p_175899_2_, p_175899_5_), Math.max(p_175899_0_, p_175899_3_), Math.max(p_175899_1_, p_175899_4_), Math.max(p_175899_2_, p_175899_5_));
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureBoundingBox(StructureBoundingBox structurebb) {
/*  68 */     this.minX = structurebb.minX;
/*  69 */     this.minY = structurebb.minY;
/*  70 */     this.minZ = structurebb.minZ;
/*  71 */     this.maxX = structurebb.maxX;
/*  72 */     this.maxY = structurebb.maxY;
/*  73 */     this.maxZ = structurebb.maxZ;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureBoundingBox(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
/*  78 */     this.minX = xMin;
/*  79 */     this.minY = yMin;
/*  80 */     this.minZ = zMin;
/*  81 */     this.maxX = xMax;
/*  82 */     this.maxY = yMax;
/*  83 */     this.maxZ = zMax;
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureBoundingBox(Vec3i vec1, Vec3i vec2) {
/*  88 */     this.minX = Math.min(vec1.getX(), vec2.getX());
/*  89 */     this.minY = Math.min(vec1.getY(), vec2.getY());
/*  90 */     this.minZ = Math.min(vec1.getZ(), vec2.getZ());
/*  91 */     this.maxX = Math.max(vec1.getX(), vec2.getX());
/*  92 */     this.maxY = Math.max(vec1.getY(), vec2.getY());
/*  93 */     this.maxZ = Math.max(vec1.getZ(), vec2.getZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public StructureBoundingBox(int xMin, int zMin, int xMax, int zMax) {
/*  98 */     this.minX = xMin;
/*  99 */     this.minZ = zMin;
/* 100 */     this.maxX = xMax;
/* 101 */     this.maxZ = zMax;
/* 102 */     this.minY = 1;
/* 103 */     this.maxY = 512;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersectsWith(StructureBoundingBox structurebb) {
/* 108 */     return (this.maxX >= structurebb.minX && this.minX <= structurebb.maxX && this.maxZ >= structurebb.minZ && this.minZ <= structurebb.maxZ && this.maxY >= structurebb.minY && this.minY <= structurebb.maxY);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean intersectsWith(int minXIn, int minZIn, int maxXIn, int maxZIn) {
/* 113 */     return (this.maxX >= minXIn && this.minX <= maxXIn && this.maxZ >= minZIn && this.minZ <= maxZIn);
/*     */   }
/*     */ 
/*     */   
/*     */   public void expandTo(StructureBoundingBox sbb) {
/* 118 */     this.minX = Math.min(this.minX, sbb.minX);
/* 119 */     this.minY = Math.min(this.minY, sbb.minY);
/* 120 */     this.minZ = Math.min(this.minZ, sbb.minZ);
/* 121 */     this.maxX = Math.max(this.maxX, sbb.maxX);
/* 122 */     this.maxY = Math.max(this.maxY, sbb.maxY);
/* 123 */     this.maxZ = Math.max(this.maxZ, sbb.maxZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public void offset(int x, int y, int z) {
/* 128 */     this.minX += x;
/* 129 */     this.minY += y;
/* 130 */     this.minZ += z;
/* 131 */     this.maxX += x;
/* 132 */     this.maxY += y;
/* 133 */     this.maxZ += z;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isVecInside(Vec3i vec) {
/* 138 */     return (vec.getX() >= this.minX && vec.getX() <= this.maxX && vec.getZ() >= this.minZ && vec.getZ() <= this.maxZ && vec.getY() >= this.minY && vec.getY() <= this.maxY);
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i func_175896_b() {
/* 143 */     return new Vec3i(this.maxX - this.minX, this.maxY - this.minY, this.maxZ - this.minZ);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getXSize() {
/* 148 */     return this.maxX - this.minX + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getYSize() {
/* 153 */     return this.maxY - this.minY + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getZSize() {
/* 158 */     return this.maxZ - this.minZ + 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public Vec3i getCenter() {
/* 163 */     return (Vec3i)new BlockPos(this.minX + (this.maxX - this.minX + 1) / 2, this.minY + (this.maxY - this.minY + 1) / 2, this.minZ + (this.maxZ - this.minZ + 1) / 2);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 168 */     return Objects.toStringHelper(this).add("x0", this.minX).add("y0", this.minY).add("z0", this.minZ).add("x1", this.maxX).add("y1", this.maxY).add("z1", this.maxZ).toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public NBTTagIntArray toNBTTagIntArray() {
/* 173 */     return new NBTTagIntArray(new int[] { this.minX, this.minY, this.minZ, this.maxX, this.maxY, this.maxZ });
/*     */   }
/*     */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\gen\structure\StructureBoundingBox.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */