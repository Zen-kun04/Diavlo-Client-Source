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
/*    */ public class NBTTagDouble
/*    */   extends NBTBase.NBTPrimitive
/*    */ {
/*    */   private double data;
/*    */   
/*    */   NBTTagDouble() {}
/*    */   
/*    */   public NBTTagDouble(double data) {
/* 19 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 24 */     output.writeDouble(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 29 */     sizeTracker.read(128L);
/* 30 */     this.data = input.readDouble();
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 35 */     return 6;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 40 */     return "" + this.data + "d";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 45 */     return new NBTTagDouble(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 50 */     if (super.equals(p_equals_1_)) {
/*    */       
/* 52 */       NBTTagDouble nbttagdouble = (NBTTagDouble)p_equals_1_;
/* 53 */       return (this.data == nbttagdouble.data);
/*    */     } 
/*    */ 
/*    */     
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 63 */     long i = Double.doubleToLongBits(this.data);
/* 64 */     return super.hashCode() ^ (int)(i ^ i >>> 32L);
/*    */   }
/*    */ 
/*    */   
/*    */   public long getLong() {
/* 69 */     return (long)Math.floor(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getInt() {
/* 74 */     return MathHelper.floor_double(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public short getShort() {
/* 79 */     return (short)(MathHelper.floor_double(this.data) & 0xFFFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getByte() {
/* 84 */     return (byte)(MathHelper.floor_double(this.data) & 0xFF);
/*    */   }
/*    */ 
/*    */   
/*    */   public double getDouble() {
/* 89 */     return this.data;
/*    */   }
/*    */ 
/*    */   
/*    */   public float getFloat() {
/* 94 */     return (float)this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagDouble.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */