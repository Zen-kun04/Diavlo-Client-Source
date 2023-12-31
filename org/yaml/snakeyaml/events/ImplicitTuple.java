/*    */ package org.yaml.snakeyaml.events;
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
/*    */ public class ImplicitTuple
/*    */ {
/*    */   private final boolean plain;
/*    */   private final boolean nonPlain;
/*    */   
/*    */   public ImplicitTuple(boolean plain, boolean nonplain) {
/* 34 */     this.plain = plain;
/* 35 */     this.nonPlain = nonplain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canOmitTagInPlainScalar() {
/* 42 */     return this.plain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canOmitTagInNonPlainScalar() {
/* 49 */     return this.nonPlain;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean bothFalse() {
/* 58 */     return (!this.plain && !this.nonPlain);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 63 */     return "implicit=[" + this.plain + ", " + this.nonPlain + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\org\yaml\snakeyaml\events\ImplicitTuple.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */