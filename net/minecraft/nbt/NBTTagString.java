/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NBTTagString
/*    */   extends NBTBase
/*    */ {
/*    */   private String data;
/*    */   
/*    */   public NBTTagString() {
/* 13 */     this.data = "";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTTagString(String data) {
/* 18 */     this.data = data;
/*    */     
/* 20 */     if (data == null)
/*    */     {
/* 22 */       throw new IllegalArgumentException("Empty string not allowed");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {
/* 28 */     output.writeUTF(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 33 */     sizeTracker.read(288L);
/* 34 */     this.data = input.readUTF();
/* 35 */     sizeTracker.read((16 * this.data.length()));
/*    */   }
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 40 */     return 8;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 45 */     return "\"" + this.data.replace("\"", "\\\"") + "\"";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 50 */     return new NBTTagString(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean hasNoTags() {
/* 55 */     return this.data.isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object p_equals_1_) {
/* 60 */     if (!super.equals(p_equals_1_))
/*    */     {
/* 62 */       return false;
/*    */     }
/*    */ 
/*    */     
/* 66 */     NBTTagString nbttagstring = (NBTTagString)p_equals_1_;
/* 67 */     return ((this.data == null && nbttagstring.data == null) || (this.data != null && this.data.equals(nbttagstring.data)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 73 */     return super.hashCode() ^ this.data.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String getString() {
/* 78 */     return this.data;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagString.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */