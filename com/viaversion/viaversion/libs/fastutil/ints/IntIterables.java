/*    */ package com.viaversion.viaversion.libs.fastutil.ints;
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
/*    */ public final class IntIterables
/*    */ {
/*    */   public static long size(IntIterable iterable) {
/* 34 */     long c = 0L;
/*    */     
/* 36 */     for (IntIterator intIterator = iterable.iterator(); intIterator.hasNext(); ) { int dummy = ((Integer)intIterator.next()).intValue(); c++; }
/* 37 */      return c;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\ints\IntIterables.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */