/*    */ package rip.diavlo.base.managers;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import rip.diavlo.base.api.value.Value;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ValueManager
/*    */ {
/* 11 */   private final List<Value> values = new ArrayList<>(); public List<Value> getValues() { return this.values; }
/*    */   
/*    */   public <T extends Value> T getValueFromOwner(Object object, String name) {
/* 14 */     for (Value<?> value : getValues()) {
/* 15 */       if (value.getOwner() == object && value.getName().equalsIgnoreCase(name)) {
/* 16 */         return (T)value;
/*    */       }
/*    */     } 
/* 19 */     return null;
/*    */   }
/*    */   
/*    */   public List<Value> getValuesFromOwner(Object owner) {
/* 23 */     List<Value> list = new ArrayList<>();
/* 24 */     for (Value value : getValues()) {
/* 25 */       if (value.getOwner() == owner) {
/* 26 */         list.add(value);
/*    */       }
/*    */     } 
/* 29 */     return list;
/*    */   }
/*    */   
/*    */   public boolean hasValues(Object owner) {
/* 33 */     for (Value value : getValues()) {
/* 34 */       if (value.getOwner() == owner)
/* 35 */         return true; 
/* 36 */     }  return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\base\managers\ValueManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */