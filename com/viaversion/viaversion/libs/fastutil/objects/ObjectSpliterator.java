/*    */ package com.viaversion.viaversion.libs.fastutil.objects;
/*    */ 
/*    */ import java.util.Spliterator;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ObjectSpliterator<K>
/*    */   extends Spliterator<K>
/*    */ {
/*    */   default long skip(long n) {
/* 46 */     if (n < 0L) throw new IllegalArgumentException("Argument must be nonnegative: " + n); 
/* 47 */     long i = n;
/* 48 */     while (i-- != 0L && tryAdvance(unused -> {
/*    */         
/*    */         }));
/* 51 */     return n - i - 1L;
/*    */   }
/*    */   
/*    */   ObjectSpliterator<K> trySplit();
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\objects\ObjectSpliterator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */