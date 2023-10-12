/*    */ package rip.diavlo.base.ui;
/*    */ public class ClickResult {
/*    */   double offset;
/*    */   boolean finish;
/*    */   
/*  6 */   public ClickResult(double offset, boolean finish) { this.offset = offset; this.finish = finish; }
/*  7 */   public void setOffset(double offset) { this.offset = offset; } public void setFinish(boolean finish) { this.finish = finish; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public double offset() {
/* 13 */     return this.offset;
/*    */   }
/*    */   
/*    */   public boolean finish() {
/* 17 */     return this.finish;
/*    */   }
/*    */ }


/* Location:              C:\Users\march\Desktop\Diavlo-client.jar!\rip\diavlo\bas\\ui\ClickResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */