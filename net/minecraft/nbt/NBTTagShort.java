/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagShort
/*    */   extends NBTBase.NBTPrimitive
/*    */ {
/*    */   private short data;
/*    */   
/*    */   public NBTTagShort() {}
/*    */   
/*    */   public NBTTagShort(short data) {
/* 17 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 22 */     output.writeShort(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 27 */     sizeTracker.read(80L);
/* 28 */     this.data = input.readShort();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 33 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "" + this.data + "s";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 43 */     return new NBTTagShort(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 48 */     if (super.equals(p_equals_1_)) {
/*    */       
/* 50 */       NBTTagShort nbttagshort = (NBTTagShort)p_equals_1_;
/* 51 */       return (this.data == nbttagshort.data);
/*    */     } 
/*    */ 
/*    */     
/* 55 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 61 */     return super.hashCode() ^ this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLong() {
/* 66 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt() {
/* 71 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public short getShort() {
/* 76 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getByte() {
/* 81 */     return (byte)(this.data & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble() {
/* 86 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat() {
/* 91 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagShort.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */