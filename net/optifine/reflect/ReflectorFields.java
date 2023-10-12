/*    */ package net.optifine.reflect;
/*    */ 
/*    */ 
/*    */ public class ReflectorFields
/*    */ {
/*    */   private ReflectorClass reflectorClass;
/*    */   private Class fieldType;
/*    */   private int fieldCount;
/*    */   private ReflectorField[] reflectorFields;
/*    */   
/*    */   public ReflectorFields(ReflectorClass reflectorClass, Class fieldType, int fieldCount) {
/* 12 */     this.reflectorClass = reflectorClass;
/* 13 */     this.fieldType = fieldType;
/*    */     
/* 15 */     if (reflectorClass.exists())
/*    */     {
/* 17 */       if (fieldType != null) {
/*    */         
/* 19 */         this.reflectorFields = new ReflectorField[fieldCount];
/*    */         
/* 21 */         for (int i = 0; i < this.reflectorFields.length; i++)
/*    */         {
/* 23 */           this.reflectorFields[i] = new ReflectorField(reflectorClass, fieldType, i);
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public ReflectorClass getReflectorClass() {
/* 31 */     return this.reflectorClass;
/*    */   }
/*    */ 
/*    */   
/*    */   public Class getFieldType() {
/* 36 */     return this.fieldType;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFieldCount() {
/* 41 */     return this.fieldCount;
/*    */   }
/*    */ 
/*    */   
/*    */   public ReflectorField getReflectorField(int index) {
/* 46 */     return (index >= 0 && index < this.reflectorFields.length) ? this.reflectorFields[index] : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\optifine\reflect\ReflectorFields.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */