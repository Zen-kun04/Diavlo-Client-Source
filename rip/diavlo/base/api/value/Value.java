/*    */ package rip.diavlo.base.api.value;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.function.BooleanSupplier;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.impl.BooleanValue;
/*    */ import rip.diavlo.base.api.value.impl.ModeValue;
/*    */ 
/*    */ public abstract class Value<Type> {
/*    */   private final String name;
/*    */   
/*    */   public void setModes(List<String> modes) {
/* 15 */     this.modes = modes; } public void setParent(Value<?> parent) { this.parent = parent; } public void setValue(Type value) { this.value = value; } public void setHideIf(BooleanSupplier hideIf) { this.hideIf = hideIf; }
/*    */   
/*    */   public String getName() {
/* 18 */     return this.name;
/* 19 */   } private final Module owner; protected Value<?> parent; protected List<String> modes = new ArrayList<>(); protected Type value; public BooleanSupplier hideIf; public List<String> getModes() { return this.modes; }
/* 20 */   public Module getOwner() { return this.owner; }
/* 21 */   public Value<?> getParent() { return this.parent; }
/* 22 */   public Type getValue() { return this.value; } public BooleanSupplier getHideIf() {
/* 23 */     return this.hideIf;
/*    */   }
/*    */   public Value(String name, Module owner, Type value) {
/* 26 */     this.name = name;
/* 27 */     this.owner = owner;
/* 28 */     this.value = value;
/*    */   }
/*    */   
/*    */   public Type get() {
/* 32 */     return getValue();
/*    */   }
/*    */   
/*    */   public Value(String name, Module owner, Type value, ModeValue parent, String... modes) {
/* 36 */     this(name, owner, value);
/* 37 */     this.parent = (Value<?>)parent;
/* 38 */     Collections.addAll(this.modes, modes);
/*    */   }
/*    */   
/*    */   public Value(String name, Module owner, Type value, BooleanValue parent) {
/* 42 */     this(name, owner, value);
/* 43 */     this.parent = (Value<?>)parent;
/*    */   }
/*    */   
/*    */   public void setValueAutoSave(Type value) {
/* 47 */     this.value = value;
/*    */   }
/*    */   
/*    */   public String getValueAsString() {
/* 51 */     return String.valueOf(this.value);
/*    */   }
/*    */   
/*    */   public static boolean hideSetting(Value<?> value) {
/* 55 */     if (value.getHideIf() == null) return false; 
/* 56 */     return value.getHideIf().getAsBoolean();
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\value\Value.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */