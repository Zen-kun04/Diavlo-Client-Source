/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class TupleIntJsonSerializable
/*    */ {
/*    */   private int integerValue;
/*    */   private IJsonSerializable jsonSerializableValue;
/*    */   
/*    */   public int getIntegerValue() {
/* 10 */     return this.integerValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setIntegerValue(int integerValueIn) {
/* 15 */     this.integerValue = integerValueIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends IJsonSerializable> T getJsonSerializableValue() {
/* 20 */     return (T)this.jsonSerializableValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setJsonSerializableValue(IJsonSerializable jsonSerializableValueIn) {
/* 25 */     this.jsonSerializableValue = jsonSerializableValueIn;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\TupleIntJsonSerializable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */