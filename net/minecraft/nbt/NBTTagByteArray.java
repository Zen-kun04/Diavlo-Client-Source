/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ import java.util.Arrays;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NBTTagByteArray
/*    */   extends NBTBase
/*    */ {
/*    */   private byte[] data;
/*    */   
/*    */   NBTTagByteArray() {}
/*    */   
/*    */   public NBTTagByteArray(byte[] data) {
/* 18 */     this.data = data;
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 23 */     output.writeInt(this.data.length);
/* 24 */     output.write(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 29 */     sizeTracker.read(192L);
/* 30 */     int i = input.readInt();
/* 31 */     sizeTracker.read((8 * i));
/* 32 */     this.data = new byte[i];
/* 33 */     input.readFully(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 38 */     return 7;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 43 */     return "[" + this.data.length + " bytes]";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 48 */     byte[] abyte = new byte[this.data.length];
/* 49 */     System.arraycopy(this.data, 0, abyte, 0, this.data.length);
/* 50 */     return new NBTTagByteArray(abyte);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 55 */     return super.equals(p_equals_1_) ? Arrays.equals(this.data, ((NBTTagByteArray)p_equals_1_).data) : false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 60 */     return super.hashCode() ^ Arrays.hashCode(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public byte[] getByteArray() {
/* 65 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagByteArray.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */