/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.io.DataInput;
/*    */ import java.io.DataOutput;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class NBTTagEnd
/*    */   extends NBTBase
/*    */ {
/*    */   void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
/* 11 */     sizeTracker.read(64L);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void write(DataOutput output) throws IOException {}
/*    */ 
/*    */   
/*    */   public byte getId() {
/* 20 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 25 */     return "END";
/*    */   }
/*    */ 
/*    */   
/*    */   public NBTBase copy() {
/* 30 */     return new NBTTagEnd();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraft\nbt\NBTTagEnd.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */