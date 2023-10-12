/*    */ package net.minecraft.world.chunk;
/*    */ 
/*    */ 
/*    */ public class NibbleArray
/*    */ {
/*    */   private final byte[] data;
/*    */   
/*    */   public NibbleArray() {
/*  9 */     this.data = new byte[2048];
/*    */   }
/*    */ 
/*    */   
/*    */   public NibbleArray(byte[] storageArray) {
/* 14 */     this.data = storageArray;
/*    */     
/* 16 */     if (storageArray.length != 2048)
/*    */     {
/* 18 */       throw new IllegalArgumentException("ChunkNibbleArrays should be 2048 bytes not: " + storageArray.length);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public int get(int x, int y, int z) {
/* 24 */     return getFromIndex(getCoordinateIndex(x, y, z));
/*    */   }
/*    */ 
/*    */   
/*    */   public void set(int x, int y, int z, int value) {
/* 29 */     setIndex(getCoordinateIndex(x, y, z), value);
/*    */   }
/*    */ 
/*    */   
/*    */   private int getCoordinateIndex(int x, int y, int z) {
/* 34 */     return y << 8 | z << 4 | x;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFromIndex(int index) {
/* 39 */     int i = getNibbleIndex(index);
/* 40 */     return isLowerNibble(index) ? (this.data[i] & 0xF) : (this.data[i] >> 4 & 0xF);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setIndex(int index, int value) {
/* 45 */     int i = getNibbleIndex(index);
/*    */     
/* 47 */     if (isLowerNibble(index)) {
/*    */       
/* 49 */       this.data[i] = (byte)(this.data[i] & 0xF0 | value & 0xF);
/*    */     }
/*    */     else {
/*    */       
/* 53 */       this.data[i] = (byte)(this.data[i] & 0xF | (value & 0xF) << 4);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private boolean isLowerNibble(int index) {
/* 59 */     return ((index & 0x1) == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   private int getNibbleIndex(int index) {
/* 64 */     return index >> 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getData() {
/* 69 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\world\chunk\NibbleArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */