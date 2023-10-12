/*    */ package com.viaversion.viaversion.libs.fastutil.ints;
/*    */ 
/*    */ import java.util.function.BinaryOperator;
/*    */ import java.util.function.IntBinaryOperator;
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
/*    */ @FunctionalInterface
/*    */ public interface IntBinaryOperator
/*    */   extends BinaryOperator<Integer>, IntBinaryOperator
/*    */ {
/*    */   @Deprecated
/*    */   default int applyAsInt(int x, int y) {
/* 47 */     return apply(x, y);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @Deprecated
/*    */   default Integer apply(Integer x, Integer y) {
/* 59 */     return Integer.valueOf(apply(x.intValue(), y.intValue()));
/*    */   }
/*    */   
/*    */   int apply(int paramInt1, int paramInt2);
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\com\viaversion\viaversion\libs\fastutil\ints\IntBinaryOperator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */