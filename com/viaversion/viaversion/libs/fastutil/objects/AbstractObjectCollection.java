/*    */ package com.viaversion.viaversion.libs.fastutil.objects;
/*    */ 
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class AbstractObjectCollection<K>
/*    */   extends AbstractCollection<K>
/*    */   implements ObjectCollection<K>
/*    */ {
/*    */   public String toString() {
/* 42 */     StringBuilder s = new StringBuilder();
/* 43 */     ObjectIterator<K> i = iterator();
/* 44 */     int n = size();
/*    */     
/* 46 */     boolean first = true;
/* 47 */     s.append("{");
/* 48 */     while (n-- != 0) {
/* 49 */       if (first) { first = false; }
/* 50 */       else { s.append(", "); }
/* 51 */        Object k = i.next();
/* 52 */       if (this == k) { s.append("(this collection)"); continue; }
/* 53 */        s.append(String.valueOf(k));
/*    */     } 
/* 55 */     s.append("}");
/* 56 */     return s.toString();
/*    */   }
/*    */   
/*    */   public abstract ObjectIterator<K> iterator();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\objects\AbstractObjectCollection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */