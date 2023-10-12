/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import net.minecraft.util.MathHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagFloat
/*    */   extends NBTBase.NBTPrimitive
/*    */ {
/*    */   private float data;
/*    */   
/*    */   NBTTagFloat() {}
/*    */   
/*    */   public NBTTagFloat(float data) {
/* 19 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 24 */     output.writeFloat(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 29 */     sizeTracker.read(96L);
/* 30 */     this.data = input.readFloat();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 35 */     return 5;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     return "" + this.data + "f";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 45 */     return new NBTTagFloat(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 50 */     if (super.equals(p_equals_1_)) {
/*    */       
/* 52 */       NBTTagFloat nbttagfloat = (NBTTagFloat)p_equals_1_;
/* 53 */       return (this.data == nbttagfloat.data);
/*    */     } 
/*    */ 
/*    */     
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     return super.hashCode() ^ Float.floatToIntBits(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLong() {
/* 68 */     return (long)this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt() {
/* 73 */     return MathHelper.floor_float(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public short getShort() {
/* 78 */     return (short)(MathHelper.floor_float(this.data) & 0xFFFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getByte() {
/* 83 */     return (byte)(MathHelper.floor_float(this.data) & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble() {
/* 88 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat() {
/* 93 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagFloat.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */