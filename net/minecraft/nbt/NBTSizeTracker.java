/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ public class NBTSizeTracker
/*    */ {
/*  5 */   public static final NBTSizeTracker INFINITE = new NBTSizeTracker(0L)
/*    */     {
/*    */       public void read(long bits) {}
/*    */     };
/*    */ 
/*    */   
/*    */   private final long max;
/*    */   
/*    */   private long read;
/*    */   
/*    */   public NBTSizeTracker(long max) {
/* 16 */     this.max = max;
/*    */   }
/*    */ 
/*    */   
/*    */   public void read(long bits) {
/* 21 */     this.read += bits / 8L;
/*    */     
/* 23 */     if (this.read > this.max)
/*    */     {
/* 25 */       throw new RuntimeException("Tried to read NBT tag that was too big; tried to allocate: " + this.read + "bytes where max allowed: " + this.max);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTSizeTracker.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */