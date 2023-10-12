/*    */ package rip.diavlo.base.api.value.impl;
/*    */ 
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ 
/*    */ public final class NumberValue<T extends Number> extends Value<T> {
/*    */   private final T min;
/*    */   private final T max;
/*    */   private final T increment;
/*    */   
/* 10 */   public T getMin() { return this.min; } public T getMax() { return this.max; } public T getIncrement() { return this.increment; }
/*    */   
/*    */   public NumberValue(String name, Module owner, T value, T min, T max, T increment) {
/* 13 */     super(name, owner, value);
/* 14 */     this.value = value;
/* 15 */     this.min = min;
/* 16 */     this.max = max;
/* 17 */     this.increment = increment;
/*    */   }
/*    */   
/*    */   public NumberValue(String name, Module owner, T value, T min, T max, T increment, BooleanValue parent) {
/* 21 */     super(name, owner, value, parent);
/* 22 */     this.value = value;
/* 23 */     this.min = min;
/* 24 */     this.max = max;
/* 25 */     this.increment = increment;
/*    */   }
/*    */   
/*    */   public NumberValue(String name, Module owner, T value, T min, T max, T increment, ModeValue parent, String... parentmode) {
/* 29 */     super(name, owner, value, parent, parentmode);
/* 30 */     this.value = value;
/* 31 */     this.min = min;
/* 32 */     this.max = max;
/* 33 */     this.increment = increment;
/*    */   }
/*    */   
/*    */   public T get() {
/* 37 */     return (T)this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getCastedValue() {
/* 42 */     return (T)this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public T getValue() {
/* 47 */     return (T)this.value;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValueAutoSave(T value) {
/* 52 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 57 */     return (super.equals(o) || getValue().equals(o));
/*    */   }
/*    */   
/*    */   public boolean isInteger() {
/* 61 */     return this.min instanceof Integer;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\value\impl\NumberValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */