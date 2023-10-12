/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagLong
/*    */   extends NBTBase.NBTPrimitive
/*    */ {
/*    */   private long data;
/*    */   
/*    */   NBTTagLong() {}
/*    */   
/*    */   public NBTTagLong(long data) {
/* 17 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 22 */     output.writeLong(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 27 */     sizeTracker.read(128L);
/* 28 */     this.data = input.readLong();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 33 */     return 4;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "" + this.data + "L";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 43 */     return new NBTTagLong(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 48 */     if (super.equals(p_equals_1_)) {
/*    */       
/* 50 */       NBTTagLong nbttaglong = (NBTTagLong)p_equals_1_;
/* 51 */       return (this.data == nbttaglong.data);
/*    */     } 
/*    */ 
/*    */     
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return super.hashCode() ^ (int)(this.data ^ this.data >>> 32L);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLong() {
/* 66 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt() {
/* 71 */     return (int)(this.data & 0xFFFFFFFFFFFFFFFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public short getShort() {
/* 76 */     return (short)(int)(this.data & 0xFFFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getByte() {
/* 81 */     return (byte)(int)(this.data & 0xFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble() {
/* 86 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat() {
/* 91 */     return (float)this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagLong.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */