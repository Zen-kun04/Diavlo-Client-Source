/*    */ package rip.diavlo.base.api.value.impl;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import rip.diavlo.base.api.module.Module;
/*    */ import rip.diavlo.base.api.value.Value;
/*    */ 
/*    */ public final class ModeValue extends Value<String> {
/*    */   private final List<String> choices;
/*    */   private final List<String> choicesLowerCase;
/*    */   
/*    */   public List<String> getChoices() {
/* 15 */     return this.choices; } public List<String> getChoicesLowerCase() {
/* 16 */     return this.choicesLowerCase;
/*    */   }
/*    */   public ModeValue(String name, Module owner, String... values) {
/* 19 */     super(name, owner, values[0]);
/* 20 */     this.choices = Arrays.asList(values);
/* 21 */     this.choicesLowerCase = new ArrayList<>();
/* 22 */     for (String choice : values) {
/* 23 */       this.choicesLowerCase.add(choice.toLowerCase());
/*    */     }
/*    */   }
/*    */   
/*    */   public ModeValue(String name, Module owner, ModeValue parent, String[] mode, String... values) {
/* 28 */     super(name, owner, values[0], parent, mode);
/* 29 */     this.choices = new ArrayList<>();
/* 30 */     Collections.addAll(this.choices, values);
/* 31 */     this.choicesLowerCase = new ArrayList<>();
/* 32 */     for (String choice : values) {
/* 33 */       this.choicesLowerCase.add(choice.toLowerCase());
/*    */     }
/*    */   }
/*    */   
/*    */   public ModeValue(String name, Module owner, BooleanValue parent, String... values) {
/* 38 */     super(name, owner, values[0], parent);
/* 39 */     this.choices = new ArrayList<>();
/* 40 */     Collections.addAll(this.choices, values);
/* 41 */     this.choicesLowerCase = new ArrayList<>();
/* 42 */     for (String choice : values) {
/* 43 */       this.choicesLowerCase.add(choice.toLowerCase());
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValueAutoSave(String value) {
/* 49 */     if (this.choicesLowerCase.contains(value.toLowerCase())) {
/* 50 */       super.setValueAutoSave(value);
/*    */     }
/*    */   }
/*    */   
/*    */   public void forceSetValue(String value) {
/* 55 */     super.setValueAutoSave(value);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 60 */     return (super.equals(o) || ((String)getValue()).equals(o));
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\api\value\impl\ModeValue.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */