/*    */ package rip.diavlo.base.api.value.impl;
/*    */ 
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.Value;
/*    */ 
/*    */ public class StringValue
/*    */   extends Value<String> {
/*    */   String value;
/*    */   
/*    */   public StringValue(String name, Module owner, String value) {
/* 11 */     super(name, owner, value);
/*    */     
/* 13 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getValue() {
/* 18 */     return this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(String value) {
/* 23 */     this.value = value;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\value\impl\StringValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */