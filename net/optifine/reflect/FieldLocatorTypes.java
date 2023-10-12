/*    */ package net.optifine.reflect;
/*    */ 
/*    */ import java.lang.reflect.Field;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import net.optifine.Log;
/*    */ 
/*    */ public class FieldLocatorTypes
/*    */   implements IFieldLocator
/*    */ {
/* 13 */   private Field field = null;
/*    */ 
/*    */   
/*    */   public FieldLocatorTypes(Class cls, Class[] preTypes, Class<?> type, Class[] postTypes, String errorName) {
/* 17 */     Field[] afield = cls.getDeclaredFields();
/* 18 */     List<Class<?>> list = new ArrayList<>();
/*    */     
/* 20 */     for (int i = 0; i < afield.length; i++) {
/*    */       
/* 22 */       Field field = afield[i];
/* 23 */       list.add(field.getType());
/*    */     } 
/*    */     
/* 26 */     List<Class<?>> list1 = new ArrayList<>();
/* 27 */     list1.addAll(Arrays.asList(preTypes));
/* 28 */     list1.add(type);
/* 29 */     list1.addAll(Arrays.asList(postTypes));
/* 30 */     int l = Collections.indexOfSubList(list, list1);
/*    */     
/* 32 */     if (l < 0) {
/*    */       
/* 34 */       Log.log("(Reflector) Field not found: " + errorName);
/*    */     }
/*    */     else {
/*    */       
/* 38 */       int j = Collections.indexOfSubList(list.subList(l + 1, list.size()), list1);
/*    */       
/* 40 */       if (j >= 0) {
/*    */         
/* 42 */         Log.log("(Reflector) More than one match found for field: " + errorName);
/*    */       }
/*    */       else {
/*    */         
/* 46 */         int k = l + preTypes.length;
/* 47 */         this.field = afield[k];
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Field getField() {
/* 54 */     return this.field;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\reflect\FieldLocatorTypes.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */