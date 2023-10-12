/*    */ package com.viaversion.viaversion.libs.fastutil.objects;
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
/*    */ public final class ObjectIterables
/*    */ {
/*    */   public static <K> long size(Iterable<K> iterable) {
/* 34 */     long c = 0L;
/*    */     
/* 36 */     for (K dummy : iterable) c++; 
/* 37 */     return c;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\objects\ObjectIterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */