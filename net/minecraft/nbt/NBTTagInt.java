/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagInt
/*    */   extends NBTBase.NBTPrimitive
/*    */ {
/*    */   private int data;
/*    */   
/*    */   NBTTagInt() {}
/*    */   
/*    */   public NBTTagInt(int data) {
/* 17 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 22 */     output.writeInt(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 27 */     sizeTracker.read(96L);
/* 28 */     this.data = input.readInt();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 33 */     return 3;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 38 */     return "" + this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 43 */     return new NBTTagInt(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 48 */     if (super.equals(p_equals_1_)) {
/*    */       
/* 50 */       NBTTagInt nbttagint = (NBTTagInt)p_equals_1_;
/* 51 */       return (this.data == nbttagint.data);
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
/* 76 */     return (short)(this.data & 0xFFFF);
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


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagInt.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */