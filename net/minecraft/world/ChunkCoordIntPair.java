/*    */ package net.minecraft.world;
/*    */ 
/*    */ import net.minecraft.util.BlockPos;
/*    */ 
/*    */ public class ChunkCoordIntPair
/*    */ {
/*    */   public final int chunkXPos;
/*    */   public final int chunkZPos;
/*  9 */   private int cachedHashCode = 0;
/*    */ 
/*    */   
/*    */   public ChunkCoordIntPair(int x, int z) {
/* 13 */     this.chunkXPos = x;
/* 14 */     this.chunkZPos = z;
/*    */   }
/*    */ 
/*    */   
/*    */   public static long chunkXZ2Int(int x, int z) {
/* 19 */     return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32L;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 24 */     if (this.cachedHashCode == 0) {
/*    */       
/* 26 */       int i = 1664525 * this.chunkXPos + 1013904223;
/* 27 */       int j = 1664525 * (this.chunkZPos ^ 0xDEADBEEF) + 1013904223;
/* 28 */       this.cachedHashCode = i ^ j;
/*    */     } 
/*    */     
/* 31 */     return this.cachedHashCode;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 36 */     if (this == p_equals_1_)
/*    */     {
/* 38 */       return true;
/*    */     }
/* 40 */     if (!(p_equals_1_ instanceof ChunkCoordIntPair))
/*    */     {
/* 42 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 46 */     ChunkCoordIntPair chunkcoordintpair = (ChunkCoordIntPair)p_equals_1_;
/* 47 */     return (this.chunkXPos == chunkcoordintpair.chunkXPos && this.chunkZPos == chunkcoordintpair.chunkZPos);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int getCenterXPos() {
/* 53 */     return (this.chunkXPos << 4) + 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getCenterZPosition() {
/* 58 */     return (this.chunkZPos << 4) + 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getXStart() {
/* 63 */     return this.chunkXPos << 4;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getZStart() {
/* 68 */     return this.chunkZPos << 4;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getXEnd() {
/* 73 */     return (this.chunkXPos << 4) + 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getZEnd() {
/* 78 */     return (this.chunkZPos << 4) + 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getBlock(int x, int y, int z) {
/* 83 */     return new BlockPos((this.chunkXPos << 4) + x, y, (this.chunkZPos << 4) + z);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getCenterBlock(int y) {
/* 88 */     return new BlockPos(getCenterXPos(), y, getCenterZPosition());
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 93 */     return "[" + this.chunkXPos + ", " + this.chunkZPos + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\ChunkCoordIntPair.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */