/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagIntArray
/*    */   extends NBTBase
/*    */ {
/*    */   private int[] intArray;
/*    */   
/*    */   NBTTagIntArray() {}
/*    */   
/*    */   public NBTTagIntArray(int[] p_i45132_1_) {
/* 18 */     this.intArray = p_i45132_1_;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 23 */     output.writeInt(this.intArray.length);
/*    */     
/* 25 */     for (int i = 0; i < this.intArray.length; i++)
/*    */     {
/* 27 */       output.writeInt(this.intArray[i]);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 33 */     sizeTracker.read(192L);
/* 34 */     int i = input.readInt();
/* 35 */     sizeTracker.read((32 * i));
/* 36 */     this.intArray = new int[i];
/*    */     
/* 38 */     for (int j = 0; j < i; j++)
/*    */     {
/* 40 */       this.intArray[j] = input.readInt();
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 46 */     return 11;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 51 */     String s = "[";
/*    */     
/* 53 */     for (int i : this.intArray)
/*    */     {
/* 55 */       s = s + i + ",";
/*    */     }
/*    */     
/* 58 */     return s + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 63 */     int[] aint = new int[this.intArray.length];
/* 64 */     System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
/* 65 */     return new NBTTagIntArray(aint);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 70 */     return super.equals(p_equals_1_) ? Arrays.equals(this.intArray, ((NBTTagIntArray)p_equals_1_).intArray) : false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 75 */     return super.hashCode() ^ Arrays.hashCode(this.intArray);
/*    */   }
/*    */ 
/*    */   
/*    */   public int[] getIntArray() {
/* 80 */     return this.intArray;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagIntArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */