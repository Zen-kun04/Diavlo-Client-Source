/*    */ package com.viaversion.viaversion.libs.fastutil.objects;
/*    */ 
/*    */ import com.viaversion.viaversion.libs.fastutil.BidirectionalIterator;
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
/*    */ public interface ObjectBidirectionalIterator<K>
/*    */   extends ObjectIterator<K>, BidirectionalIterator<K>
/*    */ {
/*    */   default int back(int n) {
/* 39 */     int i = n;
/* 40 */     for (; i-- != 0 && hasPrevious(); previous());
/* 41 */     return n - i - 1;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default int skip(int n) {
/* 47 */     return super.skip(n);
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\objects\ObjectBidirectionalIterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */