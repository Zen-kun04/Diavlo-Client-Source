/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class Tuple<A, B>
/*    */ {
/*    */   private A a;
/*    */   private B b;
/*    */   
/*    */   public Tuple(A aIn, B bIn) {
/* 10 */     this.a = aIn;
/* 11 */     this.b = bIn;
/*    */   }
/*    */ 
/*    */   
/*    */   public A getFirst() {
/* 16 */     return this.a;
/*    */   }
/*    */ 
/*    */   
/*    */   public B getSecond() {
/* 21 */     return this.b;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\net\minecraf\\util\Tuple.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */