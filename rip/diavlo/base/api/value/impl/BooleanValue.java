/*    */ package rip.diavlo.base.api.value.impl;
/*    */ 
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.Value;
/*    */ 
/*    */ public final class BooleanValue
/*    */   extends Value<Boolean> {
/*    */   public BooleanValue(String name, Module owner, Boolean value) {
/*  9 */     super(name, owner, value);
/*    */   }
/*    */   
/*    */   public BooleanValue(String name, Module owner, Boolean value, Value parent) {
/* 13 */     super(name, owner, value);
/* 14 */     this.parent = parent;
/*    */   }
/*    */   
/*    */   public BooleanValue(String name, Module owner, Boolean value, ModeValue modeValue, String... mode) {
/* 18 */     super(name, owner, value, modeValue, mode);
/*    */   }
/*    */   
/*    */   public void toggle() {
/* 22 */     this.value = Boolean.valueOf(!((Boolean)this.value).booleanValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 27 */     return (getValue() == obj || super.equals(obj));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\value\impl\BooleanValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */