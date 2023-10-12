/*    */ package com.viaversion.viaversion.api.minecraft.metadata;
/*    */ 
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ChunkPosition
/*    */ {
/*    */   private final int chunkX;
/*    */   private final int chunkZ;
/*    */   
/*    */   public ChunkPosition(int chunkX, int chunkZ) {
/* 32 */     this.chunkX = chunkX;
/* 33 */     this.chunkZ = chunkZ;
/*    */   }
/*    */   
/*    */   public ChunkPosition(long chunkKey) {
/* 37 */     this.chunkX = (int)chunkKey;
/* 38 */     this.chunkZ = (int)(chunkKey >> 32L);
/*    */   }
/*    */   
/*    */   public int chunkX() {
/* 42 */     return this.chunkX;
/*    */   }
/*    */   
/*    */   public int chunkZ() {
/* 46 */     return this.chunkZ;
/*    */   }
/*    */   
/*    */   public long chunkKey() {
/* 50 */     return this.chunkX & 0xFFFFFFFFL | (this.chunkZ & 0xFFFFFFFFL) << 32L;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 55 */     if (this == o) return true; 
/* 56 */     if (o == null || getClass() != o.getClass()) return false; 
/* 57 */     ChunkPosition that = (ChunkPosition)o;
/* 58 */     return (this.chunkX == that.chunkX && this.chunkZ == that.chunkZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return Objects.hash(new Object[] { Integer.valueOf(this.chunkX), Integer.valueOf(this.chunkZ) });
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 68 */     return "ChunkPosition{chunkX=" + this.chunkX + ", chunkZ=" + this.chunkZ + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\api\minecraft\metadata\ChunkPosition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */